package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * MapReduce Execution History Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class MapReduceHistory implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private int id;
    private String system;
    private String jobId;
    private String name;
    private String queue;
    private String user;
    private String state;
    private String username;
    private String type;
    private int mapsTotal;
    private int mapsCompleted;
    private int reducesTotal;
    private int reducesCompleted;
    private int failedMapAttempts;
    private int killedMapAttempts;
    private int failedReduceAttempts;
    private int killedReduceAttempts;
    private int avgMapTime;
    private int avgShuffleTime;
    private int avgMergeTime;
    private int avgReduceTime;
    private Date submitTime;
    private Date startTime;
    private Date finishTime;
    private String counters;
    private String configuration;
    private String tasks;
    private Date registeredDate;

    public MapReduceHistory() {
    }

    public MapReduceHistory(int id, String system, String jobId, String name, String queue, String user, String state, String username, String type, int mapsTotal, int mapsCompleted, int reducesTotal, int reducesCompleted, int failedMapAttempts, int killedMapAttempts, int failedReduceAttempts, int killedReduceAttempts, int avgMapTime, int avgShuffleTime, int avgMergeTime, int avgReduceTime, Date submitTime, Date startTime, Date finishTime, String counters, String configuration, String tasks, Date registeredDate) {
        this.id = id;
        this.system = system;
        this.jobId = jobId;
        this.name = name;
        this.queue = queue;
        this.user = user;
        this.state = state;
        this.username = username;
        this.type = type;
        this.mapsTotal = mapsTotal;
        this.mapsCompleted = mapsCompleted;
        this.reducesTotal = reducesTotal;
        this.reducesCompleted = reducesCompleted;
        this.failedMapAttempts = failedMapAttempts;
        this.killedMapAttempts = killedMapAttempts;
        this.failedReduceAttempts = failedReduceAttempts;
        this.killedReduceAttempts = killedReduceAttempts;
        this.avgMapTime = avgMapTime;
        this.avgShuffleTime = avgShuffleTime;
        this.avgMergeTime = avgMergeTime;
        this.avgReduceTime = avgReduceTime;
        this.submitTime = submitTime;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.counters = counters;
        this.configuration = configuration;
        this.tasks = tasks;
        this.registeredDate = registeredDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMapsTotal() {
        return mapsTotal;
    }

    public void setMapsTotal(int mapsTotal) {
        this.mapsTotal = mapsTotal;
    }

    public int getMapsCompleted() {
        return mapsCompleted;
    }

    public void setMapsCompleted(int mapsCompleted) {
        this.mapsCompleted = mapsCompleted;
    }

    public int getReducesTotal() {
        return reducesTotal;
    }

    public void setReducesTotal(int reducesTotal) {
        this.reducesTotal = reducesTotal;
    }

    public int getReducesCompleted() {
        return reducesCompleted;
    }

    public void setReducesCompleted(int reducesCompleted) {
        this.reducesCompleted = reducesCompleted;
    }

    public int getFailedMapAttempts() {
        return failedMapAttempts;
    }

    public void setFailedMapAttempts(int failedMapAttempts) {
        this.failedMapAttempts = failedMapAttempts;
    }

    public int getKilledMapAttempts() {
        return killedMapAttempts;
    }

    public void setKilledMapAttempts(int killedMapAttempts) {
        this.killedMapAttempts = killedMapAttempts;
    }

    public int getFailedReduceAttempts() {
        return failedReduceAttempts;
    }

    public void setFailedReduceAttempts(int failedReduceAttempts) {
        this.failedReduceAttempts = failedReduceAttempts;
    }

    public int getKilledReduceAttempts() {
        return killedReduceAttempts;
    }

    public void setKilledReduceAttempts(int killedReduceAttempts) {
        this.killedReduceAttempts = killedReduceAttempts;
    }

    public int getAvgMapTime() {
        return avgMapTime;
    }

    public void setAvgMapTime(int avgMapTime) {
        this.avgMapTime = avgMapTime;
    }

    public int getAvgShuffleTime() {
        return avgShuffleTime;
    }

    public void setAvgShuffleTime(int avgShuffleTime) {
        this.avgShuffleTime = avgShuffleTime;
    }

    public int getAvgMergeTime() {
        return avgMergeTime;
    }

    public void setAvgMergeTime(int avgMergeTime) {
        this.avgMergeTime = avgMergeTime;
    }

    public int getAvgReduceTime() {
        return avgReduceTime;
    }

    public void setAvgReduceTime(int avgReduceTime) {
        this.avgReduceTime = avgReduceTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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

    public String getCounters() {
        return counters;
    }

    public void setCounters(String counters) {
        this.counters = counters;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public String toString() {
        return "MapReduceHistory{" +
                "id=" + id +
                ", system='" + system + '\'' +
                ", jobId='" + jobId + '\'' +
                ", name='" + name + '\'' +
                ", queue='" + queue + '\'' +
                ", user='" + user + '\'' +
                ", state='" + state + '\'' +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", mapsTotal=" + mapsTotal +
                ", mapsCompleted=" + mapsCompleted +
                ", reducesTotal=" + reducesTotal +
                ", reducesCompleted=" + reducesCompleted +
                ", failedMapAttempts=" + failedMapAttempts +
                ", killedMapAttempts=" + killedMapAttempts +
                ", failedReduceAttempts=" + failedReduceAttempts +
                ", killedReduceAttempts=" + killedReduceAttempts +
                ", avgMapTime=" + avgMapTime +
                ", avgShuffleTime=" + avgShuffleTime +
                ", avgMergeTime=" + avgMergeTime +
                ", avgReduceTime=" + avgReduceTime +
                ", submitTime=" + submitTime +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", counters='" + counters + '\'' +
                ", configuration='" + configuration + '\'' +
                ", tasks='" + tasks + '\'' +
                ", registeredDate=" + registeredDate +
                '}';
    }
}
