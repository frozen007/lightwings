/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-1-6
 *
 */
package org.zmy.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * BeanUtils
 *
 */
public class BeanUtils {

    public static HashMap getJavaBeanProperties(Object bean, Class beanClass) {
        HashMap valueMap = new HashMap();
        if (!beanClass.isInstance(bean)) {
            return valueMap;
        }
        PropertyDescriptor propDesc = null;
        Field[] fields = beanClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                propDesc = new PropertyDescriptor(fields[i].getName(), beanClass);
            } catch (Exception e) {
            }
            Method readMethod = propDesc.getReadMethod();
            try {
                Object propValue = null;
                propValue = readMethod.invoke(bean, new Object[] {});
                valueMap.put(fields[i].getName(), propValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return valueMap;
    }

    public static String printMap(Map map) {
        StringBuffer buf = new StringBuffer();
        Iterator itKey = map.keySet().iterator();
        while (itKey.hasNext()) {
            String key = (String) itKey.next();
            buf.append(key + "=" + map.get(key) + ",");
        }
        return buf.toString();
    }
}
