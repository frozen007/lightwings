package org.lightwings.concurrent.skeleton;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ConcurrentLogPrinter extends Thread {
    private PrintWriter logWritter = null;

    protected LinkedList<Object> printQueue = new LinkedList<Object>();
    protected long MAX_FILE_BYTES = 20 * 1024 * 1024; //单个最大日志文件大小，默认20MB
    private long lengthCounter = 0;

    private Object lock = new Object();

    public ConcurrentLogPrinter() {

        this.setName(this.getClass().getSimpleName());

        try {
            //logWritter = new PrintWriter(new FileWriter(getPath() + getLogFileName(getLogFileName()), true), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Object o = getPrintObject();
                writeLog(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String getLogFileName() {
        return "log";
    }

    private String getFullLogFileName(String fileName) {
        String logFileName =
            fileName
                + "_"
                + getFileSuffix()
                + ".log";
        return logFileName;
    }

    protected String getPathName() {
        return "";
    }

    protected void writeLog(Object o) {
        String str = o.toString();
        this.lengthCounter += str.length();
        logWritter.println(str);
        logWritter.flush();

        if(this.lengthCounter > this.MAX_FILE_BYTES) {
            this.lengthCounter = 0;
            try {
                logWritter.close();
                logWritter = null;
            } catch (Exception e) {

            }

            try {
                logWritter =
                    new PrintWriter(new FileWriter(getPath() + getFullLogFileName(getLogFileName()), true), true);
            } catch (Exception e) {

            }
        }
    }
    
    public void addToPrintQueue(Object o) {
        synchronized (lock) {
            printQueue.add(o);
            lock.notifyAll();
        }
    }

    protected Object getPrintObject() throws Exception {
        Object o = null;
        synchronized (lock) {
            while (printQueue.isEmpty()) {
                lock.wait();
            }

            o = printQueue.removeFirst();
        }

        return o;
    }

    private String getPath() {
        String pathName = getPathName();
        String separator = System.getProperty("file.separator");
        if (separator.equals("\\"))
            separator = separator + separator;
        else if (separator.equals("/"))
            pathName = pathName.replace('\\', '/');
        File f = new File(pathName);
        if (!f.exists())
            f.mkdirs();
        return pathName + separator;
    }

    protected String getFileSuffix() {
        String currentTimeStr = "";
        //String currentTimeStr = String.valueOf(TradeDate.currentSystemTimeMillis());
        return currentTimeStr.substring(0, 8) + "_" + currentTimeStr.substring(8, 12);
    }
}
