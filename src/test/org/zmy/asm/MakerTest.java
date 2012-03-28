package test.org.zmy.asm;

public class MakerTest implements MakerTestInterface {

    @Override
    public int test(int i) {
        System.out.println("MakerTest:" + i);
        return i;
    }

    @Override
    public long test(long l) {
        System.out.println("MakerTest:" + l);
        return l;
    }

    @Override
    public String test(String s) {
        System.out.println("MakerTest:" + s);
        return s;
    }

    @Override
    public int[] test(int[] i) {
        System.out.println("MakerTest:");
        for (int a = 0; a < i.length; a++) {
            System.out.print(i[a] + ",");
        }
        System.out.println();
        return i;
    }

    @Override
    public String[] test(String[] as) {
        System.out.println("MakerTest:");
        for (int a = 0; a < as.length; a++) {
            System.out.print(as[a] + ",");
        }
        System.out.println();
        return as;
    }

    @Override
    public void test() {
        System.out.println("MakerTest");
    }

}
