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
package org.opencloudengine.flamingo2.web.hive;

import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.ParseDriver;
import org.apache.hadoop.hive.ql.parse.ParseException;

import java.util.List;

public class HiveQueryTester {

    public static void main(String[] args) throws ParseException {
        ParseDriver driver = new ParseDriver();
        ASTNode node = driver.parse("select * from hello.my join b");
        System.out.println(node);
        // TOK_FROM TOK_TABNAME
        findTokFrom(node.getChildren());
    }

    public static void findTokFrom(List<Node> nodes) {
        for (Node node : nodes) {
            if (node != null) findTokFrom((ASTNode) node);
        }
    }

    public static void findTokFrom(ASTNode node) {
        if ("TOK_TABNAME".equals(node.getText())) {
            System.out.println(node);
            // Children이 1개, 2개일때 경우가 다름
            for (Node t : node.getChildren()) {
                ASTNode table = (ASTNode) t;
                System.out.println(table.getText());
            }
        } else if ("TOK_FROM".equals(node.getText())) {
            System.out.println(node);
        }

        if (node.getChildren() != null) findTokFrom(node.getChildren());
    }
}
