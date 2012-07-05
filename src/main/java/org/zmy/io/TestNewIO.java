/**
 * Project ZmyRepo
 * by zhao-mingyu at 2008-11-13
 *
 */
package org.zmy.io;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * TestNewIO
 *
 */
public class TestNewIO extends TestCase {

    public void test001() throws Exception {
        FileOutputStream fos = new FileOutputStream("tmp.txt");
        RedirectStream io = new RedirectStream(System.out, fos);
        FileOutputStream fos2 = new FileOutputStream("tmp2.txt");
        io.appendRedirectOut(fos2);
        System.setOut(io);
        if (System.out instanceof RedirectStream) {
            System.out.println("yes");
        }

        System.out.println("testsetset");
        System.out.println("fdfdfd");
        System.out.println("878784758457");
        System.out.print("878784758457");
    }

    public void test002() {
        File file = FileRedirectStream.getRedirectFile("console.log");
        System.out.println(file.toString());
    }

    public void test003() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String consoleLog = "console" + sdf.format(new Date()) + ".log";
        RedirectStream redirectStd = new FileRedirectStream(System.out, consoleLog);
        RedirectStream redirectErr = new FileRedirectStream(System.err, consoleLog);
        System.setOut(redirectStd);
        System.setErr(redirectErr);
        while (true) {

            Thread.sleep(500);
            for (int i = 0; i < 50; i++) {
                System.out.println("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
            }
        }
    }
}
