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
