package test.org.zmy.db;

public class DBValueImpl implements DBValue {

    @Override
	public void execute() {
	    System.out.println("DBValueImpl");
    }

    public void dumpvalue() {
        System.out.println("DBValueImpl dumpvalue");
    }
}
