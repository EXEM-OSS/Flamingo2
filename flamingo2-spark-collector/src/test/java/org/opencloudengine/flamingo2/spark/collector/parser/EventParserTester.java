package org.opencloudengine.flamingo2.spark.collector.parser;

import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.spark.collector.event.*;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Apps;
import org.opencloudengine.flamingo2.spark.collector.parser.expression.JobAggregate;
import org.opencloudengine.flamingo2.spark.collector.parser.expression.StageAggregate;
import org.opencloudengine.flamingo2.spark.collector.parser.expression.StageDetailAggregate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class EventParserTester {
    private static InputStream eventStream = null;

    private static Gson gson = new Gson();

    private static String nnScheme = "hdfs";

    private static String nnAddress = "192.168.221.100";

    private static String nnPort = "9000";

    private static String logDirectory = "hdfs://192.168.221.100:9000/spark-events";

    public static void main(String args[]) throws IOException {
        String scheme = getScheme();

        List<String> returnList = new ArrayList<>();

        try {
            switch (scheme) {
                case "file":
                    File folder = new File(logDirectory);
                    File[] fileList = folder.listFiles();

                    for (File file : fileList) {
                        System.out.println(file.getAbsolutePath());
                        returnList.add(file.getAbsolutePath());
                    }
                    break;
                case "hdfs":
                    Configuration conf = new Configuration();
                    String defaultFS = org.slf4j.helpers.MessageFormatter.arrayFormat("{}://{}:{}", new String[]{nnScheme, nnAddress, nnPort}).getMessage();
                    conf.set("fs.defaultFS", defaultFS);
                    FileSystem fileSystem = FileSystem.get(conf);
                    FileStatus listStatus[] = fileSystem.listStatus(new Path(logDirectory));

                    for (FileStatus fileStatus : listStatus) {
                        if (fileStatus.isFile()) {
                            System.out.println(fileStatus.getPath().toString());
                            returnList.add(fileStatus.getPath().toString());
                        }
                    }
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getScheme() {
        try {
            URI uri = new URI(logDirectory);

            if (!uri.getScheme().equals("file") && !uri.getScheme().equals("hdfs")) {
                throw new ServiceException("Unsupported file scheme");
            }
            return uri.getScheme();
        } catch (URISyntaxException e) {
            throw new ServiceException("Unable to get Spark Event Log directory scheme");
        }
    }

    private static String getPath() {
        try {
            URI uri = new URI(logDirectory);

            return uri.getPath();
        } catch (URISyntaxException e) {
            throw new ServiceException("Unable to get Spark Event Log directory path");
        }
    }

    private static List<String> getFileList() throws IOException {
        String scheme = getScheme();

        List<String> returnList = new ArrayList<>();
        switch (scheme) {
            case "file":
                File folder = new File(logDirectory);
                File[] fileList = folder.listFiles();

                for (File file : fileList) {
                    returnList.add(file.getName());
                }
                break;
            case "hdfs":
                Configuration conf = new Configuration();
                String defaultFS = org.slf4j.helpers.MessageFormatter.arrayFormat("{}://{}:{}", new String[]{nnScheme, nnAddress, nnPort}).getMessage();
                conf.set("fs.defaultFS", defaultFS);
                FileSystem fileSystem = FileSystem.get(conf);
                FileStatus listStatus[] = fileSystem.listStatus(new Path(logDirectory));

                for (FileStatus fileStatus : listStatus) {
                    if (fileStatus.isFile()) {
                        returnList.add(fileStatus.getPath().getName());
                    }
                }
                break;
        }

        return returnList;
    }

    private static InputStream openFile(String filename) throws IOException {
        String scheme = getScheme();
        InputStream inputStream = null;
        String path = null;

        switch (scheme) {
            case "file":
                path = org.slf4j.helpers.MessageFormatter.arrayFormat("{}/{}", new String[]{getPath(), filename}).getMessage();
                File file = new File(path);
                FileInputStream fileInputStream = new FileInputStream(file);
                inputStream = fileInputStream;
                break;
            case "hdfs":
                path = org.slf4j.helpers.MessageFormatter.arrayFormat("{}/{}", new String[]{logDirectory, filename}).getMessage();
                Configuration conf = new Configuration();
                String defaultFS = org.slf4j.helpers.MessageFormatter.arrayFormat("{}://{}:{}", new String[]{nnScheme, nnAddress, nnPort}).getMessage();
                conf.set("fs.defaultFS", defaultFS);
                FileSystem fileSystem = FileSystem.get(conf);

                inputStream = fileSystem.open(new Path(path));
                break;
        }
        eventStream = inputStream;
        return inputStream;
    }

    private static void closeFile() {
        try {
            if (eventStream != null) {
                eventStream.close();
            }
        } catch (IOException e) {
            throw new ServiceException("Unable to clode EventStream");
        }
    }

    private static <T> T getEvent(String fileName, String eventName, Class<T> classOfT) throws IOException {
        String line;
        openFile(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(eventStream));

        try {
            while ((line = reader.readLine()) != null) {
                Map lineMap = gson.fromJson(line, Map.class);

                if (lineMap.get("Event").equals(eventName)) {
                    return gson.fromJson(line, classOfT);
                }
            }
            throw new ServiceException("Unable to get Event");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("");
        } finally {
            reader.close();
            closeFile();
        }
    }

    private static ApplicationEvent getApplicationEvent(String fileName) throws IOException {
        Long start = new Date().getTime();
        String line;
        InputStream inputStream = openFile(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ApplicationEvent applicationEvent = new ApplicationEvent();

        Apps apps = new Apps();
        JobAggregate jobAggregate = new JobAggregate();
        StageAggregate stageAggregate = new StageAggregate();
        StageDetailAggregate stageDetailAggregate = new StageDetailAggregate();

        try {
            while ((line = reader.readLine()) != null) {
                Map lineMap = gson.fromJson(line, Map.class);

                switch(lineMap.get("Event").toString()) {
                    case "SparkListenerApplicationStart":
                        ApplicationStart applicationStart = gson.fromJson(line, ApplicationStart.class);
                        apps.setAppID(applicationStart.getAppID());
                        apps.setAppName(applicationStart.getAppName());
                        apps.setStarted(applicationStart.getTimestamp());
                        apps.setUser(applicationStart.getUser());
                        break;
                    case "SparkListenerApplicationEnd":
                        ApplicationEnd applicationEnd = gson.fromJson(line, ApplicationEnd.class);
                        apps.setCompleted(applicationEnd.getTimestamp());
                        break;
                    case "SparkListenerBlockManagerAdded":

                        break;
                    case "SparkListenerJobStart":
                        JobStart jobStart = gson.fromJson(line, JobStart.class);
                        jobAggregate.add(jobStart);
                        stageAggregate.add(jobStart);
                        break;
                    case "SparkListenerJobEnd":
                        jobAggregate.add(gson.fromJson(line, JobEnd.class));
                        break;
                    case "SparkListenerStageSubmitted":
                        StageSubmitted stageSubmitted = gson.fromJson(line, StageSubmitted.class);
                        jobAggregate.add(stageSubmitted);
                        stageAggregate.add(stageSubmitted);
                        stageDetailAggregate.add(stageSubmitted);
                        break;
                    case "SparkListenerStageCompleted":
                        jobAggregate.add(gson.fromJson(line, StageCompleted.class));
                        break;
                    case "SparkListenerTaskStart":
                        TaskStart taskStart = gson.fromJson(line, TaskStart.class);
                        stageAggregate.add(taskStart);
                        break;
                    case "SparkListenerTaskEnd":
                        TaskEnd taskEnd = gson.fromJson(line, TaskEnd.class);
                        jobAggregate.add(taskEnd);
                        stageAggregate.add(taskEnd);
                        stageDetailAggregate.add(taskEnd);
                        break;
                }
            }

            Long end = new Date().getTime();
            System.out.println(end - start);
            System.out.println(stageDetailAggregate.getJson());
            return applicationEvent;
        } catch (IOException e) {
            throw new ServiceException("Unable to get Event");
        } finally {
            reader.close();
            inputStream.close();
        }
    }
}
