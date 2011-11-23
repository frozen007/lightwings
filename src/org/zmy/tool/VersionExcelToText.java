/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-9-16
 *
 */
package org.zmy.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.zmy.util.StringUtil;

/**
 * TestJxl
 *
 */
public class VersionExcelToText {
    
    public static String line_sep = System.getProperty("line.separator");

    public static void main(String[] args) throws Exception {
        if(args.length==0) {
            System.out.println("Input version file");
            System.exit(0);
        }
        String filePath = args[0];

        File versionExcelFile = new File(filePath);
        Workbook workbook = Workbook.getWorkbook(versionExcelFile);
        String versionExcelFileName = versionExcelFile.getName();
        File outputFile =
            new File(versionExcelFile.getParentFile(), versionExcelFileName.substring(
                0,
                versionExcelFileName.lastIndexOf('.'))
                + ".txt");
        PrintWriter outputWriter = new PrintWriter(new FileWriter(outputFile));

        Sheet sheet = workbook.getSheet(0);
        int rownum = 1;

        int totalRowNum = sheet.getRows();

        ArrayList<TracerBean> tracerBeanList = new ArrayList<TracerBean>();
        while (rownum < totalRowNum) {
            TracerBean bean = new TracerBean(sheet, rownum);
            tracerBeanList.add(bean);
            rownum++;
        }
        
        Collections.sort(tracerBeanList);

        StringBuilder sbuf = new StringBuilder();
        dumpTracerBean(tracerBeanList, sbuf);
        outputWriter.print(sbuf);
        outputWriter.println();
        outputWriter.flush();
    }

    public static void dumpTracerBean(List<TracerBean> tracerBeanList, StringBuilder sbuf) {
        Iterator<TracerBean> it = tracerBeanList.iterator();
        while (it.hasNext()) {
            TracerBean bean = it.next();
            String title =
                bean.traceridCell.getContents() + "    " + bean.tracertitleCell.getContents();
            sbuf.append(title
                + StringUtil.leftPadding(
                    bean.useridCell.getContents(),
                    ' ',
                    120 - title.getBytes().length));
            sbuf.append(line_sep);

            String[] modifiedfiles = bean.modifiedfilesCell.getContents().split("\n");
            Arrays.sort(modifiedfiles);
            for (int i = 0; i < modifiedfiles.length; i++) {
                if (modifiedfiles[i] != null) {
                    String tmp = modifiedfiles[i].trim();
                    if (!tmp.equals("")) {
                        sbuf.append(StringUtil.rightPadding(tmp, ' ', 120));
                        sbuf.append(line_sep);
                    }
                }
            }
            sbuf.append(line_sep);
            sbuf.append(line_sep);
        }
    }
}

class TracerBean implements Comparable<TracerBean> {
    private static final int traceridColumn = 0;
    private static final int tracertitleColumn = 1;
    private static final int modifiedfilesColumn = 3;
    private static final int useridColumn = 2;

    public Cell traceridCell = null;
    public Cell tracertitleCell = null;
    public Cell modifiedfilesCell = null;
    public Cell useridCell = null;

    public TracerBean(Sheet sheet, int rownum) {
        traceridCell = sheet.getCell(traceridColumn, rownum);
        tracertitleCell = sheet.getCell(tracertitleColumn, rownum);
        modifiedfilesCell = sheet.getCell(modifiedfilesColumn, rownum);
        useridCell = sheet.getCell(useridColumn, rownum);
    }

    public int compareTo(TracerBean o) {
        String tracerid1 = traceridCell.getContents();
        String tracerid2 = o.traceridCell.getContents();
        String tracerid1Num = tracerid1.substring(tracerid1.lastIndexOf('_') + 1);
        String tracerid2Num = tracerid2.substring(tracerid2.lastIndexOf('_') + 1);
        return tracerid1Num.compareTo(tracerid2Num);
    }
}
