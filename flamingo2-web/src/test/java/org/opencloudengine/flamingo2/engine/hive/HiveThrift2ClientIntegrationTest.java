package org.opencloudengine.flamingo2.engine.hive;

import org.apache.hadoop.hive.ql.parse.ParseException;
import org.apache.thrift.TException;
import org.junit.Test;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class HiveThrift2ClientIntegrationTest {

    @Test
    public void execute() throws SQLException, ParseException, IOException, TException {
        EngineConfig engineConfig = new EngineConfig();
        engineConfig.setHiveLegacy(false);
        HiveThrift2Client client = HiveThrift2ClientFactory.lookup(engineConfig, "jdbc:hive2://exo1.cdh.local:10000/default;user=cloudine");

        client.connect();
        client.openSession();

        client.executeAsync("select * from product order by url ASC");
        System.out.println(client.getLog());

        System.out.println(client.getStatus());

        print(client.getResults(10));

        System.out.println(client.getStatus());

        print(client.getResults(10));
        print(client.getResults(10));
        print(client.getResults(10));
        print(client.getResults(10));
        print(client.getResults(10));

        System.out.println(client.getStatus());

        System.out.println(client.getLog());

        client.closeSession();
    }

    private void print(Map[] results) {
        System.out.println("-----------------------------------------------------------------------");
        for (Map map : results) {
            System.out.println(map);
        }
    }

}