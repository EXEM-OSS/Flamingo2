/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.flamingo2.web.system;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.opencloudengine.flamingo2.model.rest.Language;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 다국어 처리 메뉴 서비스
 *
 * @author Seungpil PARK
 * @since 2.0
 */
public interface LanguageService {

    /**
     * 언어 리스트를 가져온다
     *
     * @return Language List
     */
    List<Language> getLanguageList() throws IOException;

    /**
     * UI의 grid 데이터로부터 xlsx 파일을 생성한다.
     *
     * @param grid List
     * @return XSSFWorkbook
     */
    XSSFWorkbook createXlsxFromGridData(List<Language> grid) throws IOException;

    /**
     * UI의 grid 데이터로부터 propertes 파일을 스트링 형태로 반환한다.
     *
     * @param grid List
     * @param lang String
     * @return String
     */
    String createPropertiesFromGridData(List<Language> grid, String lang) throws IOException;

    /**
     * UI에서 업로드한 xlsx 파일로부터 언어 리스트를 반환한다.
     *
     * @param wb XSSFWorkbook
     * @return Language List
     */
    List<Language> getLanguageListFromXlsx(XSSFWorkbook wb) throws IOException;

    /**
     * UI에서 업로드한 zip 파일로부터 언어 리스트를 반환한다.
     *
     * @param zipInputStream ZipInputStream
     * @return Language List
     */
    List<Language> getLanguageListFromZip(ZipInputStream zipInputStream) throws IOException;

    /**
     * UI의 grid 데이터로부터 resources 를 저장한다.
     *
     * @param grid List
     */
    String saveResourcesFromGridData(List<Language> grid) throws IOException;
}
