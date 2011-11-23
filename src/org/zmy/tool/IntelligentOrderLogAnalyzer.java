package org.zmy.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IntelligentOrderLogAnalyzer {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        File logFile = null;
        File analysisFile = null;
        if (args.length >= 1) {
            logFile = new File(args[0]);
        }
        if (args.length >= 2) {
            analysisFile = new File(args[1]);
        }
        if (logFile == null) {
            throw new Exception("illegal parameters");
        }
        
        if (analysisFile == null) {
            String fileName = logFile.getName();
            analysisFile =
                new File(logFile.getParentFile(), fileName.substring(0, fileName.lastIndexOf('.'))
                    + "_analysis.txt");
        }

        FileReader logReader = new FileReader(logFile);
        FileWriter analysisWriter = new FileWriter(analysisFile);
        new IntelligentOrderLogAnalyzer().analyze(logReader, analysisWriter);
        logReader.close();
        analysisWriter.close();
        
    }

    public void analyze(Reader logReader, Writer analysisWriter) throws Exception {
        BufferedReader bufReader = new BufferedReader(logReader);
        PrintWriter writer = new PrintWriter(analysisWriter);
        try {

            while (true) {
                AnalysisValue value = null;
                try {
                    //fetch begin and move to next line
                    value = parseIntelligentOrderBegin(bufReader);

                    if (value != null) {
                        //count orders and move to next line till find "Intelligent Order End" string
                        parseIntelligentOrderCount(bufReader, value);
                    }
                } catch (Exception e) {
                    break;
                } finally {
                    if (value != null) {
                        writer.println(value);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bufReader.close();
        }
    }

    private AnalysisValue parseIntelligentOrderBegin(BufferedReader bufReader) throws Exception {
        AnalysisValue value = null;
        String lineStr = bufReader.readLine();
        if (lineStr == null) {
            throw new Exception("EOF");
        }
        if (lineStr.indexOf("Intelligent Order Begin") != -1) {
            value = new AnalysisValue();
            value.beginTime = lineStr.substring(0, lineStr.indexOf("->"));
        }
        return value;
    }

    private AnalysisValue parseIntelligentOrderCount(BufferedReader bufReader, AnalysisValue value) throws Exception {
        String lineStr = bufReader.readLine();
        if (lineStr == null) {
            throw new Exception("EOF");
        }
        while (true) {
            if (lineStr.indexOf(">>>>>>batchNum") != -1) {
                value.endTime = lineStr.substring(0, lineStr.indexOf("->"));
                value.orderCount++;
            }

            lineStr = bufReader.readLine();
            if (lineStr == null) {
                throw new Exception("EOF");
            }
            if (lineStr.indexOf("Intelligent Order End") != -1) {
                value.endTime = lineStr.substring(0, lineStr.indexOf("->"));
                break;
            }
        }
        return value;
    }

    class AnalysisValue {
        String beginTime = null;
        String endTime = null;
        long duration = 0; //million seconds
        int orderCount = 0;

        public String toString() {
            try {
                duration = sdf.parse(endTime).getTime() - sdf.parse(beginTime).getTime();
            } catch (ParseException e) {
            }

            return beginTime + "," + duration + "," + orderCount;
        }
    }

}
