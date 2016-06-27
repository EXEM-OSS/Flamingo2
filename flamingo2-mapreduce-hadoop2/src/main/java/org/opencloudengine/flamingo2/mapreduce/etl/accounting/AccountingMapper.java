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
package org.opencloudengine.flamingo2.mapreduce.etl.accounting;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.mvel2.MVEL;
import org.opencloudengine.flamingo2.mapreduce.core.CsvRowParser;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.ArrayUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 사칙연산을 이용하여 컬럼을 연산하는 MapReduce Mapper.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class AccountingMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Configuration configuration;

    private String inputDelimiter;

    private String outputDelimiter;

    private Path expressionPath;

    private String expression;

    private Serializable compliedExpression;

    private int columnSize;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.configuration = context.getConfiguration();
        this.inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        this.outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        this.columnSize = configuration.getInt("columnSize", -1);

        if (columnSize == -1) {
            throw new IllegalArgumentException("Invalid Column Size");
        }

        this.expression = configuration.get("expression", null);
        this.compliedExpression = MVEL.compileExpression(expression);
    }

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
     * Hadoop File System을 반환.
     *
     * @return Hadoop FileSystem
     * @throws java.io.IOException
     */
    private FileSystem getFileSystem() throws IOException {
        return FileSystem.get(configuration);
    }
}