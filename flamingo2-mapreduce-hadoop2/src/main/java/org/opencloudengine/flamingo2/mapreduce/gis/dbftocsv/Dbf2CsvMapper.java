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
package org.opencloudengine.flamingo2.mapreduce.gis.dbftocsv;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * DBF 파일을 CSV 파일로 바꿔주는 Mapper
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class Dbf2CsvMapper extends Mapper<LongWritable, MapWritable, LongWritable, MapWritable> {

    private List<String> fieldNames;

    private boolean writeHeader;

    private MultipleOutputs<LongWritable, MapWritable> out;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();

        String[] fieldNamesArray = configuration.get("fieldNames").split(",");// set from DbfNShp2CsvDriver

        this.fieldNames = Arrays.asList(fieldNamesArray);
        this.writeHeader = configuration.getBoolean("writeHeader", false);
        this.out = new MultipleOutputs<>(context);
    }

    @Override
    protected void map(LongWritable key, MapWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, value);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        if (this.writeHeader) {
            // write HEADER file and COLUMNS file in outputPath
            out.write("HEADER", NullWritable.get(), StringUtils.join(this.fieldNames, ","));
            out.write("COLUMNS", NullWritable.get(), context.getConfiguration().get("columns"));// set from DbfNShp2CsvDriver
            out.close();
        }
    }
}