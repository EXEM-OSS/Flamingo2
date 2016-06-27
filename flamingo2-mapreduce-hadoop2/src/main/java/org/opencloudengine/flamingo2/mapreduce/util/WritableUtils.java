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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper static methods for working with Writable objects.
 */
public class WritableUtils {

    /**
     * Read fields from byteArray to a Writeable object.
     *
     * @param byteArray      Byte array to find the fields in.
     * @param writableObject Object to fill in the fields.
     */
    public static void readFieldsFromByteArray(byte[] byteArray, Writable writableObject) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(byteArray));
        try {
            writableObject.readFields(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("readFieldsFromByteArray: IOException", e);
        }
    }

    /**
     * Write object to a byte array.
     *
     * @param writableObject Object to write from.
     * @return Byte array with serialized object.
     */
    public static byte[] writeToByteArray(Writable writableObject) {
        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();
        DataOutput output = new DataOutputStream(outputStream);
        try {
            writableObject.write(output);
        } catch (IOException e) {
            throw new IllegalStateException("writeToByteArray: IOStateException", e);
        }
        return outputStream.toByteArray();
    }

    /**
     * Write list of object to a byte array.
     *
     * @param writableList List of object to write from.
     * @return Byte array with serialized objects.
     */
    public static byte[] writeListToByteArray(
            List<? extends Writable> writableList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutput output = new DataOutputStream(outputStream);
        try {
            output.writeInt(writableList.size());
            for (Writable writable : writableList) {
                writable.write(output);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "writeListToByteArray: IOException", e);
        }
        return outputStream.toByteArray();
    }

    /**
     * Read fields from byteArray to a list of Writeable objects.
     *
     * @param byteArray     Byte array to find the fields in.
     * @param writableClass Class of the objects to instantiate.
     * @param conf          Configuration used for instantiation (i.e Configurable)
     * @return List of writable objects.
     */
    public static List<? extends Writable> readListFieldsFromByteArray(
            byte[] byteArray,
            Class<? extends Writable> writableClass,
            Configuration conf) {
        try {
            DataInputStream inputStream =
                    new DataInputStream(new ByteArrayInputStream(byteArray));
            int size = inputStream.readInt();
            List<Writable> writableList = new ArrayList<Writable>(size);
            for (int i = 0; i < size; ++i) {
                Writable writable =
                        ReflectionUtils.newInstance(writableClass, conf);
                writable.readFields(inputStream);
                writableList.add(writable);
            }
            return writableList;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "readListFieldsFromZnode: IOException", e);
        }
    }
}
