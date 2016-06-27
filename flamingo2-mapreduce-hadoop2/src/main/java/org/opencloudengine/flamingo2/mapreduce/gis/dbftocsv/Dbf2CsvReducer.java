package org.opencloudengine.flamingo2.mapreduce.gis.dbftocsv;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DBF 파일과 SHP 파일을 CSV 파일로 바꿔주는 Reducer
 *
 * @author Haneul, Kim
 * @since 2.1.0
 */
public class Dbf2CsvReducer extends Reducer<LongWritable, MapWritable, NullWritable, Text> {

    private List<Text> fieldNames;

    private String outputDelimiter;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();

        String[] fieldNamesArray = configuration.get("fieldNames").split(",");// set from DbfNShp2CsvDriver
        this.fieldNames = new ArrayList<>(fieldNamesArray.length);
        for (String fieldName : fieldNamesArray) {
            this.fieldNames.add(new Text(fieldName));
        }
        this.outputDelimiter = configuration.get("outputDelimiter", Delimiter.COMMA.getDelimiter());
    }

    @Override
    protected void reduce(LongWritable key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
        Iterator<MapWritable> iterator = values.iterator();
        List<String> fieldValues = new ArrayList<>();
        fieldValues.add(key.toString());
        MapWritable writeValue = new MapWritable();
        while (iterator.hasNext()) {
            writeValue.putAll(iterator.next());
        }
        for (Text fieldName : this.fieldNames) {
            fieldValues.add(writeValue.get(fieldName).toString().replaceAll("\r\n|\n", " ").trim());
        }
        context.write(NullWritable.get(), new Text(StringUtils.join(fieldValues, this.outputDelimiter)));
    }
}