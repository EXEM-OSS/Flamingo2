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
package org.opencloudengine.flamingo2.mapreduce.document;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PdfTextExtractionMapper implements Mapper<BytesWritable, BytesWritable, NullWritable, Text> {

    String filename; // Old MapReduce API

    String lineDelimiter; // Delimiter per line

    String keyValueDelimiter; // Delimiter between filename and content

    @Override
    public void configure(JobConf job) {
        filename = job.get("map.input.file");
        lineDelimiter = job.get("line.delimiter", "    ");
        keyValueDelimiter = job.get("kv.delimiter", "####");
    }

    @Override
    public void map(BytesWritable key, BytesWritable value, OutputCollector<NullWritable, Text> collector, Reporter reporter) throws IOException {
        try {
            byte[] content = value.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Parser parser = new PDFParser(); // PDF Parser
            ContentHandler handler = new BodyContentHandler(baos);
            parser.parse(bais, handler, new Metadata(), new ParseContext());

            System.out.println(new String(key.getBytes()).trim());
            String body = new String(baos.toByteArray());
            Text text = new Text(new String(key.getBytes()).trim() + keyValueDelimiter + body.replaceAll("\n", lineDelimiter));
            collector.collect(NullWritable.get(), text);
        } catch (Exception ex) {
            reporter.getCounter("STAT", "PARSE_ERROR").increment(1);
        }
    }

    @Override
    public void close() throws IOException {
    }

}

