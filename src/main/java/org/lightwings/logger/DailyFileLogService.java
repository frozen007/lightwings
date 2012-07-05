package org.lightwings.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

/**
 * ���ڵ������ڵ���־����
 * ÿ��ᰴyyyyMMddΪ��׺�������µ���־�ļ�
 */
public class DailyFileLogService extends AbstractLogService {
    private static final String TIME_PATTERN = "yyyyMMdd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
    private long lastDate = -1;

    /**
     * ��ָ����basePath-��־�ļ���·��������־����
     * @param basePath ��־�ļ���·��
     */
    public DailyFileLogService(String basePath) {
        super(new RollingFileLogRepository(basePath, TIME_PATTERN));
        Calendar cal = Calendar.getInstance();
        lastDate = Long.parseLong(sdf.format(cal.getTime()));
    }

    public void process(Collection<LogEvent> list) {
        //�������������ر�ǰһ�����־���������µ���־�ļ�
        checkClosePreviousDateLogRepository();

        super.process(list);
    }

    /*
     * ������
     */
    protected void checkClosePreviousDateLogRepository() {
        Calendar cal = Calendar.getInstance();
        long currentDate = Long.parseLong(sdf.format(cal.getTime()));
        if (currentDate <= lastDate) {
            return;
        }

        //�ѿ��죬��������appender���ر��ļ�
        super.repository.resetAppenders();
    }
}
