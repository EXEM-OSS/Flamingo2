package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * Real-Time Spark Streaming History Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class SparkStreamingHistory implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private long id;
    private String applicationId;
    private String applicationName;
    private String username;
    private double cpuUsage;
    private long activeThreads;
    private long memoryUsage;
    private Date registeredDate;

    public SparkStreamingHistory() {}

    public SparkStreamingHistory(long id, String applicationId, String applicationName, String username, double cpuUsage, long activeThreads, long memoryUsage, Date registeredDate) {
        this.id = id;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.username = username;
        this.cpuUsage = cpuUsage;
        this.activeThreads = activeThreads;
        this.memoryUsage = memoryUsage;
        this.registeredDate = registeredDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(long activeThreads) {
        this.activeThreads = activeThreads;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public String toString() {
        return "SparkStreamingHistory{" +
                "id=" + id +
                ", applicationId='" + applicationId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", username='" + username + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", activeThreads=" + activeThreads +
                ", memoryUsage=" + memoryUsage +
                ", registeredDate=" + registeredDate +
                '}';
    }
}
