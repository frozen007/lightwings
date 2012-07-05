/**
 * 
 */
package org.zmy.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author zhao-mingyu
 * 
 */
public class VssUtil {

    private static String line_sep = System.getProperty("line.separator");

    public static String convertFileList(String baseVssPath, String vssFileList,
            Reader readerFileList) throws Exception {
        BufferedReader input = null;
        if (readerFileList != null) {
            input = new BufferedReader(readerFileList);
        } else {
            input = new BufferedReader(new StringReader(vssFileList));
        }
        StringBuffer fileList = new StringBuffer();
        String currentPath = "";
        if (baseVssPath == null || baseVssPath.equals("")) {
            baseVssPath = "$/";
        }
        String buf = input.readLine();
        while (buf != null) {
            String tmp = buf.trim();
            int start = tmp.indexOf(baseVssPath);
            if (start >= 0) {
                if (tmp.endsWith(":")) {
                    tmp = tmp.substring(0, tmp.length() - 1);
                }
                tmp = tmp.substring(start + baseVssPath.length());

                currentPath = tmp;
            } else if (!tmp.equals("")) {
                fileList.append(currentPath + "/" + tmp);
                fileList.append(line_sep);
            }
            buf = input.readLine();
        }
        if (fileList.length() > 0) {
            fileList.deleteCharAt(fileList.length() - 1);
        }
        return fileList.toString();
    }
}
