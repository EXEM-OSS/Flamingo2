package org.opencloudengine.flamingo2.spark.collector.parser.value;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 20..
 */
public class TimelineNode implements Serializable {
    private String id;

    private String start;

    private String end;

    private String className;

    private String content;

    private String group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
