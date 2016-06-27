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

import java.util.Iterator;
import java.util.List;

/**
 * 하나 이상의 File Selector를 이용하여 지정한 파일의 Upload할 HDFS 경로를 반환하는 File Selector Chain.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class FileSelectorChain implements org.opencloudengine.flamingo2.mapreduce.util.FileSelector {

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
            org.opencloudengine.flamingo2.mapreduce.util.FileSelector fileSelector = fileSelectorIterator.next();
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