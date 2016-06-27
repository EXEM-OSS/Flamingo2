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
package org.opencloudengine.flamingo2.spark.collector.parser;

import org.opencloudengine.flamingo2.spark.collector.event.Environment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyokun Park on 15. 8. 10..
 */
public interface EventParserService {
    public List<Map> getAllApplications() throws IOException;

    public ApplicationAggregate getApplicationAggregate(String fileName) throws IOException;

    public Environment getEnvironment(String appid) throws IOException;

    public List<String> getFilePathList();

    public Map getTaskTimeline(Map params);
}
