package org.lightwings.methodtimer;

import java.util.HashMap;
import java.util.HashSet;

import org.lightwings.asm.ASMClassInnovator;
import org.lightwings.asm.ASMInfoClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ExecutionRecordInnovator extends ASMClassInnovator {
    private HashMap<String, HashSet<String>> def = null;

    public ExecutionRecordInnovator(HashMap<String, HashSet<String>> def) {
        this.def = def;
    }

    @Override
    public boolean isInnovate(String className) {
        if (def.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    protected byte[] transformClass(String className, ClassReader reader, ClassWriter writer) {
        HashSet<String> methodSet = def.get(className);
        if (methodSet == null) {
            return null;
        }

        ASMInfoClassVisitor infoVisitor = new ASMInfoClassVisitor(writer);

        reader.accept(infoVisitor, ClassReader.SKIP_DEBUG);

        writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ExecutionRecordClassVisitor executeRecordVisitor =
            new ExecutionRecordClassVisitor(infoVisitor.getAsmMethodInfoMap(), writer);
        executeRecordVisitor.setMethodSet(methodSet);
        reader.accept(executeRecordVisitor, ClassReader.SKIP_DEBUG);

        return writer.toByteArray();
    }
}
