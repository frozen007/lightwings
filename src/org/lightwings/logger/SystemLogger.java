package org.lightwings.logger;


public class SystemLogger {
    private DailyFileLogService serv = null;

    /*
     * ����ϵͳ��־����
     * path ��־�ļ�����·��
     */
    protected SystemLogger(String path) {
        initializeLogService(path);
    }

    /*
     * ��ʼ����־����    
     */
    private void initializeLogService(String path) {
        serv = new DailyFileLogService(path);

        serv.start();

    }

    /**
     * �����־
     * @param logName ��־��
     * @param logContent ��־����
     */
    public void log(String logName, String logContent) {
        //������־������־���ݴ���LogEvent�����ַ�����־�����߳�
        LogEvent logE = new LogEvent(logName, logContent);
        serv.dispatch(logE);
    }
}
