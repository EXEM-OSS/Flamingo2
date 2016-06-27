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
package org.opencloudengine.flamingo2.mapreduce.rules.mvel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.opencloudengine.flamingo2.mapreduce.util.ArrayUtils;
import org.opencloudengine.flamingo2.mapreduce.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MVEL Test class for MapReduce MVEL Module
 *
 * @author Haneul, Kim
 * @since 2.0.5
 */
public class MVELTest {

    private Map<String, Object> vars;

    private String expr;

    private String line;

    private static final String PREFIX = "#{";

    private static final String POSTFIX = "}";

    @Before
    public void setUp() {
        this.vars = new HashMap<String, Object>();
        vars.put("a", 5);
        vars.put("b", 2);
        vars.put("c", 3);

        this.expr = "(a + b)*(a + c)";

        this.line = "3882 YELLOWSTONE LN,EL DORADO HILLS,95762,CA,3,2,1362,Residential,Thu May 15 00:00:00 EDT 2008,235738,38.655245,-121.075915995";
    }

    @Test
    public void compileTest() {
        ParserContext pCtx = new ParserContext();
        ExpressionCompiler compiler = new ExpressionCompiler(expr, pCtx);
        VariableResolverFactory functionFactory = new MapVariableResolverFactory(vars);

        CompiledExpression compiledExpression = compiler.compile();
        Object directValue = compiledExpression.getDirectValue(pCtx, functionFactory);

        Assert.assertEquals(directValue, 56);
    }

    @Test
    public void varPrintTest() {
        VariableResolverFactory functionFactory = new MapVariableResolverFactory(vars);
        Object result = MVEL.eval(
                "def print() {\n" +
                        "\tSystem.out.println(\"a = \" + a);\n" +
                        "};\n" +
                        "print()", functionFactory);
        System.err.println("result = " + result);
    }

    @Test
    public void userOutputTest() {
        List<String> keys = new ArrayList<>();
        List<String> values = ArrayUtils.stringArrayToCollection(line.split(","));
        for (int i = 0, size = values.size(); i < size; i++) {
            keys.add(String.valueOf(i));
        }
        System.err.println("keys = " + keys);
        System.err.println("values = " + values);

        Map<String, String> inputMap = createMap(keys, values);
        VariableResolverFactory functionFactory = new MapVariableResolverFactory(inputMap);
        String userDefineOutput = "#{0}: beds = #{4}, baths = #{5}, price = #{9}";
        String convertedOutput = convertOutput(userDefineOutput, inputMap);
        System.err.println("convertedOutput = " + convertedOutput);
        Object output = MVEL.eval(
                "def print() {\n" +
                        "output = \"" + convertedOutput + "\"" +
                        "};\n" +
                        "print()", functionFactory);

        Assert.assertEquals(output, "3882 YELLOWSTONE LN: beds = 3, baths = 2, price = 235738");
    }

    @Test
    public void mvelEvalTest() {
        vars.put("row", this.line);
        Object obj = MVEL.eval("String[] rows = row.split(',');\n" +
                "price = rows[9];\n" +
                "price;", vars);
        System.err.println("obj = " + obj);
    }

    @Test
    public void mvelDoubleQuoteTest() {
        String row = "\"21\",\"2015-07-12\"";
        vars.put("row", row);

        Object obj = MVEL.eval("String[] rows = row.split(\",\");\n" +
                "int first = rows[0].substring(1, rows[0].length() - 1);\n" +
                "if (first > 20) {\n" +
                "\trow;\n" +
                "}", vars);
        System.err.println("obj = " + obj);
    }

    // =====================================================================================
    // private methods
    // =====================================================================================
    private Map<String, String> createMap(List<String> keys, List<String> values) {
        if (keys.isEmpty() || values.isEmpty()) {
            return new HashMap<>();
        } else if (keys.size() != values.size()) {
            throw new IllegalArgumentException("key 와 value 의 개수가 다름!!");
        }
        Map<String, String> createdMap = new HashMap<>();
        for (int i = 0, size = keys.size(); i < size; i++) {
            createdMap.put(keys.get(i), values.get(i));
        }
        return createdMap;
    }

    private String convertOutput(String userDefineOutput, Map<String, String> map) {
        for (String key : map.keySet()) {
            userDefineOutput = StringUtils.replace(userDefineOutput, PREFIX + key + POSTFIX, map.get(key));
        }
        return userDefineOutput;
    }
}
