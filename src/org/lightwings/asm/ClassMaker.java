package org.lightwings.asm;

import java.lang.reflect.Constructor;
import java.util.HashSet;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
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
    protected HashSet<Method> proxyClassMethodSet = new HashSet<Method>();

    protected Class innovatedClazz = null;
    protected String innovatedClassName = null; //Internal name
    private static final String FIELD_NAME_INNOVATED = "innovatedObject";

    public ClassMaker(ClassVisitor cv, Class interfaceClazz, Class proxyClazz) {
        super(cv);
        this.interfaceClazz = interfaceClazz;
        this.proxyClazz = proxyClazz;
        this.innovatedClazz = interfaceClazz;

        interfaceName = Type.getInternalName(interfaceClazz);
        className =
            "org/lightwings/db"
                + interfaceName.substring(interfaceName.lastIndexOf('/'))
                + "ImplBy_org_lightwings_asm_ClassMaker";

        proxyClassName = Type.getInternalName(proxyClazz);
        innovatedClassName = Type.getInternalName(innovatedClazz);

        java.lang.reflect.Method[] reflectMethods = proxyClazz.getDeclaredMethods();
        for (int i = 0; i < reflectMethods.length; i++) {
            proxyClassMethodSet.add(Method.getMethod(reflectMethods[i]));
        }
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

        super.visitSource(source, debug);
        String innovatedClassInstance = getIntanceString(innovatedClassName);

        String proxyClassInstance = getIntanceString(proxyClassName);

        //create a innovated field
        cv.visitField(ACC_PUBLIC, FIELD_NAME_INNOVATED, innovatedClassInstance, null, null)
            .visitEnd();

        //create a proxy field
        cv.visitField(ACC_PUBLIC, FIELD_NAME_PROXY, proxyClassInstance, null, null).visitEnd();

        //create a constructor with a parameter of proxyClass instance
        MethodVisitor mv =
            super.visitMethod(ACC_PUBLIC, "<init>", "("
                + innovatedClassInstance
                + proxyClassInstance
                + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, className, FIELD_NAME_INNOVATED, innovatedClassInstance);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitFieldInsn(PUTFIELD, className, FIELD_NAME_PROXY, proxyClassInstance);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    public MethodVisitor visitMethod(int access,
        String name,
        String desc,
        String signature,
        String[] exceptions) {

        //Create every method in the interface
        MethodVisitor mv = super.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);
        Method method = new Method(name, desc);
        Type[] types = method.getArgumentTypes();
        mv.visitCode();

        if (this.proxyClassMethodSet.contains(method)) {
            //if method exists in proxyClass 
            //every invocation to this generated method is delegated to proxyObject
            String proxyClassInstance = getIntanceString(proxyClassName);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, FIELD_NAME_PROXY, proxyClassInstance);
            for (int i = 1; i <= types.length; i++) {
                mv.visitVarInsn(getXLoadOpcode(types[i - 1]), i);
            }
            mv.visitMethodInsn(INVOKEINTERFACE, proxyClassName, name, desc);
        }

        //then try to invoke the method in the innovatedClass
        String innovatedClassInstance = getIntanceString(innovatedClassName);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, className, FIELD_NAME_INNOVATED, innovatedClassInstance);
        for (int i = 1; i <= types.length; i++) {
            mv.visitVarInsn(getXLoadOpcode(types[i - 1]), i);
        }
        mv.visitMethodInsn(INVOKEINTERFACE, innovatedClassName, name, desc);

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
        try {
            return loadOpcodeArray[type.getSort()];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("do not support parameter type " + type);
        }
    }

    public static int getXReturnOpcode(Type type) {
        try {
            return returnOpcodeArray[type.getSort()];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("do not support return type " + type);
        }
    }

    public static String getIntanceString(String className) {
        return "L" + className + ";";
    }

    public static Class makeClass(Class interfaceClazz, Class proxyClazz) throws Exception {
        if (!interfaceClazz.isInterface()) {
            throw new IllegalArgumentException(interfaceClazz + " should be interface");
        }

        if (!proxyClazz.isInterface()) {
            throw new IllegalArgumentException(proxyClazz + " should be interface");
        }

        Class clazz = null;
        ClassReader cr = new ClassReader(interfaceClazz.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassMaker maker = new ClassMaker(cw, interfaceClazz, proxyClazz);
        cr.accept(maker, 0);
        ByteCodeClassLoader loader = new ByteCodeClassLoader();
        byte[] classbyte = cw.toByteArray();
        //ByteUtil.dumpBytesToFile("MakerTestInterfaceImplByZmy.class", classbyte);
        clazz = loader.load(maker.className.replace('/', '.'), classbyte);
        return clazz;
    }

    public static Object newInstance(Class interfaceClazz, Class proxyClazz, Object to, Object po) throws Exception {
        Class clazz = ClassMaker.makeClass(interfaceClazz, proxyClazz);
        Constructor con = clazz.getConstructor(interfaceClazz, proxyClazz);
        return con.newInstance(to, po);
    }
}
