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

import org.apache.hadoop.io.Text;
import org.openflamingo.mapreduce.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang.StringUtils.splitPreserveAllTokens;

/**
 * CSV Row Record Parser.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public class CsvRowParser {

    /**
     * 컬럼 정보를 가지고 있는 리스트.
     */
    private List<String> columns = new ArrayList<String>();

    /**
     * 파일의 1라인을 의미하는 Row. 최초 생성시 생성자의 인자로 지정하거나 파싱시 인자로 지정하면 이 변수에 값이 설졍되며 변하지 않는다.
     */
    private String row = null;

    /**
     * 컬럼의 개수. 초기값은 -1이며 생성자 호출시 지정하게 되면 Row의 컬럼의 수가 틀린 경우 예외가 발생된다.
     */
    private int initSize = -1;

    /**
     * Row를 컬럼 단위로 쪼개기 위한 입력 구분자.
     */
    private String inputDelimiter;

    /**
     * 컬럼을 최종 Row로 합치기 위한 출력 구분자.
     */
    private String outputDelimiter;

    /**
     * 최종 Row의 Hadoop {@link org.apache.hadoop.io.Text
     */
    private Text rowText = new Text();

    /**
     * 기본 생성자. 별도의 옵션이 주어져 있지 않으므로 기본 입출력 구분자만 설정한다.
     */
    public CsvRowParser() {
        setInputDelimiter(Constants.DEFAULT_DELIMITER);
        setOutputDelimiter(Constants.DEFAULT_DELIMITER);
    }

    /**
     * 기본 생성자. 기본 입출력 구분자와 지정한 컬럼의 개수를 설정한다.
     *
     * @param initSize 컬럼의 개수
     */
    public CsvRowParser(int initSize) {
        this.initSize = initSize;
        setInputDelimiter(Constants.DEFAULT_DELIMITER);
        setOutputDelimiter(Constants.DEFAULT_DELIMITER);
    }

    /**
     * 기본 생성자. 지정한 Row를 기본 입출력 구분자를 이용하여 처리하고 컬럼의 개수를 설정한다.
     *
     * @param row 컬럼의 개수
     */
    public CsvRowParser(String row) {
        setInputDelimiter(Constants.DEFAULT_DELIMITER);
        setOutputDelimiter(Constants.DEFAULT_DELIMITER);
        String[] columnsArray = splitPreserveAllTokens(row, this.inputDelimiter);
        columns = ArrayUtils.stringArrayToCollection(columnsArray);
        this.initSize = columnsArray.length;
    }

    /**
     * 기본 생성자.
     *
     * @param initSize        컬럼의 개수
     * @param inputDelimiter  입력 구분자
     * @param outputDelimiter 출력 구분자
     */
    public CsvRowParser(int initSize, String inputDelimiter, String outputDelimiter) {
        this.initSize = initSize;
        setInputDelimiter(inputDelimiter);
        setOutputDelimiter(outputDelimiter);
    }

    /**
     * 문자열로 구성된 Row를 컬럼으로 구분하기 위해서 사용하는 입력 구분자를 지정한다.
     *
     * @param inputDelimiter 입력 구분자.
     */
    public void setInputDelimiter(String inputDelimiter) {
        this.inputDelimiter = inputDelimiter;
    }

    /**
     * 출력 문자열을 구성할 때 컬럼을 구분하기 위한 출력 구분자를 지정한다.
     *
     * @param outputDelimiter 출력 구분자.
     */
    public void setOutputDelimiter(String outputDelimiter) {
        this.outputDelimiter = outputDelimiter;
    }

    /**
     * 지정한 위치에 있는 컬럼을 삭제한다
     *
     * @param index 삭제할 컬럼의 위치
     * @return 삭제한 컬럼값
     */
    public String remove(int index) {
        return columns.remove(index);
    }

    /**
     * 지정한 위치의 컬럼값을 반환한다.
     *
     * @param index 컬럼의 위치
     * @return 지정한 위치의 컬럼값
     */
    public String get(int index) {
        return columns.get(index);
    }

    /**
     * 지정한 위치의 컬럼값을 반환한다.
     *
     * @param index 컬럼의 위치
     * @return 지정한 위치의 컬럼값
     */
    public Integer getInt(int index) {
        return Integer.parseInt(columns.get(index));
    }

    /**
     * 지정한 위치의 컬럼값을 반환한다.
     *
     * @param index 컬럼의 위치
     * @return 지정한 위치의 컬럼값
     */
    public Float getFloat(int index) {
        return Float.parseFloat(columns.get(index));
    }

    /**
     * 지정한 위치에 있는 모든 컬럼을 삭제한다
     *
     * @param indexes 삭제할 컬럼의 위치
     * @return 삭제한 컬럼값
     */
    public String[] remove(Integer... indexes) {
        String[] removed = new String[indexes.length];
        Integer[] sorted = ArrayUtils.sortReverseIntegerArray(indexes);
        for (int i = 0; i < sorted.length; i++) {
            removed[i] = columns.get(sorted[i]);
            remove(indexes[i]);
        }
        return removed;
    }

    /**
     * 지정한 위치에 컬럼을 추가한다.
     *
     * @param column 컬럼값
     * @param index  추가할 위치
     */
    public void insert(String column, int index) {
        columns.add(index, column);
    }

    /**
     * 컬럼을 추가한다.
     *
     * @param column 컬럼값
     */
    public void insert(String column) {
        columns.add(column);
    }

    /**
     * 지정한 위치에 컬럼값을 변경한다.
     *
     * @param column 컬럼값
     * @param index  변경할 위치
     */
    public String change(String column, int index) {
        String changed = columns.get(index);
        columns.set(index, column);
        return changed;
    }

    /**
     * 컬럼의 개수를 반환한다.
     *
     * @return 컬럼의 개수
     */
    public int size() {
        return columns.size();
    }

    /**
     * 출력 구분자를 이용하여 하나의 Row를 구성한다.
     *
     * @return Row
     */
    public String toRow() {
        if (columns.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator iterator = columns.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(outputDelimiter);
            }
        }
        return builder.toString();
    }

    /**
     * 출력 구분자를 이용하여 하나의 Row를 {@link org.apache.hadoop.io.Text}으로 구성한다.
     *
     * @return Row
     */
    public Text toRowText() {
        rowText.set(toRow());
        return rowText;
    }

    /**
     * Row를 파싱하여 컬럼을 구성한다.
     *
     * @param row Row
     */
    public void parse(String row) {
        String[] strings = splitPreserveAllTokens(row, this.inputDelimiter);
        if (initSize == -1) { // Not initialized
            this.initSize = strings.length;
        }

        if (initSize != strings.length) {
            throw new IllegalArgumentException("Wrong column size. Init Size [" + initSize + "] Column Size [" + strings.length + "]");
        }
        clear();
        columns = ArrayUtils.stringArrayToCollection(strings);
        this.row = row;
    }

    /**
     * 모든 컬럼 정보를 삭제한다.
     */
    public void clear() {
        columns.clear();
        initSize = -1;
    }
}
