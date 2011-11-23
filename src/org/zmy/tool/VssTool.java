/**
 * 
 */
package org.zmy.tool;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.FileReader;
import java.io.Reader;

import org.zmy.util.VssUtil;

/**
 * @author zhao-mingyu
 * 
 */
public class VssTool {

    /**
     * VSS¹¤¾ß
     * @param args -f <basedir> <inputfile>
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out.println("Usage:");
            System.out.println("-f [<basedir>] [<inputfile>]");
            return;
        }
        VssTool vssTool = new VssTool();
        if (args[0].equals("-f")) {
            vssTool.convertVssFileList(args);
        }
    }

    public void convertVssFileList(String[] args) throws Exception {
        String baseDir = "$/";
        String inputStr = "";
        Reader readerFileList = null;
        if (args.length > 1) {
            baseDir = args[1];
            if (args.length > 2) {
                readerFileList = new FileReader(args[2]);
            } else {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                inputStr = (String) clip.getData(DataFlavor.stringFlavor);
            }
        } else {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            inputStr = (String) clip.getData(DataFlavor.stringFlavor);
        }
        System.out.println(VssUtil.convertFileList(baseDir, inputStr, readerFileList));
    }
}
