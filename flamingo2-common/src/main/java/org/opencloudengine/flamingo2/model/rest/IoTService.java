package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;
import java.util.Date;

/**
 * Real-Time IoT Service Domain Object.
 *
 * @author Myeongha KIM
 * @since 2.0
 */
public class IoTService implements Serializable {

    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 1;

    private long id;
    private String serviceId;
    private String serviceName;
    private String serviceTypeId;
    private String serviceTypeName;
    private String deviceTypeId;
    private String deviceTypeName;
    private String columnsType;
    private String columnName;
    private String columnType;
    private short filtering;
    private short masking;
    private Date workDate;
    private int orderby;
    private String treeId;
    private String text;
    private String nodeName;
    private boolean leaf;

    public IoTService() {
    }

    public IoTService(long id, String serviceId, String serviceName, String serviceTypeId, String serviceTypeName, String deviceTypeId, String deviceTypeName, String columnsType, String columnName, String columnType, short filtering, short masking, Date workDate, int orderby, String treeId, String text, String nodeName, boolean leaf) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = serviceTypeName;
        this.deviceTypeId = deviceTypeId;
        this.deviceTypeName = deviceTypeName;
        this.columnsType = columnsType;
        this.columnName = columnName;
        this.columnType = columnType;
        this.filtering = filtering;
        this.masking = masking;
        this.workDate = workDate;
        this.orderby = orderby;
        this.treeId = treeId;
        this.text = text;
        this.nodeName = nodeName;
        this.leaf = leaf;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getColumnsType() {
        return columnsType;
    }

    public void setColumnsType(String columnsType) {
        this.columnsType = columnsType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public short getFiltering() {
        return filtering;
    }

    public void setFiltering(short filtering) {
        this.filtering = filtering;
    }

    public short getMasking() {
        return masking;
    }

    public void setMasking(short masking) {
        this.masking = masking;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public String toString() {
        return "IoTService{" +
                "id=" + id +
                ", serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceTypeId='" + serviceTypeId + '\'' +
                ", serviceTypeName='" + serviceTypeName + '\'' +
                ", deviceTypeId='" + deviceTypeId + '\'' +
                ", deviceTypeName='" + deviceTypeName + '\'' +
                ", columnsType='" + columnsType + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", filtering=" + filtering +
                ", masking=" + masking +
                ", workDate=" + workDate +
                ", orderby=" + orderby +
                ", treeId='" + treeId + '\'' +
                ", text='" + text + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", leaf=" + leaf +
                '}';
    }
}
