package org.lightwings.sqlrabbit;

import java.sql.PreparedStatement;

public abstract class PreparedStatementProxy implements PreparedStatement {
    
    protected PreparedStatement ps = null;

    public void setPreparedStatement(PreparedStatement ps) {
        this.ps = ps;
    }

    public static PreparedStatement createPreparedStatementProxy(PreparedStatement ps) {
        return null;
    }
}
