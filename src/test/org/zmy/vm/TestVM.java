package test.org.zmy.vm;

public class TestVM {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        TestVM test = new TestVM();
        test.test001();
    }

    public void test001() throws Exception {
        int tcnt = 10;
        for (int i = 0; i < tcnt; i++) {
            Thread t = new Thread() {
                public void run() {
                    while (true) {
                        Mem m = new Mem(1);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            };
            t.start();
        }
    }

    class Mem {
        int mem_size_1MB = 1024 * 1024;
        byte[] b = null;
        public Mem(int size) {
            b = new byte[size * mem_size_1MB];
        }
    }
}
