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
package org.opencloudengine.flamingo2.mapreduce.rules.esper;

import com.espertech.esper.client.*;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.opencloudengine.flamingo2.mapreduce.core.Constants;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Esper .epl 파일을 로딩하여 로그 데이터에서 EPL을 실행하는 Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class EsperMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Configuration configuration;

    /**
     * Esper .epl 파일의 내용
     */
    private byte[] epl;

    /**
     * 로그 파일의 컬럼 구분자
     */
    private String delimiter;

    /**
     * 로그의 컬럼명
     */
    private String[] columnNames;

    /**
     * 컬럼의 자료형
     */
    private String[] columnTypes;

    private static Text output = new Text();

    private EPRuntime cepRT;

    private EPStatement cepStatement;

    private EPAdministrator cepAdministrator;

    private Context context;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.context = context;
        this.configuration = context.getConfiguration();
        this.epl = StringUtils.unescape(configuration.get("encodedEPL", "")).getBytes();
        this.delimiter = configuration.get("delimiter", Delimiter.COMMA.toString());
        this.columnNames = StringUtils.splitPreserveAllTokens(configuration.get("columnNames"), ",");
        this.columnTypes = StringUtils.splitPreserveAllTokens(configuration.get("columnTypes"), ",");

        if (columnNames.length == 0 || columnTypes.length == 0) {
            throw new RuntimeException("Invalid Arguments : columnNames or columnTypes");
        }

        // Loading Esper EPL
        if (configuration.get("eplPath") != null) {
            FileSystem fs = FileSystem.get(context.getConfiguration());
            this.epl = loadBytesFromFile(fs, configuration.get("eplPath"));
        }

        if (this.epl == null) {
            throw new RuntimeException("Invalid Arguments : encodedEPL or eplPath");
        }

        // The Configuration is meant only as an initialization-time object.
        com.espertech.esper.client.Configuration cepConfig = new com.espertech.esper.client.Configuration();

        // Register Event Type
        Map<String, Object> def = new HashMap<String, Object>();
        for (int i = 0; i < columnNames.length; i++) {
            def.put(columnNames[i], getType(columnTypes[i]));
        }
        cepConfig.addEventType("event", def);

        EPServiceProvider cep = EPServiceProviderManager.getProvider("cepEngine", cepConfig);
        this.cepRT = cep.getEPRuntime();

        // Register an EPL statement
        this.cepAdministrator = cep.getEPAdministrator();
        this.cepStatement = cepAdministrator.createEPL(new String(this.epl));
        this.cepStatement.addListener(new CEPEventListener());
    }

    private Class getType(String columnType) {
        switch (columnType) {
            case "string":
                return String.class;
            case "int":
                return Integer.class;
            case "integer":
                return Integer.class;
            case "long":
                return Long.class;
            case "boolean":
                return Boolean.class;
            case "byte":
                return Byte.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            default:
                return String.class;
        }
    }

    private Object getValueAsType(String columnType, String value) {
        switch (columnType) {
            case "string":
                return value;
            case "int":
                return Integer.parseInt(value);
            case "integer":
                return Integer.parseInt(value);
            case "long":
                return Long.parseLong(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "byte":
                return Byte.parseByte(value);
            case "float":
                return Float.parseFloat(value);
            case "double":
                return Double.parseDouble(value);
            default:
                return value;
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String row = value.toString();
        CounterUtils.writerMapperCounter(this, Constants.TOTAL_ROW_COUNT, context);
        String[] values = StringUtils.splitPreserveAllTokens(row, this.delimiter);
        if (values.length == 0 || (values.length != columnNames.length)) {
            CounterUtils.writerMapperCounter(this, "Invalid Row", context);
        } else {
            Map data = new HashMap();
            data.put("_row", row);
            for (int i = 0; i < columnNames.length; i++) {
                data.put(columnNames[i], getValueAsType(columnTypes[i], values[i]));
            }

            // Execute CEP Engine
            this.cepRT.sendEvent(data, "event");

            context.getCounter(EsperMapper.class.getName(), "Event Sent").increment(1);
        }
    }

    class CEPEventListener implements UpdateListener {
        @Override
        public void update(EventBean[] eventBeans, EventBean[] eventBeans1) {
            context.getCounter(EsperMapper.class.getName(), "Event Received").increment(1);
            try {
                EventBean eventBean = eventBeans[0];
                Map event = (Map) eventBean.getUnderlying();
                output.set((String) event.get("_row"));
                context.write(NullWritable.get(), output);

                context.getCounter(EsperMapper.class.getName(), "Event Passed").increment(1);
            } catch (Exception e) {
                context.getCounter(EsperMapper.class.getName(), "Event Not Passed").increment(1);
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        try {
            this.cepAdministrator.stopAllStatements();
            this.cepAdministrator.destroyAllStatements();
        } catch (Exception e) {
            // Nothing
        }
    }

    public static byte[] loadBytesFromFile(FileSystem fs, String sourceFilePath) throws IOException {
        FSDataInputStream is = fs.open(new Path(sourceFilePath));
        byte[] bytes = FileCopyUtils.copyToByteArray(is);
        IOUtils.closeQuietly(is);
        return bytes;
    }
}