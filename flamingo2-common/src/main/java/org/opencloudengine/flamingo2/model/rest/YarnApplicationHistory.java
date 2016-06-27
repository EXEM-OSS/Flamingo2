package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * Yarn Application Execution History Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class YarnApplicationHistory implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private int id;
    private String system;
    private String applicationId;
    private String applicationType;
    private String progress;
    private String queue;
    private int memorySeconds;
    private int rpcPort;
    private String amHost;
    private int usedResourcesMemory;
    private Date startTime;
    private int reservedResourcesVcores;
    private int reservedResourcesMemory;
    private String trackingUrl;
    private String yarnApplicationState;
    private int neededResourcesVcores;
    private String name;
    private int numReservedContainers;
    private int usedResourcesVcores;
    private Date finishTime;
    private int numUsedContainers;
    private String finalApplicationStatus;
    private String user;
    private int neededResourcesMemory;
    private int vcoreSeconds;
    private String diagnostics;
    private String log;
    private Date registeredDate;

    YarnApplicationHistory() {}

    public YarnApplicationHistory(int id, String system, String applicationId, String applicationType, String progress, String queue, int memorySeconds, int rpcPort, String amHost, int usedResourcesMemory, Date startTime, int reservedResourcesVcores, int reservedResourcesMemory, String trackingUrl, String yarnApplicationState, int neededResourcesVcores, String name, int numReservedContainers, int usedResourcesVcores, Date finishTime, int numUsedContainers, String finalApplicationStatus, String user, int neededResourcesMemory, int vcoreSeconds, String diagnostics, String log, Date registeredDate) {
        this.id = id;
        this.system = system;
        this.applicationId = applicationId;
        this.applicationType = applicationType;
        this.progress = progress;
        this.queue = queue;
        this.memorySeconds = memorySeconds;
        this.rpcPort = rpcPort;
        this.amHost = amHost;
        this.usedResourcesMemory = usedResourcesMemory;
        this.startTime = startTime;
        this.reservedResourcesVcores = reservedResourcesVcores;
        this.reservedResourcesMemory = reservedResourcesMemory;
        this.trackingUrl = trackingUrl;
        this.yarnApplicationState = yarnApplicationState;
        this.neededResourcesVcores = neededResourcesVcores;
        this.name = name;
        this.numReservedContainers = numReservedContainers;
        this.usedResourcesVcores = usedResourcesVcores;
        this.finishTime = finishTime;
        this.numUsedContainers = numUsedContainers;
        this.finalApplicationStatus = finalApplicationStatus;
        this.user = user;
        this.neededResourcesMemory = neededResourcesMemory;
        this.vcoreSeconds = vcoreSeconds;
        this.diagnostics = diagnostics;
        this.log = log;
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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public int getMemorySeconds() {
        return memorySeconds;
    }

    public void setMemorySeconds(int memorySeconds) {
        this.memorySeconds = memorySeconds;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    public String getAmHost() {
        return amHost;
    }

    public void setAmHost(String amHost) {
        this.amHost = amHost;
    }

    public int getUsedResourcesMemory() {
        return usedResourcesMemory;
    }

    public void setUsedResourcesMemory(int usedResourcesMemory) {
        this.usedResourcesMemory = usedResourcesMemory;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getReservedResourcesVcores() {
        return reservedResourcesVcores;
    }

    public void setReservedResourcesVcores(int reservedResourcesVcores) {
        this.reservedResourcesVcores = reservedResourcesVcores;
    }

    public int getReservedResourcesMemory() {
        return reservedResourcesMemory;
    }

    public void setReservedResourcesMemory(int reservedResourcesMemory) {
        this.reservedResourcesMemory = reservedResourcesMemory;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public String getYarnApplicationState() {
        return yarnApplicationState;
    }

    public void setYarnApplicationState(String yarnApplicationState) {
        this.yarnApplicationState = yarnApplicationState;
    }

    public int getNeededResourcesVcores() {
        return neededResourcesVcores;
    }

    public void setNeededResourcesVcores(int neededResourcesVcores) {
        this.neededResourcesVcores = neededResourcesVcores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumReservedContainers() {
        return numReservedContainers;
    }

    public void setNumReservedContainers(int numReservedContainers) {
        this.numReservedContainers = numReservedContainers;
    }

    public int getUsedResourcesVcores() {
        return usedResourcesVcores;
    }

    public void setUsedResourcesVcores(int usedResourcesVcores) {
        this.usedResourcesVcores = usedResourcesVcores;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public int getNumUsedContainers() {
        return numUsedContainers;
    }

    public void setNumUsedContainers(int numUsedContainers) {
        this.numUsedContainers = numUsedContainers;
    }

    public String getFinalApplicationStatus() {
        return finalApplicationStatus;
    }

    public void setFinalApplicationStatus(String finalApplicationStatus) {
        this.finalApplicationStatus = finalApplicationStatus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNeededResourcesMemory() {
        return neededResourcesMemory;
    }

    public void setNeededResourcesMemory(int neededResourcesMemory) {
        this.neededResourcesMemory = neededResourcesMemory;
    }

    public int getVcoreSeconds() {
        return vcoreSeconds;
    }

    public void setVcoreSeconds(int vcoreSeconds) {
        this.vcoreSeconds = vcoreSeconds;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    @Override
    public String toString() {
        return "YarnApplicationHistory{" +
                "id=" + id +
                ", system='" + system + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationType='" + applicationType + '\'' +
                ", progress='" + progress + '\'' +
                ", queue='" + queue + '\'' +
                ", memorySeconds=" + memorySeconds +
                ", rpcPort=" + rpcPort +
                ", amHost='" + amHost + '\'' +
                ", usedResourcesMemory=" + usedResourcesMemory +
                ", startTime=" + startTime +
                ", reservedResourcesVcores=" + reservedResourcesVcores +
                ", reservedResourcesMemory=" + reservedResourcesMemory +
                ", trackingUrl='" + trackingUrl + '\'' +
                ", yarnApplicationState='" + yarnApplicationState + '\'' +
                ", neededResourcesVcores=" + neededResourcesVcores +
                ", name='" + name + '\'' +
                ", numReservedContainers=" + numReservedContainers +
                ", usedResourcesVcores=" + usedResourcesVcores +
                ", finishTime=" + finishTime +
                ", numUsedContainers=" + numUsedContainers +
                ", finalApplicationStatus='" + finalApplicationStatus + '\'' +
                ", user='" + user + '\'' +
                ", neededResourcesMemory=" + neededResourcesMemory +
                ", vcoreSeconds=" + vcoreSeconds +
                ", diagnostics='" + diagnostics + '\'' +
                ", log='" + log + '\'' +
                ", registeredDate=" + registeredDate +
                '}';
    }
}
