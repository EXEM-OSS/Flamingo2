/**
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.flamingo2.engine.history;

import org.opencloudengine.flamingo2.engine.fs.FileSystemUtils;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.StringUtils;

import java.io.File;
import java.util.List;

public class TaskHistoryRemoteServiceImpl implements TaskHistoryRemoteService {

    @Override
    public List<TaskHistory> selectByIdentifier(String identifier) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        return taskHistoryRepository.selectByIdentifier(identifier);
    }

    @Override
    public TaskHistory selectByTaskIdAndIdentifier(TaskHistory taskHistory) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        return taskHistoryRepository.selectByTaskIdAndIdentifier(taskHistory);
    }

    @Override
    public void updateByTaskIdAndIdentifier(TaskHistory taskHistory) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        taskHistoryRepository.updateByTaskIdAndIdentifier(taskHistory);
    }

    @Override
    public TaskHistory select(Long id) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        return taskHistoryRepository.select(id);
    }


    @Override
    public String getTaskLog(String identifier, String taskId) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory(identifier, taskId));
        String logDirectory = taskHistory.getLogDirectory() + "/task.log";

        if (!StringUtils.isEmpty(logDirectory) && new File(logDirectory).exists()) {
            return FileSystemUtils.load(logDirectory);
        }
        return "";
    }

    @Override
    public String getScript(String identifier, String taskId) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory(identifier, taskId));
        String script = null;
        String bash = taskHistory.getLogDirectory() + "/script.sh";
        String hive = taskHistory.getLogDirectory() + "/script.hive";
        String pig = taskHistory.getLogDirectory() + "/script.pig";
        String r = taskHistory.getLogDirectory() + "/script.R";
        String python = taskHistory.getLogDirectory() + "/script.py";

        if (new File(hive).exists()) {
            script = hive;
        } else if (new File(pig).exists()) {
            script = pig;
        } else if (new File(r).exists()) {
            script = r;
        } else if (new File(python).exists()) {
            script = python;
        } else if (new File(bash).exists()) {
            script = bash;
        }

        try {
            return FileSystemUtils.load(script);
        } catch (Exception ex) {
            return "";
        }
    }

    @Override
    public String getCommand(String identifier, String taskId) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory(identifier, taskId));

        StringBuilder builder = new StringBuilder();
        String command = taskHistory.getLogDirectory() + "/command.sh";
        if (!StringUtils.isEmpty(command) && new File(command).exists()) {
            builder.append("[Command]").append("\n\n");
            String load = FileSystemUtils.load(command);
            builder.append(load);
            builder.append("\n\n");
        }

        String script = taskHistory.getLogDirectory() + "/script.sh";
        if (!StringUtils.isEmpty(script) && new File(script).exists()) {
            builder.append("[Script]").append("\n\n");
            String load = FileSystemUtils.load(script);
            builder.append(load);
            builder.append("\n\n");
        }
        return builder.toString();
    }

    @Override
    public String getError(String identifier, String taskId) {
        TaskHistoryRepository taskHistoryRepository = ApplicationContextRegistry.getApplicationContext().getBean(TaskHistoryRepository.class);
        TaskHistory taskHistory = taskHistoryRepository.selectByTaskIdAndIdentifier(new TaskHistory(identifier, taskId));
        String error = taskHistory.getLogDirectory() + "/err.log";

        if (!StringUtils.isEmpty(error) && new File(error).exists()) {
            return FileSystemUtils.load(error);
        }
        return "";
    }
}
