package org.lightwings.asm;

import java.util.HashMap;
import java.util.Set;

import org.objectweb.asm.MethodVisitor;

public class RegistrableMethodDedicator implements MethodDedicator {

    protected HashMap<ASMMethodInfo, MethodVisitorFactory> methodRegistry =
        new HashMap<ASMMethodInfo, MethodVisitorFactory>();

    @Override
    public MethodVisitor getMethodVisitor(int access,
        String name,
        String desc,
        String signature,
        MethodVisitor mv) {
        MethodVisitor dedicatedMv = mv;

        MethodVisitorFactory factory = match(access, name, desc, signature);
        if (factory != null) {
            dedicatedMv = factory.createMethodVisitor(dedicatedMv);
        }
        return dedicatedMv;
    }

    protected MethodVisitorFactory match(int access, String name, String desc, String signature) {

        Set<ASMMethodInfo> set = methodRegistry.keySet();
        for (ASMMethodInfo methodInfo : set) {
            if (name != null && !name.equals(methodInfo.name)) {
                continue;
            }

            if (methodInfo.desc !=null && !methodInfo.desc.equals(desc)) {
                continue;
            }

            if (access != methodInfo.access) {
                continue;
            }

            if (signature != null) {
                if (!signature.equals(methodInfo.signature)) {
                    continue;
                }
            }

            return methodRegistry.get(methodInfo);
        }

        return null;
    }

    public void registerMethod(MethodEntry methodEntry) {
        methodRegistry.put(methodEntry.methodInfo, methodEntry.mvf);
    }
}
