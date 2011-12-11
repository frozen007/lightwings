package test.org.zmy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TestDBOperation {

	public static void main(String[] args) throws Exception {
		//TestDBOperation test = new TestDBOperation();
		//test.testPreparedStatement();

		//DBValue dbv = test.getDBValue("select");
		Class.forName("test.org.zmy.db.DBValue");
//		Field f = DBValueImpl.class.getField("sql");
//		System.out.println(f.get(dbv));
		System.out.println("End");
	}

//	public void testPreparedStatement() throws Exception {
//		String driverName = "";
//		Class.forName(driverName);
//		String url = "";
//		String user= "";
//		String pass=""; 
//		Connection conn = DriverManager.getConnection(url,user,pass);
//		PreparedStatement ps = conn.prepareStatement("");
//	}

	public DBValue getDBValue(String sql) {
		DBValue v = new DBValueImpl();
		//v.sql = sql;
		return v;
	}
}
