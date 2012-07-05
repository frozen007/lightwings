package test.org.zmy.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBValueImpl implements DBValue {

    @Override
	public void execute() {
	    System.out.println("DBValueImpl");
    }

    public void dumpvalue() {
        System.out.println("DBValueImpl dumpvalue");
    }

    public PreparedStatement prepareStatement(String sql)   throws SQLException {
        System.out.println("prepareStatement:"+sql);
        return null;
    }
}
