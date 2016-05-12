/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.uima;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Custom Mapper which can filter documents before they are written out.
 * *
 */

public class DocumentFilterMapper implements Mapper<Text, UIMADocument, Text, UIMADocument> {

    public static final Logger LOG = LoggerFactory.getLogger(DocumentFilterMapper.class);

    private DocumentFilter docFilter;

    /**
     * Checks whether any filters have been specified in the configuration
     */
    public static boolean isRequired(JobConf conf) {
        return (DocumentFilter.isRequired(conf));
    }

    @Override
    public void configure(JobConf conf) {
        this.docFilter = DocumentFilter.getFilters(conf);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void map(Text key, UIMADocument inputDoc, OutputCollector<Text, UIMADocument> output, Reporter reporter) throws IOException {
        boolean keep = docFilter.keep(inputDoc);
        if (!keep) {
            reporter.incrCounter("DocumentFilterMapper", "DOC SKIPPED BY FILTERS", 1);
            return;
        }
        output.collect(key, inputDoc);
    }

}
