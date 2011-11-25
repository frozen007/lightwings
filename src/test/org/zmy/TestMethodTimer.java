package test.org.zmy;

import org.lightwings.methodtimer.ExecutionRecordLogger;


public class TestMethodTimer {
    public String a = "d";

    public static void main(String[] args) {
        TestMethodTimer timer =new TestMethodTimer();
        timer.hello();

        TestInterfaceImplSub test = new TestInterfaceImplSub();
        test.test();
    }

    public Object getO(int i, Object o) {
        return o+""+i;
    }
    
    public void hello() {
        ExecutionRecordLogger.print(a.hashCode());
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hello");
    }
}
