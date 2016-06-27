package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * Real-Time Spark Streaming Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class SparkStreaming implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private long id;
    private String server;
    private String sparkUserWorkingPath;
    private String jarFileFQP;
    private String jarFilename;
    private String applicationId;
    private String applicationName;
    private String streamingClass;
    private String javaOpts;
    private String variables;
    private String username;
    private String state;
    private Date registeredDate;
    private Date startTime;
    private Date finishTime;
    private Date duration;

    SparkStreaming() {}

    public SparkStreaming(long id, String server, String sparkUserWorkingPath, String jarFileFQP, String jarFilename, String applicationId, String applicationName, String streamingClass, String javaOpts, String variables, String username, String state, Date registeredDate, Date startTime, Date finishTime, Date duration) {
        this.id = id;
        this.server = server;
        this.sparkUserWorkingPath = sparkUserWorkingPath;
        this.jarFileFQP = jarFileFQP;
        this.jarFilename = jarFilename;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.streamingClass = streamingClass;
        this.javaOpts = javaOpts;
        this.variables = variables;
        this.username = username;
        this.state = state;
        this.registeredDate = registeredDate;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.duration = duration;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSparkUserWorkingPath() {
        return sparkUserWorkingPath;
    }

    public void setSparkUserWorkingPath(String sparkUserWorkingPath) {
        this.sparkUserWorkingPath = sparkUserWorkingPath;
    }

    public String getJarFileFQP() {
        return jarFileFQP;
    }

    public void setJarFileFQP(String jarFileFQP) {
        this.jarFileFQP = jarFileFQP;
    }

    public String getJarFilename() {
        return jarFilename;
    }

    public void setJarFilename(String jarFilename) {
        this.jarFilename = jarFilename;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getStreamingClass() {
        return streamingClass;
    }

    public void setStreamingClass(String streamingClass) {
        this.streamingClass = streamingClass;
    }

    public String getJavaOpts() {
        return javaOpts;
    }

    public void setJavaOpts(String javaOpts) {
        this.javaOpts = javaOpts;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "SparkStreaming{" +
                "id=" + id +
                ", server='" + server + '\'' +
                ", sparkUserWorkingPath='" + sparkUserWorkingPath + '\'' +
                ", jarFileFQP='" + jarFileFQP + '\'' +
                ", jarFilename='" + jarFilename + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", streamingClass='" + streamingClass + '\'' +
                ", javaOpts='" + javaOpts + '\'' +
                ", variables='" + variables + '\'' +
                ", username='" + username + '\'' +
                ", state='" + state + '\'' +
                ", registeredDate=" + registeredDate +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", duration=" + duration +
                '}';
    }
}
