/*
 The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"); you may not use this file except in
 compliance with the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Original Code is OpenFAST.

 The Initial Developer of the Original Code is The LaSalle Technology
 Group, LLC.  Portions created by The LaSalle Technology Group, LLC
 are Copyright (C) The LaSalle Technology Group, LLC. All Rights Reserved.

 Contributor(s): Jacob Northey <jacob@lasalletech.com>
 Craig Otis <cotis@lasalletech.com>
 */

package org.zmy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ByteUtil {
    /**
     * 
     * @param bitString
     *            in the format of space separated 8-bit bytes (i.e. "01010101
     *            10101010")
     * @return byte array representation of the bit string
     */
    public static byte[] convertBitStringToFastByteArray(String bitString) {
        if (bitString.length() == 0) {
            return new byte[0];
        }

        String[] bitStrings = bitString.split(" ");
        byte[] bytes = new byte[bitStrings.length];

        for (int i = 0; i < bitStrings.length; i++) {
            bytes[i] = (byte) Integer.parseInt(bitStrings[i], 2);
        }

        return bytes;
    }

    public static byte[] convertHexStringToByteArray(String hexString) {
        if (hexString == null) {
            return new byte[0];
        }

        hexString = hexString.replaceAll(" ", "");
        byte[] bytes = new byte[hexString.length() / 2];

        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }

        return bytes;
    }

    /**
     * 
     * @param bytes
     *            byte array
     * @return space separated 8-bit string encoding of byte (i.e. "01010101
     *         10101010")
     */
    public static String convertByteArrayToBitString(byte[] bytes) {
        return convertByteArrayToBitString(bytes, bytes.length);
    }

    public static String convertByteArrayToBitString(byte[] bytes, int length) {
        if (bytes.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            String bits = Integer.toString(bytes[i] & 0xFF, 2);

            for (int j = 0; j < (8 - bits.length()); j++)
                builder.append('0');

            builder.append(bits).append(' ');
        }

        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }

    public static InputStream createByteStream(String bitString) {
        return new ByteArrayInputStream(convertBitStringToFastByteArray(bitString));
    }

    public static InputStream createByteStreamFromHexBytes(String hexString) {
        return new ByteArrayInputStream(convertHexStringToByteArray(hexString));
    }

    public static byte[] combine(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean isEmpty(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++)
            if ((bytes[i] & 0x7f) != 0)
                return false;
        return true;
    }

    public static int indexOf(byte[] data, int start, byte[] find) {
        int pos = 0;

        if (find.length > data.length) {
            return -1;
        }

        for (pos = start; pos < data.length; pos++) {
            if (data[pos] == find[0] && startWith(data, pos, find)) {
                return pos;
            }
        }

        return -1;
    }

    public static boolean startWith(byte[] data, int start, byte[] find) {
        if (data.length - start < find.length) {
            return false;
        }
        for (int i = 0; i < find.length; i++) {
            if (data[start + i] != find[i]) {
                return false;
            }
        }
        return true;
    }

    public static String getRegionFromBytes(byte[] data, int fromPos, byte[] startId, byte[] endId) {
        String target = null;
        int tagPos = ByteUtil.indexOf(data, fromPos, startId);
        int tagStart = tagPos + startId.length;// point to the next byte of '='
        if (tagPos == -1) {
            return target;
        }
        int tagEnd = ByteUtil.indexOf(data, tagStart, endId);
        if (tagEnd == -1) {
            return target;
        }
        target = new String(data, tagStart, tagEnd - tagStart);
        return target;
    }

    public static void dumpBytesToFile(String fileName, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBytesFromFile(String fileName) {
        byte[] result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
            byte[] buff = new byte[2048];
            FileInputStream fis = new FileInputStream(fileName);
            while (true) {
                int len = fis.read(buff);
                if (len == -1) {
                    break;
                }
                bos.write(buff, 0, len);
            }
            result = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
