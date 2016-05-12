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

import java.util.Iterator;
import java.util.List;

/**
 * 하나 이상의 File Selector를 이용하여 지정한 파일의 Upload할 HDFS 경로를 반환하는 File Selector Chain.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FileSelectorChain implements org.openflamingo.mapreduce.util.FileSelector {

    /**
     * File Selector 목록
     */
    private List<FileSelector> fileSelectors;

    /**
     * 지정한 파일명에 대해서 업로드할 HDFS의 Target Directory를 반환한다.
     * 이 메소드는 File Selector Chain이 관리하는 File Selector 목록을 iteration하면서 처리한다.
     *
     * @param filename 파일명
     * @return HDFS의 Target Directory
     */
    public String getTargetHdfs(String filename) {
        for (Iterator<FileSelector> fileSelectorIterator = fileSelectors.iterator(); fileSelectorIterator.hasNext(); ) {
            org.openflamingo.mapreduce.util.FileSelector fileSelector = fileSelectorIterator.next();
            String hdfsUrl = fileSelector.getTargetHdfs(filename);
            if (hdfsUrl != null) {
                return hdfsUrl;
            }
        }
        return null;
    }

    /**
     * File Selector 목록을 설정한다.
     *
     * @param fileSelectors File Selector 목록
     */
    public void setFileSelectors(List<FileSelector> fileSelectors) {
        this.fileSelectors = fileSelectors;
    }
}