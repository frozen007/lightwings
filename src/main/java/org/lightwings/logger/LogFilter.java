package org.lightwings.logger;

/**
 * ��־�������ӿ�
 * 
 */
public interface LogFilter {

    /**
     * ����Ӧ��LogEvent�Ƿ���������־
     * @param e LogEvent
     * @return true-�����־��false-�������־
     */
    boolean doFilter(LogEvent e);
}
