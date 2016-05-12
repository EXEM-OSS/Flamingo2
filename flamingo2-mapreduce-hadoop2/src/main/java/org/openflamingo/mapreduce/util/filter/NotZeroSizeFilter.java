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
package org.openflamingo.mapreduce.util.filter;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.io.IOException;

/**
 * 지정한 경로의 모든 파일을 그대로 처리하는 파일 필터.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class NotZeroSizeFilter implements PathFilter {

    /**
     * Hadoop FileSystem.
     */
    private FileSystem fs;

    /**
     * 기본 생성자.
     *
     * @param fs Hadoop FileSystem
     */
    public NotZeroSizeFilter(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public boolean accept(Path path) {
        try {
            FileStatus fileStatus = fs.getFileStatus(path);
            return !fileStatus.isDir() && fileStatus.getLen() > 0;
        } catch (IOException e) {
            throw new RuntimeException(path + "에 접근할 수 없습니다.");
        }
    }

}