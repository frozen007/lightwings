package test.org.zmy.je;

import java.io.File;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

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

            Transaction ts = env.beginTransaction(null, null);
            StoreConfig storeConfig = new StoreConfig();
            storeConfig.setAllowCreate(true);
            storeConfig.setTransactional(true);
            EntityStore store = new EntityStore(env, "JeApp", storeConfig);
            PrimaryIndex<String, JeEntityValue> index =
                store.getPrimaryIndex(String.class, JeEntityValue.class);

            JeEntityValue value = index.get("test11");
            if (value == null) {
                value = new JeEntityValue();
                value.id = "11";
                value.name = "test11";
                index.put(ts, value);
            } else {
                System.out.println(value.id + "," + value.name);
            }
            ts.commit();

            store.close();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        env.close();
    }

    @Entity
    private static class JeEntityValue {
        @PrimaryKey
        public String name;

        public String id;

    }
}
