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

/**
 * 지정한 파일의 시작/종료 패턴에 부합하는지 확인한 후 부합하는 경우 업로드할 HDFS의 Directory를 반환하는 File Selector.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class StartEndWithFileSelector implements org.opencloudengine.flamingo2.mapreduce.util.FileSelector {

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