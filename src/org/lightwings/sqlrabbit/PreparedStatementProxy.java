package org.lightwings.sqlrabbit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.lightwings.sqlrabbit.log.LogManager;
import org.lightwings.sqlrabbit.log.SQLLogger;

public abstract class PreparedStatementProxy implements PreparedStatement {
    protected PreparedStatement ps = null;
    protected String originalSql = null;
    protected String formatedSqlString = null;

    protected Map<Integer, String> parameterMap = null;

    protected MessageFormat sqlformat = null;

    private static SQLLogger logger = LogManager.getSQLLogger();

    public void setPreparedStatement(PreparedStatement ps) {
        this.ps = ps;
    }

    public void setOriginalSql(String sql) {
        this.originalSql = sql;
        /*
         * parameterMap:store the parameter by the order of index.
         */
        parameterMap = new TreeMap<Integer, String>(new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        formatedSqlString = formatSqlString(sql);
        sqlformat = new MessageFormat(formatedSqlString);
    }

    public static PreparedStatement createPreparedStatementProxy(PreparedStatement ps, String sql) {
        PreparedStatementProxy proxy =
            (PreparedStatementProxy) ProxyEnhancer.create(
                PreparedStatementProxy.class,
                new Class[]{PreparedStatement.class},
                ps);
        proxy.setPreparedStatement(ps);
        proxy.setOriginalSql(sql);
        return proxy;
    }

    protected void addParameter(int index, String value) {
        parameterMap.put(index, value);
    }

    /*TODO:Methods that implement PreparedStatement Begin*/
    public void clearParameters() throws SQLException {
        ps.clearParameters();
        parameterMap.clear();
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        ps.setNull(parameterIndex, sqlType);
        addParameter(parameterIndex, "null");
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        ps.setBoolean(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        ps.setByte(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        ps.setShort(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        ps.setInt(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        ps.setLong(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        ps.setFloat(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        ps.setDouble(parameterIndex, x);
        addParameter(parameterIndex, String.valueOf(x));
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        ps.setString(parameterIndex, x);
        addParameter(parameterIndex, "'" + x + "'");
    }

    public ResultSet executeQuery() throws SQLException {
        ResultSet rs = ps.executeQuery();
        logExecutedSql();
        return rs;
    }

    public int executeUpdate() throws SQLException {
        int updCnt = ps.executeUpdate();
        logExecutedSql();
        return updCnt;
    }
    /*TODO:Methods that implement PreparedStatement End*/

    protected void logExecutedSql() {
        logger.log(sqlformat.format(parameterMap.values().toArray()));
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
}
