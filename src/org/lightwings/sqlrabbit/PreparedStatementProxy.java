package org.lightwings.sqlrabbit;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PreparedStatementProxy implements PreparedStatement {
    protected PreparedStatement ps = null;
    protected String originalSql = null;

    public void setPreparedStatement(PreparedStatement ps) {
        this.ps = ps;
    }

    public void setOriginalSql(String sql) {
        this.originalSql = sql;
    }

    public static PreparedStatement createPreparedStatementProxy(PreparedStatement ps, String sql) {
        PreparedStatementProxy proxy = (PreparedStatementProxy) ProxyEnhancer.create(PreparedStatementProxy.class,
                        new Class[] { PreparedStatement.class }, ps);
        proxy.setPreparedStatement(ps);
        proxy.setOriginalSql(sql);
        return proxy;
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        ps.setNull(parameterIndex, sqlType);
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        ps.setBoolean(parameterIndex, x);
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        ps.setByte(parameterIndex, x);
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        ps.setShort(parameterIndex, x);
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        ps.setInt(parameterIndex, x);
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        ps.setLong(parameterIndex, x);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        ps.setFloat(parameterIndex, x);
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        ps.setDouble(parameterIndex, x);
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        ps.setString(parameterIndex, x);
    }
}
