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
package org.opencloudengine.flamingo2.mapreduce.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description.
 *
 * @author Edward KIM
 * @since 1.0
 */
public class AccountingExpressionFileReadTest {

	/*@Test
	public void testFileChannel() throws IOException {
		RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
		FileChannel fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
		FileChannel toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		toChannel.transferFrom(fromChannel, position, count);

		ByteBuffer from = ByteBuffer.allocate(1024);
		fromChannel.read(from);
		ByteBuffer to = ByteBuffer.allocate(1024);
		toChannel.read(to);

		Assert.assertArrayEquals(from.array(), to.array());

		fromFile.close();
		toFile.close();
	}*/

/*	@Test
	public void testFileValidation() throws IOException {
		File file = new File("fromFile.txt");
		FileInputStream inputStream = new FileInputStream(file);
		FileChannel fromChannel = inputStream.getChannel();

		ByteBuffer from = ByteBuffer.allocate(1024);
		fromChannel.read(from);

		Assert.assertTrue(file.exists());

		file.delete();

	}*/

	@Test(expected = IllegalArgumentException.class)
	public void testReadOnlyFileValidation() {
		File file = new File("fromFile.txt", "r");
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("파일이 존재하지 않습니다.");
		}
		FileChannel fromChannel = inputStream.getChannel();
		ByteBuffer from = ByteBuffer.allocate(1024);
		try {
			fromChannel.read(from);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isFile());

		file.delete();
	}
}
