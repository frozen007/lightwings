package test.org.zmy.db;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DBValueAgent implements ClassFileTransformer {

	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new DBValueAgent());
	}

	class TestDBOperationClassVisitor extends ClassAdapter implements Opcodes {

		private String targetClass = null;
		private ClassLoader loader = null;

		public TestDBOperationClassVisitor(ClassLoader loader, ClassVisitor cv) {
			super(cv);
			this.loader = loader;
		}

		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			cv.visit(version, access, name, signature, superName, interfaces);
			if(name.indexOf("DBValueImpl")!=-1) {
				System.out.println("name2:" + name);
			}
			if (interfaces != null) {
				for (int i = 0; i < interfaces.length; i++) {
					if(interfaces[i].equals("test/org/zmy/db/DBValue")) {
						System.out.println("name:" + name);
						targetClass = name;
						cv.visitField(ACC_PUBLIC, "sql", "Ljava/lang/String;", null, null);
						break;
					}
				}
			}
		}

		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
			if (name.equals("getDBValue")) {
				mv = new DBOperationMethodVisitor(mv);
			}
			return mv;
		}

		class DBOperationMethodVisitor extends MethodAdapter implements Opcodes {

			public DBOperationMethodVisitor(MethodVisitor mv) {
				super(mv);
			}

			@Override
			public void visitInsn(int opcode) {
				if (opcode == ARETURN) {
					System.out.println("opcode:" + opcode);
					mv.visitInsn(DUP);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitFieldInsn(PUTFIELD, targetClass, "sql", "Ljava/lang/String;");
				}
				mv.visitInsn(opcode);
			}

			public void visitMethodInsn(int opcode,
                    String owner,
                    String name,
                    String desc) {
				try {
					String ownerClass = owner.replace('/', '.');
					Class c = loader.loadClass(ownerClass);
					System.out.println("load:" + c);
	                //Class.forName(ownerClass);
                } catch (ClassNotFoundException e) {
	                e.printStackTrace();
                }
				mv.visitMethodInsn(opcode, owner, name, desc);
			}
		}

	}

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
	        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		ClassReader reader = new ClassReader(classfileBuffer);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		TestDBOperationClassVisitor visitor = new TestDBOperationClassVisitor(loader, writer);

		reader.accept(visitor, ClassReader.SKIP_DEBUG);

		return writer.toByteArray();
	}
}
