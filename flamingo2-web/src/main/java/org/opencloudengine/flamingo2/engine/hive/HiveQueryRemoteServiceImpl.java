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
package org.opencloudengine.flamingo2.engine.hive;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.hive.service.cli.FetchOrientation;
import org.apache.hive.service.cli.FetchType;
import org.opencloudengine.flamingo2.core.cmd.ByteArrayOutputStream;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.Reactor;
import reactor.event.Event;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

@Service
@Primary
public class HiveQueryRemoteServiceImpl implements HiveQueryRemoteService, InitializingBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(HiveQueryRemoteServiceImpl.class);

    @Autowired
    @Qualifier("config")
    private Properties config;

    @Autowired
    private Reactor reactor;

    private Map<String, HiveThrift2Client> clientMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        clientMap = Collections.synchronizedMap(new HashMap());
    }

    @Override
    public void validateQuery(String query) throws ParseException {
        org.apache.hadoop.hive.ql.parse.ParseDriver parseDriver = new org.apache.hadoop.hive.ql.parse.ParseDriver();
        parseDriver.parse(query);
    }

    @Override
    public void executeAsync(Map params) {
        reactor.notify("query", Event.wrap(params));
    }

    @Override
    public void execute(Map params) {
        try {
            String url = MessageFormatter.arrayFormat("{}/{};user={}", new String[]{
                    params.get("hiveserver2Url").toString(),
                    params.get("database").toString(),
                    params.get("hiveserver2Username").toString(),
            }).getMessage();
            logger.info(url);
            EngineConfig engineConfig = (EngineConfig) params.get("engineConfig");
            HiveThrift2Client client = HiveThrift2ClientFactory.lookup(engineConfig, url);
            clientMap.put(params.get("uuid").toString(), client);

            client.connect();
            client.openSession();

            client.execute("use " + params.get("database").toString(), false);
            client.execute(java.net.URLDecoder.decode(params.get("query").toString(), "UTF-8"), true);
        } catch (Exception ex) {
            String message = MessageFormatter.format("Unable to run the Hive Query. [{}]\n{}", params.get("uuid"), params.get("query")).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public String getLog(String uuid) {
        try {
            return clientMap.get(uuid).getLog();
        } catch (Exception ex) {
            String message = MessageFormatter.format("Unable to check the run log of the Hive Query. [{}]", uuid).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public void getLogAsync(Map params) {
        reactor.notify("hiveLog", Event.wrap(params));
    }

    @Override
    public Map[] getResults(String uuid) {
        try {
            return clientMap.get(uuid).getResults(100);
        } catch (Exception ex) {
            String message = MessageFormatter.format("Unable to check the result of the Hive Query. [{}]", uuid).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public boolean isEnd(String uuid) {
        try {
            HiveThrift2Client client = clientMap.get(uuid);
            switch (client.getStatus()) {
                case "FINISHED":
                case "CANCELED":
                case "CLOSED":
                case "KILLED":
                case "ERROR":
                case "FINISHED_STATE":
                    return true;
                default:
                    return false;
            }
        } catch (Exception ex) {
            String message = MessageFormatter.format("Unable to check the result of the Hive Query. [{}]", uuid).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public boolean isExist(String uuid) {
        HiveThrift2Client client = clientMap.get(uuid);
        return client != null;
    }

    @Override
    public String getErrorLog(String uuid) {
        HiveThrift2Client client = clientMap.get(uuid);
        if (client == null) {
            return "";
        }
        return client.getError();
    }

    @Override
    public void remove(String uuid) {
        HiveThrift2Client removed = clientMap.remove(uuid);
        try {
            removed.closeSession();
        } catch (Exception ex) {
        }
    }

    @Override
    public String getStatus(String uuid) {
        try {
            HiveThrift2Client client = clientMap.get(uuid);
            return client.getStatus();
        } catch (Exception ex) {
            ex.printStackTrace();
            String message = MessageFormatter.format("Unable to check whether the end of the Hive Query. [{}]", uuid).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public Map[] getPage(Map params) {
        try {
            return clientMap.get(params.get("uuid").toString()).getResults(Integer.parseInt(params.get("limit").toString()));
        } catch (Exception ex) {
            String message = MessageFormatter.format("Unable to select a result of Hive Query. [{}]", params.get("uuid").toString()).getMessage();
            throw new ServiceException(message, ex);
        }
    }

    @Override
    public byte[] getResultToCsv(String uuid) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        CSVWriter writer = new CSVWriter(outputStreamWriter);

        try {
            int i, j;

            Map[] resultMap = clientMap.get(uuid).getResults(FetchOrientation.FETCH_FIRST, 1000, FetchType.QUERY_OUTPUT);

            for (i = 0; i < resultMap.length; i++) {
                Map result = resultMap[i];

                Object[] objects = result.values().toArray();
                String[] values = new String[objects.length];
                for (j = 0; j < objects.length; j++) {
                    values[j] = objects[j].toString();
                }
                writer.writeNext(values);
            }

            do {
                resultMap = clientMap.get(uuid).getResults(FetchOrientation.FETCH_NEXT, 1000, FetchType.QUERY_OUTPUT);
                for (i = 0; i < resultMap.length; i++) {
                    Map result = resultMap[i];

                    Object[] objects = result.values().toArray();
                    String[] values = new String[objects.length];
                    for (j = 0; j < objects.length; j++) {
                        values[j] = objects[j].toString();
                    }
                    writer.writeNext(values);
                }
            } while (resultMap.length > 0);

            return outputStream.toByteArray();

        } catch (Exception ex) {
            String message = MessageFormatter.format("The results of the Hive Query can not be converted to CSV. [{}]", uuid).getMessage();
            ex.printStackTrace();
            throw new ServiceException(message, ex);
        } finally {
            writer.close();
        }
    }

    @Override
    public void removeAll(List uuids) {
        for (Object obj : uuids) {
            String uuid = (String) obj;
            try {
                HiveThrift2Client client = clientMap.remove(uuid);
                client.closeSession();
            } catch (Exception ex) {
            }
        }
    }

    public Map<String, HiveThrift2Client> getClientMap() {
        return clientMap;
    }
}
