package org.openflamingo.mapreduce.parser;

import org.junit.Assert;
import org.junit.Test;
import org.openflamingo.mapreduce.core.CsvRowParser;

/**
 * CsvRowParser에 대한 단위 테스트 케이스.
 *
 * @author Edward KIM
 * @since 0.1
 */
public class CsvRowParserTest {

    @Test
    public void parse() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());
    }

    @Test
    public void get() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals("a", parser.get(0));
        Assert.assertEquals("b", parser.get(1));
        Assert.assertEquals("c", parser.get(2));
        Assert.assertEquals("d", parser.get(3));
    }

    @Test
    public void clear() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());

        parser.clear();
        Assert.assertEquals(0, parser.size());
    }

    @Test
    public void removeSingle() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());

        parser.remove(1);
        Assert.assertEquals(3, parser.size());
        Assert.assertEquals("a", parser.get(0));
        Assert.assertEquals("c", parser.get(1));
        Assert.assertEquals("d", parser.get(2));
    }

    @Test
    public void removeMultiple() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());

        parser.remove(1, 2);
        Assert.assertEquals(2, parser.size());
        Assert.assertEquals("a", parser.get(0));
        Assert.assertEquals("d", parser.get(1));
    }

    @Test
    public void insert() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());

        parser.insert("e", 1);
        Assert.assertEquals(5, parser.size());
        Assert.assertEquals("a", parser.get(0));
        Assert.assertEquals("e", parser.get(1));
        Assert.assertEquals("b", parser.get(2));
        Assert.assertEquals("c", parser.get(3));
        Assert.assertEquals("d", parser.get(4));
    }

    @Test
    public void change() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());

        parser.change("e", 1);
        Assert.assertEquals(4, parser.size());
        Assert.assertEquals("a", parser.get(0));
        Assert.assertEquals("e", parser.get(1));
        Assert.assertEquals("c", parser.get(2));
        Assert.assertEquals("d", parser.get(3));
    }

    @Test
    public void toRow() {
        CsvRowParser parser = new CsvRowParser();
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());
        Assert.assertEquals("a,b,c,d", parser.toRow());

        parser.insert("e", 1);
        Assert.assertEquals("a,e,b,c,d", parser.toRow());

        parser.remove(1, 2);
        Assert.assertEquals("a,c,d", parser.toRow());

        parser.clear();
        Assert.assertEquals("", parser.toRow());
    }

    @Test
    public void constructor() {
        CsvRowParser parser = new CsvRowParser("a,b,c,");
        Assert.assertEquals(4, parser.size());
        Assert.assertEquals("", parser.get(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorIfDifferentColumnSize() {
        CsvRowParser parser = new CsvRowParser(5);
        parser.parse("a,b,c,d");
        Assert.assertEquals(4, parser.size());
    }

    @Test
    public void constructorIfSetDelimiter() {
        CsvRowParser parser = new CsvRowParser(1, "\t", "|");
        parser.parse("a,b,c,d");
        Assert.assertEquals(1, parser.size());
        Assert.assertEquals("a,b,c,d", parser.toRow());
        Assert.assertEquals("a,b,c,d", parser.toRowText().toString());

        parser.parse("a,b,c,d\te,f,g,h");
        Assert.assertEquals(2, parser.size());
        Assert.assertEquals("a,b,c,d|e,f,g,h", parser.toRow());
        Assert.assertEquals("a,b,c,d|e,f,g,h", parser.toRowText().toString());
    }

    @Test
    public void constructorIfEmptyColumnExists() {
        CsvRowParser parser = new CsvRowParser();
        Assert.assertEquals(0, parser.size());

        parser.parse(",b,c,d");
        Assert.assertEquals(4, parser.size());
        Assert.assertEquals("", parser.get(0));

        parser.parse(",,c,d");
        Assert.assertEquals(4, parser.size());
        Assert.assertEquals("", parser.get(0));
        Assert.assertEquals("", parser.get(1));
    }
}
