package org.openflamingo.mapreduce.common;

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
