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

/**
 * 지정한 파일의 시작/종료 패턴에 부합하는지 확인한 후 부합하는 경우 업로드할 HDFS의 Directory를 반환하는 File Selector.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class StartEndWithFileSelector implements org.openflamingo.mapreduce.util.FileSelector {

    /**
     * HDFS의 Target Directory
     */
    private String targetHdfs;

    /**
     * 파일명의 시작 패턴
     */
    private String startPattern;

    /**
     * 파일명의 종료 패턴
     */
    private String endPattern;

    public String getTargetHdfs(String filename) {
        if (filename.startsWith(startPattern) && filename.endsWith(endPattern)) {
            return targetHdfs;
        }
        return null;
    }

    /**
     * HDFS의 Target Directory를 설정한다.
     *
     * @param targetHdfs HDFS의 Target Directory
     */
    public void setTargetHdfs(String targetHdfs) {
        this.targetHdfs = targetHdfs;
    }

    /**
     * 파일명의 시작 패턴을 설정한다.
     *
     * @param startPattern 파일명의 시작 패턴
     */
    public void setStartPattern(String startPattern) {
        this.startPattern = startPattern;
    }

    /**
     * 파일명의 종료 패턴을 설정한다.
     *
     * @param endPattern 파일명의 종료 패턴
     */
    public void setEndPattern(String endPattern) {
        this.endPattern = endPattern;
    }
}