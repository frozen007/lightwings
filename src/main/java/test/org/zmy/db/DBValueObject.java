package test.org.zmy.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBValueObject implements DBValue {

    @Override
    public void execute() {
        System.out.println("This is DBValueObject");
    }

    @Override
    public void dumpvalue() {
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return null;
    }

}
