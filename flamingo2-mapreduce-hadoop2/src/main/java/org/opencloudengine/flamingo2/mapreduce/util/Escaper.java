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

import org.apache.hadoop.util.ToolRunner;
import org.opencloudengine.flamingo2.mapreduce.core.AbstractJob;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class Escaper extends AbstractJob {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Escaper(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        addOption("file", "f", "Escape 처리할 파일", true);

        Map<String, String> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return -1;
        }

        String file = parsedArgs.get("--file");

        // If the script file exists in working directory, load the script file.
        String scriptPath = parsedArgs.get("--file");
        if (!StringUtils.isEmpty(scriptPath)) {
            String workingDirectory = System.getProperty("user.dir");
            File sf = new File(workingDirectory, scriptPath);
            if (sf.exists()) {
                String script = ResourceUtils.getResourceTextContents(new FileInputStream(sf));
                System.out.println(StringUtils.escape(script));
            } else {
                File f = new File(file);
                String script = ResourceUtils.getResourceTextContents(new FileInputStream(f));
                System.out.println(StringUtils.escape(script));
            }
        }
        return 0;
    }
}