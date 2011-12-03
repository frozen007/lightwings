package org.lightwings.methodtimer;

import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ExecutionRecordTransformer extends SimpleClassTransformer {
    private HashMap<String, HashSet<String>> def = null;

    public ExecutionRecordTransformer(HashMap<String, HashSet<String>> def) {
        this.def = def;
    }

    public byte[] transformClass(String className, byte[] classfileBuffer) {
        if (def.isEmpty()) {
            return null;
        }

        HashSet<String> methodSet = def.get(className);
        if (methodSet == null) {
            return null;
        }
        System.out.println("class " + className + " ripped.");

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