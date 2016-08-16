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
 * RFC 3174
 */
public class SHA1 {

    /**
     *  Constant Hex values 
     */
    public static char[] hex = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(
            create("".getBytes()));
    }

    /**
     * @param message
     * @return SHA1 hash
     */
    public static String create(byte[] message) {
        ByteBuffer message_byte_buffer = prepareMessage(message);
        //(hash Output is in little-endian)
        ByteBuffer hash = ByteBuffer.allocate(20);
        hash.order(ByteOrder.BIG_ENDIAN);

        int temp = 0x00000000;
        int a0 = 0x00000000;
        int b0 = 0x00000000;
        int c0 = 0x00000000;
        int d0 = 0x00000000;
        int e0 = 0x00000000;
        int F = 0x00000000;
        int K = 0x00000000;
        int W[] = new int[80];

        // Variable initialisation
        int A = 0x67452301;
        int B = 0xEFCDAB89;
        int C = 0x98BADCFE;
        int D = 0x10325476;
        int E = 0xC3D2E1F0;

        for (byte[] chunk = new byte[64]; message_byte_buffer.hasRemaining();) {
            message_byte_buffer.get(chunk, 0, 64); //Process the message in successive 512-bit / 64 byte chunks:
            prepareW(W, chunk);

            //Initialize hash value for this chunk
            a0 = A;
            b0 = B;
            c0 = C;
            d0 = D;
            e0 = E;

            //Main loop
            for (int i = 0; i < 80; i++) {
                switch (i / 20) { //division 80 / 20 = 4 stages
                    case 0:
                        F = (B & C) | ((~B) & D);
                        K = 0x5A827999;
                        break;
                    case 1:
                        F = B ^ C ^ D;
                        K = 0x6ED9EBA1;
                        break;
                    case 2:
                        F = ((B & C) | (B & D) | (C & D));
                        K = 0x8F1BBCDC;
                        break;
                    case 3:
                        F = B ^ C ^ D;
                        K = 0xCA62C1D6;
                        break;
                }

                temp = Integer.rotateLeft(A, 5) + F + E + K + W[i];
                E = D;
                D = C;
                C = Integer.rotateLeft(B, 30);
                B = A;
                A = temp;
            }

            //Add this chunk's hash to result so far
            A += a0;
            B += b0;
            C += c0;
            D += d0;
            E += e0;
        }

        hash.putInt(A);
        hash.putInt(B);
        hash.putInt(C);
        hash.putInt(D);
        hash.putInt(E);

        return toHex(hash.array());
    }

    /**
     * Break 512-bit chunk into an array of sixteen 32-bit int's and 
     * @param M
     * @param chunk
     */
    private static void prepareW(int[] W, byte[] chunk) {

        for (int i = 0, c = 0; i < 16; c += 4, i++) {
            W[i] = ((chunk[c] & 0xff) << 24) | ((chunk[c + 1] & 0xff) << 16) | ((chunk[c + 2] & 0xff) << 8)
                | (chunk[c + 3] & 0xff);
        }

        extendW(W);
    }

    /**
     * Extend the sixteen 32-bit words into eighty 32-bit words
     *
     * @param W the w
     */
    private static void extendW(int[] W) {

        for (int i = 16; i < 80; i++) {
            W[i] = Integer.rotateLeft((W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16]), 1);
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
        message_buffer.order(ByteOrder.BIG_ENDIAN);

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
