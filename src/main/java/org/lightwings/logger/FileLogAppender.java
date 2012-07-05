package org.lightwings.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * FileLogAppender
 * 输出日志到文件
 */
public class FileLogAppender implements LogAppender {

    private PrintWriter pw = null;

    /**
     * 创建FileLogAppender
     * @param logFile 文件名
     * @param append 文件是否可以追加
     */
    public FileLogAppender(String logFile, boolean append) {
        this(new File(logFile), append);
    }

    public FileLogAppender(File logFile, boolean append) {
        try {
            pw = new PrintWriter(new FileOutputStream(logFile, append));
        } catch (Exception e) {
        }
    }

    /**
     * 将日志内容-log保存到文件
     */
    public void append(String log) {
        if (pw == null) {
            return;
        }
        pw.println(log);
        pw.flush();
    }

    /**
     * 关闭文件
     */
    public void close() {
        if (pw == null) {
            return;
        }
        pw.close();
    }

}
