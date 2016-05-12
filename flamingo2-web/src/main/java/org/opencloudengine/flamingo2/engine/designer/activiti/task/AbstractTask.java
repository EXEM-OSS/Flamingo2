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
package org.opencloudengine.flamingo2.engine.designer.activiti.task;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.engine.designer.activiti.WorkflowService;
import org.opencloudengine.flamingo2.model.rest.User;
import org.opencloudengine.flamingo2.model.rest.Workflow;
import org.opencloudengine.flamingo2.util.ApplicationContextRegistry;
import org.opencloudengine.flamingo2.util.JsonUtils;
import org.opencloudengine.flamingo2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.graph.Transition;

import java.util.*;

import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

public abstract class AbstractTask extends DefaultActivity {

    public final static String SEPARATOR = ",";
    /**
     * Task가 실행하는 시점에서 Task의 각종 파라미터를 Key Value넣은 자료형.
     */
    protected TypedMap<String, String> params;
    /**
     * 워크플로우 디자이너의 워크플로우 변수가
     */
    protected TypedMap<String, String> workflowVariables;
    /**
     * 워크플로우 변수와 파라미터를 결합한 자료형.
     */
    protected TypedMap<String, String> mergedParams;
    /**
     * 현재 액티비티 Subflow 여부. 디폴트 false.
     */
    protected boolean isSubflow = false;
    /**
     * 서브워크플로우 트리아이디
     */
    protected Long subflowTreeId;
    /**
     * 서브워크플로우 이름
     */
    protected String subflowName;
    /**
     * 서브워크플로우 원본 변수 < UI 에서 입력한 서브워크플로우 변수 순서로 overwrite 한 자료형.
     */
    protected TypedMap<String, String> subflowParams;
    /**
     * 서브워크플로우 원본 변수 < UI 에서 입력한 서브워크플로우 변수 < 현재 워크플로우 변수 순서로 overwrite 한 자료형.
     */
    protected TypedMap<String, String> subflowMergedParams;
    /**
     * 게이트웨이 벡터의 Async 정보를 담은 자료형.
     */
    protected List<Map> parallelVectors;
    /**
     * 현재 액티비티의 Async 동작 여부. 기본값 false;
     */
    protected boolean isAsync = false;
    /**
     * 현재 액티비티의 를 실행중인 유저 정보.
     */
    protected User user;
    /**
     * 실행중인 워크플로우 객체
     */
    ProcessInstance instance;
    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(AbstractTask.class);

    @Override
    protected void executeActivity(final ProcessInstance instance) throws Exception {

        this.instance = instance;

        this.user = (User) instance.get("user");

        Map variable = (Map) instance.get("variable");

        String taskId = this.getTaskId();
        Map local = (Map) variable.get("local");
        if (local.containsKey(taskId)) {
            this.params = new TypedMap((Map) local.get(taskId));
        }
        Map global = (Map) variable.get("global");
        List parallelVectors = (List) variable.get("parallelVectors");

        this.parallelVectors = parallelVectors;

        this.workflowVariables = new TypedMap(global);

        this.mergedParams = new TypedMap();
        this.mergedParams.putAll(this.workflowVariables);
        this.mergedParams.putAll(this.params);


        //게이트웨이 벡터일람으로부터 자신이 Async 인지 Sync 인지 판별한다.

        //서비스 타스크의 인커밍 트랜젝션은 하나여야 한다.
        if (getIncomingTransitions().size() > 1)
            throw new Exception("Incoming transaction of " + getTaskId() + " must have one. Current " + getIncomingTransitions().size() + " transactions occurred.");

        Transition transition = getIncomingTransitions().get(0);
        Activity sourceActivity = transition.getSourceActivity();
        String sourceId = sourceActivity.getTracingTag();

        for (int i = 0; i < parallelVectors.size(); i++) {
            Map parallelVector = (Map) parallelVectors.get(i);
            String fromNode = parallelVector.get("fromNode").toString();
            String toNode = parallelVector.get("toNode").toString();
            boolean parallel = (boolean) parallelVector.get("parallel");
            if (fromNode.equals(sourceId) && toNode.equals(this.getTaskId())) {
                this.isAsync = parallel;
            }
        }

        /**
         * 이 타스크가 서브플로우 타스크일경우 서브플로우 변수를 구하고, 고정값이 아닌 변수에 대해 현재의 워크플로우 변수를 overwrite 시킨 자료형을 구한다.
         */
        if (this.getClass().getName().equals(SubflowTask.class.getName())) {
            this.isSubflow = true;

            Long treeId = Long.parseLong(getParams().getString("treeId"));
            this.subflowTreeId = treeId;


            //서브플로우의 원본 전역변수를 구한다.
            ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
            WorkflowService workflowService = context.getBean(WorkflowService.class);
            Workflow workflow = workflowService.getByTreeId(treeId);
            this.subflowName = workflow.getWorkflowName();

            Map subflowvars = getSubWorkflowVariables(workflow);
            Map subflowGlobal = (Map) subflowvars.get("global");

            //UI 에서 입력한 서브플로우 변수를 결합가능 형태로 변경한다.
            Map subflowmap = new HashMap();
            subflowmap.putAll(subflowGlobal);
            subflowmap.putAll(getGridSubflowVariables());
            this.subflowParams = new TypedMap(subflowmap);


            //protect 가 걸리지 않은 변수에 한해 현재 워크플로우 변수를 오버라이딩 한다.
            Map subflowMergedmap = new HashMap();
            subflowMergedmap.putAll(subflowmap);

            Set<String> parentvarkeys = this.workflowVariables.keySet();
            for (String parentvarkey : parentvarkeys) {
                if (!isProtectedVariable(parentvarkey)) {
                    subflowMergedmap.put(parentvarkey, this.workflowVariables.get(parentvarkey));
                }
            }
            this.subflowMergedParams = new TypedMap(subflowMergedmap);
        }

        if (logger.isDebugEnabled()) {
            Set<String> names = params.keySet();
            for (String name : names) {

                logger.debug("[Local Variable] {} = {}", name, params.get(name));
            }

            Set keySet = workflowVariables.keySet();
            for (Object key : keySet) {

                logger.debug("[Global Parameter] {} = {}", key, workflowVariables.get(key));
            }
        }
        this.doExecute(instance, this.params);
    }


    public String[] getValues(String key) {
        return splitPreserveAllTokens(mergedParams.getString(key), SEPARATOR);
    }

    public String getTaskId() {
        return getTracingTag();
    }

    public String getTaskName() {
        return getName();
    }


    abstract public void doExecute(ProcessInstance instance, Map params) throws Exception;

    public TypedMap getParams() {
        return params;
    }

    public TypedMap getWorkflowVariables() {
        return workflowVariables;
    }

    public TypedMap getMergedParams() {
        return mergedParams;
    }

    public Long getSubflowTreeId() {
        return subflowTreeId;
    }

    public List<Map> getParallelVectors() {
        return parallelVectors;
    }

    public boolean getAsync() {
        return isAsync;
    }

    public User getUser() {
        return user;
    }

    private Map getSubWorkflowVariables(Workflow wf) {
        try {
            return JsonUtils.unmarshal((StringUtils.unescape((String) wf.getVariable())));
        } catch (Exception ex) {
            throw new ServiceException("Unable to parse the workflow variables.", ex);
        }
    }

    private Map getGridSubflowVariables() {
        Map map = new HashMap();
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        if (getParams().getString("keys") != null) {
            keys = Arrays.asList(getParams().getString("keys").split(","));
        }
        if (getParams().getString("values") != null) {
            values = Arrays.asList(getParams().getString("values").split(","));
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (values.size() >= (i + 1)) {
                String value = values.get(i);
                map.put(key, value);
            }
        }
        return map;
    }

    private boolean isProtectedVariable(String variable) {
        List<String> list = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        List<String> protects = new ArrayList<>();
        if (getParams().getString("keys") != null) {
            keys = Arrays.asList(getParams().getString("keys").split(","));
        }
        if (getParams().getString("protected") != null) {
            protects = Arrays.asList(getParams().getString("protected").split(","));
        }
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (protects.size() >= (i + 1)) {
                if (protects.get(i).equals("true"))
                    list.add(key);
            }
        }
        return list.contains(variable);
    }

}