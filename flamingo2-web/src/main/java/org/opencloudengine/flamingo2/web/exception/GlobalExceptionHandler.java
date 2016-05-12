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
package org.opencloudengine.flamingo2.web.exception;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.core.exception.WholeBodyException;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger("flamingo.exception");

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public void handleServiceException(HttpServletRequest req, HttpServletResponse res, Exception ex) throws IOException {
        if (ex instanceof WholeBodyException) {
            res.setStatus(500);
            res.setHeader("code", "100");
            res.setHeader("id", ((WholeBodyException) ex).getExceptionId());
            res.setHeader("message", ex.getMessage());
            if (ex.getCause() != null) {
                res.setHeader("cause", ex.getCause().getMessage());
            }

            logger.warn("{} : {} {}\n{}", new Object[]{
                    ex.getClass().getName(), req.getRequestURI(), ((WholeBodyException) ex).getExceptionId(), ExceptionUtils.getFullStackTrace(ex)
            });

            PrintWriter out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), "UTF-8"), true);
            out.println(ExceptionUtils.getFullStackTrace(ex));
            out.flush();
            return;
        } else {
            Response response = new Response();
            if (ex instanceof ServiceException) {
                response.getError().setId(((ServiceException) ex).getExceptionId());
                response.getError().setMessage(ex.getMessage());
                response.getError().setCode("100");

                logger.warn("{} : {} {}\n{}", new Object[]{
                        ex.getClass().getName(), req.getRequestURI(), ((ServiceException) ex).getExceptionId(), ExceptionUtils.getFullStackTrace(ex)
                });

            } else {
                response.getError().setMessage("An error occurs in the system was unable to process your request.");
                response.getError().setCode("999");

                logger.warn("{} : {} (System) \n{}", new Object[]{
                        ex.getClass().getName(), req.getRequestURI(), ExceptionUtils.getFullStackTrace(ex)
                });

            }

            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            response.setSuccess(false);

            res.setStatus(200);
            res.setContentType("application/json; charset=UTF-8");

            String json = JsonUtils.marshal(response);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), "UTF-8"), true);
            out.println(json);
            out.flush();
        }
    }
}