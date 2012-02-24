package test.org.zmy.nio;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;

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

    public void test001() throws Exception {
        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        IoAcceptor acceptor = new SocketAcceptor();

        SocketAcceptorConfig cfg = new SocketAcceptorConfig();
        cfg.getFilterChain().addLast("logger", new LoggingFilter());
        cfg.getFilterChain().addLast("codec",
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
            session.write(date.toString());
            System.out.println("Message written...");
        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println("Session created...");

            if (session.getTransportType() == TransportType.SOCKET) {
                SocketSessionConfig config = (SocketSessionConfig) session.getConfig();
                config.setReceiveBufferSize(2048);
            }

            session.setIdleTime(IdleStatus.BOTH_IDLE, 10);
        }
    }
}
