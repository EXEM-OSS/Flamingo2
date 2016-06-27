package org.opencloudengine.flamingo2.mapreduce.gis.esri.dbf;

import java.io.Serializable;

/**
 */
public final class DBFType implements Serializable {

    public final static byte END = 0x1A;
    public final static byte DELETED = 0x2A;

    private DBFType() {
    }
}