package test.org.zmy.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DBValue {

    //public String sql = "";

    public void execute();

    public void dumpvalue();

    public PreparedStatement prepareStatement(String sql) throws SQLException;
}
