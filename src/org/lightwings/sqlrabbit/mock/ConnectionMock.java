package org.lightwings.sqlrabbit.mock;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConnectionMock {

    public PreparedStatement prepareStatement(String sql) throws SQLException;

}
