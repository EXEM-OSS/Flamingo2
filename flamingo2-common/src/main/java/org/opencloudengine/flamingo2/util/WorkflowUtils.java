/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencloudengine.flamingo2.util;

import org.opencloudengine.flamingo2.core.exception.ServiceException;
import org.opencloudengine.flamingo2.model.workflow.*;
import org.opencloudengine.flamingo2.util.el.ELEvaluator;
import org.slf4j.helpers.MessageFormatter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Properties;

/**
 * Workflow XML에 포함되어 있는 값들을 손쉽게 처리할 수 있도록 해주는 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @since 0.2
 */
public class WorkflowUtils {
    /**
     * Variables에 포함되어 있는 변수의 Key Value를 Map으로 변환한다.
     *
     * @param variables {@link org.opencloudengine.flamingo2.model.workflow.Variables}
     * @return Key Value Map
     */
    public static Map<String, String> variablesToMap(Variables variables) {
        List<Variable> vars = variables.getVariable();
        return variablesToMap(vars);
    }

    /**
     * Variables에 포함되어 있는 변수의 Key Value를 Properties으로 변환한다.
     *
     * @param variables {@link org.opencloudengine.flamingo2.model.workflow.Variables}
     * @return Key Value Properties
     */
    public static Properties variablesToProperties(Variables variables) {
        Properties properties = new Properties();
        List<Variable> vars = variables.getVariable();
        for (Variable var : vars) {
            properties.setProperty(var.getName(), var.getValue());
        }
        return properties;
    }

    /**
     * Variable의 리스트를 Key Value 형식의 Map으로 변환한다.
     *
     * @param vars {@link org.opencloudengine.flamingo2.model.workflow.Variables}의 리스트
     * @return Key Value Map
     */
    public static Map<String, String> variablesToMap(List<Variable> vars) {
        Map<String, String> params = new HashMap<String, String>();
        for (Variable var : vars) {
            params.put(var.getName(), var.getValue());
        }
        return params;
    }

    /**
     * Variable의 리스트를 Key Value 형식의 Map으로 변환한다.
     *
     * @param vars      {@link org.opencloudengine.flamingo2.model.workflow.Variables}의 리스트
     * @param evaluator EL Evaluator
     * @return Key Value Map
     */
    public static Map<String, String> variablesToMap(List<Variable> vars, ELEvaluator evaluator) {
        Map<String, String> params = new HashMap<String, String>();
        for (Variable var : vars) {
            try {
                params.put(var.getName(), evaluator.evaluate(var.getValue(), String.class));
            } catch (Exception ex) {
                String message = MessageFormatter.format("EL Evaluator가 '{0}={1}'값을 처리할 수 없습니다.", var.getName(), var.getValue()).getMessage();
                throw new ServiceException(message, ex);
            }
        }
        return params;
    }

    /**
     * Workflow에 정의되어 있는 Global Variable을 추출하여 Map으로 구성한다.
     *
     * @param workflow Workflow JAXB Object
     * @return Global Variable의 Map
     */
    public static Map<String, String> getGlobalVariables(Workflow workflow) {
        Map<String, String> globalVariables = new HashMap<String, String>();
        List<GlobalVariable> globalVariable = workflow.getGlobalVariables().getGlobalVariable();
        for (GlobalVariable variable : globalVariable) {
            globalVariables.put(variable.getName(), variable.getValue());
        }
        return globalVariables;
    }

    /**
     * HDFS 입력 경로를 반환한다.
     *
     * @param hdfs HDFS JAXB Object
     * @return HDFS 입력 경로
     */
    public static List<String> getHdfsInputPaths(Hdfs hdfs) {
        List<String> inputPaths = new LinkedList<String>();
        List<InputPath> inputPath = hdfs.getInputPaths().getInputPath();
        for (InputPath anInputPath : inputPath) {
            inputPaths.add(anInputPath.getValue());
        }
        return inputPaths;
    }

    /**
     * HDFS 입력 경로와 식별자를 반환한다.
     *
     * @param hdfs HDFS JAXB Object
     * @return HDFS 입력 경로와 식별자를 매핑한 맵
     */
    public static Map<String, String> getHdfsQualifieredInputPaths(Hdfs hdfs) {
        Map<String, String> inputPaths = new HashMap<String, String>();
        List<InputPath> inputPath = hdfs.getInputPaths().getInputPath();
        for (InputPath anInputPath : inputPath) {
            inputPaths.put(anInputPath.getQualifier(), anInputPath.getValue());
        }
        return inputPaths;
    }

    /**
     * HDFS 출력 경로를 반환한다.
     *
     * @param hdfs HDFS JAXB Object
     * @return HDFS 출력 경로
     */
    public static String getHdfsOutputPath(Hdfs hdfs) {
        return hdfs.getOutputPath().getValue();
    }

    /**
     * MapReduce를 동작시키는 Cluster명을 반환한다.
     *
     * @param mapreduce MapReduce
     * @return Cluster명
     */
    public static String getMapReduceClusterName(Mapreduce mapreduce) {
        return mapreduce.getClusterName().getValue();
    }

    /**
     * MapReduce의 Jar Path를 반환한다.
     *
     * @param mapreduce MapReduce
     * @return Jar Path
     */
    public static String getMapReduceJarPath(Mapreduce mapreduce) {
        return mapreduce.getJar().getValue();
    }

    /**
     * MapReduce의 Driver명을 반환한다.
     *
     * @param mapreduce MapReduce
     * @return Fully qualified class name
     */
    public static String getMapReduceClassname(Mapreduce mapreduce) {
        return mapreduce.getClassName().getValue();
    }

    /**
     * MapReduce의 Classpath를 추출한다.
     *
     * @param mapreduce Workflow의 MapReduce 노드
     * @return MapReduce 동작에 필요한 Classpath 리스트
     */
    public static List<String> getMapReduceClasspaths(Mapreduce mapreduce) {
        List<String> classpaths = new LinkedList<String>();
        List<Classpath> classpath = mapreduce.getClasspaths().getClasspath();
        for (Classpath aClasspath : classpath) {
            classpaths.add(aClasspath.getValue());
        }
        return classpaths;
    }

    /**
     * MapReduce에 정의되어 있는 Variable을 추출하여 Map으로 구성한다.
     *
     * @param mapreduce MapReduce
     * @return Variable의 Map
     */
    public static Map<String, String> getMapReduceVariables(Mapreduce mapreduce) {
        Map<String, String> variables = new HashMap<String, String>();
        List<Variable> variable = mapreduce.getVariables().getVariable();
        for (Variable aVariable : variable) {
            variables.put(aVariable.getName(), aVariable.getValue());
        }
        return variables;
    }

    /**
     * 임시 출력 경로를 생성한다.
     *
     * @param actionName 액션명
     * @return 임시 출력 경로
     */
    public static String getOutputTemporaryPath(String actionName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
        return "/temp/" + actionName + "_" + formatter.format(new Date());
    }
}