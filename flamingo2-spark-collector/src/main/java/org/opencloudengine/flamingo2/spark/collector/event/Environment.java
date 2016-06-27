package org.opencloudengine.flamingo2.spark.collector.event;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 17..
 */
public class Environment implements Serializable {
    @SerializedName("JVM Information") private Map jvmInformation;
    @SerializedName("Spark Properties") private Map sparkProperties;
    @SerializedName("System Properties") private Map systemProperties;
    @SerializedName("Classpath Entries") private Map classpathEntries;

    public Map getJvmInformation() {
        return jvmInformation;
    }

    public void setJvmInformation(Map jvmInformation) {
        this.jvmInformation = jvmInformation;
    }

    public Map getSparkProperties() {
        return sparkProperties;
    }

    public void setSparkProperties(Map sparkProperties) {
        this.sparkProperties = sparkProperties;
    }

    public Map getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map systemProperties) {
        this.systemProperties = systemProperties;
    }

    public Map getClasspathEntries() {
        return classpathEntries;
    }

    public void setClasspathEntries(Map classpathEntries) {
        this.classpathEntries = classpathEntries;
    }
}
