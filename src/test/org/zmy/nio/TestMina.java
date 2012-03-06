package test.org.zmy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.common.TransportType;
import org.apache.mina.filter.LoggingFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;

public class TestMina extends TestCase {
    public static void main(String[] args) throws Exception {
        new TestMina().test001();
    }

    public void test001() throws Exception {
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        IoAcceptor acceptor = new SocketAcceptor();

        SocketAcceptorConfig cfg = new SocketAcceptorConfig();
        cfg.getFilterChain().addLast("logger", new LoggingFilter());
        cfg.getFilterChain().addLast(
            "codec",
            new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

        acceptor.bind(new InetSocketAddress(8880), new TimeServerHandler(), cfg);
        System.out.println("MINA Time server started.");
    }

    class TimeServerHandler extends IoHandlerAdapter {
        public void exceptionCaught(IoSession session, Throwable t) throws Exception {
            t.printStackTrace();
            session.close();
        }

        public void messageReceived(IoSession session, Object msg) throws Exception {
            String str = msg.toString();
            if (str.trim().equalsIgnoreCase("quit")) {
                session.close();
                return;
            }

            Date date = new Date();
            session.write(date.toString() + "\n");
            System.out.println("Message written...");
        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println("Session created...");

            if (session.getTransportType() == TransportType.SOCKET) {
                SocketSessionConfig config = (SocketSessionConfig) session.getConfig();
                config.setReceiveBufferSize(2048);
            }

            session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
            session.write("welcome");
        }
    }

    public void test002() throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        final Selector selector = Selector.open();
        sc.register(selector, SelectionKey.OP_CONNECT);
        sc.register(selector, SelectionKey.OP_READ);
        Thread listenThread = new Thread() {
            public void run() {
                Charset charset = Charset.forName("GBK");
                CharsetDecoder decoder = charset.newDecoder();

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
                            if(ops == SelectionKey.OP_CONNECT) {
                                System.out.println("Connected.");
                            } else if(ops == SelectionKey.OP_READ) {
                                SocketChannel sc =(SocketChannel) key.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                sc.read(buffer.buf());
                                
                                buffer.flip();
                                System.out.println(decoder.decode(buffer.buf()).toString());
                            }
                            
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        listenThread.start();
        sc.connect(new InetSocketAddress("127.0.0.1", 8880));
        sc.finishConnect();

        while(true) {
            Thread.sleep(1000);
        }
    }
}
