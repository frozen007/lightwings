package test.org.zmy.je;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class JeApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Environment env = null;
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            envConfig.setTransactional(true);
            env = new Environment(new File("data/dbenv"), envConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        env.close();
    }

}
