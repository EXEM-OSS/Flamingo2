package org.opencloudengine.flamingo2.engine.hadoop;

import org.opencloudengine.flamingo2.web.configuration.EngineConfig;

public class ResourceManagerServiceTester {

    public static void main(String[] args) {
        EngineConfig config = new EngineConfig();
        config.setRmAgentAddress("exo1.cdh.local");
        config.setRmAgentPort(18032);
        ResourceManagerRemoteServiceImpl service = new ResourceManagerRemoteServiceImpl();

        System.out.println(service.getNodes(config));
    }

}