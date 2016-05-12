/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openflamingo.mapreduce.etl;

import org.apache.hadoop.util.ProgramDriver;
import org.openflamingo.mapreduce.core.Constants;
import org.openflamingo.mapreduce.etl.accounting.AccountingDriver;
import org.openflamingo.mapreduce.etl.aggregate.AggregateDriver;
import org.openflamingo.mapreduce.etl.apache.ApacheHttpAccessLogDriver;
import org.openflamingo.mapreduce.etl.clean.CleanDriver;
import org.openflamingo.mapreduce.etl.filter.FilterDriver;
import org.openflamingo.mapreduce.etl.generate.GenerateKeyDriver;
import org.openflamingo.mapreduce.etl.grep.GrepDriver;
import org.openflamingo.mapreduce.etl.groupby.GroupByDriver;
import org.openflamingo.mapreduce.etl.rank.RankDriver;
import org.openflamingo.mapreduce.etl.remover.CharacterRemoverDriver;
import org.openflamingo.mapreduce.etl.replace.column.ReplaceColumnDriver;
import org.openflamingo.mapreduce.etl.replace.delimiter.ReplaceDelimiterDriver;
import org.openflamingo.mapreduce.uima.SequenceFileConversionDriver;
import org.openflamingo.mapreduce.uima.UIMADriver;

/**
 * 모든 MapReduce를 실행하기 위한 Alias를 제공하는 Program Driver.
 *
 * @author Byoung Gon, Kim
 * @author Seo Ji Hye
 * @since 0.1
 */
public class MapReduceDriver {

    public static void main(String argv[]) {
        ProgramDriver programDriver = new ProgramDriver();
        try {
            programDriver.addClass("aggregate", AggregateDriver.class, "하나 이상의 파일을 합치는 MapReduce ETL");
            programDriver.addClass("clean", CleanDriver.class, "지정한 컬럼을 삭제하는 MapReduce ETL");
            programDriver.addClass("grep", GrepDriver.class, "파일의 ROW를 RegEx로 Grep하는 MapReduce ETL");
            programDriver.addClass("filter", FilterDriver.class, "파일의 특정 컬럼을 필터링 조건에 따라 필터링하는 MapReduce ETL");
            programDriver.addClass("replace_delimiter", ReplaceDelimiterDriver.class, "컬럼 구분자를 일괄 변경하는 MapReduce ETL");
            programDriver.addClass("replace_column", ReplaceColumnDriver.class, "컬럼 구분자를 일괄 변경하는 MapReduce ETL");
            programDriver.addClass("groupby", GroupByDriver.class, "지정한 Key를 기준으로 Value를 취합하는 MapReduce ETL");
            programDriver.addClass("generate", GenerateKeyDriver.class, "특정 COLUMN을 기준으로 SEQUENCE NUMBER를 생성하는 MapReduce ETL");
            programDriver.addClass("rank", RankDriver.class, "특정 COLUMN을 기준으로 순위를 정하는 MapReduce ETL(Top K기능 옵션)");
            programDriver.addClass("accounting", AccountingDriver.class, "사용자가 정의한 수식을 이용하여 컬럼별 계산을 할 수 있는 MapReduce ETL");
            programDriver.addClass("textToSeq", org.openflamingo.mapreduce.etl.sequence.SequenceFileConversionDriver.class, "Text File To Sequence File Conversion MapReduce Job");
            programDriver.addClass("apache_access", ApacheHttpAccessLogDriver.class, "Apache Http Server Access Log MapReduce Job");
            programDriver.addClass("remove_chars", CharacterRemoverDriver.class, "Character Remover MapReduce Job");
            programDriver.addClass("uima_sequence", SequenceFileConversionDriver.class, "To Sequence File for Apache UIMA MapReduce Job");
            programDriver.addClass("uima", UIMADriver.class, "Apache UIMA MapReduce Job");
            programDriver.driver(argv);
            System.exit(Constants.JOB_SUCCESS);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(Constants.JOB_FAIL);
        }
    }
}

