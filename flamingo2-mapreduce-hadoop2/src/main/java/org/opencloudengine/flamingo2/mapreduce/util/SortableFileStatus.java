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
