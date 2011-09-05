package org.zmy.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * RedirectStream
 * @author zhao-mingyu
 */
public class RedirectStream extends PrintStream {

    protected OutputStream redirectOut = null;

    protected RedirectStreamChain chain = null;

    protected String line_sep = System.getProperty("line.separator");

    /**
     * Constructor a RedirectStream without chain attached
     * @param stdOut
     *          the OutputStream whose contents will be redirect to another stream
     * @param redirectOut
     *          the stream that the stdOut will be redirected to
     */
    public RedirectStream(OutputStream stdOut, OutputStream redirectOut) {
        super(stdOut);
        this.redirectOut = redirectOut;
    }

    /**
     * Constructor a RedirectStream with a chain attached
     * @param stdOut
     *          the OutputStream whose contents will be redirect to another stream
     * @param redirectOut
     *          the stream that the stdOut will be redirected to
     * @param chain
     *          the RedirectStreamChain which stands for the next
     */
    public RedirectStream(OutputStream stdOut, OutputStream redirectOut, RedirectStreamChain chain) {
        this(stdOut, redirectOut);
        this.chain = chain;
    }

    /**
     * Print a String and then terminate the line.
     * @see java.io.PrintStream#println(String s)
     */
    public void println(String x) {
        redirect(x + line_sep);
    }

    /**
     * Print a String.
     * @see java.io.PrintStream#print(String s)
     */
    public void print(String x) {
        redirect(x);
    }

    /**
     * Print an Object and then terminate the line.
     * @see java.io.PrintStream#println(Object s)
     */
    public void println(Object o) {
        println(o.toString());
    }

    /**
     * Print a String.
     * @see java.io.PrintStream#print(String s)
     */
    public void print(Object o) {
        print(o.toString());
    }

    /**
     * Print an char Array and then terminate the line.
     * @see java.io.PrintStream#println(char[] s)
     */
    public void println(char[] s) {
        println(new String(s));
    }

    /**
     * Print an char Array
     * @see java.io.PrintStream#print(char[] s)
     */
    public void print(char[] s) {
        print(new String(s));
    }

    protected void attachStdStream(OutputStream out) {
        super.out = out;
    }

    protected void setChain(RedirectStreamChain chain) {
        this.chain = chain;
    }

    /**
     * Append a OutputStream as the descendant of this RedirectStream.
     * If there is already a descendant(the <code>{@link #chain}</code> is not null),
     * append it to the last descendent RedirectStream recursively.
     * @param redirectOut
     */
    public void appendRedirectOut(OutputStream redirectOut) {
        if (chain == null) {
            chain = new RedirectStreamChain(this.redirectOut, redirectOut);
        } else {
            chain.getRedirectStream().appendRedirectOut(redirectOut);
        }
    }

    /**
     * Output String x to all of the descendent RedirectStream recursively.
     * @param x
     */
    protected void redirect(String x) {
        super.print(x);
        if (chain == null) {
            try {
                writeToRedirectOut(x.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            RedirectStream next = chain.getRedirectStream();
            if (this != next) {
                next.redirect(x);
            }
        }
    }

    /**
     * Output String x to redirect stream 
     * @param bytes
     * @throws IOException
     */
    protected void writeToRedirectOut(byte[] bytes) throws IOException {
        this.redirectOut.write(bytes);
    }

    /**
     * Close all of the OutputStream recursively. 
     */
    public void close() {
        super.close();
        if (chain == null) {
            try {
                this.redirectOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            chain.getRedirectStream().close();
        }
    }
}
