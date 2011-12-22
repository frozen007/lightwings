package test.org.zmy.db;

public class DBValueObject implements DBValue {

    @Override
    public void execute() {
        System.out.println("This is DBValueObject");
    }

}
