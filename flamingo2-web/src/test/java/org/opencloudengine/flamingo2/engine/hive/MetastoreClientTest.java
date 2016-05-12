package org.opencloudengine.flamingo2.engine.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.thrift.TException;

public class MetastoreClientTest {

    public static void main(String[] args) throws HiveException, TException {
        Configuration configuration = new Configuration();
        configuration.set("hive.metastore.uris", "thrift://exo5.cdh.local:9083");
        configuration.set("hadoop.security.authentication", "simple");
        HiveConf conf = new HiveConf(configuration, MetastoreClientTest.class);

        IMetaStoreClient msc = Hive.get(conf, false).getMSC();
        System.out.println(msc.getAllTables("default"));

        Table table = msc.getTable("default", "omniture");
        System.out.println(table);
        System.out.println(table.getViewOriginalText());
        System.out.println(table.getViewExpandedText());

        Table table1 = msc.getTable("default", "omniturelogs");
        System.out.println(table1);
        System.out.println(msc.getSchema("default", "omniture"));
        msc.close();
    }

}
