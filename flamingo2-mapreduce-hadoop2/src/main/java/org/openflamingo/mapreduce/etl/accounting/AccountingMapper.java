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
package org.openflamingo.mapreduce.etl.accounting;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.mvel2.MVEL;
import org.openflamingo.mapreduce.core.CsvRowParser;
import org.openflamingo.mapreduce.core.Delimiter;
import org.openflamingo.mapreduce.util.ArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 하나 이상의 입력 파일을 받아서 합치는 Aggregation ETL Mapper.
 * 이 Mapper는 입력을 그대로 다시 출력한다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class AccountingMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    /**
     *
     */
    private Configuration configuration;
    /**
     *
     */
    private String inputDelimiter;

    /**
     *
     */
    private String outputDelimiter;

    /**
     * user가 입력한 수식
     */
    private Path expressionPath;

    /**
     *
     */
    private String expression;

    /**
     * compile된 수식
     */
    private Serializable compliedExpression;

    /**
     *
     */
    private int columnSize;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        columnSize = configuration.getInt("columnSize", -1);

        if (columnSize == -1) {
            throw new IllegalArgumentException("column size를 입력해 주세요.");
        }

        expressionPath = new Path(configuration.get("expression", null));

        validateInputFile();
        readFromInputFile();
        compliedExpression = MVEL.compileExpression(expression);    // FIXME validation
    }

    /**
     * @param key
     * @param value
     * @param context
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        CsvRowParser parser = new CsvRowParser();
        parser.setInputDelimiter(inputDelimiter);
        parser.setOutputDelimiter(outputDelimiter);
        parser.parse(value.toString());

        System.out.println(" INPUT FILE VALUE :  " + value.toString());
        // collect columns
        String[] columnsForKeys = extractColumnsAsKeys();
        Integer[] columnsForValues = extractColumnsAsValues();

        Map columnsToEvaluate = new HashMap();
        for (int i = 0; i < columnsForValues.length; i++) {
            columnsToEvaluate.put(columnsForKeys[i], Double.parseDouble(parser.get(columnsForValues[i])));
        }

        // evaluate
        Double result = (Double) MVEL.executeExpression(compliedExpression, columnsToEvaluate);
        context.write(NullWritable.get(), new Text(result.toString()));        // TODO format?
    }

    /**
     * Map의 key에 넣을 인덱스값 파싱
     */
    private String[] extractColumnsAsKeys() {
        // FIXME embedded conditions
        String str = expression.replaceAll("[\\(|\\)|\u0009|\u0020|\\n||\u0004|\u0000]", "");
        return str.split("[\\+|\\-|\\*|\\/]");
    }

    /**
     * Map에 value에 넣을 인덱스값 파싱
     */
    private Integer[] extractColumnsAsValues() {
        // FIXME embedded conditions
        String str = expression.replaceAll("[\\(|\\)|\\$|\u0009|\u0020|\\n|\u0004|\u0000]", "");
        String[] tempArray = str.split("[\\+|\\-|\\*|\\/]");

        // convert string array to integer array
        return ArrayUtils.toIntegerArray(tempArray);
    }

    /**
     * 사용자가 입력한 수식 파일이 존재하는지 검사.
     *
     * @throws java.io.IOException
     */
    private void validateInputFile() throws IOException {
        final FileSystem fs = getFileSystem();
        if (!fs.exists(expressionPath)) {
            throw new IllegalArgumentException("expression의 경로가 올바르지 않습니다. 입력한 경로 [" + expressionPath + "]");
        } else if (!fs.isFile(expressionPath)) {
            throw new IllegalArgumentException("expression이 File이 아닙니다.");
        }
    }

    /**
     * 사용자가 입력한 파일에서 bytes array를 읽어 string으로 변환하여 expression을 읽어와 저장.
     *
     * @throws java.io.IOException
     */
    private void readFromInputFile() throws IOException {
        FileSystem fs = getFileSystem();
        FSDataInputStream in = fs.open(expressionPath);

        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[1024]; // FIXME fixed memory size

        while (in.read(buffer) > 0) {
            builder.append(new String(buffer, "UTF-8"));
        }

        expression = builder.toString();
    }

    /**
     * Hadoop File System을 반환.
     *
     * @return Hadoop FileSystem
     * @throws java.io.IOException
     */
    private FileSystem getFileSystem() throws IOException {
        return FileSystem.get(configuration);
    }
}