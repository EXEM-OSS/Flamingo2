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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.openflamingo.mapreduce.util.filter.BypassPathFilter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HDFS 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class HdfsUtils {

    /**
     * 지정한 경로를 삭제한다.
     *
     * @param client    DFS Client
     * @param path      삭제할 경로
     * @param recursive Recusive 적용 여부
     * @return 성공시 <tt>true</tt>
     * @throws java.io.IOException 파일을 삭제할 수 없는 경우
     */
    public static boolean remove(DFSClient client, String path, boolean recursive) throws IOException {
        return client.exists(path) && client.delete(path, recursive);
    }

    /**
     * Input Split의 경로를 반환한다.
     *
     * @param inputSplit Input Split
     * @return 경로
     */
    public static String getPath(InputSplit inputSplit) {
        FileSplit fileSplit = (FileSplit) inputSplit;
        Path path = fileSplit.getPath();
        return path.toUri().getPath();
    }

    /**
     * DFS Client의 출력 스트립을 얻는다.
     *
     * @param client    DFS Client
     * @param filename  파일명
     * @param overwrite Overwrite 여부
     * @return 출력 스트림
     * @throws java.io.IOException HDFS IO를 처리할 수 없는 경우
     */
    public static OutputStream getOutputStream(DFSClient client, String filename, boolean overwrite) throws IOException {
        return client.create(filename, overwrite);
    }

    /**
     * DFS Client의 입력 스트립을 얻는다.
     *
     * @param client   DFS Client
     * @param filename 파일 경로
     * @return 입력 스트림
     * @throws java.io.IOException HDFS IO를 처리할 수 없는 경우
     */
    public static InputStream getInputStream(DFSClient client, String filename) throws IOException {
        return client.open(filename);
    }

    /**
     * 출력 스트림을 종료한다.
     *
     * @param outputStream 출력 스트림
     * @throws java.io.IOException 출력 스트림을 종료할 수 없는 경우
     */
    public static void closeOuputStream(OutputStream outputStream) throws IOException {
        outputStream.close();
    }

    /**
     * 입력 스트림을 종료한다.
     *
     * @param inputStream 입력 스트림
     * @throws java.io.IOException 입력 스트림을 종료할 수 없는 경우
     */
    public static void closeInputStream(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    /**
     * Input Split의 파일명을 반환한다.
     * Input Split은 기본적으로 <tt>file + ":" + start + "+" + length</tt> 형식으로 구성되어 있다.
     *
     * @param inputSplit Input Split
     * @return 파일명
     */
    public static String getFilename(InputSplit inputSplit) {
        String filename = org.openflamingo.mapreduce.util.FileUtils.getFilename(inputSplit.toString());
        int start = filename.indexOf(":");
        return filename.substring(0, start);
    }

    /**
     * @param hdfsUrl
     * @return
     * @throws java.io.IOException
     */
    public static FileSystem getFileSystem(String hdfsUrl) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.default.name", hdfsUrl);
        configuration.set("fs.defaultFS", hdfsUrl);
        configuration.set("fs.AbstractFileSystem.hdfs.impl", "org.apache.hadoop.fs.Hdfs");
        FileSystem fileSystem = FileSystem.get(configuration);
        return fileSystem;
    }

    /**
     * 지정한 경로가 존재하는지 확인한다.
     *
     * @param client DFS Client
     * @param path   존재 여부를 판단할 경로
     * @return 존재하면 <tt>true</tt>
     * @throws java.io.IOException HDFS IO를 처리할 수 없는 경우
     */
    public static boolean exists(DFSClient client, String path) throws IOException {
        return client.exists(path);
    }

    /**
     * 지정한 경로가 디렉토리인지 확인한다.
     *
     * @param fs   {@link org.apache.hadoop.fs.FileSystem}
     * @param path 경로
     * @return 디렉토리인 경우 <tt>true</tt>
     * @throws java.io.IOException HDFS IO를 처리할 수 없는 경우
     */
    public static boolean isDirectory(FileSystem fs, String path) throws IOException {
        try {
            FileStatus status = fs.getFileStatus(new Path(path));
            return status.isDir();
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    /**
     * HDFS 상에서 지정한 파일을 다른 디렉토리로 파일을 이동시킨다.
     *
     * @param conf            Hadoop Configuration
     * @param path            이동할 파일
     * @param prefixToAppend  파일을 이동할 때 파일명의 prefix에 추가할 문자열
     * @param targetDirectory 목적 디렉토리
     * @throws java.io.IOException 파일을 이동할 수 없는 경우
     */
    public static void moveFileToDirectory(Configuration conf, String path, String prefixToAppend, String targetDirectory) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        FileStatus[] statuses = fileSystem.listStatus(new Path(path));
        for (FileStatus fileStatus : statuses) {
            String filename = prefixToAppend + "_" + fileStatus.getPath().getName();
            if (!isExist(conf, targetDirectory + "/" + filename)) {
                fileSystem.rename(fileStatus.getPath(), new Path(targetDirectory + "/" + filename));
            } else {
                throw new RuntimeException("\t  Warn: '" + fileStatus.getPath() + "' cannot moved. Already exists.");
            }
        }
    }

    /**
     * HDFS 상에서 지정한 파일을 다른 디렉토리로 파일을 이동시킨다.
     *
     * @param conf            Hadoop Configuration
     * @param delayFiles      이동할 파일 목록
     * @param targetDirectory 목적 디렉토리
     * @throws java.io.IOException 파일을 이동할 수 없는 경우
     */
    public static void moveFilesToDirectory(Configuration conf, List<String> delayFiles, String targetDirectory) throws IOException {
        for (String path : delayFiles) {
            String filename = FileUtils.getFilename(path);
            String delayedFilePrefix = filename.split("-")[0];
            String outputHead = delayedFilePrefix.replaceAll("delay", "");
            String outputMiddle = delayedFilePrefix.substring(0, 5);    // todo
            String outputTail = filename.replaceAll(delayedFilePrefix, "");

            System.out.println("Acceleration Dir " + targetDirectory + "/" + outputHead + "_" + outputMiddle + outputTail);
            makeDirectoryIfNotExists(targetDirectory, conf);

            FileSystem fileSystem = FileSystem.get(conf);
            fileSystem.rename(
                    new Path(path),
                    new Path(targetDirectory + "/" + outputHead + "_" + outputMiddle + outputTail));

            System.out.println("\t Moved: '" + path + "' --> '" + targetDirectory + "'");
        }
    }

    /**
     * HDFS 상에서 지정한 파일을 다른 디렉토리로 파일을 이동시킨다.
     *
     * @param conf            Hadoop Configuration
     * @param paths           이동할 파일 목록
     * @param prefixToAppend  파일을 이동할 때 파일명의 prefix에 추가할 문자열
     * @param targetDirectory 목적 디렉토리
     * @throws java.io.IOException 파일을 이동할 수 없는 경우
     */
    public static void moveFilesToDirectory(Configuration conf, List<String> paths, String prefixToAppend, String targetDirectory) throws IOException {
        for (String file : paths) {
            try {
                HdfsUtils.moveFileToDirectory(conf, file, prefixToAppend, targetDirectory);
                System.out.println("\t Moved: '" + file + "' --> '" + targetDirectory + "'");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * 디렉토리가 존재하지 않는다면 생성한다.
     *
     * @param directory 디렉토리
     * @param conf      Hadoop Configuration
     * @throws java.io.IOException HDFS 작업을 실패한 경우
     */
    public static void makeDirectoryIfNotExists(String directory, Configuration conf) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        if (!isExist(conf, directory) && !isDirectory(fileSystem, directory)) {
            fileSystem.mkdirs(new Path(directory));
        }
    }

    /**
     * HDFS의 해당 경로의 모든 파일에서 prefix로 시작하는 파일 목록을 반환한다.
     *
     * @param conf       Configuration
     * @param path       경로
     * @param prefix     파일의 Prefix
     * @param pathFilter 파일을 필터링하는 필터
     * @return 지정한 prefix로 파일명이 시작하는 파일 목록
     * @throws java.io.IOException HDFS 작업을 실패한 경우
     */
    public static List<String> getPrefixFiles(Configuration conf, String path, String prefix, PathFilter pathFilter) throws IOException {
        List<String> files = new ArrayList<String>();
        FileSystem fs = FileSystem.get(conf);
        FileStatus[] statuses = fs.listStatus(new Path(path), pathFilter != null ? pathFilter : new BypassPathFilter());
        if (statuses != null) {
            for (FileStatus fileStatus : statuses) {
                if (!fileStatus.isDir() && fileStatus.getPath().getName().startsWith(prefix)) {
                    files.add(fileStatus.getPath().toUri().getPath());
                }
            }
        }
        return files;
    }

    /**
     * HDFS의 해당 경로의 모든 파일에서 가장 최신 파일 하나를 반환한다.
     *
     * @param conf       Hadoop Configuration
     * @param path       경로
     * @param pathFilter 파일을 필터링하는 필터
     * @return 가장 최신 파일
     * @throws java.io.IOException HDFS 작업을 실패한 경우
     */
    public static String getLatestFile(Configuration conf, String path, PathFilter pathFilter) throws IOException {
        List<SortableFileStatus> files = new ArrayList<SortableFileStatus>();
        FileSystem fs = FileSystem.get(conf);
        FileStatus[] statuses = fs.listStatus(new Path(path), pathFilter != null ? pathFilter : new BypassPathFilter());
        if (statuses != null) {
            for (FileStatus fileStatus : statuses) {
                if (!fileStatus.isDir()) {
                    files.add(new SortableFileStatus(fileStatus));
                }
            }
        }
        Collections.sort(files);
        FileStatus fileStatus = files.get(0).fileStatus;
        return fileStatus.getPath().toUri().getPath();
    }

    /**
     * 지정한 경로에 파일이 존재하는지 확인한다.
     *
     * @param conf Haodop Job Configuration
     * @param path 존재 여부를 확인할 절대 경로
     * @return 존재한다면 <tt>true</tt>
     * @throws java.io.IOException 파일 존재 여부를 알 수 없거나, HDFS에 접근할 수 없는 경우
     */
    public static boolean isExist(Configuration conf, String path) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        return fs.exists(new Path(path));
    }

    /**
     * 해당 경로에 있는 파일을 MERGE한다.
     *
     * @param conf Hadoop Configuration
     * @param path HDFS Path
     * @throws java.io.IOException Get Merge할 수 없는 경우
     */
    public static void merge(Configuration conf, String path) throws IOException {
        // 입력 경로의 모든 파일을 Get Merge하여 임시 파일에 기록한다.
        FileSystem fileSystem = FileSystem.get(conf);
        Path source = new Path(path);
        if (!fileSystem.getFileStatus(source).isDir()) {
            // 이미 파일이라면 더이상 Get Merge할 필요없다.
            return;
        }
        Path target = new Path(path + "_temporary");
        FileUtil.copyMerge(fileSystem, source, fileSystem, target, true, conf, null);

        // 원 소스 파일을 삭제한다.
        fileSystem.delete(source, true);

        // 임시 파일을 원 소스 파일명으로 대체한다.
        Path in = new Path(path + "_temporary");
        Path out = new Path(path);
        fileSystem.rename(in, out);

        // 임시 디렉토리를 삭제한다.
        fileSystem.delete(new Path(path + "_temporary"), true);
    }

    /**
     * 지정한 경로를 삭제한다.
     *
     * @param configuration Hadoop Configuration
     * @param path          삭제할 경로
     * @throws java.io.IOException 삭제할 수 없는 경우
     */
    public static void delete(Configuration configuration, String path) throws IOException {
        FileSystem fileSystem = FileSystem.get(configuration);
        Path source = new Path(path);
        fileSystem.delete(source, true);
    }
}
