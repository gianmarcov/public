import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vitelli Gianmarco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @Autor Vitelli Gianmarco
 */
public final class UUID {
	/* Costant difference between 1583 and 1970 in 100 ns format */
	public static long CONVERSION_1583_TO_1970 = 122192928000000000l;
	/* Constant Hex values */
	public static char[] hex = "0123456789abcdef".toCharArray();
	/* UUID Version */
	public static short TYPE1_UUID_VERSION = 0b0001;
	/* last timestamp */
	public static long last_timestamp = 0l;
	/* clock sequence */
	public static long clock_sequence = 0l;
	/* mac adress */
	public static String mac_adress = "";

	public static void main(String[] args) {
		for(int i=0;i<3;i++) {
			System.out.println(create());
		}
	}
	
	/* UUID Type 1 implementation See RFC 4122 for more information */
	public static String create() {
		try {
			long timestamp = getTimestamp();

			/* Check if mac_adress is initialized */
			if (mac_adress == "") {
				mac_adress = toHex(getMacAdress());
			}

			/* Check if clock_sequence is initialized */
			if (clock_sequence == 0l) {
				clock_sequence = getClockSequence();
			}

			/* Check if have same timestamp as last time */
			if (timestamp == last_timestamp) {
				clock_sequence++;
			}

			/* Conversion to structure */
			int time_low = (int) (timestamp & 0xFFFFFFFF);
			short time_mid = (short) ((timestamp >> 32) & 0xFFFF);
			short time_hi_and_version = (short) (((timestamp >> 48) & 0x0FFF) | TYPE1_UUID_VERSION << 12);
			short clock_seq_low = (short) (clock_sequence & 0xFF);
			short clock_seq_hi_and_reserved = (short) (((clock_sequence & 0x3F00) >> 8) | 0x80);

			last_timestamp = timestamp;

			/* Conversion to Hex String */
			return toHex(toBytes(time_low))
					+ '-'
					+ toHex(toBytes(time_mid))
					+ '-'
					+ toHex(toBytes(time_hi_and_version))
					+ '-'
					+ toHex(toBytes((short) (clock_seq_hi_and_reserved << 8 | clock_seq_low)))
					+ '-' + mac_adress;
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
			return "";
		}
	}

	/* Generate random number between 0 and 16383 (14bit) */
	private static short getClockSequence() {
		Random random = new Random();
		return (short) random.nextInt(16383);
	}

	/*
	 * Does not work under Windows, only Mac OS X +10.10 private static long
	 * getTimestamp() { return (System.nanoTime() / 100) +
	 * CONVERSION_1583_TO_1970; }
	 */

	/*
	 * Generate timestamp UTC in 100 ns format, start from 00:00:00.00, 15
	 * October 1582
	 */
	private static long getTimestamp() {
		return (System.currentTimeMillis() * 10000) + CONVERSION_1583_TO_1970;
	}

	/* Get networkinterface mac adress */
	private static byte[] getMacAdress() throws SocketException,
			UnknownHostException {
		return NetworkInterface.getByInetAddress(InetAddress.getLocalHost())
				.getHardwareAddress();
	}

	/* Converts integer to byte array */
	private static byte[] toBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (i & 0xFF);
		bytes[2] = (byte) ((i >> 8) & 0xFF);
		bytes[1] = (byte) ((i >> 16) & 0xFF);
		bytes[0] = (byte) ((i >> 24) & 0xFF);
		return bytes;
	}

	/* Converts short to byte array */
	private static byte[] toBytes(short s) {
		byte[] bytes = new byte[2];
		bytes[1] = (byte) (s & 0xFF);
		bytes[0] = (byte) (s >> 8);
		return bytes;
	}

	/* Converts bytes to hex string */
	private static String toHex(byte[] bytes) {

		char[] buffer = new char[bytes.length * 2];

		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i] & 0xFF;
			buffer[i * 2] = hex[(b >>> 4)];
			buffer[(i * 2) + 1] = hex[(b & 0x0F)];
		}

		return new String(buffer);
	}

}
