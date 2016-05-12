package org.openflamingo.mapreduce.etl.accounting;

import org.junit.Test;
import org.junit.Assert;
import org.openflamingo.mapreduce.util.ArrayUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Description.
 *
 * @author Edward KIM
 * @since 1.0
 */
public class ExpressionHandleTest {

	@Test
	public void test1() {
		String str = "($1+$2)";
		String[] temp = str.split("\\+");
		Assert.assertEquals(2, temp.length);
	}

	@Test
	public void test2() {
		String str = "($1+$2)";

		str = str.replaceAll("\\(", "");
		Assert.assertEquals("$1+$2)", str);

		str = str.replaceAll("\\)", "");
		Assert.assertEquals("$1+$2", str);

		String[] temp = str.split("\\+");
		Assert.assertEquals("$1", temp[0]);
		Assert.assertEquals("$2", temp[1]);
	}

	@Test
	public void test3() {
		String str = "(($1+$2)+$3)";

		str = str.replaceAll("\\(", "");
		Assert.assertEquals("$1+$2)+$3)", str);

		str = str.replaceAll("\\)", "");
		Assert.assertEquals("$1+$2+$3", str);

		String[] temp = str.split("\\+");
		Assert.assertEquals("$1", temp[0]);
		Assert.assertEquals("$2", temp[1]);
		Assert.assertEquals("$3", temp[2]);
	}

	@Test
	public void test4() {
		String str = "(($1+$2)-$3*($4/$5))";

		str = str.replaceAll("\\(", "");
		Assert.assertEquals("$1+$2)-$3*$4/$5))", str);

		str = str.replaceAll("\\)", "");
		Assert.assertEquals("$1+$2-$3*$4/$5", str);

		String[] temp = str.split("[\\+|\\-|\\*|\\/]");
		Assert.assertEquals("$1", temp[0]);
		Assert.assertEquals("$2", temp[1]);
		Assert.assertEquals("$3", temp[2]);
		Assert.assertEquals("$4", temp[3]);
		Assert.assertEquals("$5", temp[4]);
	}

	@Test
	public void test5() {
		String str = "(($1+$2)-$3*($4/$5))";
		str = str.replaceAll("[\\(|\\)|\\$]", "");
		Assert.assertEquals("1+2-3*4/5", str);
	}

	@Test
	public void test6() {
		String str = "(($1+$2)-$3*($4/$5))";
		str = str.replaceAll("[\\(|\\)|\\$]", "");

		String[] stringColumns = str.split("[\\+|\\-|\\*|\\/]");
		Integer[] integerColumns = ArrayUtils.toIntegerArray(stringColumns);

		Integer[] expected = {new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
		Assert.assertArrayEquals(expected, integerColumns);
	}
}
