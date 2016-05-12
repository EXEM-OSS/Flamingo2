package org.opencloudengine.flamingo2.web.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZkUtilsTest {

    TestingServer zkTestServer;

    CuratorFramework client;

    @Before
    public void startZookeeper() throws Exception {
        String dataDirectory = System.getProperty("java.io.tmpdir");
        File dir = new File(dataDirectory, "zookeeper").getAbsoluteFile();
        zkTestServer = new TestingServer(12181, dir);

        client = ZkUtils.createClient(zkTestServer.getConnectString(), new RetryOneTime(2000));
        client.start();
    }

    @After
    public void stopZookeeper() throws IOException {
        client.close();
        zkTestServer.stop();
    }

    public void test() throws Exception {
        System.out.println(ZkUtils.getChildren(client, "/"));
        System.out.println(ZkUtils.getChildren(client, "/zookeeper"));
        System.out.println(new String(ZkUtils.getData(client, "/zookeeper")));

        System.out.println(ZkUtils.getChildren(client, "/zookeeper/quota"));
        System.out.println(new String(ZkUtils.getData(client, "/zookeeper/quota")));
    }

    @Test
    public void createAndExistsAndDelete() throws Exception {
        boolean exists = ZkUtils.exists(client, "/flamingo", false);
        if (exists) {
            ZkUtils.delete(client, "/flamingo");
        }

        assertFalse(ZkUtils.exists(client, "/flamingo", false));

        ZkUtils.create(client, "/flamingo", "asdfasdfadsfasdf".getBytes());
        System.out.println(new String(ZkUtils.getData(client, "/flamingo")));

        assertTrue(ZkUtils.exists(client, "/flamingo", false));

        ZkUtils.delete(client, "/flamingo");
        assertFalse(ZkUtils.exists(client, "/flamingo", false));
    }


    public void createAndAddWatcher() throws Exception {
        boolean exists = ZkUtils.exists(client, "/flamingo", false);
        if (exists) {
            ZkUtils.delete(client, "/flamingo/nodes");
            ZkUtils.delete(client, "/flamingo");
        }

        assertFalse(ZkUtils.exists(client, "/flamingo", false));

        ZkUtils.create(client, "/flamingo", "".getBytes());
        ZkUtils.create(client, "/flamingo/nodes", "".getBytes());
        assertTrue(ZkUtils.exists(client, "/flamingo/nodes", false));

        client.getChildren().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                System.out.println(path + " is " + watchedEvent.getType());
            }
        }).forPath("/flamingo/nodes");

        client.getUnhandledErrorListenable().addListener(
                new UnhandledErrorListener() {
                    @Override
                    public void unhandledError(String message, Throwable e) {
                        e.printStackTrace();
                    }
                }
        );

        ZkUtils.delete(client, "/flamingo/nodes");
        ZkUtils.delete(client, "/flamingo");
        assertFalse(ZkUtils.exists(client, "/flamingo", false));
    }

}