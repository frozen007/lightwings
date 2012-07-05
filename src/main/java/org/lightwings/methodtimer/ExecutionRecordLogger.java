package org.lightwings.methodtimer;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ExecutionRecordLogger extends Thread {
    private static final String log_file_name = "executionlog";
    private static Thread t = null;
    public static PrintStream out = System.out;
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

    static {
        initialize();
    }

    public static void initialize() {
        t = new ExecutionRecordLogger();
        t.setDaemon(true);
        t.start();
    }

    public static void println(String methodDesc, long cost) {
        println(methodDesc+cost);
    }

    public static void println(String str) {
        queue.offer(str);
        synchronized (queue) {
            queue.notifyAll();
        }
    }

    private static void log(String str) {
        out.println(str);
        out.flush();
    }

    public void run() {
        String fileName = log_file_name + "_" + sdf.format(new Date()) + ".log";
        try {
            out = new PrintStream(new FileOutputStream(fileName, true));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            while (queue.isEmpty()) {
                synchronized (queue) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            String str = queue.poll();
            if (str != null) {
                log(str);
            }
        }
    }
}
