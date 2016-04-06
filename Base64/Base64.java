import java.util.concurrent.TimeUnit;

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

public class Base64 {
	
	private static char[] BASE64_TABLE_ENCODE = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
			                             ,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
			                      ,'0','1','2','3','4','5','6','7','8','9','+','/'};

	public static void main(String[] args) {
		long start,stop;
		String tmp;
		System.out.println("Start: "+(TimeUnit.MILLISECONDS.convert(start=System.nanoTime(), TimeUnit.NANOSECONDS)));
		
		for(int i=0; i <10_000_000;i++){
			Base64.encode("12345678901234567891234567890123456789");
		};
		System.out.println(tmp= Base64.encode("12345678901234567891234567890123456789"));
		System.out.println((Base64.decode(tmp)));
		System.out.println("Stop: "+(TimeUnit.MILLISECONDS.convert(stop=System.nanoTime(), TimeUnit.NANOSECONDS)));
		System.out.println("diff: "+(TimeUnit.MILLISECONDS.convert(stop-start, TimeUnit.NANOSECONDS)));
	}
	
	public static String encode(String s) {
		byte[] bytes = s.getBytes(); //Get 8bit value
		boolean fill = ((bytes.length % 3) != 0); // Need to fill up?
		byte zerobyte = (byte) (fill ? (((bytes.length % 3) == 1) ? 0x2 : 0x1 ): 0x0);//How much '=' must be inserted
		char[] buffer = new char[((bytes.length+zerobyte)/3)*4];//Calculate output size, initialize buffer
		int i=0,n=0,r=0x00000000; // i is for index, r for register
		
		while(i <= bytes.length-3 || (zerobyte == 0x0 && i == 0)) { //Second condition is explicit, when byte length is 3
			//Fill register with 3 x 8bit
			r = (bytes[i] << 16) | (bytes[i+1] << 8) | bytes[i+2];
			//cut 24bit into 4 x 6bit chunks
			buffer[n] =   BASE64_TABLE_ENCODE[((r & 0b00000000111111000000000000000000) >> 18)];
			buffer[n+1] = BASE64_TABLE_ENCODE[((r & 0b00000000000000111111000000000000) >> 12)];
			buffer[n+2] = BASE64_TABLE_ENCODE[((r & 0b00000000000000000000111111000000) >> 6)];
			buffer[n+3] = BASE64_TABLE_ENCODE[((r & 0b00000000000000000000000000111111))];
			i+=3;//Increment index
			n+=4;//Increment buffer index
			r=0x00000000;//Reset
		}
		
		if(fill) {
			if(zerobyte == 0x1) {
				r = bytes[i] << 16;
				r |= bytes[i+1] << 8;
				//cut 24bit into 4 x 6bit chunks
				buffer[n] =   BASE64_TABLE_ENCODE[((r & 0b00000000111111000000000000000000) >> 18)];
				buffer[n+1] = BASE64_TABLE_ENCODE[((r & 0b00000000000000111111000000000000) >> 12)];
			    buffer[n+2] = BASE64_TABLE_ENCODE[((r & 0b00000000000000000000111111000000) >> 6)];
			    buffer[n+3] = '=';
			} else {
				r = bytes[i] << 16;
				//cut 24bit into 4 x 6bit chunks
				buffer[n] =   BASE64_TABLE_ENCODE[((r & 0b00000000111111000000000000000000) >> 18)];
				buffer[n+1] = BASE64_TABLE_ENCODE[((r & 0b00000000000000111111000000000000) >> 12)];
				buffer[n+2] = '=';
				buffer[n+3] = '=';
			}	
		}
		
		return new String(buffer); 
	}
	
	public static String decode(String s) {
		byte[] bytes = s.getBytes(); //Get 8bit value
		char[] buffer = new char[((bytes.length/4)*3)];//Calculate output size, initialize buffer
		int blocks = (bytes.length)-4; // Get how much we can iterate without to check zerobyte fill up -4 for the last 4 byte
		int i=0,n=0,r=0x00000000; // i is for index, r for register
		
		//translate Base64 table to integer
		for(int c=0;c<bytes.length;c++) {
			if(bytes[c] < 65) {
				// + or / or numbers
				if(bytes[c] > 46) {
					if(bytes[c] == 47) {
						bytes[c] = 63;
					} else {
						if (bytes[c] < 58) {
							bytes[c] = (byte) (bytes[c] + 4);	
						}
					}
				} else  {
					bytes[c] = 62;
				}
			} else if(bytes[c] < 91) {
				// A - Z
				bytes[c] = (byte) (bytes[c] - 65);
			} else {
				// a - z
				bytes[c] = (byte) (bytes[c] - 71);
			}			
		}
		
		while(i < blocks) {
				//Fill register with 4 x 8bit
				r = (bytes[i] << 18) | (bytes[i+1] << 12) | (bytes[i+2] << 6) | bytes[i+3];
				//cut 24bit into 4 x 6bit chunks
				buffer[n] =   (char) ((r & 0b00000000111111110000000000000000) >> 16);
				buffer[n+1] = (char) ((r & 0b00000000000000001111111100000000) >> 8);
				buffer[n+2] = (char) ( r & 0b00000000000000000000000011111111);
				i+=4;//Increment index
				n+=3;//Increment buffer index
				r=0x00000000;//Reset
		}
		
		if(bytes[i+3] == '=') { //check the last position have a '='	
			if(bytes[i+2] == '=') {
				//Fill register with 4 x 8bit 
				r = (bytes[i] << 18) | (bytes[i+1] << 12);
				//cut 24bit into 4 x 6bit chunks
				buffer[n] =   (char) ((r & 0b00000000111111110000000000000000) >> 16);
				buffer[n+1] = (char) ((r & 0b00000000000000001111111100000000) >> 8);
			} else {
				//Fill register with 4 x 8bit 
				r = (bytes[i] << 18) | (bytes[i+1] << 12) | (bytes[i+2] << 6) ;
				//cut 24bit into 4 x 6bit chunks
				buffer[n] =   (char) ((r & 0b00000000111111110000000000000000) >> 16);
				buffer[n+1] = (char) ((r & 0b00000000000000001111111100000000) >> 8);
				buffer[n+2] = (char) ( r & 0b00000000000000000000000011111111);
			}
		} else {
			//Fill register with 4 x 8bit
			r = (bytes[i] << 18) | (bytes[i+1] << 12) | (bytes[i+2] << 6) | bytes[i+3];
			//cut 24bit into 4 x 6bit chunks
			buffer[n] =   (char) ((r & 0b00000000111111110000000000000000) >> 16);
			buffer[n+1] = (char) ((r & 0b00000000000000001111111100000000) >> 8);
			buffer[n+2] = (char) ( r & 0b00000000000000000000000011111111);
		}
		
		return new String(buffer);
	}
}
