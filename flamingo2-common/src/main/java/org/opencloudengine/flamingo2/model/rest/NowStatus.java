package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;

public class NowStatus implements Serializable {
    private String name;
    private int cnt;

    public NowStatus() {
    }

    public NowStatus(String name, int cnt) {
        this.name = name;
        this.cnt = cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "NowStatus{" +
                "name='" + name + '\'' +
                ", cnt=" + cnt +
                '}';
    }
}
