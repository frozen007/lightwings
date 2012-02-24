package org.lightwings.logger;

public class TestLogger {

    public static DailyFileLogService serv = null;

    public static void main(String[] args) {
        serv = new DailyFileLogService("E:/ztmp");
        serv.start();

        while (true) {
            log("hh", "this is hehe");
            log("xx", "this is xixi");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(String name, String msg) {
        LogEvent e = new LogEvent(name, msg);
        serv.dispatch(e);
    }

}
