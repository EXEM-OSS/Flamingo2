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
package org.openflamingo.mapreduce.util;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

/**
 * MapReduce Job의 기본 커맨드 라인 파라미터를 생성하는 생성기.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class DefaultOptionCreator {

    /**
     * 입력 경로 옵션명.
     */
    public static final String INPUT_OPTION = "input";

    /**
     * 출력 경로 옵션명.
     */
    public static final String OUTPUT_OPTION = "output";

    /**
     * 커맨드 라인에서 사용할 도움말 옵션을 생성한다.
     *
     * @return 도움말 옵션
     */
    public static Option helpOption() {
        return new DefaultOptionBuilder().withLongName("help")
                .withDescription("도움말을 출력합니다.").withShortName("h").create();
    }

    /**
     * 커맨드 라인에서 사용할 입력 경로 옵션을 생성한다.
     *
     * @return 입력 경로 옵션
     */
    public static DefaultOptionBuilder inputOption() {
        return new DefaultOptionBuilder()
                .withLongName(INPUT_OPTION)
                .withRequired(false)
                .withShortName("i")
                .withArgument(
                        new ArgumentBuilder().withName(INPUT_OPTION).withMinimum(1)
                                .withMaximum(1).create())
                .withDescription("MapReduce Job의 입력 경로");
    }

    /**
     * 커맨드 라인에서 사용할 출력 경로 옵션을 생성한다.
     *
     * @return 출력 경로 옵션
     */
    public static DefaultOptionBuilder outputOption() {
        return new DefaultOptionBuilder()
                .withLongName(OUTPUT_OPTION)
                .withRequired(false)
                .withShortName("o")
                .withArgument(
                        new ArgumentBuilder().withName(OUTPUT_OPTION).withMinimum(1)
                                .withMaximum(1).create())
                .withDescription("MapReduce Job의 출력 경로");
    }

}
