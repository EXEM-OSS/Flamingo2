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
package org.opencloudengine.flamingo2.engine.scheduler;

/**
 * Job 내부에서 사용하는 Constant Variable.
 *
 * @author Byoung Gon, Kim
 * @since 0.2
 */
public interface JobVariable {

    /**
     * Workflow 변수를 꺼내오기 위한 Key
     */
    public final static String WORKFLOW_VARIABLES = "WORKFLOW_VARIABLES";

    /**
     * Job 변수를 꺼내오기 위한 Key
     */
    public final static String JOB_VARIABLES = "JOB_VARIABLES";

    /**
     * Workflow XML을 꺼내오기 위한 Key
     */
    public final static String WORKFLOW_XML = "WORKFLOW_XML";

    /**
     * Workflow Domain을 꺼내오기 위한 Key
     */
    public final static String WORKFLOW = "WORKFLOW";

    /**
     * Job Domain을 꺼내오기 위한 Key
     */
    public final static String JOB = "JOB";

    /**
     * Workflow Execution Planner를 꺼내오기 위한 Key
     */
    public final static String PLANNER = "PLANNER";

    /**
     * Workflow 실행중 발생한 Exception을 꺼내오기 위한 Key
     */
    public final static String WORKFLOW_EXCEPTION = "WORKFLOW_EXCEPTION";

    /**
     * Workflow의 실행 이력을 꺼내오기 위한 Key
     */
    public final static String WORKFLOW_HISTORY = "WORKFLOW_HISTORY";

    /**
     * ACTION의 실행 이력을 꺼내오기 위한 Key
     */
    public final static String ACTION_HISTORY = "ACTION_HISTORY";

    /**
     * 현재 동작중인 노드의 이름을 꺼내오기 위한 Key
     */
    public final static String ACTION_CURRENT = "ACTION_CURRENT";

    /**
     * 현재 동작중인 노드의 설명을 꺼내오기 위한 Key
     */
    public final static String ACTION_CURRENT_DESC = "ACTION_CURRENT_DESC";

    /**
     * 전체 실행하고자 하는 Action의 개수를 꺼내오기 위한 Key
     */
    public final static String TOTAL_STEPS = "TOTAL_STEPS";

    /**
     * 현재 실행하고 있는 Action의 위치를 꺼내오기 위한 Key
     */
    public final static String CURRENT_STEP = "CURRENT_STEP";

    /**
     * Action Context를 꺼내오기 위한 Key
     */
    public final static String ACTION_CONTEXT = "ACTION_CONTEXT";

    /**
     * Action 실행중 발생한 Exception을 꺼내오기 위한 Key
     */
    public final static String ACTION_EXCEPTION = "ACTION_EXCEPTION";

    /**
     * Action의 출력 경로를 꺼내오기 위한 Key
     */
    public final static String OUTPUT_PATH = "outputPath";

    /**
     * Action의 입력 경로를 꺼내오기 위한 Key
     */
    public final static String INPUT_PATH = "inputPath";

    /**
     * Java의 JAR를 꺼내오기 위한 Key
     */
    public final static String JAVA_JAR = "JAVA_JAR";

    /**
     * MapReduce의 JAR를 꺼내오기 위한 Key
     */
    public final static String MAPREDUCE_JAR = "MAPREDUCE_JAR";

    /**
     * CLASSPATH를 꺼내오기 위한 Key
     */
    public final static String CLASSPATH = "CLASSPATH";

    /**
     * JOB_ID를 꺼내오기 위한 Key
     */
    public final static String JOB_ID = "JOB_ID";

    /**
     * WORKFLOW_ID를 꺼내오기 위한 Key
     */
    public final static String WORKFLOW_ID = "WORKFLOW_ID";

    /**
     * WORKFLOW_ID를 꺼내오기 위한 Key
     */
    public final static String WID = "WID";

    /**
     * 현재 시간을 꺼내오기 위한 Key
     */
    public final static String CURRENT = "CURRENT";

    /**
     * Hadooop Cluster를 꺼내오기 위한 Key
     */
    public final static String HADOOP_CLUSTER = "HADOOP_CLUSTER";

    public final static String HIVE_SERVER = "HIVE_SERVER";

    /**
     * Job Type을 꺼내오기 위한 Key
     */
    public final static String JOB_TYPE = "JOB_TYPE";

    /**
     * Job Name을 꺼내오기 위한 Key
     */
    public final static String JOB_NAME = "JOB_NAME";

    /**
     * Job Key를 꺼내오기 위한 Key
     */
    public final static String JOB_KEY = "JOB_KEY";
}