package test.org.zmy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDBOperation extends DBOperation {

    public static void main(String[] args) throws Exception {
        TestDBOperation test = new TestDBOperation();
        //test.init();
        test.testPreparedStatement();

        /*
        test.getDBValue("select");
        Class.forName("test.org.zmy.db.DBValue");
        // Field f = DBValueImpl.class.getField("sql");
        // System.out.println(f.get(dbv));
         * 
         */
        System.out.println("End");
    }

    public void testPreparedStatement() throws Exception {
        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);
        System.out.println("Class.forName");
        String url = "jdbc:mysql://localhost/mydev";
        String user = "zmyhr";
        String pass = "zmyhr";
        Connection conn = DriverManager.getConnection(url, user, pass);
        System.out.println("DriverManager.getConnection");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM pet WHERE name=?");
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
