package org.lightwings.sqlrabbit;

import org.lightwings.asm.MethodVisitorFactory;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ConnectionProxyMethodFactory implements MethodVisitorFactory {

    @Override
    public MethodVisitor createMethodVisitor(MethodVisitor mv) {
        return new GetConnectionMethodVisitor(mv);
    }

    class GetConnectionMethodVisitor extends MethodAdapter implements Opcodes {

        public GetConnectionMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == ARETURN) {
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "org/lightwings/sqlrabbit/ConnectionProxy",
                    "createConnectionProxy",
                    "(Ljava/sql/Connection;)Ljava/sql/Connection;");
            }

            mv.visitInsn(opcode);
        }

    }
}
