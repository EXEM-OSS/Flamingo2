package org.opencloudengine.flamingo2.engine.scheduler.jobs;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;
import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.history.TaskHistory;
import org.opencloudengine.flamingo2.engine.history.TaskHistoryRemoteService;
import org.opencloudengine.flamingo2.engine.history.WorkflowHistoryRemoteService;
import org.opencloudengine.flamingo2.model.rest.State;
import org.opencloudengine.flamingo2.model.rest.WorkflowHistory;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.sf.expectit.matcher.Matchers.regexp;

/**
 * Workflow Engine의 Running Job을 확인해서 실행이 끝난 작업을 Complete 처리하는 배치 작업.
 *
 * @author Jae Hee, Lee
 * @since 2.0
 */
public class RunningJob extends QuartzJobBean {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(RunningJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Process process = null;
        try {
            logger.debug("Now check the running jobs of Flamingo Engine.");
            ApplicationContext applicationContext = ApplicationContextRegistry.getApplicationContext();
            TaskHistoryRemoteService taskHistoryRemoteService = applicationContext.getBean(TaskHistoryRemoteService.class);

            List<TaskHistory> taskHistories = taskHistoryRemoteService.selectRunning();

            Expect expect;
            process = Runtime.getRuntime().exec("/bin/sh");

            expect = new ExpectBuilder()
                    .withInputs(process.getInputStream())
                    .withOutput(process.getOutputStream())
                    .withTimeout(1, TimeUnit.SECONDS)
                    .withExceptionOnFailure()
                    .build();
            for (TaskHistory taskHistory : taskHistories) {
                File pidFile = new File(taskHistory.getLogDirectory() + "/PID");
                if (!pidFile.exists()) continue;

                try {
                    expect.sendLine("cat " + pidFile.getAbsolutePath());
                    String[] lines = expect.expect(regexp("[0-9]+")).getInput().split("\n");
                    if (lines.length == 1) continue;

                    String pid = lines[1];// cat 결과 앞부분에 \n 이 붙어있어서 1번째
                    expect.sendLine("ps -p " + pid);
                    expect.expect(regexp(".+")).getInput();

                    File codeFile = new File(taskHistory.getLogDirectory() + "/CODE");
                    if (!codeFile.exists()) continue;

                    expect.sendLine("cat " + codeFile.getAbsolutePath());
                    expect.expect(regexp("0")).getInput();
                    taskHistory.setStatus(State.SUCCEEDED.toString());
                    taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    taskHistory.setStatus(State.FAILED.toString());
                    taskHistoryRemoteService.updateByTaskIdAndIdentifier(taskHistory);
                }
            }

            expect.close();

            WorkflowHistoryRemoteService workflowHistoryRemoteService = applicationContext.getBean(WorkflowHistoryRemoteService.class);
            List<WorkflowHistory> workflowHistories = workflowHistoryRemoteService.selectRunning();

            for (WorkflowHistory workflowHistory : workflowHistories) {
                List<TaskHistory> taskHistoryList = taskHistoryRemoteService.selectByIdentifier(workflowHistory.getJobStringId());
                if (taskHistoryList.size() == 0) {
                    workflowHistory.setStatus(State.FAILED);
                } else {
                    workflowHistory.setStatus(State.SUCCEEDED);

                    for (TaskHistory taskHistory : taskHistoryList) {
                        if ("FAILED".equals(taskHistory.getStatus())) {
                            workflowHistory.setStatus(State.FAILED);
                            break;
                        } else if ("RUNNING".equals(taskHistory.getStatus())) {
                            workflowHistory.setStatus(State.RUNNING);
                            break;
                        }
                    }
                }

                workflowHistoryRemoteService.updateStatus(workflowHistory);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceException("Unable to retreive scheduled jobs", ex);
        } finally {
            process.destroy();
        }
    }
}
