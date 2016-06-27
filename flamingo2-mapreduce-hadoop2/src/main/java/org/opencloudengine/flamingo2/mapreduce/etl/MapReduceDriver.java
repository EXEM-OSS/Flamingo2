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
package org.opencloudengine.flamingo2.mapreduce.etl;

import org.apache.hadoop.util.ProgramDriver;
import org.opencloudengine.flamingo2.mapreduce.core.Constants;
import org.opencloudengine.flamingo2.mapreduce.etl.accounting.AccountingDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.aggregate.AggregateDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.apache.ApacheHttpAccessLogDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.clean.CleanDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.filter.FilterDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.generate.GenerateKeyDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.grep.GrepDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.groupby.GroupByDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.rank.RankDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.remover.CharacterRemoverDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.replace.column.ReplaceColumnDriver;
import org.opencloudengine.flamingo2.mapreduce.etl.replace.delimiter.ReplaceDelimiterDriver;
import org.opencloudengine.flamingo2.mapreduce.gis.dbftocsv.DbfNShp2CsvDriver;
import org.opencloudengine.flamingo2.mapreduce.gis.gps.ExtractLinkDriver;
import org.opencloudengine.flamingo2.mapreduce.rules.drools.DroolsDriver;
import org.opencloudengine.flamingo2.mapreduce.rules.esper.EsperDriver;
import org.opencloudengine.flamingo2.mapreduce.rules.mvel.MvelDriver;
import org.opencloudengine.flamingo2.mapreduce.uima.SequenceFileConversionDriver;
import org.opencloudengine.flamingo2.mapreduce.uima.UIMADriver;
import org.opencloudengine.flamingo2.mapreduce.util.Escaper;

/**
 * 모든 MapReduce를 실행하기 위한 Alias를 제공하는 Program Driver.
 *
 * @author Byoung Gon, Kim
 * @author Haneul, Kim
 * @since 0.1
 */
public class MapReduceDriver {

    public static void main(String argv[]) {
        ProgramDriver programDriver = new ProgramDriver();
        try {
            programDriver.addClass("accounting", AccountingDriver.class, "사용자가 정의한 수식을 이용하여 컬럼별 계산을 할 수 있는 MapReduce ETL");
            programDriver.addClass("aggregate", AggregateDriver.class, "하나 이상의 파일을 합치는 MapReduce ETL");
            programDriver.addClass("apache_access", ApacheHttpAccessLogDriver.class, "Apache Http Server Access Log MapReduce Job");
            programDriver.addClass("clean", CleanDriver.class, "지정한 컬럼을 삭제하는 MapReduce ETL");
            programDriver.addClass("filter", FilterDriver.class, "파일의 특정 컬럼을 필터링 조건에 따라 필터링하는 MapReduce ETL");
            programDriver.addClass("generate", GenerateKeyDriver.class, "특정 COLUMN을 기준으로 SEQUENCE NUMBER를 생성하는 MapReduce ETL");
            programDriver.addClass("remove_chars", CharacterRemoverDriver.class, "Character Remover MapReduce Job");
            programDriver.addClass("grep", GrepDriver.class, "파일의 ROW를 RegEx로 Grep하는 MapReduce ETL");
            programDriver.addClass("groupby", GroupByDriver.class, "지정한 Key를 기준으로 Value를 취합하는 MapReduce ETL");
            programDriver.addClass("rank", RankDriver.class, "특정 COLUMN을 기준으로 순위를 정하는 MapReduce ETL (Top K 기능 옵션)");
            programDriver.addClass("replace_column", ReplaceColumnDriver.class, "컬럼 구분자를 일괄 변경하는 MapReduce ETL");
            programDriver.addClass("replace_delimiter", ReplaceDelimiterDriver.class, "컬럼 구분자를 일괄 변경하는 MapReduce ETL");
            programDriver.addClass("textToSeq", org.opencloudengine.flamingo2.mapreduce.etl.sequence.SequenceFileConversionDriver.class, "Text File To Sequence File Conversion MapReduce Job");
            programDriver.addClass("uima_sequence", SequenceFileConversionDriver.class, "To Sequence File for Apache UIMA MapReduce Job");
            programDriver.addClass("uima", UIMADriver.class, "Apache UIMA MapReduce Job");
            programDriver.addClass("mvel", MvelDriver.class, "MVEL MapReduce Job");
            programDriver.addClass("drools", DroolsDriver.class, "Drools .dri MapReduce Job");
            programDriver.addClass("esper", EsperDriver.class, "Esper .epl MapReduce Job");
            programDriver.addClass("escape", Escaper.class, "String Escape Utility");
            programDriver.addClass("dbfToCsv", DbfNShp2CsvDriver.class, "dbf 에서 csv 로 바꾸는 MapReduce Job");
            programDriver.addClass("extractLink", ExtractLinkDriver.class, "GPS 의 위도, 경도로부터 LINK ID 를 추출하는 MapReduce");
            programDriver.driver(argv);
            System.exit(Constants.JOB_SUCCESS);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(Constants.JOB_FAIL);
        }
    }
}

