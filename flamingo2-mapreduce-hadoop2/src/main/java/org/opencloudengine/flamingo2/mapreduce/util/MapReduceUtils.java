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

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * MapReduce 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class MapReduceUtils {

    /**
     * Comma Separated Input Path를 설정한다.
     *
     * @param job   Hadoop Job
     * @param files 문자열 파일 경로
     * @throws java.io.IOException 경로를 설정할 수 없는 경우
     */
    public static void setFileInputPaths(Job job, String files) throws IOException {
        for (String file : files.split(",")) {
            FileInputFormat.addInputPath(job, new Path(file));
        }
    }

    /**
     * String Array Input Path를 설정한다.
     *
     * @param job   Hadoop Job
     * @param files 문자열 파일 경로
     * @throws java.io.IOException 경로를 설정할 수 없는 경우
     */
    public static void setFileInputPaths(Job job, String... files) throws IOException {
        for (String file : files) {
            FileInputFormat.addInputPath(job, new Path(file));
        }
    }

    /**
     * String Input Path를 설정한다.
     *
     * @param job  Hadoop Job
     * @param file 문자열 파일 경로
     * @throws java.io.IOException 경로를 설정할 수 없는 경우
     */
    public static void setFileInputPath(Job job, String file) throws IOException {
        FileInputFormat.addInputPath(job, new Path(file));
    }

    /**
     * String Output Path를 설정한다.
     *
     * @param job  Hadoop Job
     * @param file 문자열 파일 경로
     * @throws java.io.IOException 경로를 설정할 수 없는 경우
     */
    public static void setFileOutputPath(Job job, String file) throws IOException {
        FileOutputFormat.setOutputPath(job, new Path(file));
    }

    /**
     * Mapper와 Reducer를 실행하는 JVM의 인자를 설정한다.
     *
     * @param job  Hadoop Job
     * @param opts JVM Args
     */
    public void setChildJavaOpts(Job job, String opts) {
        job.getConfiguration().set("mapred.child.java.opts", opts);
    }
}
