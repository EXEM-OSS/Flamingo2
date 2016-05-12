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
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

/**
 * Custom Reducer which can filter documents before they are written out.
 * *
 */

public class DocumentFilterReducer implements Reducer<Text, UIMADocument, Text, UIMADocument> {

    private DocumentFilter docFilter;

    /**
     * Checks whether any filters have been specified in the configuration
     */
    public static boolean isRequired(JobConf conf) {
        return DocumentFilter.isRequired(conf);
    }

    @Override
    public void configure(JobConf conf) {
        this.docFilter = DocumentFilter.getFilters(conf);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void reduce(Text key, Iterator<UIMADocument> doc, OutputCollector<Text, UIMADocument> output, Reporter reporter) throws IOException {
        while (doc.hasNext()) {
            UIMADocument inputDoc = doc.next();
            boolean keep = docFilter.keep(inputDoc);
            if (!keep) {
                reporter.incrCounter("DocumentFilterReducer", "DOC SKIPPED BY FILTERS", 1);
                continue;
            }
            output.collect(key, inputDoc);
        }
    }
}
