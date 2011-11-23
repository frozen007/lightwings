package org.lightwings.methodtimer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ExecutionRecordTransformer implements ClassFileTransformer {
    private HashMap<String, HashSet<String>> def = null;

    public ExecutionRecordTransformer(HashMap<String, HashSet<String>> def) {
        this.def = def;
    }

    public byte[] transform(ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.startsWith("org/lightwings/methodtimer/")) {
            return classfileBuffer;
        }

        if (!def.isEmpty() && !def.containsKey(className)) {
            return classfileBuffer;
        }

        HashSet<String> methodSet = def.get(className);

        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        AsmInfoClassVisitor infoVisitor = new AsmInfoClassVisitor(writer);

        reader.accept(infoVisitor, ClassReader.SKIP_DEBUG);

        writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ExecutionRecordClassVisitor executeRecordVisitor =
            new ExecutionRecordClassVisitor(infoVisitor.getAsmInfoMap(), writer);
        executeRecordVisitor.setMethodSet(methodSet);
        reader.accept(executeRecordVisitor, ClassReader.SKIP_DEBUG);
        return writer.toByteArray();
    }

}
