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
package org.opencloudengine.flamingo2.engine.scheduler.jobs;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.scheduler.JobScheduler;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.DateUtils;
import org.opencloudengine.flamingo2.web.configuration.ConfigurationHolder;
import org.opencloudengine.flamingo2.web.configuration.EngineConfig;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Properties;

/**
 * Workflow Engine의 정보를 수집해서 저장하는 배치 작업.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class EngineJob extends QuartzJobBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(EngineJob.class);

    public static final long MEGA_BYTES = 1024 * 1024;

    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        try {
            logger.debug("Now collecting the system information of Flamingo Engine.");
            ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
            JobScheduler scheduler = applicationContext.getBean(JobScheduler.class);
            JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
            Properties config = applicationContext.getBean("config", Properties.class);

            // 현재 자신의 워크플로우 엔진의 식별자를 확인한다. 테이블에 데이터를 저장하기 위해서는
            // 엔진이 여러개인 경우 식별을 위해서 config.properties 파일의 식별자를 확인한다.
            String clusterName = config.getProperty("system.qualifier");

            // 엔진을 식별한 후 구분자와 시스템명을 꺼내온다.
            EngineConfig engine = ConfigurationHolder.getEngine(clusterName);
            String system = engine.getId();
            String name = engine.getName();

            // JVM Heap 정보를 시각화 하기 위해서 추출한다.
            final Runtime rt = Runtime.getRuntime();
            final long maxMemory = rt.maxMemory() / MEGA_BYTES;
            final long totalMemory = rt.totalMemory() / MEGA_BYTES;
            final long freeMemory = rt.freeMemory() / MEGA_BYTES;
            final long usedMemory = totalMemory - freeMemory;

            // 현재 실행중인 Job과 모든 Job의 개수를 확인한다.
            int executingJobs = scheduler.getCurrentExecutingJobs().size() - 1;
            int allJobs = scheduler.getAllJobs().size() - 1;

            // 8일 이후 데이터는 모두 제거한다.
            jdbcTemplate.update("DELETE FROM FL_CL_ENGINE WHERE reg_dt < DATE_ADD(now(), INTERVAL -8 DAY)");

            // 7일치 데이터를 SELECT 한다.
            Date date = new Date();
            jdbcTemplate.update("INSERT INTO FL_CL_ENGINE (system, name, running, total, jvmMaxMemory, jvmTotalMemory, jvmFreeMemory, jvmUsedMemory, yyyy, mm, dd) VALUES (?,?,?,?,?,?,?,?,?,?,?)", new Object[]{
                    system, name, executingJobs, allJobs, maxMemory, totalMemory, freeMemory, usedMemory, DateUtils.parseDate(date, "yyyy"), DateUtils.parseDate(date, "MM"), DateUtils.parseDate(date, "dd")
            });
        } catch (Exception e) {
            throw new ServiceException("Unable to retreive scheduled jobs", e);
        }
    }
}