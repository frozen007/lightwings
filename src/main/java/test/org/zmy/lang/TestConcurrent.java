package test.org.zmy.lang;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

public class TestConcurrent extends TestCase {

    public void test001() {
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.poll();

        AtomicInteger atomicInt = new AtomicInteger();
    }
}
