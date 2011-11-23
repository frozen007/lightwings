package test.org.zmy;


public class TestMethodTimer {

    public static void main(String[] args) {
        TestMethodTimer timer =new TestMethodTimer();
        timer.getO(0, new Object());
        timer.hello();
    }

    public Object getO(int i, Object o) {
        return o+""+i;
    }
    
    public void hello() {
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
