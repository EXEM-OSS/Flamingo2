package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;

public class Top10 implements Serializable {
    private int no;
    private String name;
    private int cnt;

    public Top10() {
    }

    public Top10(int no, String name, int cnt) {
        this.no = no;
        this.name = name;
        this.cnt = cnt;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
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
        return "Top10{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", cnt=" + cnt +
                '}';
    }
}
