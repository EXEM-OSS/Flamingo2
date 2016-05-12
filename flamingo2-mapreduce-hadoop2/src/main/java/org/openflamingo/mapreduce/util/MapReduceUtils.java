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
