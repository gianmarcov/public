import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
 * RFC 1321
 */
public class MD5 {

    /**
     *  Constant Hex values 
     */
    public static char[] hex = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * s defines the number of Bits, that will shifted pro round
     */
    private static int S[][] = {
        {
            7, 12, 17, 22
        }, {
            5, 9, 14, 20
        }, {
            4, 11, 16, 23
        }, {
            6, 10, 15, 21
        }
    };

    /**
     * Use the binary integer part of 2 ^ 32 times the amount of the sine of integer values as constants
     */
    private static int K[] = {
        0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501, 0x698098d8,
        0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821, 0xf61e2562, 0xc040b340,
        0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6, 0xc33707d6, 0xf4d50d87,
        0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
        0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05, 0xd9d4d039,
        0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039, 0x655b59c3, 0x8f0ccc92,
        0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82, 0xbd3af235, 0x2ad7d2bb,
        0xeb86d391
    };

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(
            create("12345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes()));
    }

    /**
     * @param message
     * @return MD5 hash
     */
    public static String create(byte[] message) {
        ByteBuffer message_byte_buffer = prepareMessage(message);
        //(hash output is in little-endian)
        ByteBuffer hash = ByteBuffer.allocate(16);
        hash.order(ByteOrder.LITTLE_ENDIAN);

        int temp = 0x00000000;
        int a0 = 0x00000000;
        int b0 = 0x00000000;
        int c0 = 0x00000000;
        int d0 = 0x00000000;
        int F = 0x00000000;
        int G = 0x00000000;
        int M[] = new int[16];

        // initialize variables : (lt. RFC 1321)
        int A = 0x67452301;
        int B = 0xEFCDAB89;
        int C = 0x98BADCFE;
        int D = 0x10325476;

        for (byte[] chunk = new byte[64]; message_byte_buffer.hasRemaining();) {
            message_byte_buffer.get(chunk, 0, 64); //Process the message in successive 512-bit / 64 byte chunks:
            prepareM(M, chunk);
            a0 = A;
            b0 = B;
            c0 = C;
            d0 = D;

            for (int i = 0, stage = 0; i < 64; i++) {
                switch (stage = i >>> 4) { //division 16 / 64 = 4 stages
                    case 0:
                        F = (B & C) | (~B & D);
                        G = i;
                        break;
                    case 1:
                        F = (D & B) | (C & ~D);
                        G = (5 * i + 1) % 16;
                        break;
                    case 2:
                        F = B ^ C ^ D;
                        G = (3 * i + 5) % 16;
                        break;
                    case 3:
                        F = C ^ (B | ~D);
                        G = (7 * i) % 16;
                        break;
                }
                temp = D;
                D = C;
                C = B;
                B = B + Integer.rotateLeft((A + F + K[i] + M[G]), S[stage][i % 4]);
                A = temp;
            }

            A += a0;
            B += b0;
            C += c0;
            D += d0;
        }

        hash.putInt(A);
        hash.putInt(B);
        hash.putInt(C);
        hash.putInt(D);
        return toHex(hash.array());
    }

    /**
     * Break 512-bit chunk into an array of sixteen 32-bit int's
     * @param M
     * @param chunk
     */
    private static void prepareM(int[] M, byte[] chunk) {
        for (int i = 0, c = 0; i < M.length; c += 4, i++) {
            M[i] = (chunk[c] & 0xff) | ((chunk[c + 1] & 0xff) << 8) | ((chunk[c + 2] & 0xff) << 16)
                | ((chunk[c + 3] & 0xff) << 24);
        }
    }

    /**
     * Prepare the message
     */
    private static ByteBuffer prepareMessage(byte[] message) {

        int message_size_in_bytes = message.length;
        int message_size_in_bits = message_size_in_bytes * 8;
        short fill_bytes = (short) ((512 - ((message_size_in_bits + 64l) % 512)) / 8);

        ByteBuffer message_buffer = ByteBuffer.allocate(message_size_in_bytes + fill_bytes + 8);
        message_buffer.order(ByteOrder.LITTLE_ENDIAN);

        message_buffer.put(message);
        message_buffer.put((byte) 0b1000_0000);
        for (int i = 1; i < fill_bytes; i++) {
            message_buffer.put((byte) 0b0000_0000);
        }

        message_buffer.putLong(message_size_in_bits);
        message_buffer.position(0);
        return message_buffer.asReadOnlyBuffer();
    }

    /**
     *  Converts bytes to hex string 
     */
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
