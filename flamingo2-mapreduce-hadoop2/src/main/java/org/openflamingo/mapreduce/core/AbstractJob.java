/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.core;

import com.google.common.base.Preconditions;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.Tool;
import org.openflamingo.mapreduce.util.CommandLineUtil;
import org.openflamingo.mapreduce.util.DefaultOptionCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flamingo MapReduce의 모든 Hadoop Job Driver의 최상위 클래스.
 * 기본적으로 MapReduce Driver는 사용자의 커맨드 라인에서 입력받은 파라미터를 Map과 Reduce Task에서
 * 사용할 수 있도록 Hadoop Configuration에 설정해야 한다. 이 클래스는
 * 모든 MapReduce Driver가 커맨드 라인을 파싱하고 사용하기 위한 표준 규격을 제공해준다.
 * <p/>
 * 커맨드 라인은 모든 자식 MapReduce Driver에서 다음과 같이 사용할 수 있다.
 * <ul>
 * <li><tt>--tempDir</tt> (path): Job 동작시 임시로 사용하는 임시 디렉토리. (기본값 "<tt>/temp/${user.home}</tt>")
 * <li><tt>--help</tt>: 도움말</li>
 * </ul>
 * <p/>
 * 추가적으로 MapReduce Job이 동작하기 위한 부가적인 파라미터는 다음과 같이 설정할 수 있다.
 * <p/>
 * <ul>
 * <li><tt>-Dmapred.job.name=(name)</tt>: Hadoop MapReduce Job의 이름. 기본으로 Driver 클래스명으로 설정.</li>
 * <li><tt>-Dmapred.output.compress={true,false}</tt>: 출력 압축 여부 (기본값 true)</li>
 * <li><tt>-Dmapred.input.dir=(path)</tt>: 입력 파일 또는 입력 디렉토리 (필수)</li>
 * <li><tt>-Dmapred.output.dir=(path)</tt>: 출력 파일 (필수)</li>
 * </ul>
 * <p/>
 * <tt>-D</tt>로 시작하는 모든 파라미터는 반드시 그렇지 않은 다른 파라미터 보다 앞서 사용해야 한다.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public abstract class AbstractJob extends Configured implements Tool {

    /**
     * SLF4J API
     */
    private static final Logger log = LoggerFactory.getLogger(AbstractJob.class);

    /**
     * MapReduce 입력 경로를 지정하기 위한 옵션.
     */
    private Option inputOption;

    /**
     * MapReduce 출력 경로를 지정하기 위한 옵션.
     */
    private Option outputOption;

    /**
     * {@link #parseArguments(String[])}을 통해 설정되는 MapReduce 입력 경로.
     */
    private Path inputPath;

    /**
     * {@link #parseArguments(String[])}을 통해 설정되는 MapReduce 출력 경로.
     */
    private Path outputPath;

    /**
     * {@link #parseArguments(String[])}을 통해 설정되는 MapReduce 임시 경로.
     * 이 경로는 CLASSPATH의 <tt>flamingo-hadoop-site.xml</tt>에 <tt>tempDir</tt>로 지정되어 있는 값으로써
     * <tt>/temp/${user.home}</tt>을 기본으로 한다.
     */
    private Path tempPath;

    /**
     * MapReduce Job을 동작시키기 위한 파라미터의 Key Value Map.
     */
    private Map<String, String> argMap;

    /**
     * 내부적으로 사용하기 위한 옵션 목록.
     */
    private final List<Option> options;

    /**
     * 기본 생성자.
     */
    protected AbstractJob() {
        options = new LinkedList<Option>();
        if (getConf() == null) {
            setConf(new Configuration());
            // Flamingo MapReduce의 기본 설정 파일을 로딩하여 Hadoop Configuration을 구성한다.
            getConf().addResource(getClass().getResource("/flamingo-mapreduce-site.xml"));
        }
    }

    /**
     * {@link #parseArguments(String[])} 호출을 통해서 생성된 입력 경로를 반환한다.
     * Hadoop MapReduce Driver에서는 {@link #addInputOption()} 메소드 호출을 통해서
     * 입력 경로를 설정할 수 있으며 별도로 지정하지 않고 Hadoop Configuration에
     * {@code mapred.input.dir} 설정값을 추가혀여 설정한다.
     *
     * @return 입력 디렉토리
     */
    protected Path getInputPath() {
        return inputPath;
    }

    /**
     * {@link #parseArguments(String[])} 호출을 통해서 생성된 출력 경로를 반환한다.
     * Hadoop MapReduce Driver에서는 {@link #addOutputOption()} 메소드 호출을 통해서
     * 출력 경로를 설정할 수 있으며 별도로 지정하지 않고 Hadoop Configuration에
     * {@code mapred.output.dir} 설정값을 추가혀여 설정한다.
     *
     * @return 출력 디렉토리
     */
    protected Path getOutputPath() {
        return outputPath;
    }

    /**
     * 지정한 출력 경로에 대해서 서브 디렉토리의 자식 경로를 반환한다.
     *
     * @param child 자식 경로
     * @return Path
     */
    protected Path getOutputPath(String child) {
        return new Path(outputPath, child);
    }

    /**
     * 자식 디렉토리를 갖는 임시 디렉토리를 반환한다.
     *
     * @param path 자식 디렉토리명
     * @return 임시 경로
     */
    protected Path getTempPath(String path) {
        return new Path(getTempPath(), path);
    }

    /**
     * 임시 경로를 반환한다.
     * 임시 경로를 표현하는 설정값인 {@link Constants#TEMP_DIR}은 CLASSPATH의 <tt>flamingo-mapreudce-site.xml</tt> 파일에 정의되어 있으나
     * 사용자가 직접 이 값을 수정하고자 하는 경우 커맨드 라인에서 <tt>--tempDir</tt>을 이용하여 지정할 수 있다.
     * 이 경우 이 메소드에서는 우선적으로 사용자가 설정한 것을 먼저 사용하게 된다.
     * 이 경우 Hadoop MapReduce Driver에서는 다음과 같이 코드를 작성하여 임시 디렉토리를 가져올 수 있다.
     * <p/>
     * <pre>
     *     Path tempDir = getTempPath();
     * </pre>
     *
     * @return 임시 경로
     */
    protected Path getTempPath() {
        String defaultTempDir = getConf().get("tempDir");
        if (argMap.containsKey(keyFor(Constants.TEMP_DIR))) {
            defaultTempDir = argMap.get(keyFor(Constants.TEMP_DIR));
        }
        return new Path(defaultTempDir);
    }

    /**
     * 현재 시간을 기준으로 한 임시 경로를 반환한다. 날짜 패턴은
     * <tt>flamingo-hadoop-site.xml</tt> 파일에 <tt>tempDir.date.pattern</tt>으로
     * 설정할 수 있으며 기본으로 <tt>yyyyMMdd-HHmmss-SSS</tt>을 사용한다.
     *
     * @return 임시 경로
     */
    protected Path getTimestampTempPath() {
        SimpleDateFormat formatter = new SimpleDateFormat(getConf().get("tempDir.date.pattern"));
        return getTempPath(formatter.format(new Date()));
    }

    /**
     * Prefix를 가진 임시 경로를 반환한다.
     *
     * @param prefix Prefix
     * @param path   자식 디렉토리명
     * @return 임시 경로
     */
    protected Path getTempPath(String prefix, String path) {
        Path tempPath = getTempPath();
        Path prefixPath = new Path(tempPath, prefix);
        return new Path(prefixPath, path);
    }

    /**
     * 인자가 없는 옵션을 추가한다. 인자가 없으므로 키가 포함되어 있는지 여부로 존재 여부를 판단한다.
     *
     * @param name        파라미터명(예; <tt>inputPath</tt>)
     * @param shortName   출약 파라미터명(예; <tt>i</tt>)
     * @param description 파라미터에 대한 설명문
     */
    protected void addFlag(String name, String shortName, String description) {
        options.add(buildOption(name, shortName, description, false, false, null));
    }

    /**
     * 기본값이 없는 옵션을 추가한다. 이 옵션은 필수 옵션이 아니다.
     *
     * @param name        파라미터명(예; <tt>inputPath</tt>)
     * @param shortName   출약 파라미터명(예; <tt>i</tt>)
     * @param description 파라미터에 대한 설명문
     */
    protected void addOption(String name, String shortName, String description) {
        options.add(buildOption(name, shortName, description, true, false, null));
    }

    /**
     * Hadoop MapReduce에 옵션을 추가한다. 옵션 추가는 커맨드 라인을 통해서 가능하며
     * 커맨드 라인은 {@link #parseArguments(String[])} 메소드를 호출하여 파싱하게 된다.
     *
     * @param name        파라미터명(예; <tt>inputPath</tt>)
     * @param shortName   출약 파라미터명(예; <tt>i</tt>)
     * @param description 파라미터에 대한 설명문
     * @param required    이 값이 <tt>true</tt>인 경우 커맨드 라인에서 입력 파라미터를 지정하지 않는 경우
     *                    예외를 발생시킨다. 사용법과 에러 메시지를 포함한 예외를 던진다.
     */
    protected void addOption(String name, String shortName, String description, boolean required) {
        options.add(buildOption(name, shortName, description, true, required, null));
    }

    /**
     * Hadoop MapReduce에 옵션을 추가한다. 옵션 추가는 커맨드 라인을 통해서 가능하며
     * 커맨드 라인은 {@link #parseArguments(String[])} 메소드를 호출하여 파싱하게 된다.
     *
     * @param name         파라미터명(예; <tt>inputPath</tt>)
     * @param shortName    출약 파라미터명(예; <tt>i</tt>)
     * @param description  파라미터에 대한 설명문
     * @param defaultValue 커맨드 라인에서 입력 인자를 지정하지 않는 경우 설정할 기본값으로써 null을 허용한다.
     */
    protected void addOption(String name, String shortName, String description, String defaultValue) {
        options.add(buildOption(name, shortName, description, true, false, defaultValue));
    }

    /**
     * Hadoop MapReduce에 옵션을 추가한다. 옵션 추가는 커맨드 라인을 통해서 가능하며
     * 커맨드 라인은 {@link #parseArguments(String[])} 메소드를 호출하여 파싱하게 된다.
     * 만약에 옵션이 인자를 가지고 있지 않다면 {@code parseArguments} 메소드 호출을 통해 리턴한
     * map의 {@code containsKey} 메소드를 통해서 옵션의 존재 여부를 확인하게 된다.
     * 그렇지 않은 경우 옵션의 영문 옵션명 앞에 '--'을 붙여서 map에서 해당 키가 존재하는지 확인한 후
     * 존재하는 경우 해당 옵션의 문자열값을 사용한다.
     *
     * @param option 추가할 옵션
     * @return 추가한 옵션
     */
    protected Option addOption(Option option) {
        options.add(option);
        return option;
    }

    /**
     * 기본 입력 디렉토리 옵션을 추가한다. 기본 입력 디렉토리는 커맨드 라인에서 <tt>'-i'</tt>를 지정함으로써
     * 가능하며 {@link #parseArguments(String[])} 메소드를 호출했을 때 이 옵션을 기준으로
     * 입력 경로가 설정된다. 기본적으로 입력 경로는 모든 Hadoop Job이 시작하기 위해서 반드시 필요하므로
     * 이 옵션은 기본으로 <tt>required</tt> 속서을 갖는다.
     */
    protected void addInputOption() {
        this.inputOption = addOption(DefaultOptionCreator.inputOption().create());
    }

    /**
     * 기본 출력 디렉토리 옵션을 추가한다. 기본 출력 디렉토리는 커맨드 라인에서 <tt>'-o'</tt>를 지정함으로써
     * 가능하며 {@link #parseArguments(String[])} 메소드를 호출했을 때 이 옵션을 기준으로
     * 입력 경로가 설정된다. 기본적으로 입력 경로는 모든 Hadoop Job이 시작하기 위해서 반드시 필요하므로
     * 이 옵션은 기본으로 <tt>required</tt> 속서을 갖는다.
     */
    protected void addOutputOption() {
        this.outputOption = addOption(DefaultOptionCreator.outputOption().create());
    }

    /**
     * 지정한 파라미터를 가진 옵션을 구성한다. 이름과 설명은 필수로 입력해야 한다.
     * required.
     *
     * @param name         커맨드 라인에서 '--'을 prefix로 가진 옵션의 이름
     * @param shortName    커맨드 라인에서 '--'을 prefix로 가진 옵션의 짧은 이름
     * @param description  도움말에 출력할 옵션에 대한 상세 설명
     * @param hasArg       인자를 가진다면 <tt>true</tt>
     * @param required     필수 옵션이라면 <tt>true</tt>
     * @param defaultValue 인자의 기본값. <tt>null</tt>을 허용한다.
     * @return 옵션
     */
    public static Option buildOption(String name,
                                     String shortName,
                                     String description,
                                     boolean hasArg,
                                     boolean required,
                                     String defaultValue) {

        DefaultOptionBuilder optBuilder = new DefaultOptionBuilder().withLongName(name).withDescription(description)
                .withRequired(required);

        if (shortName != null) {
            optBuilder.withShortName(shortName);
        }

        if (hasArg) {
            ArgumentBuilder argBuilder = new ArgumentBuilder().withName(name).withMinimum(1).withMaximum(1);

            if (defaultValue != null) {
                argBuilder = argBuilder.withDefault(defaultValue);
            }

            optBuilder.withArgument(argBuilder.create());
        }

        return optBuilder.create();
    }

    /**
     * 사용자가 입력한 커맨드 라인을 파싱한다.
     * 만약에 <tt>-h</tt>를 지정하거나 예외가 발생하는 경우 도움말을 출력하고 <tt>null</tt>을 반환한다.
     *
     * @param args 커맨드 라인 옵션
     * @return 인자와 인자에 대한 값을 포함하는 {@code Map<String,String>}.
     * 인자의 key는 옵션명에 되며 옵션명은 '--'을 prefix로 갖는다.
     * 따라서 옵션을 기준으로 {@code Map<String,String>} 에서 찾고자 하는 경우 반드시 옵션명에 '--'을 붙이도록 한다.
     */
    public Map<String, String> parseArguments(String[] args) throws Exception {
        Option helpOpt = addOption(DefaultOptionCreator.helpOption());
        addOption("tempDir", null, "임시 디렉토리", false);
        addOption("startPhase", null, "실행할 첫번쨰 단계", "0");
        addOption("endPhase", null, "실행할 마지막 단계", String.valueOf(Integer.MAX_VALUE));

        GroupBuilder groupBuilder = new GroupBuilder().withName("Hadoop MapReduce Job 옵션:");

        for (Option opt : options) {
            groupBuilder = groupBuilder.withOption(opt);
        }

        Group group = groupBuilder.create();

        CommandLine cmdLine;
        try {
            Parser parser = new Parser();
            parser.setGroup(group);
            parser.setHelpOption(helpOpt);
            cmdLine = parser.parse(args);
        } catch (OptionException e) {
            log.error(e.getMessage());
            CommandLineUtil.printHelpWithGenericOptions(group, e);
            return null;
        }

        if (cmdLine.hasOption(helpOpt)) {
            CommandLineUtil.printHelpWithGenericOptions(group);
            return null;
        }

        try {
            parseDirectories(cmdLine);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            CommandLineUtil.printHelpWithGenericOptions(group);
            return null;
        }

        argMap = new TreeMap<String, String>();
        maybePut(argMap, cmdLine, this.options.toArray(new Option[this.options.size()]));
        log.info("Command line arguments: ", argMap);
        Set<String> keySet = argMap.keySet();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            log.info("   {} = {}", key, argMap.get(key));
        }
        return argMap;
    }

    /**
     * 지정한 파라미터의 Boolean 값을 반환한다.
     *
     * @param key          커맨드 라인 파라미터
     * @param defaultValue Boolean 기본값
     * @return Boolean 값
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (StringUtils.isEmpty(argMap.get(keyFor(key)))) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(argMap.get(keyFor(key)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 지정한 파라미터의 Integer 값을 반환한다.
     *
     * @param key          커맨드 라인 파라미터
     * @param defaultValue Integer 기본값
     * @return Integer 값
     */
    public int getInt(String key, int defaultValue) {
        if (StringUtils.isEmpty(argMap.get(keyFor(key)))) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(argMap.get(keyFor(key)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 지정한 파라미터의 Float 값을 반환한다.
     *
     * @param key          커맨드 라인 파라미터
     * @param defaultValue Float 기본값
     * @return Float 값
     */
    public float getFloat(String key, float defaultValue) {
        if (StringUtils.isEmpty(argMap.get(keyFor(key)))) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(argMap.get(keyFor(key)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 지정한 파라미터의 Double 값을 반환한다.
     *
     * @param key          커맨드 라인 파라미터
     * @param defaultValue Double 기본값
     * @return Double 값
     */
    public double getDouble(String key, double defaultValue) {
        if (StringUtils.isEmpty(argMap.get(keyFor(key)))) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(argMap.get(keyFor(key)));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * 지정한 파라미터의 String 값을 반환한다.
     *
     * @param key          커맨드 라인 파라미터
     * @param defaultValue String 기본값
     * @return String 값
     */
    public String getString(String key, String defaultValue) {
        if (StringUtils.isEmpty(argMap.get(keyFor(key)))) {
            return defaultValue;
        }
        return getString(key);
    }

    /**
     * 지정한 파라미터의 String 값을 반환한다.
     *
     * @param key 커맨드 라인 파라미터
     * @return String 값
     */
    public String getString(String key) {
        return argMap.get(keyFor(key));
    }

    /**
     * 지정한 옵션명에 대해서 옵션 키를 구성한다. 예를 들여 옵션명이 <tt>name</tt> 이라면 실제 옵션 키는 <tt>--name</tt>이 된다.
     *
     * @param optionName 옵션명
     */
    public static String keyFor(String optionName) {
        return "--" + optionName;
    }

    /**
     * 지정한 옵션에 대해서 Option 객체를 반환한다.
     *
     * @return 요청한 옵션이 존재하는 경우 <tt>Option</tt>, 그렇지 않다면 <tt>null</tt>을 반환한다.
     */
    public String getOption(String optionName) {
        return argMap.get(keyFor(optionName));
    }

    /**
     * 지정한 옵션이 존재하는지 확인한다.
     *
     * @return 요청한 옵션이 존재하는 경우 <tt>true</tt>
     */
    public boolean hasOption(String optionName) {
        return argMap.containsKey(keyFor(optionName));
    }

    /**
     * 커맨드 라인 옵션 또는 Hadoop Configuration 파라미터에서 입출력 파일 및 디렉토리를 획득한다.
     * {@code addInputOption} 또는 {@code addOutputOption} 메소드를 호출할 때
     * 커맨드 라인 또는 Hadoop Configuration에 입출력 경로 및 파일값이 존재하지 않는 경우
     * {@code OptionException}을 던진다. 만약에 Hadoop Configuration 속성으로
     * {@code inputPath} 또는 {@code outputPath}을 지정하는 경우
     * <tt>non-null</tt>만 사용할 수 있다. 커맨드 라인 옵션은 Hadoop Configuration 속성보다
     * 앞서서 사용할 수 있다.
     *
     * @param cmdLine 커맨드 라인
     * @throws IllegalArgumentException inputOption 또는 {@code --input}와 {@code -Dmapred.input dir} 둘 줄 하나라도 존재하지 않는 경우, outputOption 또는 {@code --output}와 {@code -Dmapred.output dir} 둘 줄 하나라도 존재하지 않는 경우,
     */
    protected void parseDirectories(CommandLine cmdLine) {
        Configuration conf = getConf();

        if (inputOption != null && cmdLine.hasOption(inputOption)) {
            this.inputPath = new Path(cmdLine.getValue(inputOption).toString());
        }
        if (inputPath == null && conf.get("mapred.input.dir") != null) {
            this.inputPath = new Path(conf.get("mapred.input.dir"));
        }

        if (outputOption != null && cmdLine.hasOption(outputOption)) {
            this.outputPath = new Path(cmdLine.getValue(outputOption).toString());
        }
        if (outputPath == null && conf.get("mapred.output.dir") != null) {
            this.outputPath = new Path(conf.get("mapred.output.dir"));
        }

        // Temporary Path를 설정한다. 기본값은 CLASSPATH의 <tt>flamingo-hadoop-site.xml</tt> 파일에 있는 값을 사용한다.
        if (tempPath == null && conf.get("tempDir") != null) {
            this.tempPath = new Path(conf.get("tempDir"));
        }

        Preconditions.checkArgument(inputOption == null || inputPath != null, "입력 디렉토리가 지정되어 있지 않거나 -Dmapred.input.dir 파라미터가 잘못 지정되어 있습니다. -Dmapred.input.dir 옵션은 다른 옵션을 사용하기 전에 제일 앞에 사용해야 합니다.");
        Preconditions.checkArgument(outputOption == null || outputPath != null, "출력 디렉토리가 지정되어 있지 않거나 -Dmapred.output.dir 파라미터가 잘못 지정되어 있습니다. -Dmapred.input.dir 옵션은 다른 옵션을 사용하기 전에 제일 앞에 사용해야 합니다.");
    }

    protected static void maybePut(Map<String, String> args, CommandLine cmdLine, Option... opt) {
        for (Option o : opt) {

            // 커맨드 라인에 옵션이 있거나 기본값과 같은 값이 있는 경우
            if (cmdLine.hasOption(o) || cmdLine.getValue(o) != null) {

                // 커맨드 라인의 옵션에 값이 OK
                // nulls are ok, for cases where options are simple flags.
                Object vo = cmdLine.getValue(o);
                String value = vo == null ? null : vo.toString();
                args.put(o.getPreferredName(), value);
            }
        }
    }

    /**
     * 여려 단계의 Job을 실행하는 경우 다음 Phase을 실행할지 여부를 결정한다.
     *
     * @param args         Key Value 파라미터 맵
     * @param currentPhase Phase
     * @return 더 실행해야 하는지 여부. 더 실행해야 하는 경우 <tt>true</tt>를 반환.
     */
    protected static boolean shouldRunNextPhase(Map<String, String> args, AtomicInteger currentPhase) {
        int phase = currentPhase.getAndIncrement();
        String startPhase = args.get("--startPhase");
        String endPhase = args.get("--endPhase");
        boolean phaseSkipped = (startPhase != null && phase < Integer.parseInt(startPhase))
                || (endPhase != null && phase > Integer.parseInt(endPhase));
        if (phaseSkipped) {
            log.info("Skipping phase {}", phase);
        }
        return !phaseSkipped;
    }

    /**
     * Mappre, Reducer 기반 Hadoop Job을 생성한다.
     *
     * @param inputPath    입력 경로
     * @param outputPath   출력 경로
     * @param inputFormat  입력 포맷
     * @param mapper       Mapper
     * @param mapperKey    Mapper의 Output Key Class
     * @param mapperValue  Mapper의 Output Value Class
     * @param reducer      Reducer
     * @param reducerKey   Reducer의 Output Key Class
     * @param reducerValue Reducer의 Output Value Class
     * @param outputFormat 출력 포맷
     * @return Hadoop Job
     * @throws java.io.IOException Hadoop Job을 생성할 수 없는 경우
     */
    protected Job prepareJob(Path inputPath, Path outputPath,
                             Class<? extends InputFormat> inputFormat,
                             Class<? extends Mapper> mapper,
                             Class<? extends Writable> mapperKey,
                             Class<? extends Writable> mapperValue,
                             Class<? extends Reducer> reducer,
                             Class<? extends Writable> reducerKey,
                             Class<? extends Writable> reducerValue,
                             Class<? extends OutputFormat> outputFormat) throws IOException {

        Job job = new Job(new Configuration(getConf()));
        Configuration jobConf = job.getConfiguration();

        if (reducer.equals(Reducer.class)) {
            if (mapper.equals(Mapper.class)) {
                throw new IllegalStateException("Can't figure out the user class jar file from mapper/reducer");
            }
            job.setJarByClass(mapper);
        } else {
            job.setJarByClass(reducer);
        }

        job.setInputFormatClass(inputFormat);
        jobConf.set("mapred.input.dir", inputPath.toString());

        job.setMapperClass(mapper);
        job.setMapOutputKeyClass(mapperKey);
        job.setMapOutputValueClass(mapperValue);

        job.setReducerClass(reducer);
        job.setOutputKeyClass(reducerKey);
        job.setOutputValueClass(reducerValue);

        job.setJobName(getCustomJobName(job, mapper, reducer));

        job.setOutputFormatClass(outputFormat);
        jobConf.set("mapred.output.dir", outputPath.toString());

        return job;
    }

    /**
     * Mappre, Reducer 기반 Hadoop Job을 생성한다.
     *
     * @param inputPath       입력 경로
     * @param outputPath      출력 경로
     * @param mapper          Mapper
     * @param mapperKey       Mapper의 Output Key Class
     * @param mapperValue     Mapper의 Output Value Class
     * @param reducer         Reducer
     * @param reducerKey      Reducer의 Output Key Class
     * @param reducerValue    Reducer의 Output Value Class
     * @param ignoreInOutPath 입출력 경로 무시
     * @return Hadoop Job
     * @throws java.io.IOException Hadoop Job을 생성할 수 없는 경우
     */
    protected Job prepareJob(Path inputPath, Path outputPath,
                             Class<? extends Mapper> mapper,
                             Class<? extends Writable> mapperKey,
                             Class<? extends Writable> mapperValue,
                             Class<? extends Reducer> reducer,
                             Class<? extends Writable> reducerKey,
                             Class<? extends Writable> reducerValue,
                             boolean ignoreInOutPath) throws IOException {

        Job job = new Job(new Configuration(getConf()));
        Configuration jobConf = job.getConfiguration();

        if (reducer.equals(Reducer.class)) {
            if (mapper.equals(Mapper.class)) {
                throw new IllegalStateException("Can't figure out the user class jar file from mapper/reducer");
            }
            job.setJarByClass(mapper);
        } else {
            job.setJarByClass(reducer);
        }

        if (!ignoreInOutPath) jobConf.set("mapred.input.dir", inputPath.toString());

        job.setMapperClass(mapper);
        job.setMapOutputKeyClass(mapperKey);
        job.setMapOutputValueClass(mapperValue);

        job.setReducerClass(reducer);
        job.setOutputKeyClass(reducerKey);
        job.setOutputValueClass(reducerValue);

        job.setJobName(getCustomJobName(job, mapper, reducer));

        if (!ignoreInOutPath) jobConf.set("mapred.output.dir", outputPath.toString());

        return job;
    }

    /**
     * Mapper, Reducer 기반 Hadoop Job을 생성한다.
     *
     * @param inputPath    입력 경로
     * @param outputPath   출력 경로
     * @param inputFormat  입력 포맷
     * @param mapper       Mapper
     * @param mapperKey    Mapper의 Output Key Class
     * @param mapperValue  Mapper의 Output Value Class
     * @param reducer      Reducer
     * @param reducerKey   Reducer의 Output Key Class
     * @param reducerValue Reducer의 Output Value Class
     * @param outputFormat 출력 포맷
     * @param reduceTask   Reducer의 개수
     * @return Hadoop Job
     * @throws java.io.IOException Hadoop Job을 생성할 수 없는 경우
     */
    protected Job prepareJob(Path inputPath, Path outputPath,
                             Class<? extends InputFormat> inputFormat,
                             Class<? extends Mapper> mapper,
                             Class<? extends Writable> mapperKey,
                             Class<? extends Writable> mapperValue,
                             Class<? extends Reducer> reducer,
                             Class<? extends Writable> reducerKey,
                             Class<? extends Writable> reducerValue,
                             Class<? extends OutputFormat> outputFormat,
                             int reduceTask) throws IOException {

        Job job = new Job(new Configuration(getConf()));
        Configuration jobConf = job.getConfiguration();

        if (reducer.equals(Reducer.class)) {
            if (mapper.equals(Mapper.class)) {
                throw new IllegalStateException("Can't figure out the user class jar file from mapper/reducer");
            }
            job.setJarByClass(mapper);
        } else {
            job.setJarByClass(reducer);
        }

        job.setInputFormatClass(inputFormat);
        jobConf.set("mapred.input.dir", inputPath.toString());

        job.setMapperClass(mapper);
        job.setMapOutputKeyClass(mapperKey);
        job.setMapOutputValueClass(mapperValue);

        job.setReducerClass(reducer);
        job.setOutputKeyClass(reducerKey);
        job.setOutputValueClass(reducerValue);

        job.setJobName(getCustomJobName(job, mapper, reducer));

        job.setNumReduceTasks(reduceTask);

        job.setOutputFormatClass(outputFormat);
        jobConf.set("mapred.output.dir", outputPath.toString());

        return job;
    }

    /**
     * Mapper 기반 Hadoop Job을 생성한다.
     * 이 메소드는 Mapper로만 구성된 MapReduce Job을 생서할 때 사용한다.
     *
     * @param inputPath    입력 경로
     * @param outputPath   출력 경로
     * @param inputFormat  입력 포맷
     * @param mapper       Mapper
     * @param mapperKey    Mapper의 Output Key Class
     * @param mapperValue  Mapper의 Output Value Class
     * @param outputFormat 출력 포맷
     * @return Hadoop Job
     * @throws java.io.IOException Hadoop Job을 생성할 수 없는 경우
     */
    protected Job prepareJob(Path inputPath, Path outputPath,
                             Class<? extends InputFormat> inputFormat,
                             Class<? extends Mapper> mapper,
                             Class<? extends Writable> mapperKey,
                             Class<? extends Writable> mapperValue,
                             Class<? extends OutputFormat> outputFormat) throws IOException {

        Job job = new Job(new Configuration(getConf()));
        Configuration jobConf = job.getConfiguration();

        if (mapper.equals(Mapper.class)) {
            throw new IllegalStateException("Can't figure out the user class jar file from mapper");
        }
        job.setJarByClass(mapper);

        job.setInputFormatClass(inputFormat);
        jobConf.set("mapred.input.dir", inputPath.toString());

        job.setMapperClass(mapper);
        job.setMapOutputKeyClass(mapperKey);
        job.setMapOutputValueClass(mapperValue);

        job.setNumReduceTasks(0);

        job.setJobName(getCustomJobName(job, mapper));

        job.setOutputFormatClass(outputFormat);
        jobConf.set("mapred.output.dir", outputPath.toString());

        return job;
    }

    /**
     * Mapper와 Reducer를 기반으로 Hadoop Job 이름을 반환한다.
     *
     * @param job     Hadoop Job
     * @param mapper  {@link org.apache.hadoop.mapreduce.Mapper}
     * @param reducer Reducer
     * @return Job Name
     */
    private String getCustomJobName(JobContext job,
                                    Class<? extends Mapper> mapper,
                                    Class<? extends Reducer> reducer) {
        StringBuilder name = new StringBuilder(100);
        String customJobName = job.getJobName();
        if (customJobName == null || customJobName.trim().length() == 0) {
            name.append(getClass().getSimpleName());
        } else {
            name.append(customJobName);
        }
        return name.toString();
    }

    /**
     * Mapper를 기반으로 Hadoop Job 이름을 반환한다.
     *
     * @param job    Hadoop Job
     * @param mapper {@link org.apache.hadoop.mapreduce.Mapper}
     * @return Job Name
     */
    private String getCustomJobName(JobContext job,
                                    Class<? extends Mapper> mapper) {
        StringBuilder name = new StringBuilder(100);
        String customJobName = job.getJobName();
        if (customJobName == null || customJobName.trim().length() == 0) {
            name.append(getClass().getSimpleName());
        } else {
            name.append(customJobName);
        }
        return name.toString();
    }

    /**
     * 입력한 Key Value로 커맨드 라인 입력 옵션을 구성한다. 이 메소드는
     * Workflow Engine에서 넘어온 Key Value 형태의 Map을 커맨드 라인으로 구성하기 위해서 사용하며 그 이외의 경우
     * 사용할 필요가 없다.
     *
     * @param key   MapReduce Job의 파라미터 Key
     * @param value MapReduce Job의 파라미터 Key의 Value
     * @return 커맨드 라인 입력 옵션
     */
    public static String getKeyValuePair(String key, String value) {
        return keyFor(key) + " " + value;
    }

    /**
     * HDFS Configuration을 덤프한다.
     *
     * @throws Exception Hadoop Configuration의 값을 읽을 수 없는 경우
     */
    public void dump() throws Exception {
        StringWriter writer = new StringWriter();
        this.getConf().dumpConfiguration(this.getConf(), writer);
        System.out.println(writer.toString());
    }
}
