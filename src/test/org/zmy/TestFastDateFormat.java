package test.org.zmy;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

public class TestFastDateFormat {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        testDateFormat();
    }

    /**
     * FastDateFormat:      663ms
     * SimpleDateFormat:    
     * 
     */
    public static long currentSystemTimeMillis() {
//        FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMddHHmmss");
//        return Long.parseLong(fdf.format(System.currentTimeMillis()));

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(new Date(System.currentTimeMillis()));
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH) + 1;
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        String strDateTime =
            year
                + (month < 10 ? "0" + month : month + "")
                + (day < 10 ? "0" + day : day + "")
                + (hour < 10 ? "0" + hour : hour + "")
                + (minute < 10 ? "0" + minute : minute + "")
                + (second < 10 ? "0" + second : second + "");
        return Long.parseLong(strDateTime);
    }

    public static void testDateFormat() throws Exception {
        System.out.println("Begin test of currentSystemTimeMillis()");
        System.out.println("currentSystemTimeMillis:"+currentSystemTimeMillis());
        int tCnt = 50;
        Thread[] threads = new Thread[tCnt];
        for (int i = 0; i < tCnt; i++) {
            Runnable run = new Runnable() {

                public void run() {
                    try {
                        int runCounter = 0;
                        for (long i = 0; i < 100000l; i++) {
                            currentSystemTimeMillis();
                            runCounter++;
                        }
                        System.out.println(Thread.currentThread().getName()
                            + " finished. runCounter="
                            + runCounter);
                    } catch (Exception e) {

                    }
                }
            };
            threads[i] = new Thread(run, "Thread" + i);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < tCnt; i++) {
            threads[i].start();
        }

        for (int i = 0; i < tCnt; i++) {
            threads[i].join();
        }

        System.out.println("Test ended cost:" + (System.currentTimeMillis() - start));
    }
}
