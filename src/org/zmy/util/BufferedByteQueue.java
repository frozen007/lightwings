/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-8-5
 *
 */
package org.zmy.util;

/**
 * BufferedByteQueue
 *
 */
public class BufferedByteQueue {

    protected static int DEFAULT_INITIAL_BUFF_SIZE = 1024 * 10; //10KB
    protected static int DEFAULT_INCREMENT_SIZE = 1024 * 2; //2KB

    protected byte[] buff = null;
    protected int incSize = DEFAULT_INCREMENT_SIZE;
    protected int writeCursor = 0; //points to the available position currently
    protected int readCursor = 0; //points to the position where can be red

    public int getWriteCursor() {
        return writeCursor;
    }

    public int getReadCursor() {
        return readCursor;
    }

    public BufferedByteQueue() {
        init(DEFAULT_INITIAL_BUFF_SIZE, DEFAULT_INCREMENT_SIZE);
    }

    public BufferedByteQueue(int initSize, int incSize) {
        init(initSize, incSize);
    }

    protected void init(int initSize, int incSize) {
        buff = new byte[initSize];
        this.incSize = incSize;
    }

    public int availableSize() {
        return writeCursor - readCursor;
    }

    public synchronized void write(byte[] bytes, int len) {
        int remainingSize = buff.length - writeCursor;
        int currentSize = buff.length;
        int trashSize = readCursor; //space that can be released
        int incomingSize = len;
        if (remainingSize < incomingSize) {
            if (trashSize + remainingSize >= incomingSize) {
                System.arraycopy(buff, readCursor, buff, 0, writeCursor - readCursor);
                writeCursor = writeCursor - trashSize;
                readCursor = 0;
            } else {
                //allocate new space for incoming bytes
                int incCnt = (int) Math.ceil((double) (incomingSize - remainingSize) / incSize);
                byte[] newbuff = new byte[currentSize + incCnt * incSize];

                //transfer to new buffer
                System.arraycopy(buff, 0, newbuff, 0, writeCursor);
                buff = newbuff;
            }
        }
        System.arraycopy(bytes, 0, buff, writeCursor, incomingSize);
        writeCursor += incomingSize;
    }

    public synchronized void write(byte[] bytes) {
        write(bytes, bytes.length);
    }

    public synchronized int read(byte[] bytes, int len) {
        if (readCursor == writeCursor) {
            return -1;
        }
        int redSize = Math.min(len, writeCursor - readCursor);
        System.arraycopy(buff, readCursor, bytes, 0, redSize);
        readCursor += redSize;
        return redSize;
    }

    public synchronized int read(byte[] bytes) {
        return read(bytes, bytes.length);
    }

    public byte[] getAvailableBytes() {
        byte[] currentBytes = new byte[availableSize()];
        System.arraycopy(buff, 0, currentBytes, 0, currentBytes.length);
        return currentBytes;
    }

    public static void main(String[] args) {
        BufferedByteQueue q = new BufferedByteQueue(1024, 200);
        String src = "select * from fixalgo_parentexec t where t.instructid='xxxx' and t.stkid='xxx'";
        while (true) {
            q.write(src.getBytes());
            byte[] bytes = new byte[76];
            q.read(bytes);
            System.out.println(new String(bytes));
        }
    }
}
