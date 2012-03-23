package org.lightwings.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

public class ClassMaker extends ClassAdapter implements Opcodes {

    protected Class interfaceClazz = null;
    protected String interfaceName = null; //Internal name

    protected String className = null;

    protected Class proxyClazz = null;
    protected String proxyClassName = null; //Internal name

    private static final String FIELD_NAME_PROXY = "proxyObject";

    public ClassMaker(ClassVisitor cv, Class interfaceClazz, Class proxyClazz) {
        super(cv);
        this.interfaceClazz = interfaceClazz;
        this.proxyClazz = proxyClazz;

        interfaceName = Type.getInternalName(interfaceClazz);
        className = interfaceName + "Impl";

        proxyClassName = Type.getInternalName(proxyClazz);
    }

    public void visit(int version,
        int access,
        String name,
        String signature,
        String superName,
        String[] interfaces) {
        String[] newInterfaces = new String[]{interfaceName};
        super.visit(version, ACC_PUBLIC, className, signature, superName, newInterfaces);
    }

    public void visitSource(String source, String debug) {

        String proxyClassInstance = getIntanceString(proxyClassName);

        //create a proxy field
        FieldVisitor fv =
            cv.visitField(ACC_PUBLIC, FIELD_NAME_PROXY, proxyClassInstance, null, null);
        fv.visitEnd();

        //create a constructor with a parameter of proxyClass instance
        MethodVisitor mv =
            super.visitMethod(ACC_PUBLIC, "<init>", "(" + proxyClassInstance + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, className, FIELD_NAME_PROXY, proxyClassInstance);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        super.visitSource(source, debug);
    }

    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {

        String proxyClassInstance = getIntanceString(proxyClassName);

        //Create every method in the interface
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, FIELD_NAME_PROXY, proxyClassInstance);

        //Every invocation to this generated method is delegated to proxyObject
        Method method = new Method(name, desc);
        Type[] types = method.getArgumentTypes();
        for (int i = 1; i <= types.length; i++) {
            mv.visitVarInsn(getXLoadOpcode(types[i - 1]), i);
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, proxyClassName, name, desc);
        mv.visitInsn(getXReturnOpcode(method.getReturnType()));
        mv.visitMaxs(types.length + 1, types.length);
        mv.visitEnd();
        return mv;
    }

    private static int[] loadOpcodeArray = new int[11];
    private static int[] returnOpcodeArray = new int[11];
    static {
        //Initialize load opcodes for all types
        loadOpcodeArray[Type.INT] = ILOAD;
        loadOpcodeArray[Type.BOOLEAN] = loadOpcodeArray[Type.INT];
        loadOpcodeArray[Type.BYTE] = loadOpcodeArray[Type.INT];
        loadOpcodeArray[Type.CHAR] = loadOpcodeArray[Type.INT];
        loadOpcodeArray[Type.SHORT] = loadOpcodeArray[Type.INT];

        loadOpcodeArray[Type.LONG] = LLOAD;
        loadOpcodeArray[Type.FLOAT] = FLOAD;
        loadOpcodeArray[Type.DOUBLE] = DLOAD;

        loadOpcodeArray[Type.OBJECT] = ALOAD;
        loadOpcodeArray[Type.ARRAY] = ALOAD;

        //Initialize return opcodes for all types
        returnOpcodeArray[Type.INT] = IRETURN;
        returnOpcodeArray[Type.BOOLEAN] = returnOpcodeArray[Type.INT];
        returnOpcodeArray[Type.BYTE] = returnOpcodeArray[Type.INT];
        returnOpcodeArray[Type.CHAR] = returnOpcodeArray[Type.INT];
        returnOpcodeArray[Type.SHORT] = returnOpcodeArray[Type.INT];

        returnOpcodeArray[Type.LONG] = LRETURN;
        returnOpcodeArray[Type.FLOAT] = FRETURN;
        returnOpcodeArray[Type.DOUBLE] = DRETURN;

        returnOpcodeArray[Type.OBJECT] = ARETURN;
        returnOpcodeArray[Type.ARRAY] = ARETURN;
        returnOpcodeArray[Type.VOID] = RETURN;
    }

    public static int getXLoadOpcode(Type type) {
        try{
            return loadOpcodeArray[type.getSort()];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("do not support parameter type " + type);
        }
    }

    public static int getXReturnOpcode(Type type) {
        try{
            return returnOpcodeArray[type.getSort()];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("do not support return type " + type);
        }
    }

    public static String getIntanceString(String className) {
        return "L" + className + ";";
    }

    public static Class makeClass(Class interfaceClazz, Class proxyClazz) throws Exception {
        Class clazz = null;
        ClassReader cr = new ClassReader(interfaceClazz.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassMaker maker = new ClassMaker(cw, interfaceClazz, proxyClazz);
        cr.accept(maker, 0);
        ByteCodeClassLoader loader = new ByteCodeClassLoader();
        byte[] classbyte = cw.toByteArray();
        clazz = loader.load(maker.className.replace('/', '.'), classbyte);
        return clazz;
    }
}
