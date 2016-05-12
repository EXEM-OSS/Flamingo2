package org.opencloudengine.flamingo2.engine.hadoop;

public class HistoryServerServiceTester {

    public static void main(String[] args) {
        HistoryServerRemoteService service = new HistoryServerRemoteServiceImpl();

        System.out.println(service.getHistoryServerInfo("exo1.cdh.local:19888"));
        System.out.println(service.getJob("exo1.cdh.local:19888", "job_1423442407955_0173"));
        System.out.println(service.getJobConf("exo1.cdh.local:19888", "job_1423442407955_0173"));
        System.out.println(service.getCounters("exo1.cdh.local:19888", "job_1423442407955_0173"));
        System.out.println(service.getJobToList("exo1.cdh.local:19888", "job_1423442407955_0173"));
        System.out.println(service.getTasks("exo1.cdh.local:19888", "job_1423442407955_0173"));
    }

}