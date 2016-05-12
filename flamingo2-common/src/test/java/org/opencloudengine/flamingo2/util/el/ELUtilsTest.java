package org.opencloudengine.flamingo2.util.el;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class ELUtilsTest {

    @Test
    public void resolve() throws Exception {
        Properties props = new Properties();
        props.setProperty("flamingo.home", "${catalina.home}");
        props.setProperty("catalina.home", "/data1/cloudine/web");
        props.setProperty("visual.rdata.tmp", "${flamingo.home}/working/viz/temp");
        props.setProperty("visual.output", "${flamingo.home}/working/viz/output");
        props.setProperty("flamingo.workflow.logging.dir", "${flamingo.home}/working/logs");
        props.setProperty("artifact.cache.path", "${flamingo.home}/working/cache");

        Assert.assertEquals("/data1/cloudine/web/working/cache", ELUtils.resolve(props, "${artifact.cache.path}"));
    }
}