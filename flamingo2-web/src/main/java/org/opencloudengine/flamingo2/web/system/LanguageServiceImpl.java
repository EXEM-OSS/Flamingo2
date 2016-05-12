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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.opencloudengine.flamingo2.logging.StringUtils;
import org.opencloudengine.flamingo2.model.rest.Language;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.Messages;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 로컬 DB에 저장된 플라밍고 사용자 정보를 관리하기 위한 User Service Implements
 *
 * @author Myeongha KIM
 */
@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ConfigurationHelper configurationHelper;

    private String baseDir = "/locale";

    private String tempDir = "/temp";

    @Override
    public List<Language> getLanguageList() throws IOException {
        Map<String, Map<String, String>> merged = new HashMap();
        List<Locale> list = getLocaleListFromExistResources();
        for (int i = 0; i < list.size(); i++) {
            Locale locale = list.get(i);
            Map map = Messages.toMap(locale);
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String id = iterator.next();
                if (!merged.containsKey(id)) {
                    merged.put(id, new HashMap());
                }
                Map<String, String> lineMap = merged.get(id);
                lineMap.put(locale.toString(), map.get(id).toString());
                merged.put(id, lineMap);
            }
        }
        List<Language> mergedList = new ArrayList();

        //오름차순 정렬후 Language 리스트로 변환한다.
        TreeMap tm = new TreeMap(merged);
        Iterator<String> iteratorKey = tm.keySet().iterator();
        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            Map linemap = (Map) tm.get(key);
            Language language = new Language();
            language.setId(key);
            if (linemap.containsKey(Locale.KOREA.toString()))
                language.setKo_KR(linemap.get(Locale.KOREA.toString()).toString());
            if (linemap.containsKey(Locale.US.toString()))
                language.setEn_US(linemap.get(Locale.US.toString()).toString());
            if (linemap.containsKey(Locale.JAPAN.toString()))
                language.setJa_JP(linemap.get(Locale.JAPAN.toString()).toString());
            if (linemap.containsKey(Locale.CHINA.toString()))
                language.setZh_CN(linemap.get(Locale.CHINA.toString()).toString());
            mergedList.add(language);
        }
        return mergedList;
    }

    @Override
    public XSSFWorkbook createXlsxFromGridData(List<Language> grid) throws IOException {
        String sheetName = "Sheet1";
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        for (int r = 0; r < grid.size(); r++) {
            Language language = grid.get(r);
            XSSFRow row = sheet.createRow(r);

            XSSFCell keyCell = row.createCell(0);
            keyCell.setCellValue(language.getId());

            ((XSSFCell) row.createCell(getGridColumnIndexOfLocal("ko_KR"))).setCellValue(!StringUtils.isEmpty(language.getKo_KR()) ? language.getKo_KR() : "");
            ((XSSFCell) row.createCell(getGridColumnIndexOfLocal("en_US"))).setCellValue(!StringUtils.isEmpty(language.getEn_US()) ? language.getEn_US() : "");
            ((XSSFCell) row.createCell(getGridColumnIndexOfLocal("ja_JP"))).setCellValue(!StringUtils.isEmpty(language.getJa_JP()) ? language.getJa_JP() : "");
            ((XSSFCell) row.createCell(getGridColumnIndexOfLocal("zh_CN"))).setCellValue(!StringUtils.isEmpty(language.getZh_CN()) ? language.getZh_CN() : "");
        }
        return wb;
    }

    @Override
    public String createPropertiesFromGridData(List<Language> grid, String lang) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < grid.size(); r++) {
            Language language = grid.get(r);
            if (!StringUtils.isEmpty(language.getId())) {
                builder.append(language.getId() + "=");
                switch (lang) {
                    case "ko_KR":
                        builder.append(!StringUtils.isEmpty(language.getKo_KR()) ? language.getKo_KR() + "\n" : "\n");
                        break;
                    case "en_US":
                        builder.append(!StringUtils.isEmpty(language.getEn_US()) ? language.getEn_US() + "\n" : "\n");
                        break;
                    case "ja_JP":
                        builder.append(!StringUtils.isEmpty(language.getJa_JP()) ? language.getJa_JP() + "\n" : "\n");
                        break;
                    case "zh_CN":
                        builder.append(!StringUtils.isEmpty(language.getZh_CN()) ? language.getZh_CN() + "\n" : "\n");
                        break;
                    default:
                        builder.append("\n");
                        break;
                }
            }
        }
        return builder.toString();
    }

    @Override
    public List<Language> getLanguageListFromXlsx(XSSFWorkbook wb) throws IOException {
        List<Language> list = new ArrayList();
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Language language = new Language();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                int columnIndex = cell.getColumnIndex();
                String cellValue = getCellValueAsString(cell);
                switch (columnIndex) {
                    case 0:
                        language.setId(cellValue);
                        break;
                    case 1:
                        language.setKo_KR(cellValue);
                        break;
                    case 2:
                        language.setEn_US(cellValue);
                        break;
                    case 3:
                        language.setJa_JP(cellValue);
                        break;
                    case 4:
                        language.setZh_CN(cellValue);
                        break;
                }
            }
            if (!StringUtils.isEmpty(language.getId()))
                list.add(language);
        }
        return list;
    }

    @Override
    public List<Language> getLanguageListFromZip(ZipInputStream zipInputStream) throws IOException {

        this.unZip(zipInputStream);
        Map<String, Map<String, String>> merged = new HashMap();
        List<Locale> list = getLocaleListFromUnzipedResources();
        for (int i = 0; i < list.size(); i++) {
            Locale locale = list.get(i);
            Map map = toMapFromUnzip(locale);
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String id = iterator.next();
                if (!merged.containsKey(id)) {
                    merged.put(id, new HashMap());
                }
                Map<String, String> lineMap = merged.get(id);
                lineMap.put(locale.toString(), map.get(id).toString());
                merged.put(id, lineMap);
            }
        }
        List<Language> mergedList = new ArrayList();

        //오름차순 정렬후 Language 리스트로 변환한다.
        TreeMap tm = new TreeMap(merged);
        Iterator<String> iteratorKey = tm.keySet().iterator();
        while (iteratorKey.hasNext()) {
            String key = iteratorKey.next();
            Map linemap = (Map) tm.get(key);
            Language language = new Language();
            language.setId(key);
            if (linemap.containsKey(Locale.KOREA.toString()))
                language.setKo_KR(linemap.get(Locale.KOREA.toString()).toString());
            if (linemap.containsKey(Locale.US.toString()))
                language.setEn_US(linemap.get(Locale.US.toString()).toString());
            if (linemap.containsKey(Locale.JAPAN.toString()))
                language.setJa_JP(linemap.get(Locale.JAPAN.toString()).toString());
            if (linemap.containsKey(Locale.CHINA.toString()))
                language.setZh_CN(linemap.get(Locale.CHINA.toString()).toString());
            mergedList.add(language);
        }
        return mergedList;
    }

    @Override
    public String saveResourcesFromGridData(List<Language> grid) throws IOException {

        ResourceLoader loader = new DefaultResourceLoader();
        Resource resourceDir = loader.getResource(baseDir);
        File baseLocaleDir = resourceDir.getFile();

        String ko_kr = createPropertiesFromGridData(grid, "ko_KR");
        Files.write(Paths.get(baseLocaleDir + "/" + "messages_ko_KR.properties"), ko_kr.getBytes());

        String en_US = createPropertiesFromGridData(grid, "en_US");
        Files.write(Paths.get(baseLocaleDir + "/" + "messages_en_US.properties"), en_US.getBytes());

        String ja_JP = createPropertiesFromGridData(grid, "ja_JP");
        Files.write(Paths.get(baseLocaleDir + "/" + "messages_ja_JP.properties"), ja_JP.getBytes());

        String zh_CN = createPropertiesFromGridData(grid, "zh_CN");
        Files.write(Paths.get(baseLocaleDir + "/" + "messages_zh_CN.properties"), zh_CN.getBytes());


        ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
        ReloadableResourceBundleMessageSource res = (ReloadableResourceBundleMessageSource)
                applicationContext.getBean("messageSource");
        res.clearCache();

        return baseLocaleDir.getAbsolutePath();
    }

    /**
     * 실제 서버에 있는 언어리소스 파일들로부터 언어종류를 파악해 반환한다.
     */
    private List<Locale> getLocaleListFromExistResources() throws IOException {
        ArrayList<Locale> list = new ArrayList<Locale>();

        ResourceLoader loader = new DefaultResourceLoader();
        Resource resourceDir = loader.getResource(baseDir);
        File baseLocaleDir = resourceDir.getFile();

        File[] files = baseLocaleDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File resource = files[i];
            String resourceName = resource.getName();
            String tag = resourceName.replaceAll("messages_", "").replaceAll(".properties", "");
            if (getLocale(tag) != null)
                list.add(getLocale(tag));
        }
        return list;
    }

    /**
     * 압축을 풀은 언어리소스 파일들로부터 언어종류를 파악해 반환한다.
     */
    private List<Locale> getLocaleListFromUnzipedResources() throws IOException {
        ArrayList<Locale> list = new ArrayList<Locale>();

        ResourceLoader loader = new DefaultResourceLoader();
        Resource resourceDir = loader.getResource(tempDir);
        File baseLocaleDir = resourceDir.getFile();

        File[] files = baseLocaleDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File resource = files[i];
            String resourceName = resource.getName();
            String tag = resourceName.replaceAll("messages_", "").replaceAll(".properties", "");
            if (getLocale(tag) != null)
                list.add(getLocale(tag));
        }
        return list;
    }

    /**
     * 언어 태그로부터 Locale 객체를 반환한다.
     *
     * @param language 언어
     */
    private Locale getLocale(String language) {
        switch (language) {
            case "ko_KR":
                return Locale.KOREA;
            case "en_US":
                return Locale.US;
            case "ja_JP":
                return Locale.JAPAN;
            case "zh_CN":
                return Locale.CHINA;
            default:
                return null;
        }
    }

    /**
     * 엑셀 파일생성시 해당 언어의 인덱스 지정한다.
     *
     * @param language 언어
     */
    private int getGridColumnIndexOfLocal(String language) {
        String[] locales = new String[]{"ko_KR", "en_US", "ja_JP", "zh_CN"};
        List<String> list = Arrays.asList(locales);
        return list.indexOf(language) + 1;
    }

    private String getCellValueAsString(XSSFCell cell) {
        String value = "";
        if (cell == null)
            return value;
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue() + "";
                break;
            case XSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue() + "";
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                break;
            default:
                break;
        }
        return value;
    }

    private void unZip(ZipInputStream zis) throws IOException {
        byte[] buffer = new byte[1024];
        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

            String fileName = ze.getName();
            ResourceLoader loader = new DefaultResourceLoader();
            Resource resourceDir = loader.getResource("/temp");
            File baseTempDir = resourceDir.getFile();
            File newFile = new File(baseTempDir + File.separator + fileName);

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();

            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    private Map toMapFromUnzip(Locale locale) throws IOException {
        Map map = new HashMap();

        ResourceLoader loader = new DefaultResourceLoader();
        Resource resourceDir = loader.getResource("/temp");
        File baseTempDir = resourceDir.getFile();
        File messageFile = new File(baseTempDir + File.separator + "messages_" + locale.toString() + ".properties");

        Properties prop = new Properties();
        InputStream input = null;
        input = new FileInputStream(messageFile.getAbsolutePath());
        Reader reader = new InputStreamReader(input, "UTF-8");
        prop.load(reader);

        Enumeration<?> e = prop.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = prop.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
