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
package org.opencloudengine.flamingo2.web.system;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.opencloudengine.flamingo2.core.rest.Response;
import org.opencloudengine.flamingo2.model.rest.Language;
import org.opencloudengine.flamingo2.util.ExceptionUtils;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.opencloudengine.flamingo2.web.configuration.DefaultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Language Management REST Controller
 *
 * @author Seungpil Park, Kim
 * @author Byoung Gon, Kim
 * @since 2.0
 */
@RestController
@RequestMapping("/system/language")
public class LanguageController extends DefaultController {

    @Autowired
    private LanguageService languageService;

    @Value("#{config['user.system.agent.apply']}")
    private boolean systemAgentApply;

    /**
     * 전체 메세지 번들을 가져온다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "languageList", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public Response getLanguage() {
        Response response = new Response();

        List<Language> languageList = new ArrayList<>();
        try {
            languageList = languageService.getLanguageList();
        } catch (Exception ex) {
            response.setSuccess(false);
            return response;
        }

        response.getList().addAll(languageList);
        response.setTotal(response.getList().size());
        response.setSuccess(true);
        return response;
    }

    /**
     * UI의 데이터를 파일로 반환한다
     *
     * @param grid
     * @return Response File Content
     */
    @RequestMapping(value = "/export/message", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity get(HttpServletResponse response, @RequestParam(defaultValue = "") String grid, @RequestParam(defaultValue = "xlsx") String type) throws IOException {
        if (grid.length() > 0) {
            if (type.equals("xlsx")) {
                OutputStream out = response.getOutputStream();
                try {
                    XSSFWorkbook wb = languageService.createXlsxFromGridData(fromGridJsonToList(grid));
                    wb.write(out);
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment; filename=message.xlsx");
                    response.setHeader("Expires:", "0"); // eliminates browser caching
                    response.setStatus(200);
                    out.flush();
                    out.close();
                    return new ResponseEntity(HttpStatus.OK);

                } catch (Exception ex) {
                    return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
                } finally {
                    if (out != null)
                        IOUtils.closeQuietly(out);
                }
            } else if (type.equals("zip")) {
                try {

                    ZipOutputStream out = new ZipOutputStream(response.getOutputStream());

                    InputStream ko_Krin = new ByteArrayInputStream(languageService.createPropertiesFromGridData(fromGridJsonToList(grid), "ko_KR").getBytes(StandardCharsets.UTF_8));
                    insertEntryZip(ko_Krin, "messages_ko_KR.properties", out);

                    InputStream en_USin = new ByteArrayInputStream(languageService.createPropertiesFromGridData(fromGridJsonToList(grid), "en_US").getBytes(StandardCharsets.UTF_8));
                    insertEntryZip(en_USin, "messages_en_US.properties", out);

                    InputStream ja_JPin = new ByteArrayInputStream(languageService.createPropertiesFromGridData(fromGridJsonToList(grid), "ja_JP").getBytes(StandardCharsets.UTF_8));
                    insertEntryZip(ja_JPin, "messages_ja_JP.properties", out);

                    InputStream zh_CNin = new ByteArrayInputStream(languageService.createPropertiesFromGridData(fromGridJsonToList(grid), "zh_CN").getBytes(StandardCharsets.UTF_8));
                    insertEntryZip(zh_CNin, "messages_zh_CN.properties", out);

                    response.setContentType("application/zip");
                    response.setHeader("Content-Disposition", "attachment; filename=message.zip");
                    response.setHeader("Expires:", "0"); // eliminates browser caching
                    response.setStatus(200);

                    // Complete the ZIP file
                    out.flush();
                    out.close();
                    return new ResponseEntity(HttpStatus.OK);
                } catch (IOException e) {
                    return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * UI의 데이터를 메세지 리소스로 저장한다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Response save(@RequestBody Map<String, Object> map) throws IOException {
        Response response = new Response();
        try {
            String grid = map.get("grid").toString();
            String baseDir = languageService.saveResourcesFromGridData(fromGridJsonToList(grid));
            response.getMap().put("dir", baseDir);
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            response.setSuccess(false);

            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            return response;
        }
    }

    /**
     * UI로부터 xlsx 파일을 받아 메세지 번들을 반환한다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "/import/message", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public Response upload(HttpServletRequest req) throws IOException {
        Response response = new Response();

        if (!(req instanceof DefaultMultipartHttpServletRequest)) {
            response.setSuccess(false);
            response.getError().setCause("Invalid Request.");
            response.getError().setMessage("Invalid Request.");
            return response;
        }

        try {
            DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
            InputStream ExcelFileToRead = request.getFile("file").getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            List<Language> listFromXlsx = languageService.getLanguageListFromXlsx(wb);
            ExcelFileToRead.close();

            response.getList().addAll(listFromXlsx);
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            return response;
        }
    }

    /**
     * UI로부터 zip 파일을 받아 메세지 번들을 반환한다.
     *
     * @return REST Response JAXB Object
     */
    @RequestMapping(value = "/import/zip", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.OK)
    public Response zip(HttpServletRequest req) throws IOException {
        Response response = new Response();

        if (!(req instanceof DefaultMultipartHttpServletRequest)) {
            response.setSuccess(false);
            response.getError().setCause("Invalid Request.");
            response.getError().setMessage("Invalid Request.");
            return response;
        }

        try {
            DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) req;
            InputStream ZipFileToRead = request.getFile("file").getInputStream();
            ZipInputStream zipInputStream = new ZipInputStream(ZipFileToRead);


            List<Language> listFromZip = languageService.getLanguageListFromZip(zipInputStream);
            ZipFileToRead.close();

            response.getList().addAll(listFromZip);
            response.setSuccess(true);
            return response;
        } catch (Exception ex) {
            response.setSuccess(false);
            response.getError().setMessage(ex.getMessage());
            if (ex.getCause() != null) response.getError().setCause(ex.getCause().getMessage());
            response.getError().setException(ExceptionUtils.getFullStackTrace(ex));
            return response;
        }
    }

    private List<Language> fromGridJsonToList(String grid) throws IOException {
        List languageList = new ArrayList();
        List list = JsonUtils.unmarshalToList(grid);
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            Language language = new Language();
            if (map.containsKey("id")) language.setId(map.get("id").toString());
            if (map.containsKey("ko_KR")) language.setKo_KR(map.get("ko_KR").toString());
            if (map.containsKey("en_US")) language.setEn_US(map.get("en_US").toString());
            if (map.containsKey("ja_JP")) language.setJa_JP(map.get("ja_JP").toString());
            if (map.containsKey("zh_CN")) language.setZh_CN(map.get("zh_CN").toString());
            languageList.add(language);
        }
        return languageList;
    }

    private void insertEntryZip(InputStream in, String entryName, ZipOutputStream zip) throws IOException {
        byte[] buf = new byte[1024];

        // Add ZIP entry to output stream.
        zip.putNextEntry(new ZipEntry(entryName));

        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
            zip.write(buf, 0, len);
        }
        // Complete the entry
        zip.closeEntry();
        in.close();
    }

}