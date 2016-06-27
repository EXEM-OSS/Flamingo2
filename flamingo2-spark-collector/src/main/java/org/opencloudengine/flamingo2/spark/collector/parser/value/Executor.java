/*
 * Copyright (C) 2011 Flamingo Project (https://github.com/OpenCloudEngine/flamingo2).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.spark.collector.parser.value;

import java.io.Serializable;

/**
 * Created by Hyokun Park on 15. 8. 12..
 */
public class Executor implements Serializable {
    private String appID;
    private String executorID;
    private String address;
    private String port;
    private Long rdd_blocks = Long.valueOf(0);
    private Long max_memory = Long.valueOf(0);
    private Long memory_used = Long.valueOf(0);
    private Long disk_used = Long.valueOf(0);
    private Long active_tasks = Long.valueOf(0);
    private Long failed_tasks = Long.valueOf(0);
    private Long completed_tasks = Long.valueOf(0);
    private Long total_tasks = Long.valueOf(0);
    private Long total_duration = Long.valueOf(0);
    private Long total_input_bytes = Long.valueOf(0);
    private Long total_shuffle_read = Long.valueOf(0);
    private Long total_shuffle_write = Long.valueOf(0);
    private Integer total_cores;
    private String stdout_url;
    private String stderr_url;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getExecutorID() {
        return executorID;
    }

    public void setExecutorID(String executorID) {
        this.executorID = executorID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Long getRdd_blocks() {
        return rdd_blocks;
    }

    public void setRdd_blocks(Long rdd_blocks) {
        this.rdd_blocks = rdd_blocks;
    }

    public Long getMax_memory() {
        return max_memory;
    }

    public void setMax_memory(Long max_memory) {
        this.max_memory = max_memory;
    }

    public Long getMemory_used() {
        return memory_used;
    }

    public void setMemory_used(Long memory_used) {
        this.memory_used = memory_used;
    }

    public Long getDisk_used() {
        return disk_used;
    }

    public void setDisk_used(Long disk_used) {
        this.disk_used = disk_used;
    }

    public Long getActive_tasks() {
        return active_tasks;
    }

    public void setActive_tasks(Long active_tasks) {
        this.active_tasks = active_tasks;
    }

    public Long getFailed_tasks() {
        return failed_tasks;
    }

    public void setFailed_tasks(Long failed_tasks) {
        this.failed_tasks = failed_tasks;
    }

    public Long getCompleted_tasks() {
        return completed_tasks;
    }

    public void setCompleted_tasks(Long completed_tasks) {
        this.completed_tasks = completed_tasks;
    }

    public Long getTotal_tasks() {
        return total_tasks;
    }

    public void setTotal_tasks(Long total_tasks) {
        this.total_tasks = total_tasks;
    }

    public Long getTotal_duration() {
        return total_duration;
    }

    public void setTotal_duration(Long total_duration) {
        this.total_duration = total_duration;
    }

    public Long getTotal_input_bytes() {
        return total_input_bytes;
    }

    public void setTotal_input_bytes(Long total_input_bytes) {
        this.total_input_bytes = total_input_bytes;
    }

    public Long getTotal_shuffle_read() {
        return total_shuffle_read;
    }

    public void setTotal_shuffle_read(Long total_shuffle_read) {
        this.total_shuffle_read = total_shuffle_read;
    }

    public Long getTotal_shuffle_write() {
        return total_shuffle_write;
    }

    public void setTotal_shuffle_write(Long total_shuffle_write) {
        this.total_shuffle_write = total_shuffle_write;
    }

    public Integer getTotal_cores() {
        return total_cores;
    }

    public void setTotal_cores(Integer total_cores) {
        this.total_cores = total_cores;
    }

    public String getStdout_url() {
        return stdout_url;
    }

    public void setStdout_url(String stdout_url) {
        this.stdout_url = stdout_url;
    }

    public String getStderr_url() {
        return stderr_url;
    }

    public void setStderr_url(String stderr_url) {
        this.stderr_url = stderr_url;
    }
}
