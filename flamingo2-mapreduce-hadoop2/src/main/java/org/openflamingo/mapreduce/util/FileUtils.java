/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.util;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import org.apache.tika.io.IOUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;

/**
 * File 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FileUtils {

    /**
     * 폴더 구분자
     */
    private static final String FOLDER_SEPARATOR = "/";

    /**
     * 윈도 폴더 구분자
     */
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    /**
     * 상위 경로
     */
    private static final String TOP_PATH = "..";

    /**
     * 현재 경로
     */
    private static final String CURRENT_PATH = ".";

    /**
     * 확장자 구분자
     */
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 지정한 경로에서 파일명을 추출한다.
     * e.g. "mypath/myfile.txt" -> "myfile.txt".
     *
     * @param path 파일 경로
     * @return 파일명. 파일 경로가 <tt>null</tt>인 경우 <tt>null</tt>
     */
    public static String getFilename(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    /**
     * 지정한 경로에서 파일의 확장자를 추출한다.
     * e.g. "mypath/myfile.txt" -> "txt".
     *
     * @param path 파일 경로
     * @return 확장자. 파일 경로가 <tt>null</tt>인 경우 <tt>null</tt>
     */
    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
    }

    /**
     * 지정한 경로에서 파일의 확장자를 삭제한다.
     * e.g. "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path 파일 경로
     * @return 확장자가 삭제된 경로. 파일 경로가 <tt>null</tt>인 경우 <tt>null</tt>
     */
    public static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        }
        int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
        return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
    }

    /**
     * 지정한 디렉토리의 모든 파일 및 디렉토리를 반환한다. 지정한 위치가 디렉토리가 아닌 경우 null을 반환한다.
     *
     * @param location 디렉토리
     * @return 파일 목록(디렉토리가 아닌 경우 null)
     */
    public static String[] getFileList(final String location) {
        File file = new File(location);
        if (file.isDirectory()) {
            return file.list();
        }
        return null;
    }

    /**
     * 지정한 위치와 파일의 확장자 명에 포함되는 모든 파일 목록을 반환한다. 지정한 위치가 디렉토리가 아닌 경우 null을 반환한다.
     *
     * @param location  디렉토리
     * @param extension 파일의 확장자
     * @return 파일 목록(디렉토리가 아닌 경우 null)
     */
    public static String[] getFileListByExtension(final String location, final String extension) {
        File file = new File(location);
        if (file.isDirectory()) {
            String[] lists = file.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.endsWith(extension.toLowerCase()) || name.endsWith(extension.toUpperCase())) {
                        return true;
                    }
                    return false;
                }
            });
            return lists;
        }
        return null;
    }

    /**
     * 지정한 파일을 삭제한다.
     *
     * @param filename 파일명
     * @return 정상적으로 삭제되면 <tt>true</tt>를 반환한다. 디렉토리 이거나 파일을 지울 수 없는 경우 <tt>false</tt>를 반환한다.
     */
    public static boolean delete(final String filename) {
        File file = new File(filename);
        if (!file.isDirectory() && file.isFile()) {
            file.delete();
            if (!file.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 지정한 파일의 최근 변경된 시간을 반환한다.
     *
     * @param filename 파일명
     * @return 변경된 시간. 만약에 파일이 존재하지 않거나 입출력 에러가 발생하면 0L을 반환한다.
     */
    public static long lastModified(final String filename) {
        return new File(filename).lastModified();
    }

    /**
     * 문자열 라인을 파일로 기록한다.
     *
     * @param file  문자열 라인을 기록할 파일
     * @param lines 파일에 기록할 문자열
     * @throws java.io.IOException 파일에 기록할 수 없는 경우
     */
    public static void writeLines(File file, String... lines) throws IOException {
        Writer writer = Files.newWriter(file, Charsets.UTF_8);
        try {
            for (String line : lines) {
                writer.write(line);
                writer.write('\n');
            }
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }


    /**
     * 테스트 후에 삭제할 임시 디렉토리를 생성한다.
     *
     * @param clazz 디렉토리를 생성할 때 사용하는 클래스명
     * @return 파일 또는 디렉토리
     */
    public static File createTestDir(Class<?> clazz) throws IOException {
        String systemTmpDir = System.getProperty("java.io.tmpdir");
        long simpleRandomLong = (long) (Long.MAX_VALUE * Math.random());
        File testTempDir = new File(systemTmpDir, "flamingo-" + clazz.getClass().getSimpleName() + '-' + simpleRandomLong);
        if (!testTempDir.mkdir()) {
            throw new IOException("임시 디렉토리를 생성할 수 없습니다: " + testTempDir);
        }
        testTempDir.deleteOnExit();
        return testTempDir;
    }

    /**
     * 임시 파일을 생성합니다.
     *
     * @param parent 부모 디렉토리
     * @param name   파일명
     * @return 임시 파일
     * @throws java.io.IOException 임시 파일을 생성할 수 없는 경우
     */
    public static File createTempFile(File parent, String name) throws IOException {
        return createTestTempFileOrDir(parent, name, false);
    }

    /**
     * 임시 디렉토리를 생성합니다.
     *
     * @param parent 부모 디렉토리
     * @param name   디렉토리명
     * @return 임시 디렉토리
     * @throws java.io.IOException 임시 디렉토리를 생성할 수 없는 경우
     */
    public static File createTempDir(File parent, String name) throws IOException {
        File dir = createTestTempFileOrDir(parent, name, true);
        dir.delete();
        return dir;
    }

    /**
     * 테스트용 임시 파일 또는 디렉토리를 생성합니다.
     *
     * @param parent 부모 디렉토리
     * @param name   파일명
     * @param dir    디렉토리 여부
     * @return 디렉토리 또는 파일
     * @throws java.io.IOException 디렉토리를 생성할 수 없는 경우
     */
    public static File createTestTempFileOrDir(File parent, String name, boolean dir) throws IOException {
        File f = new File(parent, name);
        f.deleteOnExit();
        if (dir && !f.mkdirs()) {
            throw new IOException("디렉토리를 생성할 수 없습니다: " + f);
        }
        return f;
    }
}
