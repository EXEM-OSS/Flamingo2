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
package org.opencloudengine.flamingo2.engine.visual;

import com.jcraft.jsch.JSchException;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.sf.expectit.matcher.Matchers.regexp;

@Service
public class VisualServiceImpl implements VisualService {

    /**
     * SLF4J Logging
     */
    private static Logger logger = LoggerFactory.getLogger(VisualServiceImpl.class);

    @Autowired
    ServletContext servletContext = null;

    @Autowired
    ConfigurationHelper configurationHelper;

    @Override
    public Map loadHdfs(Map params) throws JSchException, IOException {

        Map returnMap = new HashMap();

        String filename = RandomStringUtils.randomAlphanumeric(10);
        Process process = Runtime.getRuntime().exec("/bin/sh");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withExceptionOnFailure()
                .build();

        try {
            expect.sendLine("Rscript /appl/Rscript/loadHdfsData.R " + params.get("datafile").toString() + " " + filename);

            String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

            returnMap.put("success", true);
            returnMap.put("filename", filename);
            returnMap.put("rData", result[result.length - 1]);
            logger.info(result[result.length - 1]);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            expect.close();
        }

        return returnMap;
    }

    @Override
    public Map listVariablesHdfs(Map params) throws JSchException, IOException {
        Map returnMap = new HashMap();
        String rDataTemp = configurationHelper.get("visual.rdata.tmp");
        String scriptPath = servletContext.getRealPath("/WEB-INF") + "/classes/rscript";
        Process process = Runtime.getRuntime().exec("/bin/sh");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withExceptionOnFailure()
                .build();

        try {
            //expect.expect(contains("$"));
            String filename = RandomStringUtils.randomAlphanumeric(10);
            String cmd = MessageFormatter.arrayFormat("Rscript {}/listVariablesHdfs.R {} {} {} {}", new String[]{
                    scriptPath,
                    rDataTemp,
                    params.get("dataFile").toString(),
                    filename,
                    params.get("header").toString()
            }).getMessage();

            logger.info(cmd);
            expect.sendLine(cmd);

            String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

            returnMap.put("success", true);
            returnMap.put("listVariables", result[result.length - 1]);
            logger.info(result[result.length - 1]);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            expect.close();
        }

        return returnMap;
    }

    @Override
    public Map listVariablesLocal(Map params) throws JSchException, IOException {
        Map returnMap = new HashMap();
        String rDataTemp = configurationHelper.get("visual.rdata.tmp");
        String scriptPath = servletContext.getRealPath("/WEB-INF") + "/classes/rscript";
        Process process = Runtime.getRuntime().exec("/bin/sh");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withExceptionOnFailure()
                .build();

        String dataFile = params.get("dataFile").toString();

        int fileIndex = dataFile.lastIndexOf(".");
        String filename = dataFile.substring(0, fileIndex);

        String cmd = MessageFormatter.arrayFormat("Rscript {}/listVariablesLocal.R {} {} {} {} '{}' ", new String[]{
                scriptPath,
                rDataTemp,
                params.get("dataFile").toString(),
                filename,
                params.get("header").toString(),
                params.get("options").toString()
        }).getMessage();

        logger.info(cmd);
        expect.sendLine(cmd);
        String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

        returnMap.put("success", true);
        returnMap.put("listVariables", result[result.length - 1]);
        logger.info(result[result.length - 1]);
        return returnMap;
    }

    @Override
    public Map createPng(Map params) throws JSchException, IOException {
        Map returnMap = new HashMap();
        String rDataTemp = configurationHelper.get("visual.rdata.tmp");
        String outputPath = configurationHelper.get("visual.output");

        String scriptPath = servletContext.getRealPath("/WEB-INF") + "/classes/rscript";
        Process process = Runtime.getRuntime().exec("/bin/sh");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withEchoOutput(System.err)
                .withExceptionOnFailure()
                .build();

        try {
            String cmd = MessageFormatter.arrayFormat("Rscript {}/png.R '{}' {} {}", new String[]{
                    scriptPath,
                    params.get("plotRequest").toString(),
                    rDataTemp,
                    outputPath
            }).getMessage();

            logger.info(cmd);

            expect.sendLine(cmd);
            String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

            returnMap.put("success", true);

            returnMap.put("image", result[result.length - 1]);
            logger.info(result[result.length - 1]);
        } catch (Exception e) {
            throw new ServiceException(e);
        } finally {
            expect.close();
        }

        return returnMap;
    }

    @Override
    public Map saveFile(MultipartFile file, String options) throws IOException {
        Map returnMap = new HashMap();
        String rDataTemp = configurationHelper.get("visual.rdata.tmp");
        String filename = RandomStringUtils.randomAlphanumeric(10);
        ;
        int fileIndex = StringUtils.lastIndexOf(file.getOriginalFilename(), '.');
        String fileExt = StringUtils.substring(file.getOriginalFilename(), fileIndex + 1);

        File saveFile = new File(rDataTemp, filename + "." + fileExt);
        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(saveFile));

        logger.debug("File path is {}", new Object[]{
                saveFile.getAbsolutePath()
        });

        returnMap.put("filename", filename + "." + fileExt);

        Process process = Runtime.getRuntime().exec("/bin/sh");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withExceptionOnFailure()
                .build();

        String scriptPath = servletContext.getRealPath("/WEB-INF") + "/classes/rscript";
        String cmd = MessageFormatter.arrayFormat("Rscript {}/loadLocalData.R {} '{}'", new String[]{
                scriptPath,
                saveFile.getAbsolutePath(),
                options
        }).getMessage();

        logger.info("{}", new Object[]{cmd});
        expect.sendLine(cmd);
        String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

        returnMap.put("success", true);
        returnMap.put("rData", result[result.length - 1]);

        logger.info(result[result.length - 1]);
        return returnMap;
    }

    @Override
    public Map reloadData(Map params) throws IOException {
        Map returnMap = new HashMap();
        Process process = Runtime.getRuntime().exec("/bin/sh");
        String rDataTemp = configurationHelper.get("visual.rdata.tmp");

        Expect expect = new ExpectBuilder()
                .withInputs(process.getInputStream())
                .withOutput(process.getOutputStream())
                .withExceptionOnFailure()
                .build();

        String scriptPath = servletContext.getRealPath("/WEB-INF") + "/classes/rscript";
        String rScriptFile;
        String dataFile;
        if (params.get("location").toString().equals("local")) {
            rScriptFile = "loadLocalData";
            dataFile = rDataTemp + "/" + params.get("dataFile").toString();
        } else {
            rScriptFile = "loadHdfsData";
            dataFile = params.get("dataFile").toString();
        }

        String cmd = MessageFormatter.arrayFormat("Rscript {}/{}.R {} '{}'", new String[]{
                scriptPath,
                rScriptFile,
                dataFile,
                params.get("options").toString()
        }).getMessage();

        logger.info("{}", new Object[]{cmd});
        expect.sendLine(cmd);
        String result[] = expect.expect(regexp("\\{success.+\\}$")).getInput().split("\n");

        returnMap.put("success", true);
        returnMap.put("rData", result[result.length - 1]);

        logger.info(result[result.length - 1]);
        return returnMap;
    }
}
