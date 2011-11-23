package org.lightwings.methodtimer;

public class MethodAsmInfo {
    int access;
    String name;
    String desc;
    String signature;
    String methodKey;
    int maxStack;
    int maxLocals;

    public MethodAsmInfo(int access, String name, String desc, String signature) {
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.methodKey = genKey(access, name, desc, signature);
    }

    public String getKey() {
        return methodKey;
    }

    public static String genKey(int access, String name, String desc, String signature) {
        return access + "^" + name + "^" + desc + "^" + signature;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSignature() {
        return signature;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int maxStack) {
        this.maxStack = maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public void setMaxLocals(int maxLocals) {
        this.maxLocals = maxLocals;
    }

}
