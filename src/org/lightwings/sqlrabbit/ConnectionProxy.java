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
        ps = PreparedStatementProxy.createPreparedStatementProxy(ps, sql);
        return ps;
    }

    public static Connection createConnectionProxy(Connection conn) {
        if(conn instanceof ConnectionProxy) {
            return conn;
        }

        ConnectionProxy proxy = (ConnectionProxy) ProxyEnhancer.create(ConnectionProxy.class,
                        new Class[] { Connection.class }, conn);
        proxy.setConnection(conn);
        return proxy;
    }
}
