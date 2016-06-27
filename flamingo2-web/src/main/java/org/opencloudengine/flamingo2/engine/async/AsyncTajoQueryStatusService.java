package org.opencloudengine.flamingo2.engine.async;

import org.apache.tajo.QueryId;
import org.apache.tajo.client.QueryStatus;
import org.apache.tajo.client.TajoClient;
import org.apache.tajo.client.TajoClientImpl;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.rpc.RpcUtils;
import org.apache.tajo.util.KeyValueSet;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencloudengine.flamingo2.backend.ConsumerNameAware;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.util.EscapeUtils;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.opencloudengine.flamingo2.websocket.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.event.Event;
import reactor.function.Consumer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Park on 15. 7. 20..
 */
@Service
public class AsyncTajoQueryStatusService implements ConsumerNameAware, Consumer<Event<Map>> {

    /**
     * Jackson JSON Object Mapper
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(AsyncHiveLogService.class);

    /**
     * WebSocket Util Send to Client
     */
    @Autowired
    private WebSocketUtil webSocketUtil;

    @Override
    public String getName() {
        return "tajoStatus";
    }

    @Override
    public void accept(Event<Map> event) {
        Map params = event.getData();
        Map messageMap = new HashMap();

        TajoClient client = connect((EngineConfig) params.get("engineConfig"), params.get("database").toString());
        ClientProtos.SubmitQueryResponse response = (ClientProtos.SubmitQueryResponse) params.get("SubmitQueryResponse");
        QueryId queryId = new QueryId(response.getQueryId());

        String uuid = params.get("uuid").toString();
        String websocketKey = EscapeUtils.unescape((String) params.get("websocketKey"));
        String message = "";

        try {
            Thread.sleep(500);

            while (!isEnd(client.getQueryStatus(queryId))) {
                messageMap = new HashMap();
                QueryStatus queryStatus = client.getQueryStatus(queryId);


                if (queryStatus.getState().equals("QUERY_ERROR")) {
                    messageMap.put("isError", true);
                } else {
                    messageMap.put("isError", false);
                }

                messageMap.put("isEnd", false);
                messageMap.put("uuid", uuid);
                messageMap.put("error", queryStatus.getErrorMessage());
                messageMap.put("progress", queryStatus.getProgress());

                message = objectMapper.writeValueAsString(messageMap);
                webSocketUtil.PushNotification(websocketKey, "/topic/tajo", message);
                Thread.sleep(1000);
            }

            QueryStatus queryStatus = client.getQueryStatus(queryId);

            messageMap.put("isEnd", true);
            messageMap.put("uuid", uuid);
            messageMap.put("error", queryStatus.getErrorMessage());
            messageMap.put("progress", queryStatus.getProgress());
            messageMap.put("finish", queryStatus.getFinishTime());
            messageMap.put("runningTime", queryStatus.getFinishTime() - queryStatus.getSubmitTime());
            messageMap.put("status", queryStatus.getState().toString());

            message = objectMapper.writeValueAsString(messageMap);
            webSocketUtil.PushNotification(websocketKey, "/topic/tajo", message);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } finally {
            client.close();
        }
    }

    private boolean isEnd(QueryStatus queryStatus) {
        switch (queryStatus.getState().toString()) {
            case "QUERY_ERROR":
            case "QUERY_KILLED":
            case "QUERY_FAILED":
            case "QUERY_SUCCEEDED":
                return true;
            default:
                return false;
        }
    }

    private TajoClient connect(EngineConfig engineConfig, String database) {
        try {
            KeyValueSet clientProperties = new KeyValueSet();
            return new TajoClientImpl(RpcUtils.createSocketAddr(engineConfig.getTajoAddress(), Integer.parseInt(engineConfig.getTajoPort())), database, clientProperties);
        } catch (Exception e) {
            throw new ServiceException("Unable to connect Tajo Master", e);
        }
    }
}
