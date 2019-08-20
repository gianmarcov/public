/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Vitelli Gianmarco
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
package uuid

import (
	"fmt"
	"math/rand"
	"net"
	"strings"
	"time"
)

var last_timestamp uint64
var  mac_adress string
var clock_sequence uint16
const TYPE1_UUID_VERSION int64 = 0x1
const CONVERSION_1583_TO_1970 uint64 = 122192928000000000;

/* UUID Type 1 implementation See RFC 4122 for more information */
func CreateUUID() string {

	timestamp := getTimestamp();

	/* Check if mac_adress is initialized */
	if mac_adress == "" {
		mac_adress = strings.Replace(getMacAdress(), ":", "", -1)
	}

	/* Check if clock_sequence is initialized */
	if clock_sequence == 0 {
		clock_sequence = getClockSequence();
	}

	/* Check if have same timestamp as last time */
	if timestamp == last_timestamp {
		clock_sequence++;
	}

	/* Conversion to structure */
 	time_low := (uint) (timestamp & 0xFFFFFFFF);
 	time_mid := (uint16) ((timestamp >> 32) & 0xFFFF);
 	time_hi_and_version := (uint16) (((int64(timestamp >> 48)) & 0x0FFF) | TYPE1_UUID_VERSION << 12);
    clock_seq_low := (uint16) (clock_sequence & 0xFF);
    clock_seq_hi_and_reserved := (uint16) (((clock_sequence & 0x3F00) >> 8) | 0x80);

	last_timestamp = timestamp;

	/* Conversion to Hex String */
	return toHex(time_low) + "-" +
		   toHex(time_mid) + "-" +
		   toHex(time_hi_and_version) + "-" +
		   toHex((uint16) (clock_seq_hi_and_reserved << 8 | clock_seq_low))+ "-"+
		   mac_adress
}

func toHex(i interface{}) string {
	return fmt.Sprintf("%x", i);
}

func getTimestamp() uint64 {
	return uint64(time.Now().UnixNano() / 100) + CONVERSION_1583_TO_1970;
}

func getClockSequence() uint16 {
	min := 0
	max := 16383
	return uint16(rand.Intn(max - min) + min);
}

func getMacAdress() (string) {
	eths, err := net.Interfaces()
	if err != nil {
		return ""
	}

	for _, eth := range eths {
		mac := eth.HardwareAddr.String()
		if mac != "" {
			return mac
		}
	}

	return "000000000000"
}
