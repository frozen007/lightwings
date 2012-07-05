package org.lightwings.logger;

import java.util.ArrayList;
import java.util.Collection;

import org.lightwings.concurrent.skeleton.CommonDispatchService;

/**
 * 异步日志打印服务类
 *
 */
public abstract class AbstractLogService extends CommonDispatchService<LogEvent> {

    protected LogRepository repository = null;

    /*
     * 日志过滤器列表
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
     * 日志服务主方法
     * 由日志服务线程调用
     */
    public void process(Collection<LogEvent> list) {
        for (LogEvent e : list) {

            /*
             * 对每一个LogEvent检查是否被过滤
             */
            if (checkFilter(e) == false) {
                continue;
            }

            //获取LogAppender并输出日志
            LogAppender appender = repository.getLogAppender(e);
            appender.append(e.getLogContent());
        }
    }

    /*
     * 过滤器检查
     * true-输出日志，false-不输出日志
     */
    protected boolean checkFilter(LogEvent e) {
        if (filterList.isEmpty()) {
            //过滤器列表为空，则输出日志
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
