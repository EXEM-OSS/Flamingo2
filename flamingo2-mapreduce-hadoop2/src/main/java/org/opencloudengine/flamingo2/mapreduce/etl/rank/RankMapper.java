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

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class RankMapper extends Mapper<LongWritable, Text, Text, Text> {

    /**
     * 입력 파일의 구분자.
     */
    private String inputDelimiter;

    /**
     * Rank의 기준 Key 위치.
     */
    private int columnToRank;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        inputDelimiter = configuration.get("inputDelimiter");
        columnToRank = configuration.getInt("columnToRank", -1);
        if (columnToRank == -1) {
            throw new IllegalArgumentException("You must specify 'columnToRank' for Rank");
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = StringUtils.splitByWholeSeparatorPreserveAllTokens(value.toString(), inputDelimiter);
        context.write(new Text(columns[columnToRank + 1]), value);
    }
}