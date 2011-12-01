package org.lightwings.methodtimer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public abstract class SimpleClassTransformer implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer) throws IllegalClassFormatException {

        return transformClass(className, classfileBuffer);
    }

    public abstract byte[] transformClass(String className, byte[] classfileBuffer);
}
