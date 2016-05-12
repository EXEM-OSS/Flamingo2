package org.opencloudengine.flamingo2.model.rest;

import java.io.Serializable;

public class Trend implements Serializable {
    private String time;
    private int data1;
    private int data2;
    private int data3;
    private int data4;
    private int data5;
    private int data6;
    private int data7;
    private int data8;
    private int data9;
    private int data10;

    public Trend(String time, int data1, int data2, int data3, int data4, int data5, int data6, int data7, int data8, int data9, int data10) {
        this.time = time;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.data5 = data5;
        this.data6 = data6;
        this.data7 = data7;
        this.data8 = data8;
        this.data9 = data9;
        this.data10 = data10;
    }

    public int getData(int i) {
        if (i == 1) {
            return data1;
        } else if (i == 2) {
            return data2;
        } else if (i == 3) {
            return data3;
        } else if (i == 4) {
            return data4;
        } else if (i == 5) {
            return data5;
        } else if (i == 6) {
            return data6;
        } else if (i == 7) {
            return data7;
        } else if (i == 8) {
            return data8;
        } else if (i == 9) {
            return data9;
        } else {
            return data10;
        }
    }

    public void setData(int i, int value) {
        if (i == 1) {
            this.data1 = value;
        } else if (i == 2) {
            this.data2 = value;
        } else if (i == 3) {
            this.data3 = value;
        } else if (i == 4) {
            this.data4 = value;
        } else if (i == 5) {
            this.data5 = value;
        } else if (i == 6) {
            this.data6 = value;
        } else if (i == 7) {
            this.data7 = value;
        } else if (i == 8) {
            this.data8 = value;
        } else if (i == 9) {
            this.data9 = value;
        } else {
            this.data10 = value;
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }

    public int getData3() {
        return data3;
    }

    public void setData3(int data3) {
        this.data3 = data3;
    }

    public int getData4() {
        return data4;
    }

    public void setData4(int data4) {
        this.data4 = data4;
    }

    public int getData5() {
        return data5;
    }

    public void setData5(int data5) {
        this.data5 = data5;
    }

    public int getData6() {
        return data6;
    }

    public void setData6(int data6) {
        this.data6 = data6;
    }

    public int getData7() {
        return data7;
    }

    public void setData7(int data7) {
        this.data7 = data7;
    }

    public int getData8() {
        return data8;
    }

    public void setData8(int data8) {
        this.data8 = data8;
    }

    public int getData9() {
        return data9;
    }

    public void setData9(int data9) {
        this.data9 = data9;
    }

    public int getData10() {
        return data10;
    }

    public void setData10(int data10) {
        this.data10 = data10;
    }

    @Override
    public String toString() {
        return "Trend{" +
                "time='" + time + '\'' +
                ", data1=" + data1 +
                ", data2=" + data2 +
                ", data3=" + data3 +
                ", data4=" + data4 +
                ", data5=" + data5 +
                ", data6=" + data6 +
                ", data7=" + data7 +
                ", data8=" + data8 +
                ", data9=" + data9 +
                ", data10=" + data10 +
                '}';
    }
}
