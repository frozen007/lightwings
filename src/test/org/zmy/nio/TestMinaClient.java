package test.org.zmy.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

public class TestMinaClient {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        new TestMinaClient().test001();
    }

    public void test001() throws Exception {
        SocketChannel sc = SocketChannel.open();
        Selector selector = Selector.open();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_CONNECT);
        sc.connect(new InetSocketAddress("127.0.0.1", 8880));

        //sc.register(selector, SelectionKey.OP_READ);

        Thread listner = new Listener(selector);
        listner.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (true) {
            String str = reader.readLine();
            buf.clear();
            buf.put(str.getBytes());
            buf.flip();
            sc.write(buf);
        }
    }

    class Listener extends Thread {
        Selector selector;

        public Listener(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            SelectionKey rkey = null;
            while (true) {
                try {
                    int cnt = selector.select();
                    if (cnt <= 0) {
                        continue;
                    }
                    Iterator<SelectionKey> itKeys = selector.selectedKeys().iterator();
                    while (itKeys.hasNext()) {
                        SelectionKey key = itKeys.next();
                        itKeys.remove();
                        int ops = key.readyOps();
                        SocketChannel ch = (SocketChannel) key.channel();
                        if ((ops & SelectionKey.OP_CONNECT) != 0) {
                            ch.finishConnect();
                            System.out.println("Connected.");
                            rkey = ch.register(selector, SelectionKey.OP_READ);
                        } else if ((ops & SelectionKey.OP_READ) != 0) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            sc.read(buffer);

                            buffer.flip();
                            System.out.println(decoder.decode(buffer).toString());
                            buffer.clear();
                            buffer.put("Hi this is client.".getBytes());
                            buffer.flip();
                            //sc.write(buffer);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
