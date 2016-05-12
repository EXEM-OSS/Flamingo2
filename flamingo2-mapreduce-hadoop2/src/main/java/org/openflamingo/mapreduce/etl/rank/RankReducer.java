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
package org.openflamingo.mapreduce.etl.rank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.openflamingo.mapreduce.core.CsvRowParser;
import org.openflamingo.mapreduce.core.Delimiter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * 특정 컬럼을 기준으로 Sort된 파일에 순위를 기록하는 Rank ETL.
 * 최하 순위를 지정하여 이하의 순위들은 기록하지 않을 수 있다.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class RankReducer extends Reducer<NullWritable, Text, NullWritable, Text> {

    /**
     *
     */
    private String inputDelimiter;

    /**
     *
     */
    private String outputDelimiter;

    /**
     * 랭킹 시작 번호
     */
    private int startNumber;

    /**
     *
     */
    private int generatedSequenceIndex;

    /**
     *
     */
    private int keyColumn;

    /**
     *
     */
    private int topK;


    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        generatedSequenceIndex = configuration.getInt("generatedSequenceIndex", -1);
        startNumber = configuration.getInt("startNumber", 1);
        keyColumn = configuration.getInt("columnToRank", -1);
        topK = configuration.getInt("topK", -1);

        if (generatedSequenceIndex == -1) {
            throw new IllegalArgumentException("sequence index 오류. sequence의 index를 확인해 주세요.");
        }

        if (keyColumn == -1) {
            throw new IllegalArgumentException("-columnToRank의 index값을 다시 입력해 주세요. index는 0부터 시작 됩니다.");
        }
    }

    public void reduce(NullWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iterator = values.iterator();
        TreeMap map = new TreeMap();
        CsvRowParser parser = new CsvRowParser();
        parser.setInputDelimiter(inputDelimiter);

        while (iterator.hasNext()) {
            Text value = iterator.next();
            parser.parse(value.toString());
            String sequenceNumber = parser.get(generatedSequenceIndex);
            parser.remove(generatedSequenceIndex);  // remove Generate Sequence
            map.put(sequenceNumber, parser.toRow());
        }

        Set keySet = map.keySet();
        int rank = startNumber;
        String beforeKeyColumn = "";

        for (Iterator keySetIterator = keySet.iterator(); keySetIterator.hasNext(); ) {
            String line = (String) map.get(keySetIterator.next());
            parser.parse(line);

            if (!beforeKeyColumn.equals(parser.get(keyColumn))) {   // columnToRank들이 다른지 비교
                rank = startNumber;
            }

            if (topK == -1 || rank <= topK) {
                context.write(NullWritable.get(), new Text(line + outputDelimiter + rank));
            }

            beforeKeyColumn = parser.get(keyColumn);
            rank++;
        }
    }
}