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
package org.opencloudengine.flamingo2.mapreduce.etl.rank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencloudengine.flamingo2.mapreduce.core.CsvRowParser;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;

import java.io.File;
import java.io.FileOutputStream;
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
public class RankReducer extends Reducer<Text, Text, NullWritable, Text> {

    private String inputDelimiter;

    private String outputDelimiter;

    /**
     * 랭킹 시작 번호
     */
    private int startNumber;

    /**
     * Generate Sequence에서 생성한 시퀀스의 위치 (기본값은 0)
     */
    private int generatedSequenceIndex;

    /**
     * Rank를 생성하기 위해서 기준이 되는 컬럼의 위치 (예; 과목)
     */
    private int keyColumn;

    /**
     * Rank를 계산한 후 상위 N개를 추출하기 위한 개수
     */
    private int topK;


    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
        inputDelimiter = configuration.get("inputDelimiter", Delimiter.COMMA.getDelimiter());
        generatedSequenceIndex = configuration.getInt("generatedSequenceIndex", 0);
        startNumber = configuration.getInt("startNumber", 1);
        keyColumn = configuration.getInt("columnToRank", -1);
        topK = configuration.getInt("topK", -1);

        if (generatedSequenceIndex == -1) {
            throw new IllegalArgumentException("sequence index 오류. sequence의 index를 확인해 주세요.");
        }

        if (keyColumn == -1) {
            throw new IllegalArgumentException("--columnToRank의 index값을 다시 입력해 주세요. index는 0부터 시작 됩니다.");
        }
    }

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iterator = values.iterator();
        TreeMap map = new TreeMap();
        CsvRowParser parser = new CsvRowParser();
        parser.setInputDelimiter(inputDelimiter);

        while (iterator.hasNext()) {
            Text value = iterator.next();
            parser.parse(value.toString());
            long sequenceNumber = Long.parseLong(parser.get(generatedSequenceIndex));
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