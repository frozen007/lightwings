/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-3-25
 *
 */
package org.zmy.db.proxy;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.cglib.proxy.Enhancer;

/**
 * LogPreparedStatement
 *
 */
public abstract class LogPreparedStatement implements PreparedStatement {

    protected String originalSqlString = null;

    protected String formatedSqlString = null;

    protected Map parameterMap = null;

    protected MessageFormat sqlformat = null;

    public void setPreparedSqlString(String str) {
        /*
         * parameterMap:store the parameter by the order of index.
         */
        parameterMap = new TreeMap(new Comparator() {
            public int compare(Object o1, Object o2) {
                int intO1 = 0;
                int intO2 = 0;
                try {
                    intO1 = Integer.parseInt(o1.toString());
                    intO2 = Integer.parseInt(o2.toString());
                } catch (Exception e) {
                    return 0;
                }
                return intO1 - intO2;
            }
        });
        originalSqlString = str;
        formatedSqlString = formatSqlString(str);
        sqlformat = new MessageFormat(formatedSqlString);
    }

    public void addParameter(Object index, Object value) {
        String paraValue = null;
        if (value instanceof String) {
            paraValue = "'" + value + "'";
        } else {
            paraValue = value == null ? "" : value.toString();
        }
        parameterMap.put(index, paraValue);
    }

    public String getActualSql() {
        return sqlformat.format(parameterMap.values().toArray());
    }

    /**
     * Replace the '?' with the "{X}"
     * @param sqlStr
     * @return
     */
    public static String formatSqlString(String sqlStr) {
        StringBuffer sb = new StringBuffer(sqlStr);
        int count = 0;
        int cursor = 0;
        while (cursor < sb.length()) {
            String tmp = null;
            if (sb.charAt(cursor) == '?') {
                tmp = "{" + count + "}";
                count++;
            } else if (sb.charAt(cursor) == '\'') {
                tmp = "''";
            } else {
            }
            if (tmp != null) {
                sb.replace(cursor, cursor + 1, tmp);
                cursor = cursor + tmp.length();
            } else {
                cursor++;
            }
        }
        return sb.toString();
    }

    public static PreparedStatement createLogPreparedStatement(PreparedStatement ps, Class subClass) {
        //use cglib technic create a LogPreparedStatement which is an implementation of PreparedStatement
        PreparedStatementCallbackFilter pscf = new PreparedStatementCallbackFilter(subClass, ps);
        LogPreparedStatement advPs =
            (LogPreparedStatement) Enhancer.create(subClass, null, pscf, pscf
                .getCallbacks());

        //if the target PreparedStatement Object is compatible with oracle 
        if ("oracle.jdbc.driver.OraclePreparedStatement".equals(ps.getClass().getName())) {
            Class psClass = ps.getClass();
            try {
                //get the getOriginalSql() method in OraclePreparedStatement 
                Method method = psClass.getMethod("getOriginalSql", new Class[]{});
                Object ret = method.invoke(ps, new Object[]{});
                //set the compiled sql string for the LogPreparedStatement object
                advPs.setPreparedSqlString(ret.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return advPs;
    }
    
    /**
     * Create a LogPreparedStatement
     * @param ps the raw PreparedStatement which is created by database connection object directly
     * @return a LogPreparedStatement attached with original sql string
     */
    public static PreparedStatement createLogPreparedStatement(PreparedStatement ps) {
        return createLogPreparedStatement(ps, LogPreparedStatement.class);
    }

    public String getOriginalSqlString() {
        return originalSqlString;
    }

    public void dumpActualSql() {
        System.out.println(getActualSql());
    }
}
