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
package org.opencloudengine.flamingo2.mapreduce.util.filter;

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