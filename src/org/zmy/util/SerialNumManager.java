package org.zmy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class SerialNumManager {

    public static synchronized long getSerialNum() {
        long current = 0;
        long next = 0;
        File serialFile = new File("serialNum");
        try {
            if (serialFile.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(serialFile));
                    String no = reader.readLine();
                    current = Long.parseLong(no);
                    reader.close();
                } catch (Exception e) {
                    current = 0;
                }
            }
            next = current + 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                FileOutputStream fos = new FileOutputStream(serialFile, false);
                fos.write(String.valueOf(next).getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return current;
    }

    public static String getSerialNumString(int size) {
        long serialNum = (long) (getSerialNum() % (Math.pow(10, size)));
        String serialNumStr = StringUtil.leftPadding(String.valueOf(serialNum), '0', size);
        return serialNumStr.substring(0, size);
    }
}
