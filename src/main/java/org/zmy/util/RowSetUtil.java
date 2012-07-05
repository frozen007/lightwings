/**
 * Project StudentPro
 * by zhao-mingyu at 2008-3-4
 * 
 */
package org.zmy.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p><code>RowSetUtil</code></p>
 * <p>An implementation of composing a rowset from a ResultSet</p>
 */
public class RowSetUtil {

    /**
     * <p>getRowsFromResultSet</p>
     * @param rs the ResultSet
     * @return a list of row composed in the form of HashMap
     */
    public static List<HashMap> getRowsFromResultSet(ResultSet rs) {
        List<HashMap> list = new ArrayList<HashMap>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            String[] colNames = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                colNames[i] = rsmd.getColumnName(i + 1).toUpperCase();
            }
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < colNames.length; i++) {
                    map.put(colNames[i], rs.getObject(i + 1));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            list = null;
        }
        return list;
    }

    public static StringBuilder rowSetPrint(List<HashMap> rowlist) {
        String[] keys = null;
        if (rowlist.size() > 0) {
            keys = (String[]) rowlist.get(0).keySet().toArray();
        } else {
            return null;
        }
        return rowSetPrint(keys, keys, rowlist);
    }

    public static StringBuilder rowSetPrint(String[] columnNames, String[] keyNames,
            List<HashMap> rowlist) {
        StringBuilder sbuilder = new StringBuilder();
        if (columnNames.length != keyNames.length) {
            return sbuilder;
        }

        int[] columnSizes = new int[columnNames.length];
        Iterator<HashMap> itrow = null;
        for (int i = 0; i < keyNames.length; i++) {
            itrow = rowlist.iterator();
            int maxSize = 0;
            while (itrow.hasNext()) {
                HashMap tmpMap = itrow.next();
                int tmpSize = tmpMap.get(keyNames[i].toUpperCase()).toString().getBytes().length;
                if (tmpSize > maxSize) {
                    maxSize = tmpSize;
                }
            }
            int tmpSize2 = columnNames[i].getBytes().length;
            if (tmpSize2 > maxSize) {
                maxSize = tmpSize2;
            }
            columnSizes[i] = maxSize;
        }

        for (int i = 0; i < columnNames.length; i++) {
            sbuilder.append(StringUtil.rightPadding(columnNames[i], ' ', columnSizes[i]));
            sbuilder.append('|');
        }
        sbuilder.append('\n');

        itrow = rowlist.iterator();
        while (itrow.hasNext()) {
            HashMap map = itrow.next();
            for (int i = 0; i < keyNames.length; i++) {
                String buf = map.get(keyNames[i].toUpperCase()).toString();
                if (StringUtil.isDigitValue(buf)) {
                    sbuilder.append(StringUtil.leftPadding(buf, ' ', columnSizes[i]));
                } else {
                    sbuilder.append(StringUtil.rightPadding(buf, ' ', columnSizes[i]));
                }
                sbuilder.append('|');
            }
            sbuilder.append('\n');
        }
        return sbuilder;
    }

}