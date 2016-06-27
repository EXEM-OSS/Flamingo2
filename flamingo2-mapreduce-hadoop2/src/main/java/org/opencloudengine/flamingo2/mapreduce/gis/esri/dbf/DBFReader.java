package org.opencloudengine.flamingo2.mapreduce.gis.esri.dbf;

import org.apache.hadoop.io.Writable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Based on https://code.google.com/p/javadbf/
 */
public class DBFReader implements Serializable {

    private final transient DataInputStream m_dataInputStream;
    private final transient DBFHeader m_header;
    private final String characterSet;

    public DBFReader(final DataInputStream dataInputStream, final String characterSet) throws IOException {
        m_dataInputStream = dataInputStream;
        m_header = DBFHeader.read(dataInputStream);
        this.characterSet = characterSet;
    }

    public Map<String, Object> readRecordAsMap(final Map<String, Object> map) throws IOException {
        final byte dataType = nextDataType();
        if (dataType == DBFType.END) {
            return null;
        }
        for (final DBFField field : m_header.fields) {
            map.put(field.fieldName, field.readValue(m_dataInputStream, this.characterSet));
        }
        return map;
    }

    public Map<String, Object> readRecordAsMap() throws IOException {
        return readRecordAsMap(new HashMap<String, Object>());
    }

    public Object[] createValueArray() {
        return new Object[m_header.numberOfFields];
    }

    private Object[] queryValues(final Object[] values) throws IOException {
        final int numberOfFields = m_header.numberOfFields;
        for (int i = 0; i < numberOfFields; i++) {
            values[i] = readFieldValue(i);
        }
        return values;
    }

    public Object[] queryRecord(final Object[] values) throws IOException {
        final byte dataType = nextDataType();
        if (dataType == DBFType.END) {
            return null;
        }
        return queryValues(values);
    }

    public Object[] readRecord() throws IOException {
        final byte dataType = nextDataType();
        if (dataType == DBFType.END) {
            return null;
        }
        return queryValues(createValueArray());
    }

    public List<Object> readValues() throws IOException {
        final List<Object> values = new ArrayList<Object>();
        final int numberOfFields = m_header.numberOfFields;
        for (int i = 0; i < numberOfFields; i++) {
            values.add(readFieldValue(i));
        }
        return values;
    }

    public DBFHeader getHeader() {
        return this.m_header;
    }

    public List<DBFField> getFields() {
        return m_header.fields;
    }

    public int getNumberOfFields() {
        return m_header.numberOfFields;
    }

    public int getNumberOfRecords() {
        return m_header.numberOfRecords;
    }

    public byte nextDataType() throws IOException {
        byte dataType;
        do {
            dataType = m_dataInputStream.readByte();
            if (dataType == DBFType.END) {
                break;
            } else if (dataType == DBFType.DELETED) {
                skipRecord();
            }
        }
        while (dataType == DBFType.DELETED);
        return dataType;
    }

    public void skipRecord() throws IOException {
        m_dataInputStream.skipBytes(m_header.recordLength - 1);
    }

    public Object readFieldValue(final int index) throws IOException {
        return m_header.getField(index).readValue(m_dataInputStream, this.characterSet);
    }

    public Writable readFieldWritable(final int index) throws IOException {
        return m_header.getField(index).readWritable(m_dataInputStream, this.characterSet);
    }
}