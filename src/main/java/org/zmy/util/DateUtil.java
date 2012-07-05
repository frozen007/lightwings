/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-3-6
 *
 */
package org.zmy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtil
 *
 */
public class DateUtil {

    public static String getDateTimeStr(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Get datetime string in the form of "yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String getFormatedDateTimeStr(Date date) {
        return getDateTimeStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Get datetime string in the form of "yyyyMMddHHmmss"
     * @param date
     * @return
     */
    public static String getPlainDateTimeStr(Date date) {
        return getDateTimeStr(date, "yyyyMMddHHmmss");
    }

    /**
     * Get date string in the form of "yyyy-MM-dd"
     * @param date
     * @return
     */
    public static String getFormatedDateStr(Date date) {
        String str = getFormatedDateTimeStr(date);
        return str.substring(0, 10);
    }

    /**
     * Get time string in the form of "HH:mm:ss"
     * @param date
     * @return
     */
    public static String getFormatedTimeStr(Date date) {
        String str = getFormatedDateTimeStr(date);
        return str.substring(11);
    }

    /**
     * Get date string in the form of "yyyyMMdd"
     * @param date
     * @return
     */
    public static String getPlainDateStr(Date date) {
        String str = getPlainDateTimeStr(date);
        return str.substring(0, 8);
    }

    /**
     * Get time string in the form of "HHmmss"
     * @param date
     * @return
     */
    public static String getPlainTimeStr(Date date) {
        String str = getPlainDateTimeStr(date);
        return str.substring(8);
    }

    /**
     * Get current system date string in the form of "yyyy-MM-dd"
     * @return
     */
    public static String getFormatedSysDateStr() {
        return getFormatedDateStr(new Date());
    }

    /**
     * Get current system date string in the form of "yyyyMMdd"
     * @return
     */
    public static String getPlainSysDateStr() {
        return getPlainDateStr(new Date());
    }

    /**
     * Get current system time string in the form of "HH:mm:ss"
     * @return
     */
    public static String getFormatedSysTimeStr() {
        return getFormatedTimeStr(new Date());
    }

}
