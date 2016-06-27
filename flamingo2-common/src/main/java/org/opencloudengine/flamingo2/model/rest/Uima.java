package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * Apache UIMA Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.1.0
 */
public class Uima implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private long id;
    private String processId;
    private String processType;
    private String loggingLevel;
    private String collectionReader;
    private String ip;
    private String annotatorType;
    private String data;
    private Date logDate;

    Uima() {}

    public Uima(long id, String processId, String processType, String loggingLevel, String collectionReader, String ip, String annotatorType, String data, Date logDate) {
        this.id = id;
        this.processId = processId;
        this.processType = processType;
        this.loggingLevel = loggingLevel;
        this.collectionReader = collectionReader;
        this.ip = ip;
        this.annotatorType = annotatorType;
        this.data = data;
        this.logDate = logDate;
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

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(String loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public String getCollectionReader() {
        return collectionReader;
    }

    public void setCollectionReader(String collectionReader) {
        this.collectionReader = collectionReader;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAnnotatorType() {
        return annotatorType;
    }

    public void setAnnotatorType(String annotatorType) {
        this.annotatorType = annotatorType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    @Override
    public String toString() {
        return "Uima{" +
                "id=" + id +
                ", processId='" + processId + '\'' +
                ", processType='" + processType + '\'' +
                ", loggingLevel='" + loggingLevel + '\'' +
                ", collectionReader='" + collectionReader + '\'' +
                ", ip='" + ip + '\'' +
                ", annotatorType='" + annotatorType + '\'' +
                ", data='" + data + '\'' +
                ", logDate=" + logDate +
                '}';
    }
}
