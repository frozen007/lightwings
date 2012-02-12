package org.zmy.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

public class TestNIO extends TestCase {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        TestNIO nio = new TestNIO();
        nio.test001();
    }

    public void test001() throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        InetSocketAddress isa = new InetSocketAddress(8880);
        ssc.socket().bind(isa);

        Charset charset = Charset.forName("US-ASCII");
        CharsetEncoder encoder = charset.newEncoder();

        while (true) {
            SocketChannel sc = ssc.accept();
            try {
                String now = new Date().toString();
                sc.write(encoder.encode(CharBuffer.wrap(now + "\r\n")));
                System.out.println(sc.socket().getInetAddress() + " : " + now);
                sc.close();
            } finally {
                // Make sure we close the channel (and hence the socket)
                sc.close();
            }
        }
    }

    public void test002() throws Exception {
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(8880);
        ssc.socket().bind(isa);

        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            System.out.println("selected...");
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                i.remove();
                int ops = sk.readyOps();
                System.out.println("processing key: " + ops);

                if (ops == SelectionKey.OP_ACCEPT) {
                    ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
                    SocketChannel sc = nextReady.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put("welcome!\n".getBytes());
                    buffer.flip();
                    sc.write(buffer);

                    sc.register(selector, SelectionKey.OP_READ);
                } else if (ops == SelectionKey.OP_READ) {
                    SocketChannel sc = (SocketChannel) sk.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    sc.read(buffer);
                    
                    String recStr = null;
                    buffer.flip();
                    Charset charset = Charset.forName("US-ASCII");
                    CharsetDecoder decoder = charset.newDecoder();
                    recStr = decoder.decode(buffer).toString();
                    System.out.println("received:" + recStr);
                }
            }
        }
    }
}
