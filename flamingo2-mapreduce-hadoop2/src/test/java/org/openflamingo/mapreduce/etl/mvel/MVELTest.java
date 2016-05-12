package org.openflamingo.mapreduce.etl.mvel;

import org.junit.Assert;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.junit.Test;
import org.mvel2.PropertyAccessException;
import org.mvel2.ScriptRuntimeException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * MVEL 학습 테스트 케이스.
 *
 * @author Jihye Seo
 * @since 1.0
 */
public class MVELTest {

	@Test
	public void test1() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);

		Integer result = (Integer) MVEL.eval("x * y", vars);
		Assert.assertEquals(new Integer(20), result);
	}

	@Test
	public void test2() {
		Double result = (Double) MVEL.eval("1.0 * 2.0 + 3");
		Assert.assertEquals(new Double(5.0), result);
	}

	@Test
	public void test3() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);

		Serializable compiled = MVEL.compileExpression("x + y");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertEquals(new Integer(9), result);
	}

	@Test(expected = ScriptRuntimeException.class)
	public void test4() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);

		Serializable compiled = MVEL.compileExpression("x + ");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertEquals(new Integer(9), result);
	}

	@Test(expected = PropertyAccessException.class)
	public void test5() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);

		Serializable compiled = MVEL.compileExpression("x + y + z");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertEquals(new Integer(9), result);
	}

	@Test(expected = PropertyAccessException.class)
	public void test6() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);

		Serializable compiled = MVEL.compileExpression("x + z");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertEquals(new Integer(9), result);
	}

	@Test(expected = CompileException.class)
	public void test7() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);
		vars.put("z", 5);

		Serializable compiled = MVEL.compileExpression("(x + y * z");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertEquals(new Integer(45), result);
	}

	@Test
	public void test8() {
		Map vars = new HashMap();
		vars.put("x", 4);
		vars.put("y", 5);
		vars.put("z", 5);

		Serializable compiled = MVEL.compileExpression("");
		Integer result = (Integer) MVEL.executeExpression(compiled, vars);
		Assert.assertNull(result);
	}

	@Test
	public void testWithAccountingExpression() {
		Map map = new HashMap();
		map.put("$1", 1);
		map.put("$2", 1);

		Integer result = (Integer) MVEL.eval("$1+$2", map);
		Assert.assertEquals(new Integer(2), result);
	}

	@Test
	public void testWithAccountingExpression2() {
		Map map = new HashMap();
		map.put("$1", 1);

		Integer result = (Integer) MVEL.eval("$1+1", map);
		Assert.assertEquals(new Integer(2), result);
	}
}
