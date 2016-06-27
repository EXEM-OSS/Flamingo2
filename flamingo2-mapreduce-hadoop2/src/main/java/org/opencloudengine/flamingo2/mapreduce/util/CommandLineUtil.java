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
package org.opencloudengine.flamingo2.mapreduce.util;

import org.apache.commons.cli.Options;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 커맨드 라인 유틸리티.
 *
 * @author Byoung Gon, Kim
 * @since 0.1
 */
public final class CommandLineUtil {

    public static void printHelp(Group group) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setGroup(group);
        formatter.print();
    }

    public static void printHelpWithGenericOptions(Group group) throws IOException {
        Options ops = new Options();
        new GenericOptionsParser(new Configuration(), ops, new String[0]);
        org.apache.commons.cli.HelpFormatter fmt = new org.apache.commons.cli.HelpFormatter();
        fmt.printHelp("<command> [Generic Options] [Job-Specific Options]",
                "Generic Options:", ops, "");

        PrintWriter pw = new PrintWriter(System.out, true);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setGroup(group);
        formatter.setPrintWriter(pw);
        formatter.printHelp();
        formatter.setFooter("Hadoop Job을 실행하는데 필요한 HDFS 디렉토리를 지정하거나 로컬 파일 시스템의 디렉토리를 지정하십시오.");
        formatter.printFooter();

        pw.flush();
    }

    /**
     * 커맨드 라인의 사용법을 구성한다.
     *
     * @param group Option Group
     * @param oe    예외
     * @throws java.io.IOException
     */
    public static void printHelpWithGenericOptions(Group group, OptionException oe) throws IOException {
        Options ops = new Options();
        new GenericOptionsParser(new Configuration(), ops, new String[0]);
        org.apache.commons.cli.HelpFormatter fmt = new org.apache.commons.cli.HelpFormatter();
        fmt.printHelp("<command> [Generic Options] [Job-Specific Options]",
                "Generic Options:", ops, "");

        PrintWriter pw = new PrintWriter(System.out, true);
        HelpFormatter formatter = new HelpFormatter();
        formatter.setGroup(group);
        formatter.setPrintWriter(pw);
        formatter.setException(oe);
        formatter.print();
        pw.flush();
    }

}

