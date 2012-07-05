/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-6-17
 *
 */
package org.zmy.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileRedirectStream
 *
 */
public class FileRedirectStream extends RedirectStream {

    protected File redirectFile = null;
    protected String redirectFileName = null;

    //    private static long FILE_SIZE_LIMIT = 20 * 1024 * 1024; //50MB
    private static long FILE_SIZE_LIMIT = 1 * 300 * 1024;

    public FileRedirectStream(OutputStream stdOut, String redirectFile) throws Exception {
        this(stdOut, getRedirectFile(redirectFile));
        redirectFileName = redirectFile;
    }

    /**
     * @param stdOut
     * @param redirectOut
     * @throws Exception 
     */
    private FileRedirectStream(OutputStream stdOut, File redirectFile) throws Exception {
        super(stdOut, new FileOutputStream(redirectFile, true));
        this.redirectFile = redirectFile;
    }

    /**
     * Output String x to redirect stream 
     * @param bytes
     * @throws IOException
     */
    protected void writeToRedirectOut(byte[] bytes) throws IOException {
        this.redirectOut.write(bytes);
        if (this.redirectFile.length() > FILE_SIZE_LIMIT) {
            this.redirectOut.close();
            this.redirectFile = generateFile(redirectFileName);
            this.redirectOut = new FileOutputStream(this.redirectFile, true);
        }
    }

    private static File generateFile(String redirectFileName) {
        int i = redirectFileName.indexOf('.');
        String prefix = redirectFileName.substring(0, i); //consoleyyyyMMdd
        String suffix = redirectFileName.substring(i); //.log

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        File newRedirectFile = new File(prefix + "_" + sdf.format(new Date()) + suffix);
        return newRedirectFile;
    }

    public static File getRedirectFile(String redirectFile) {
        File file = generateFile(redirectFile).getAbsoluteFile();

        String prefix = redirectFile.substring(0, redirectFile.indexOf('.'));
        File[] files = file.getParentFile().listFiles();

        File maxFile = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(prefix)) {
                if (maxFile == null) {
                    maxFile = files[i];
                } else {
                    if (maxFile.getName().compareTo(files[i].getName()) < 0) {
                        maxFile = files[i];
                    }
                }
            }
        }
        if (maxFile == null) {
            maxFile = file;
        }

        return maxFile;
    }
}
