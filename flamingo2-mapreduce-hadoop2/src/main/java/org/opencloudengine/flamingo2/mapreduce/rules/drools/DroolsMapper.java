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
package org.opencloudengine.flamingo2.mapreduce.rules.drools;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.rule.builder.dialect.java.JavaDialectConfiguration;
import org.drools.runtime.StatelessKnowledgeSession;
import org.mvel2.optimizers.OptimizerFactory;
import org.opencloudengine.flamingo2.mapreduce.core.Constants;
import org.opencloudengine.flamingo2.mapreduce.core.Delimiter;
import org.opencloudengine.flamingo2.mapreduce.util.CounterUtils;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Drools .drl 파일을 로딩하여 로그 데이터에서 Rule을 실행하는 Mapper.
 *
 * @author Byoung Gon, Kim
 * @since 2.0
 */
public class DroolsMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

    private Configuration configuration;

    /**
     * Drools .drl 파일의 내용
     */
    private byte[] rule;

    /**
     * 로그 파일의 컬럼 구분자
     */
    private String delimiter;

    /**
     * 로그의 컬럼명
     */
    private String[] columns;

    private static Text output = new Text();

    private StatelessKnowledgeSession ksession;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.configuration = context.getConfiguration();
        this.rule = StringUtils.unescape(configuration.get("encodedRule", "")).getBytes();
        this.delimiter = configuration.get("delimiter", Delimiter.COMMA.toString());
        this.columns = StringUtils.splitPreserveAllTokens(configuration.get("columnNames"), ",");

        if (columns.length == 0) {
            throw new RuntimeException("Invalid Arguments : columnNames");
        }

        // Loading Drools DRI
        if (configuration.get("rulePath") != null) {
            FileSystem fs = FileSystem.get(context.getConfiguration());
            this.rule = loadBytesFromFile(fs, configuration.get("rulePath"));
        }

        if (this.rule == null) {
            throw new RuntimeException("Invalid Arguments : encodedRule or rulePath");
        }

        // Initialization Drools
        OptimizerFactory.setDefaultOptimizer(OptimizerFactory.SAFE_REFLECTIVE);
        Properties properties = new Properties();
        properties.setProperty("drools.dialect.java.compiler", "JANINO");
        PackageBuilderConfiguration cfg = new PackageBuilderConfiguration(properties);
        JavaDialectConfiguration javaConf = (JavaDialectConfiguration) cfg.getDialectConfiguration("java");
        javaConf.setCompiler(JavaDialectConfiguration.JANINO);
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(cfg);

        Resource res = ResourceFactory.newByteArrayResource(rule);
        kbuilder.add(res, ResourceType.DRL);

        if (kbuilder.hasErrors()) {
            throw new RuntimeException("Unable to compile rule file: " + kbuilder.getErrors().toString());
        }

        final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(pkgs);

        this.ksession = kbase.newStatelessKnowledgeSession();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String row = value.toString();
        CounterUtils.writerMapperCounter(this, Constants.TOTAL_ROW_COUNT, context);
        String[] values = StringUtils.splitPreserveAllTokens(row, this.delimiter);
        if (values.length == 0 || (values.length != columns.length)) {
            CounterUtils.writerMapperCounter(this, "Invalid Row", context);
        } else {
            Map data = new HashMap();
            for (int i = 0; i < columns.length; i++) {
                data.put(columns[i], values[i]);
            }

            // Execute Drools Rule
            this.ksession.execute(data);

            if(data.get("result") != null) {
                output.set(String.valueOf(data.get("result")));
                context.write(NullWritable.get(), output);
                CounterUtils.writerMapperCounter(this, "Rule Passed", context);
            } else {
                CounterUtils.writerMapperCounter(this, "Rule Not Passed", context);
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        this.ksession = null;
    }

    public static byte[] loadBytesFromFile(FileSystem fs, String sourceFilePath) throws IOException {
        FSDataInputStream is = fs.open(new Path(sourceFilePath));
        byte[] bytes = FileCopyUtils.copyToByteArray(is);
        IOUtils.closeQuietly(is);
        return bytes;
    }
}