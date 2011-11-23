package org.zmy.tool;

import java.io.File;

import org.apache.velocity.app.Velocity;

import jxl.Sheet;
import jxl.Workbook;

public class DBFReportGen {

    public static void main(String[] args) throws Exception {
        try {
            Velocity.init();
        } catch (Exception e) {
            System.out.println("Problem initializing Velocity : " + e);
            return;
        }

        String filePath = "E:/develop/报表开发/dbf-form.xls";
        if (args.length >= 1) {
            filePath = args[0];
        }
        File excelFile = new File(filePath);
        Workbook workbook = Workbook.getWorkbook(excelFile);
        Sheet sheet = workbook.getSheet(0);
        int rownum = 1;

        int totalRowNum = sheet.getRows();

        while (rownum < totalRowNum) {
            //sheet.getCell(rownum, rownum).getCellFormat().getFont().
            //rownum++;
        }

    }

    public void parseVertically() {

    }

    public void parseHorizontally() {
        
    }
}
