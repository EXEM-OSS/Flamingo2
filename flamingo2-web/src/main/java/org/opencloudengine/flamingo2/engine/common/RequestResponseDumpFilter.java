/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.common;

import org.opencloudengine.flamingo2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestResponseDumpFilter implements Filter {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(RequestResponseDumpFilter.class);

    private boolean dumpRequest;
    private boolean requestUrlOnly;
    private boolean dumpResponse;

    private static class ByteArrayServletStream extends ServletOutputStream {

        ByteArrayOutputStream baos;

        ByteArrayServletStream(ByteArrayOutputStream baos) {
            this.baos = baos;
        }

        @Override
        public void write(int param) throws IOException {
            baos.write(param);
        }
    }

    private static class ByteArrayPrintWriter {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        private PrintWriter pw = new PrintWriter(baos);

        private ServletOutputStream sos = new ByteArrayServletStream(baos);

        public PrintWriter getWriter() {
            return pw;
        }

        public ServletOutputStream getStream() {
            return sos;
        }

        byte[] toByteArray() {
            return baos.toByteArray();
        }
    }

    private class BufferedServletInputStream extends ServletInputStream {

        ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return bais.available();
        }

        @Override
        public int read() {
            return bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return bais.read(buf, off, len);
        }

    }

    private class BufferedRequestWrapper extends HttpServletRequestWrapper {

        ByteArrayInputStream bais;

        ByteArrayOutputStream baos;

        BufferedServletInputStream bsis;

        byte[] buffer;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            InputStream is = req.getInputStream();
            baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                baos.write(buf, 0, letti);
            }
            buffer = baos.toByteArray();
        }

        public ServletInputStream getInputStream() {
            try {
                bais = new ByteArrayInputStream(buffer);
                bsis = new BufferedServletInputStream(bais);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return bsis;
        }

        public byte[] getBuffer() {
            return buffer;
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dumpRequest = Boolean.valueOf(filterConfig.getInitParameter("dumpRequest"));
        requestUrlOnly = Boolean.valueOf(filterConfig.getInitParameter("requestUrlOnly"));
        dumpResponse = Boolean.valueOf(filterConfig.getInitParameter("dumpResponse"));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
            final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest);

            if (dumpRequest) {
                Map requestHeader = new HashMap();
                java.util.Enumeration names = httpRequest.getHeaderNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    String value = httpRequest.getHeader(name);
                    requestHeader.put(name, value);
                }

                String requestMsg = new String(bufferedRequest.getBuffer());
                if (!StringUtils.isEmpty(requestMsg)) {
                    if (requestUrlOnly) {
                        logger.debug("REQUEST MESSAGE -> URL:{}\nHEADERS:\n{}", new String[]{
                                httpRequest.getRequestURL().toString(), requestHeader.toString()
                        });
                    } else {
                        logger.debug("REQUEST MESSAGE -> URL:{}\nHEADERS:\n{}\nBODY:\n{}", new String[]{
                                httpRequest.getRequestURL().toString(), requestHeader.toString(), requestMsg
                        });
                    }
                } else {
                    logger.debug("REQUEST MESSAGE -> URL:{}\nHEADERS:\n{}", new String[]{
                            httpRequest.getRequestURL().toString(), requestHeader.toString()
                    });
                }
            }

            final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

            final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
            HttpServletResponse wrappedResp = new HttpServletResponseWrapper(httpResponse) {
                public PrintWriter getWriter() {
                    return pw.getWriter();
                }

                public ServletOutputStream getOutputStream() {
                    return pw.getStream();
                }
            };


            filterChain.doFilter(bufferedRequest, wrappedResp);

            byte[] bytes = pw.toByteArray();
            httpResponse.getOutputStream().write(bytes);
            if (dumpResponse) {
                String responseMsg = new String(bytes);
                if (!StringUtils.isEmpty(responseMsg) && (
                        httpRequest.getHeader("accept").startsWith("application/json") ||
                                httpRequest.getHeader("accept").startsWith("application/xml")
                )) {
                    logger.debug("RESPONSE MESSAGE -> {}", responseMsg);
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}