package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;

public class Trends implements Serializable {
    private String time;
    private int count;
    private String searchType;

    public Trends() {
    }

    public Trends(String time, int count, String searchType) {
        this.time = time;
        this.count = count;
        this.searchType = searchType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "Trends{" +
                "time='" + time + '\'' +
                ", count=" + count +
                ", searchType='" + searchType + '\'' +
                '}';
    }
}
