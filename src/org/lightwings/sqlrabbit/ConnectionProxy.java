package org.lightwings.sqlrabbit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ConnectionProxy implements Connection {

    protected Connection conn = null;

    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps = PreparedStatementProxy.createPreparedStatementProxy(ps);
        return ps;
    }

    public static Connection createConnectionProxy(Connection conn) {
        //ConnectionProxy proxy =  
        return null;
    }
}
