/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector.parser;

import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.spark.collector.event.*;
import org.opencloudengine.flamingo2.spark.collector.parser.expression.*;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Apps;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Job;
import org.opencloudengine.flamingo2.spark.collector.parser.value.Stage;
import org.opencloudengine.flamingo2.spark.collector.parser.value.TimelineNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Hyokun Park on 15. 8. 10..
 */
@Service
public class EventParserServiceImpl implements EventParserService {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(EventParserServiceImpl.class);

    private final Gson gson = new Gson();

    @Value("#{config['nn.scheme']}")
    private String nnScheme;

    @Value("#{config['nn.address']}")
    private String nnAddress;

    @Value("#{config['nn.port']}")
    private String nnPort;

    @Value("#{config['spark.history.fs.logDirectory']}")
    private String logDirectory;

    @Override
    public List<Map> getAllApplications() {

        List<Map> returnList = new ArrayList<>();
        List<String> fileList = getFileList();

        for (String fileName : fileList) {
            try {
                Map applicationMap = new HashMap();

                ApplicationStart applicationStart = getEvent(fileName, "SparkListenerApplicationStart", ApplicationStart.class);
                ApplicationEnd applicationEnd = getEvent(fileName, "SparkListenerApplicationEnd", ApplicationEnd.class);

                applicationMap.put("AppID", applicationStart.getAppID());
                applicationMap.put("AppName", applicationStart.getAppName());
                applicationMap.put("Started", applicationStart.getTimestamp());
                applicationMap.put("User", applicationStart.getUser());
                applicationMap.put("Completed", applicationEnd.getTimestamp());

                returnList.add(applicationMap);
            } catch (IOException ex) {
                continue;
            }

        }
        return returnList;
    }

    @Override
    public ApplicationAggregate getApplicationAggregate(String fileName) throws IOException {
        String line = "";
        InputStream inputStream = openFile(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ApplicationAggregate aggregate = new ApplicationAggregate();

        Apps apps = new Apps();
        JobAggregate jobAggregate = new JobAggregate();
        StageAggregate stageAggregate = new StageAggregate();
        StageDetailAggregate stageDetailAggregate = new StageDetailAggregate();
        ExecutorAggregate executorAggregate = new ExecutorAggregate();
        StorageAggregate storageAggregate = new StorageAggregate();
        TimelineAggregate timelineAggregate = new TimelineAggregate();

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
                        BlockManagerAdded blockManagerAdded = gson.fromJson(line, BlockManagerAdded.class);
                        executorAggregate.add(blockManagerAdded);
                        break;
                    case "SparkListenerExecutorAdded":
                        ExecutorAdded executorAdded = gson.fromJson(line, ExecutorAdded.class);
                        executorAggregate.add(executorAdded);
                        timelineAggregate.add(executorAdded);
                        break;
                    case "SparkListenerExecutorRemoved":
                        ExecutorRemoved executorRemoved = gson.fromJson(line, ExecutorRemoved.class);
                        timelineAggregate.add(executorRemoved);
                        break;
                    case "SparkListenerJobStart":
                        JobStart jobStart = gson.fromJson(line, JobStart.class);
                        jobAggregate.add(jobStart);
                        stageAggregate.add(jobStart);
                        storageAggregate.add(jobStart);
                        timelineAggregate.add(jobStart);
                        break;
                    case "SparkListenerJobEnd":
                        JobEnd jobEnd = gson.fromJson(line, JobEnd.class);
                        jobAggregate.add(jobEnd);
                        timelineAggregate.add(jobEnd);
                        break;
                    case "SparkListenerStageSubmitted":
                        StageSubmitted stageSubmitted = gson.fromJson(line, StageSubmitted.class);
                        jobAggregate.add(stageSubmitted);
                        stageAggregate.add(stageSubmitted);
                        stageDetailAggregate.add(stageSubmitted);
                        break;
                    case "SparkListenerStageCompleted":
                        StageCompleted stageCompleted = gson.fromJson(line, StageCompleted.class);
                        jobAggregate.add(stageCompleted);
                        stageAggregate.add(stageCompleted);
                        timelineAggregate.add(stageCompleted);
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
                        executorAggregate.add(taskEnd);
                        storageAggregate.add(taskEnd);
                        //timelineAggregate.add(taskEnd);
                        break;
                }
            }

            for (Object stageIDObj : jobAggregate.getMaxAttemptMap().keySet().toArray()) {
                Integer max = jobAggregate.getMaxAttemptMap().get(stageIDObj);

                String stageID = stageIDObj.toString() + "-" + max;

                Stage stage = stageAggregate.getStageMap().get(stageID);
                if (stage.getStatus().equals("Succeeded")) {
                    Integer jobID = jobAggregate.getStageJobMap().get(stageIDObj);

                    Job job = jobAggregate.getJobMap().get(jobID);
                    job.setStageCompleted(job.getStageCompleted() + 1);
                    jobAggregate.getJobMap().put(jobID, job);
                }
            }

            aggregate.setApps(apps);
            aggregate.setJobAggregate(jobAggregate);
            aggregate.setStageAggregate(stageAggregate);
            aggregate.setStageDetailAggregate(stageDetailAggregate);
            aggregate.setExecutorAggregate(executorAggregate);
            aggregate.setStorageAggregate(storageAggregate);
            aggregate.setTimelineAggregate(timelineAggregate);

            return aggregate;
        } catch (IOException e) {
            throw new ServiceException("Unable to get Event");
        } finally {
            reader.close();
            inputStream.close();
        }
    }

    @Override
    public Environment getEnvironment(String appid) throws IOException {
        return getEvent(appid, "SparkListenerEnvironmentUpdate", Environment.class);
    }

    @Override
    public List<String> getFilePathList() {
        String scheme = getScheme();

        List<String> returnList = new ArrayList<>();

        try {
            switch (scheme) {
                case "file":
                    File folder = new File(logDirectory);
                    File[] fileList = folder.listFiles();

                    for (File file : fileList) {

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
                            returnList.add(fileStatus.getPath().toString());
                        }
                    }
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return returnList;
    }

    @Override
    public Map getTaskTimeline(Map params) {

        try {
            logger.info("Start getting Spark tasks timeline events / AppID: " + params.get("appid").toString());
            String line = "";
            InputStream inputStream = null;
            inputStream = openFile(params.get("appid").toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            TimelineAggregate timelineAggregate = new TimelineAggregate();

            Integer stageID = Integer.parseInt(params.get("stageid").toString());
            Integer attemptID = Integer.parseInt(params.get("attemptid").toString());

            List<Map> returnList = new ArrayList<>();

            Map executorMap = new HashMap();

            while ((line = reader.readLine()) != null) {
                Map lineMap = gson.fromJson(line, Map.class);

                switch(lineMap.get("Event").toString()) {
                    case "SparkListenerExecutorAdded":
                        ExecutorAdded executorAdded = gson.fromJson(line, ExecutorAdded.class);
                        executorMap.put(executorAdded.getExecutorID(), executorAdded.getExecutorInfo().getHost());
                        break;
                    case "SparkListenerTaskEnd":
                        TaskEnd taskEnd = gson.fromJson(line, TaskEnd.class);

                        if (taskEnd.getStageID().equals(stageID) && taskEnd.getStageAttemptID().equals(attemptID)) {

                            String status = "";
                            if (taskEnd.getTaskEndReason().get("Reason").equals("Success")) {
                                status = "SUCCESS";
                            } else {
                                continue;
                            }

                            Map elementMap = new HashMap();

                            elementMap.put("start", taskEnd.getTaskInfo().getLaunchTime());
                            elementMap.put("end", taskEnd.getTaskInfo().getFinishTime());
                            elementMap.put("className", "task task-assignment-timeline-object");
                            elementMap.put("group", taskEnd.getTaskInfo().getExecutorID());

                            Long totalExecutionTime = taskEnd.getTaskInfo().getFinishTime() - taskEnd.getTaskInfo().getLaunchTime();
                            Long shuffleReadTime = (taskEnd.getTaskMetrics().getShuffleReadMetrics()==null)?0L:taskEnd.getTaskMetrics().getShuffleReadMetrics().getFetchWaitTime();
                            Long shuffleWriteTime = (taskEnd.getTaskMetrics().getShuffleWriteMetrics()==null)?0L:taskEnd.getTaskMetrics().getShuffleWriteMetrics().getShuffleWriteTime();
                            shuffleWriteTime = new Double(shuffleWriteTime / 1e6).longValue();
                            Long executorComputingTime = (taskEnd.getTaskMetrics().getExecutorRunTime()==null)?0L:taskEnd.getTaskMetrics().getExecutorRunTime() - shuffleReadTime - shuffleWriteTime;
                            Long serializationTime = Long.valueOf(taskEnd.getTaskMetrics().getResultSerializationTime());
                            Long deserializationTime = Long.valueOf(taskEnd.getTaskMetrics().getExecutorDeserializeTime());
                            Long gettingResultTime = taskEnd.getTaskInfo().getGettingResultTime();



                            Float shuffleReadTimeProportion = toProportion(totalExecutionTime, shuffleReadTime);
                            Float shuffleWriteTimeProportion = toProportion(totalExecutionTime, shuffleWriteTime);
                            Float executorComputingTimeProportion = toProportion(totalExecutionTime, executorComputingTime);
                            Float serializationTimeProportion = toProportion(totalExecutionTime, serializationTime);
                            Float deserializationTimeProportion = toProportion(totalExecutionTime, deserializationTime);

                            Float gettingResultTimeProportion = toProportion(totalExecutionTime, gettingResultTime);

                            Long schedulerDelay = totalExecutionTime - (executorComputingTime + shuffleReadTime + shuffleWriteTime + serializationTime + deserializationTime + gettingResultTime);
                            Float schedulerDelayProportion = (100 - executorComputingTimeProportion - shuffleReadTimeProportion - shuffleWriteTimeProportion - serializationTimeProportion - deserializationTimeProportion - gettingResultTimeProportion);

                            schedulerDelayProportion = (schedulerDelayProportion<0)?0:schedulerDelayProportion;

                            Long schedulerDelayProportionPos = 0L;
                            Float deserializationTimeProportionPos = schedulerDelayProportionPos + schedulerDelayProportion;
                            Float shuffleReadTimeProportionPos = deserializationTimeProportionPos + deserializationTimeProportion;
                            Float executorRuntimeProportionPos = shuffleReadTimeProportionPos + shuffleReadTimeProportion;
                            Float shuffleWriteTimeProportionPos = executorRuntimeProportionPos + executorComputingTimeProportion;
                            Float serializationTimeProportionPos = shuffleWriteTimeProportionPos + shuffleWriteTimeProportion;
                            Float gettingResultTimeProportionPos = serializationTimeProportionPos + serializationTimeProportion;

                            String messge = "<div class=\"task-assignment-timeline-content\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\" data-container=\"body\"" +
                                    "data-title=\"Task {} (attempt {})<br>Status: {}<br>Launch Time: {}<br>Finish Time: {}<br>Scheduler Delay: {} ms<br>Task Deserialization Time: {} <br>Shuffle Read Time: {}" +
                                    "<br>Executor Computing Time: {}<br>Shuffle Write Time: {}<br>Result Serialization Time: {}<br>Getting Result Time: {}\"> " +
                                    "<svg class=\"task-assignment-timeline-duration-bar\">" +
                                    "<rect class=\"scheduler-delay-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"deserialization-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"shuffle-read-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"executor-runtime-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"shuffle-write-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"serialization-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect>" +
                                    "<rect class=\"getting-result-time-proportion\" x=\"{}%\" y=\"0px\" height=\"26px\" width=\"{}%\"></rect></svg>";

                            String content = org.slf4j.helpers.MessageFormatter.arrayFormat(messge, new String[]{
                                    taskEnd.getTaskInfo().getIndex().toString(),
                                    taskEnd.getTaskInfo().getAttempt().toString(),
                                    status,
                                    getDate((Long) elementMap.get("start")),
                                    getDate((Long) elementMap.get("end")),
                                    schedulerDelay.toString(),
                                    deserializationTime.toString(),
                                    shuffleReadTime.toString(),
                                    executorComputingTime.toString(),
                                    shuffleWriteTime.toString(),
                                    serializationTime.toString(),
                                    gettingResultTime.toString(),
                                    schedulerDelayProportionPos.toString(),
                                    schedulerDelayProportion.toString(),
                                    deserializationTimeProportionPos.toString(),
                                    deserializationTimeProportion.toString(),
                                    shuffleReadTimeProportionPos.toString(),
                                    shuffleReadTimeProportion.toString(),
                                    executorRuntimeProportionPos.toString(),
                                    executorComputingTimeProportion.toString(),
                                    shuffleWriteTimeProportionPos.toString(),
                                    shuffleWriteTimeProportion.toString(),
                                    serializationTimeProportionPos.toString(),
                                    serializationTimeProportion.toString(),
                                    gettingResultTimeProportionPos.toString(),
                                    gettingResultTimeProportion.toString()
                            }).getMessage();

                            elementMap.put("content", content);

                            returnList.add(elementMap);
                        }
                        break;
                }
            }

            Map returnMap = new HashMap();

            Collections.sort(returnList, new TimelineDescCompare());

            int i;
            List<Map> removeList = new ArrayList<>();
            for (i = 0; i < returnList.size(); i++) {
                if (i < 1000) {
                    removeList.add(returnList.get(i));
                }
            }

            returnMap.put("group", executorMap);
            returnMap.put("list", removeList);

            logger.info("Finished getting Spark tasks timeline events / AppID: " + params.get("appid").toString());

            return returnMap;
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    private String getScheme() {
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

    private List<String> getFileList() {
        String scheme = getScheme();

        List<String> returnList = new ArrayList<>();

        try {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return returnList;
    }

    private InputStream openFile(String filename) throws IOException {
        String scheme = getScheme();
        InputStream inputStream = null;
        String path = org.slf4j.helpers.MessageFormatter.arrayFormat("{}/{}", new String[]{logDirectory, filename}).getMessage();

        switch (scheme) {
            case "file":
                File file = new File(path);
                FileInputStream fileInputStream = new FileInputStream(file);
                inputStream = fileInputStream;
                break;
            case "hdfs":
                Configuration conf = new Configuration();
                String defaultFS = org.slf4j.helpers.MessageFormatter.arrayFormat("{}://{}:{}", new String[]{nnScheme, nnAddress, nnPort}).getMessage();
                conf.set("fs.defaultFS", defaultFS);
                FileSystem fileSystem = FileSystem.get(conf);

                inputStream = fileSystem.open(new Path(path));
                break;
        }

        return inputStream;
    }

    private <T> T getEvent(String fileName, String eventName, Class<T> classOfT) throws IOException {
        String line;

        if (eventName.equals("SparkListenerApplicationEnd")) {
            line = getLastLine(fileName);
            return gson.fromJson(line, classOfT);
        }

        InputStream inputStream = openFile(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while ((line = reader.readLine()) != null) {
                Map lineMap = gson.fromJson(line, Map.class);

                if (lineMap.get("Event").equals(eventName)) {
                    return gson.fromJson(line, classOfT);
                }
            }
            throw new ServiceException("Unable to get Event");
        } catch (IOException e) {
            throw new ServiceException("");
        } finally {
            reader.close();
            inputStream.close();
        }
    }

    private String getLastLine(String filename) throws IOException {
        String scheme = getScheme();
        String path = org.slf4j.helpers.MessageFormatter.arrayFormat("{}/{}", new String[]{logDirectory, filename}).getMessage();
        String last = null;

        switch (scheme) {
            case "file":
                File file = new File(path);
                last = getLastLine(file);
                break;
            case "hdfs":
                Configuration conf = new Configuration();
                String defaultFS = org.slf4j.helpers.MessageFormatter.arrayFormat("{}://{}:{}", new String[]{nnScheme, nnAddress, nnPort}).getMessage();
                conf.set("fs.defaultFS", defaultFS);
                FileSystem fileSystem = FileSystem.get(conf);
                last = getLastLine(fileSystem, path);
                break;
        }
        return last;
    }

    private String getLastLine(FileSystem fileSystem, String path) throws IOException {

        Long position = fileSystem.getFileStatus(new Path(path)).getLen() - 2;
        int breakLine = "\n".charAt(0);
        FSDataInputStream inputStream = fileSystem.open(new Path(path));
        BufferedReader reader = null;
        String last = null;

        try {
            while (position > 0) {
                int read = inputStream.read();

                if (read == breakLine) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    last = reader.readLine();
                }

                position --;
            }
        } finally {
            inputStream.close();
            reader.close();
        }

        return last;
    }

    private String getLastLine(final File file) {
        String result = "";

        // file needs to exist
        if(file.exists() == false || file.isDirectory()){
            return "";
        }

        // avoid empty files
        if(file.length() == 0){
            return "";
        }

        try {
            // open the file for read-only mode
            RandomAccessFile fileAccess = new RandomAccessFile(file, "r");

            // set initial position as last one, except if this is an empty line
            long position = file.length()-2;
            int breakLine = "\n".charAt(0);

            // keep looking for a line break
            while(position > 0){
                // look for the offset
                fileAccess.seek(position);
                // read the new char
                final int thisChar = fileAccess.read();
                // do we have a match?
                if(thisChar == breakLine){
                    result = fileAccess.readLine();
                    break;
                }
                // decrease the offset
                position--;
            }
            // close the stream (thanks to Michael Schierl)
            fileAccess.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // all done
        return result;
    }

    private String getDate(Long time) {
        Date date = new Date(time);
        return format.format(date);
    }

    private Float toProportion(Long total, Long time) {
        Float result = Float.valueOf(time) / Float.valueOf(total) * 100;

        return Float.valueOf(time) / Float.valueOf(total) * 100;
    }

    class TimelineDescCompare implements Comparator<Map> {
        @Override
        public int compare(Map o1, Map o2) {
            return (Long) o1.get("start") > (Long) o2.get("start") ? -1 : (Long) o1.get("start") < (Long) o2.get("start") ? 1:0;
        }
    }
}
