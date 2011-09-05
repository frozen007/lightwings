/**
 * Project StudentPro
 * by zhao-mingyu at 2008-3-6
 * 
 */
package org.zmy.util;

/**
 * StringUtil
 *
 */
public class StringUtil {

    // size is ascii level
    public static String rightPadding(String src, char pad, int size) {
        byte[] srcbuf = src.getBytes();
        if (srcbuf.length >= size) {
            return src;
        }
        char[] targetbuf = new char[size - srcbuf.length];

        for (int i = 0; i < targetbuf.length; i++) {
            targetbuf[i] = pad;
        }

        return src + new String(targetbuf);
    }

    public static String leftPadding(String src, char pad, int size) {
        byte[] srcbuf = src.getBytes();
        if (srcbuf.length >= size) {
            return src;
        }
        char[] targetbuf = new char[size - srcbuf.length];

        for (int i = 0; i < targetbuf.length; i++) {
            targetbuf[i] = pad;
        }

        return new String(targetbuf) + src;
    }

    public static boolean isDigitValue(String src) {
        char[] cs = src.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] > '9' || cs[i] < '0') {
                return false;
            }
        }
        return true;
    }
}
