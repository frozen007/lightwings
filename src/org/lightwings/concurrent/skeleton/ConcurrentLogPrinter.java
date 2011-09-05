package org.lightwings.concurrent.skeleton;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ConcurrentLogPrinter extends Thread {
    private PrintWriter log = null;

    protected LinkedList<Object> printQueue = new LinkedList<Object>();

    private Object lock = new Object();

    public ConcurrentLogPrinter() {

        this.setName(this.getClass().getSimpleName());

        try {
            log =
                new PrintWriter(new FileWriter(getPath() + getLogFileName(getLogFileName()), true), true);
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

    protected String getPathName() {
        return "applog\\";
    }

    protected void writeLog(Object o) {
        log.println(o);
        log.flush();
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

    private String getLogFileName(String fileName) {
        String logFileName =
            fileName
                + "_"
                + getFileSuffix()
                + ".log";
        return logFileName;
    }

    protected String getFileSuffix() {
        return "";
    }
}
