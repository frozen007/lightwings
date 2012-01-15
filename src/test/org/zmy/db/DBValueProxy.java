package test.org.zmy.db;

public abstract class DBValueProxy implements DBValue {

    protected DBValue db = null;

    public DBValueProxy() {
        
    }

    public DBValueProxy(DBValue db) {
        this.db = db;
    }

    public void setDBValue(DBValue db) {
        this.db = db;
    }

    @Override
    public void execute() {
        System.out.println("This is DBValueProxy");
    }
}
