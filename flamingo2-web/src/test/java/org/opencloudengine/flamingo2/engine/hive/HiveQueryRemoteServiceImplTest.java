package org.opencloudengine.flamingo2.engine.hive;

import org.junit.Assert;
import org.junit.Test;

public class HiveQueryRemoteServiceImplTest {

    HiveQueryRemoteService hiveQueryRemoteService = new HiveQueryRemoteServiceImpl();

    @Test
    public void validateQuery() throws Exception {
        hiveQueryRemoteService.validateQuery("select * from product");
        hiveQueryRemoteService.validateQuery("select * from asdf");
        try {
            hiveQueryRemoteService.validateQuery("selec * from asdf");
            Assert.assertFalse(false);
        } catch (Exception ex) {
            Assert.assertTrue(true);
        }
    }
}