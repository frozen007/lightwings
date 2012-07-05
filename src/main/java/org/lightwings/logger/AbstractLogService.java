package org.lightwings.logger;

import java.util.ArrayList;
import java.util.Collection;

import org.lightwings.concurrent.skeleton.CommonDispatchService;

/**
 * �첽��־��ӡ������
 *
 */
public abstract class AbstractLogService extends CommonDispatchService<LogEvent> {

    protected LogRepository repository = null;

    /*
     * ��־�������б�
     */
    protected ArrayList<LogFilter> filterList = new ArrayList<LogFilter>();

    public AbstractLogService(LogRepository repository) {
        if (repository == null) {
            this.repository = new DefaultLogRepository();
        } else {
            this.repository = repository;
        }
    }

    /**
     * ��־����������
     * ����־�����̵߳���
     */
    public void process(Collection<LogEvent> list) {
        for (LogEvent e : list) {

            /*
             * ��ÿһ��LogEvent����Ƿ񱻹���
             */
            if (checkFilter(e) == false) {
                continue;
            }

            //��ȡLogAppender�������־
            LogAppender appender = repository.getLogAppender(e);
            appender.append(e.getLogContent());
        }
    }

    /*
     * ���������
     * true-�����־��false-�������־
     */
    protected boolean checkFilter(LogEvent e) {
        if (filterList.isEmpty()) {
            //�������б�Ϊ�գ��������־
            return true;
        }

        for (LogFilter filter : filterList) {
            if (filter.doFilter(e) == false) {
                return false;
            }
        }
        return true;
    }

    public void addLogFilter(LogFilter filter) {
        filterList.add(filter);
    }
}
