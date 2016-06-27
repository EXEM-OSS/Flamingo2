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
package org.opencloudengine.flamingo2.mapreduce.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;

/**
 * 로컬 파일 시스템의 지정한 파일의 크기를 확인하는 유틸리티 클래스.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class FileSystemFactory {

    /**
     * FileSystem을 생성한다.
     *
     * @return FileSystem
     * @throws java.io.IOException 파일 시스템에 접근할 수 없는 경우
     */
    public static FileSystem getFileSystem() throws IOException {
        return FileSystem.get(new Configuration());
    }

    /**
     * FileSystem을 생성한다.
     *
     * @param url FileSysem Scheme URL
     * @return FileSystem
     * @throws java.io.IOException 파일 시스템에 접근할 수 없는 경우
     */
    public static FileSystem getFileSystem(String url) throws IOException {
        if (!StringUtils.hasLength(url)) {
            return getFileSystem();
        } else if (url.startsWith("hdfs://")) {
            Configuration conf = new Configuration();
            conf.set("fs.default.name", url);
            conf.set("fs.defaultFS", url);
            conf.set("fs.AbstractFileSystem.hdfs.impl", "org.apache.hadoop.fs.Hdfs");
            return FileSystem.get(conf);
        }
        return getFileSystem();
    }
}
