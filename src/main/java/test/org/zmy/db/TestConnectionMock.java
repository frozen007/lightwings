package test.org.zmy.db;

import java.sql.Connection;

import org.lightwings.asm.ClassMaker;

public class TestConnectionMock {

    public static Connection createConnectionProxy(Connection connection) {
        try {
            return (Connection) ClassMaker.newInstance(Connection.class, DBValue.class, connection, new DBValueImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
