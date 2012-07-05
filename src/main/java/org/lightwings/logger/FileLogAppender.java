package org.lightwings.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * FileLogAppender
 * �����־���ļ�
 */
public class FileLogAppender implements LogAppender {

    private PrintWriter pw = null;

    /**
     * ����FileLogAppender
     * @param logFile �ļ���
     * @param append �ļ��Ƿ����׷��
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
     * ����־����-log���浽�ļ�
     */
    public void append(String log) {
        if (pw == null) {
            return;
        }
        pw.println(log);
        pw.flush();
    }

    /**
     * �ر��ļ�
     */
    public void close() {
        if (pw == null) {
            return;
        }
        pw.close();
    }

}
