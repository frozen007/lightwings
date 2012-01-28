package test.org.zmy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import junit.framework.TestCase;

import org.lightwings.sqlrabbit.ConnectionProxy;
import org.lightwings.sqlrabbit.PreparedStatementProxy;

public class TestDBOperation extends TestCase {

    public static void main(String[] args) throws Exception {
    }

    public void testOracle() throws Exception {

        String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl10";
        String user = "zmy1019";
        String pass = "zmy1019";
        Connection conn = DriverManager.getConnection(url, user, pass);
        String sql = "SELECT * FROM account WHERE acctid=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps = PreparedStatementProxy.createPreparedStatementProxy(ps, sql);
        ps.setString(1, "tiantian");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
        }
        ps.close();
        conn.close();

    }

    public void testPreparedStatementMysql() throws Exception {
        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);
        System.out.println("Class.forName");
        String url = "jdbc:mysql://localhost/mydev";
        String user = "zmyhr";
        String pass = "zmyhr";
        Connection conn = DriverManager.getConnection(url, user, pass);
        conn = ConnectionProxy.createConnectionProxy(conn);
        System.out.println("DriverManager.getConnection");
        String sql = "SELECT * FROM pet WHERE name=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        // ps = PreparedStatementProxy.createPreparedStatementProxy(ps, sql);
        ps.setString(1, "tiantian");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3));
        }
        ps.close();
        conn.close();
    }

    public DBValue getDBValue(String sql) {
        DBValue v = null;
        // v = new DBValueImpl();
        // v.sql = sql;
        return v;
    }
}
