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

import org.apache.hadoop.fs.FileStatus;

/**
 * 날짜를 기반으로 정렬 기능을 제공하는 Hadoop {@link org.apache.hadoop.fs.FileStatus}.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class SortableFileStatus implements Comparable<SortableFileStatus> {

    /**
     * 파일 메타데이터.
     */
    FileStatus fileStatus;

    /**
     * 기본 생성자.
     *
     * @param fileStatus 파일 메타데이터.
     */
    public SortableFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    @Override
    public int compareTo(SortableFileStatus other) {
        FileStatus otherFileStatus = other.getFileStatus();
        if (fileStatus.getModificationTime() > otherFileStatus.getModificationTime()) {
            return 1;
        } else if (fileStatus.getModificationTime() < otherFileStatus.getModificationTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 파일 메타데이터를 반환한다.
     *
     * @return 파일 메타데이터.
     */
    public FileStatus getFileStatus() {
        return fileStatus;
    }
}
