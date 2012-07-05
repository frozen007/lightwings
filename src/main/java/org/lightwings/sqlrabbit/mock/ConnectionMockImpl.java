package org.lightwings.sqlrabbit.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.lightwings.asm.ClassMaker;
import org.lightwings.sqlrabbit.PreparedStatementProxy;
import org.lightwings.sqlrabbit.log.LogManager;

public class ConnectionMockImpl implements ConnectionMock {
	Connection conn;

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps = PreparedStatementProxy.createPreparedStatementProxy(ps, sql);
        return ps;
    }

    public static Connection createConnectionMock(Connection connection) {
        try {
            return (Connection) ClassMaker.newInstance(
                Connection.class,
                ConnectionMock.class,
                connection,
                new ConnectionMockImpl());
        } catch (Exception e) {
            LogManager.getSQLLogger().err(e);
        }
        return connection;
    }
}
