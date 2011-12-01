package test.org.zmy;

import org.lightwings.methodtimer.ExecutionRecordLogger;
import org.lightwings.methodtimer.SQLIntercepter;

public class TestMethodTimer {
    public String a = "UPDATE DSF";

    public static void main(String[] args) {
        TestMethodTimer timer = new TestMethodTimer();
        timer.hello();
        timer.test();

        TestInterfaceImplSub test = new TestInterfaceImplSub();
        test.test();
    }

    public String getSql() {
        return a;
    }

    public int[] test() {
        if (SQLIntercepter.isIntercept(getSql())) {
            return null;
        }
        int a = 0;
        int b = 1;
        System.out.println("sss" + (b - a));
        return null;
    }

    public void hello() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ExecutionRecordLogger.println("hello" + 101);
    }
}
