/**
 * Project ZmyRepo
 * by zhao-mingyu at 2009-3-25
 *
 */
package org.zmy.db.proxy;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.HashSet;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * PreparedStatementInterceptor
 *
 */
public class PreparedStatementInterceptor implements MethodInterceptor {

    private PreparedStatement stdPs = null;

    private static HashSet methodToIntercepte = new HashSet();
    private static HashSet methodToDumpSql = new HashSet();

    static {
        methodToIntercepte.add("setDouble");
        methodToIntercepte.add("setInt");
        methodToIntercepte.add("setLong");
        methodToIntercepte.add("setShort");
        methodToIntercepte.add("setString");

        methodToDumpSql.add("executeUpdate");
        methodToDumpSql.add("executeQuery");
    }

    public PreparedStatementInterceptor(PreparedStatement ps) {
        stdPs = ps;
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {
        /*Use PreparedStatementCallbackFilter to implement the function below 
         //if the intercepted method is a member of LogPreparedStatement,
         //then we must invoke LogPreparedStatement object itself.
         if (method.getDeclaringClass().equals(LogPreparedStatement.class)) {
         //obj is a subclass of LogPreparedStatement, 
         //so we must use invokeSuper to call the actual method in LogPreparedStatement,
         //otherwise we will encounter stack overflow
         return proxy.invokeSuper(obj, args);
         }*/

        LogPreparedStatement lps = (LogPreparedStatement) obj;
        String methodName = method.getName();

        if (methodToIntercepte.contains(methodName)) {
            lps.addParameter(args[0], args[1]);
        }
        if (methodToDumpSql.contains(methodName)) {
            lps.dumpActualSql();
        }
        return proxy.invoke(stdPs, args);
    }
}
