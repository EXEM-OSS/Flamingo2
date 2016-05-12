package org.opencloudengine.flamingo2.web.util;

import org.junit.Test;
import org.opencloudengine.flamingo2.util.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class TimeSeriesUtilsTest {

    @Test
    public void sum() {
        Date currentTime = new Date();
        Date startTime = DateUtils.addDays(currentTime, -5);

        Map<String, AtomicInteger> timeSeries = TimeSeriesUtils.getTimeSeries(startTime, currentTime);
        TimeSeriesUtils.sum(timeSeries, DateUtils.addHours(currentTime, -42));
        TimeSeriesUtils.sum(timeSeries, DateUtils.addHours(currentTime, -39));
        TimeSeriesUtils.sum(timeSeries, DateUtils.addHours(currentTime, -38));
        TimeSeriesUtils.sum(timeSeries, DateUtils.addHours(currentTime, -109));

        assertEquals(121, timeSeries.size());
    }

}