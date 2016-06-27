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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.dbf.DBFField;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.dbf.DBFReader;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce.DBFInputFormat;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce.PointInputFormat;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce.PolygonInputFormat;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.mapreduce.PolylineInputFormat;
import org.opencloudengine.flamingo2.mapreduce.gis.esri.shp.ShpReader;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_FAIL;
import static org.opencloudengine.flamingo2.mapreduce.core.Constants.JOB_SUCCESS;

/**
 * DBF 파일을 CSV 파일로 바꿔주는 MapReduce Driver.
 * 이 MapReduce Driver 는 다음의 파라미터를 가진다.
 * <ul>
 * <li><tt>outputDelimiter (od)</tt> - 출력 파일의 컬럼 구분자 (선택) (기본값 ,)</li>
 * <li><tt>characterSet (c)</tt> - dbf 파일의 Character Set (선택) (기본값 UTF-8)</li>
 * <li><tt>writeHeader (w)</tt> - 헤더 쓰기 (선택) (기본값 false)</li>
 * <li><tt>shpPath (s)</tt> - shp 파일의 HDFS 경로 (선택)</li>
 * <li><tt>divideSectionMeter (d)</tt> - shp 파일의 형태가 point 일 경우 구간 분할 거리 (선택) (-1 은 나누지 않음, 1 씩 증가) (기본값 -1)</li>
 * </ul>
 *
 * @author Haneul, Kim
 * @since 2.0.5
 */
public class DbfNShp2CsvDriver extends AbstractJob {

    public static final String COORDINATES_COLUMN_NAME = "COORDINATES";

    public static final String COORDINATES_SECTION = "SECTION";

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new DbfNShp2CsvDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addInputOption();
        addOutputOption();

        addOption("outputDelimiter", "od", "출력 파일 컬럼 구분자", Delimiter.COMMA.getDelimiter());
        addOption("characterSet", "c", "파일 인코딩", "UTF-8");
        addOption("writeHeader", "w", "헤더 쓰기", "false");
        addOption("shpPath", "s", "shp 파일의 HDFS 경로", false);
        addOption("divideSectionMeter", "d", "구간 분할 거리", false);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return JOB_FAIL;
        }

        Path inputPath = getInputPath();
        Path dbfCsvPath = getOutputPath();

        Job dbfToCsvJob = new Job(new Configuration(getConf()));
        //Job dbfToCsvJob = Job.getInstance();
        Configuration jobConf = dbfToCsvJob.getConfiguration();

        dbfToCsvJob.setJobName(dbfToCsvJob.getClass().getSimpleName());
        dbfToCsvJob.setJarByClass(DbfNShp2CsvDriver.class);

        // Mapper
        dbfToCsvJob.setMapOutputKeyClass(LongWritable.class);
        dbfToCsvJob.setMapOutputValueClass(MapWritable.class);

        // Reducer
        dbfToCsvJob.setOutputKeyClass(NullWritable.class);
        dbfToCsvJob.setOutputValueClass(Text.class);
        dbfToCsvJob.setReducerClass(Dbf2CsvReducer.class);

        FileOutputFormat.setOutputPath(dbfToCsvJob, dbfCsvPath);

        jobConf.set("outputDelimiter", parsedArgs.get("--outputDelimiter"));
        jobConf.set("characterSet", parsedArgs.get("--characterSet"));
        jobConf.set("writeHeader", parsedArgs.get("--writeHeader"));
        jobConf.set("divideSectionMeter", parsedArgs.get("--divideSectionMeter"));

        FileSystem fileSystem = FileSystem.get(getConf());
        DBFReader dbfReader = new DBFReader(fileSystem.open(inputPath), parsedArgs.get("--characterSet"));

        MultipleOutputs.addNamedOutput(dbfToCsvJob, "HEADER", TextOutputFormat.class, NullWritable.class, Text.class);
        MultipleOutputs.addNamedOutput(dbfToCsvJob, "COLUMNS", TextOutputFormat.class, NullWritable.class, Text.class);

        MultipleInputs.addInputPath(dbfToCsvJob, inputPath, DBFInputFormat.class, Dbf2CsvMapper.class);

        String shpPathString = parsedArgs.get("--shpPath");
        boolean withShp = false;
        if (StringUtils.isNotEmpty(shpPathString)) {
            setShpInput(dbfToCsvJob, fileSystem, shpPathString);
            withShp = true;
        }
        int divideSectionMeter = Integer.parseInt(parsedArgs.get("--divideSectionMeter"));
        setColumns(jobConf, dbfReader, withShp, divideSectionMeter);// set fieldNames, columns

        return dbfToCsvJob.waitForCompletion(true) ? JOB_SUCCESS : JOB_FAIL;
    }

    private void setColumns(Configuration configuration, DBFReader dbfReader, boolean withShp, int divideSectionMeter) {
        List<String> fieldNames = new ArrayList<>();
        List<String> fieldNameTypes = new ArrayList<>();

        for (DBFField field : dbfReader.getHeader().fields) {
            fieldNames.add(field.fieldName);
            fieldNameTypes.add(field.fieldName + " " + getHiveDataType(field.dataType));
        }

        if (divideSectionMeter > -1) {
            fieldNames.add(COORDINATES_SECTION);
            fieldNameTypes.add(COORDINATES_SECTION + " string");
        }

        if (withShp) {
            fieldNames.add(COORDINATES_COLUMN_NAME);
            fieldNameTypes.add(COORDINATES_COLUMN_NAME + " string");
        }

        configuration.set("fieldNames", StringUtils.join(fieldNames, ","));
        configuration.set("columns", StringUtils.join(fieldNameTypes, ", "));
    }

    private String getHiveDataType(byte type) {
        switch (type) {
            case 'C':
                return "string";
            case 'D':
                return "bigint";
            case 'F':
                return "double";
            case 'L':
                return "boolean";
            case 'N':
                return "bigint";
            default:
                return null;
        }
    }

    private void setShpInput(Job dbfToCsvJob, FileSystem fileSystem, String shpPathString) throws IOException {
        Path shpPath = new Path(shpPathString);
        int shapeType = new ShpReader(fileSystem.open(shpPath)).getHeader().shapeType;

        Class shpInputFormat = null;
        Class shpToCsvMapper = null;
        switch (shapeType) {
            case 1:
            case 8:
            case 11:
            case 18:
            case 21:
            case 28:
                shpInputFormat = PointInputFormat.class;
                shpToCsvMapper = PointMapper.class;
                break;
            case 3:
            case 13:
            case 23:
                shpInputFormat = PolylineInputFormat.class;
                shpToCsvMapper = PolylineMapper.class;
                break;
            case 5:
            case 15:
            case 25:
                shpInputFormat = PolygonInputFormat.class;
                shpToCsvMapper = PolygonMapper.class;
                break;
            case 31:
                throw new RuntimeException("[Flamingo2 DBF To CSV with SHP] Not supported MultiPatch.");
            case 0:
            default:
                throw new RuntimeException("[Flamingo2 DBF To CSV with SHP] Shape Type is required.");
        }

        MultipleInputs.addInputPath(dbfToCsvJob, shpPath, shpInputFormat, shpToCsvMapper);
    }
}