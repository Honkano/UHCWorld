/*
 * Decompiled with CFR 0.152.
 */
package com.thoughtworks.paranamer;

import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class BytecodeReadingParanamer
implements Paranamer {
    private static final Map<String, String> primitives = new HashMap<String, String>(){
        {
            this.put("int", "I");
            this.put("boolean", "Z");
            this.put("byte", "B");
            this.put("char", "C");
            this.put("short", "S");
            this.put("float", "F");
            this.put("long", "J");
            this.put("double", "D");
        }
    };
    public static final String __PARANAMER_DATA = "lookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";

    @Override
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return this.lookupParameterNames(methodOrConstructor, true);
    }

    @Override
    public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
        Class[] types = null;
        Class<Object> declaringClass = null;
        String name = null;
        if (methodOrCtor instanceof Method) {
            Method method = (Method)methodOrCtor;
            types = method.getParameterTypes();
            name = method.getName();
            declaringClass = method.getDeclaringClass();
        } else {
            Constructor constructor = (Constructor)methodOrCtor;
            types = constructor.getParameterTypes();
            declaringClass = constructor.getDeclaringClass();
            name = "<init>";
        }
        if (types.length == 0) {
            return EMPTY_NAMES;
        }
        InputStream byteCodeStream = this.getClassAsStream(declaringClass);
        if (byteCodeStream == null) {
            if (throwExceptionIfMissing) {
                throw new ParameterNamesNotFoundException("Unable to get class bytes");
            }
            return Paranamer.EMPTY_NAMES;
        }
        try {
            ClassReader reader = new ClassReader(byteCodeStream);
            TypeCollector visitor = new TypeCollector(name, types, throwExceptionIfMissing);
            reader.accept(visitor);
            String[] parameterNamesForMethod = visitor.getParameterNamesForMethod();
            try {
                byteCodeStream.close();
            }
            catch (IOException e) {
                // empty catch block
            }
            return parameterNamesForMethod;
        }
        catch (IOException e) {
            if (throwExceptionIfMissing) {
                throw new ParameterNamesNotFoundException("IoException while reading class bytes", e);
            }
            return Paranamer.EMPTY_NAMES;
        }
    }

    private InputStream getClassAsStream(Class<?> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        return this.getClassAsStream(classLoader, clazz.getName());
    }

    private InputStream getClassAsStream(ClassLoader classLoader, String className) {
        String name = className.replace('.', '/') + ".class";
        InputStream asStream = classLoader.getResourceAsStream(name);
        if (asStream == null) {
            asStream = BytecodeReadingParanamer.class.getResourceAsStream(name);
        }
        return asStream;
    }

    private static class Type {
        private static final int VOID = 0;
        private static final int BOOLEAN = 1;
        private static final int CHAR = 2;
        private static final int BYTE = 3;
        private static final int SHORT = 4;
        private static final int INT = 5;
        private static final int FLOAT = 6;
        private static final int LONG = 7;
        private static final int DOUBLE = 8;
        private static final int ARRAY = 9;
        private static final int OBJECT = 10;
        private static final Type VOID_TYPE = new Type(0, null, 0x56050000, 1);
        private static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
        private static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
        private static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
        private static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
        private static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
        private static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
        private static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
        private static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
        private final int sort;
        private char[] buf;
        private int off;
        private final int len;
        public static final String __PARANAMER_DATA = "";

        private Type(int sort) {
            this.sort = sort;
            this.len = 1;
        }

        private Type(int sort, char[] buf, int off, int len) {
            this.sort = sort;
            this.buf = buf;
            this.off = off;
            this.len = len;
        }

        private static Type[] getArgumentTypes(String methodDescriptor) {
            char car;
            char[] buf = methodDescriptor.toCharArray();
            int off = 1;
            int size = 0;
            while ((car = buf[off++]) != ')') {
                if (car == 'L') {
                    while (buf[off++] != ';') {
                    }
                    ++size;
                    continue;
                }
                if (car == '[') continue;
                ++size;
            }
            Type[] args = new Type[size];
            off = 1;
            size = 0;
            while (buf[off] != ')') {
                args[size] = Type.getType(buf, off);
                off += args[size].len + (args[size].sort == 10 ? 2 : 0);
                ++size;
            }
            return args;
        }

        private static Type getType(char[] buf, int off) {
            switch (buf[off]) {
                case 'V': {
                    return VOID_TYPE;
                }
                case 'Z': {
                    return BOOLEAN_TYPE;
                }
                case 'C': {
                    return CHAR_TYPE;
                }
                case 'B': {
                    return BYTE_TYPE;
                }
                case 'S': {
                    return SHORT_TYPE;
                }
                case 'I': {
                    return INT_TYPE;
                }
                case 'F': {
                    return FLOAT_TYPE;
                }
                case 'J': {
                    return LONG_TYPE;
                }
                case 'D': {
                    return DOUBLE_TYPE;
                }
                case '[': {
                    int len = 1;
                    while (buf[off + len] == '[') {
                        ++len;
                    }
                    if (buf[off + len] == 'L') {
                        ++len;
                        while (buf[off + len] != ';') {
                            ++len;
                        }
                    }
                    return new Type(9, buf, off, len + 1);
                }
            }
            int len = 1;
            while (buf[off + len] != ';') {
                ++len;
            }
            return new Type(10, buf, off + 1, len - 1);
        }

        private int getDimensions() {
            int i = 1;
            while (this.buf[this.off + i] == '[') {
                ++i;
            }
            return i;
        }

        private Type getElementType() {
            return Type.getType(this.buf, this.off + this.getDimensions());
        }

        private String getClassName() {
            switch (this.sort) {
                case 0: {
                    return "void";
                }
                case 1: {
                    return "boolean";
                }
                case 2: {
                    return "char";
                }
                case 3: {
                    return "byte";
                }
                case 4: {
                    return "short";
                }
                case 5: {
                    return "int";
                }
                case 6: {
                    return "float";
                }
                case 7: {
                    return "long";
                }
                case 8: {
                    return "double";
                }
                case 9: {
                    StringBuffer b = new StringBuffer(this.getElementType().getClassName());
                    for (int i = this.getDimensions(); i > 0; --i) {
                        b.append("[]");
                    }
                    return b.toString();
                }
            }
            return new String(this.buf, this.off, this.len).replace('/', '.');
        }
    }

    private static class ClassReader {
        public final byte[] b;
        private final int[] items;
        private final String[] strings;
        private final int maxStringLength;
        public final int header;
        static final int FIELD = 9;
        static final int METH = 10;
        static final int IMETH = 11;
        static final int INT = 3;
        static final int FLOAT = 4;
        static final int LONG = 5;
        static final int DOUBLE = 6;
        static final int NAME_TYPE = 12;
        static final int UTF8 = 1;
        public static final String __PARANAMER_DATA = "";

        private ClassReader(byte[] b) {
            this(b, 0);
        }

        private ClassReader(byte[] b, int off) {
            this.b = b;
            this.items = new int[this.readUnsignedShort(off + 8)];
            int n = this.items.length;
            this.strings = new String[n];
            int max = 0;
            int index = off + 10;
            for (int i = 1; i < n; ++i) {
                int size;
                this.items[i] = index + 1;
                switch (b[index]) {
                    case 3: 
                    case 4: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: {
                        size = 5;
                        break;
                    }
                    case 5: 
                    case 6: {
                        size = 9;
                        ++i;
                        break;
                    }
                    case 1: {
                        size = 3 + this.readUnsignedShort(index + 1);
                        if (size <= max) break;
                        max = size;
                        break;
                    }
                    default: {
                        size = 3;
                    }
                }
                index += size;
            }
            this.maxStringLength = max;
            this.header = index;
        }

        private ClassReader(InputStream is) throws IOException {
            this(ClassReader.readClass(is));
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private static byte[] readClass(InputStream is) throws IOException {
            if (is == null) {
                throw new IOException("Class not found");
            }
            try {
                byte[] b = new byte[is.available()];
                int len = 0;
                while (true) {
                    int n;
                    if ((n = is.read(b, len, b.length - len)) == -1) {
                        byte[] c;
                        if (len < b.length) {
                            c = new byte[len];
                            System.arraycopy(b, 0, c, 0, len);
                            b = c;
                        }
                        c = b;
                        return c;
                    }
                    if ((len += n) != b.length) continue;
                    int last = is.read();
                    if (last < 0) {
                        byte[] ex = b;
                        return ex;
                    }
                    byte[] c = new byte[b.length + 1000];
                    System.arraycopy(b, 0, c, 0, len);
                    c[len++] = (byte)last;
                    b = c;
                }
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException ex) {}
            }
        }

        private void accept(TypeCollector classVisitor) {
            int j;
            int i;
            char[] c = new char[this.maxStringLength];
            boolean anns = false;
            boolean ianns = false;
            int u = this.header;
            int v = this.items[this.readUnsignedShort(u + 4)];
            int len = this.readUnsignedShort(u + 6);
            boolean w = false;
            u += 8;
            for (i = 0; i < len; ++i) {
                u += 2;
            }
            v = u;
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                j = this.readUnsignedShort(v + 6);
                v += 8;
                while (j > 0) {
                    v += 6 + this.readInt(v + 2);
                    --j;
                }
                --i;
            }
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                j = this.readUnsignedShort(v + 6);
                v += 8;
                while (j > 0) {
                    v += 6 + this.readInt(v + 2);
                    --j;
                }
                --i;
            }
            i = this.readUnsignedShort(v);
            v += 2;
            while (i > 0) {
                v += 6 + this.readInt(v + 2);
                --i;
            }
            i = this.readUnsignedShort(u);
            u += 2;
            while (i > 0) {
                j = this.readUnsignedShort(u + 6);
                u += 8;
                while (j > 0) {
                    u += 6 + this.readInt(u + 2);
                    --j;
                }
                --i;
            }
            i = this.readUnsignedShort(u);
            u += 2;
            while (i > 0) {
                u = this.readMethod(classVisitor, c, u);
                --i;
            }
        }

        private int readMethod(TypeCollector classVisitor, char[] c, int u) {
            String attrName;
            int access = this.readUnsignedShort(u);
            String name = this.readUTF8(u + 2, c);
            String desc = this.readUTF8(u + 4, c);
            int v = 0;
            int w = 0;
            int j = this.readUnsignedShort(u + 6);
            u += 8;
            while (j > 0) {
                attrName = this.readUTF8(u, c);
                int attrSize = this.readInt(u + 2);
                u += 6;
                if (attrName.equals("Code")) {
                    v = u;
                }
                u += attrSize;
                --j;
            }
            if (w != 0) {
                w += 2;
                for (j = 0; j < this.readUnsignedShort(w); ++j) {
                    w += 2;
                }
            }
            MethodCollector mv = classVisitor.visitMethod(access, name, desc);
            if (mv != null && v != 0) {
                int codeEnd;
                int codeLength = this.readInt(v + 4);
                int codeStart = v += 8;
                v = codeEnd = v + codeLength;
                j = this.readUnsignedShort(v);
                v += 2;
                while (j > 0) {
                    v += 8;
                    --j;
                }
                int varTable = 0;
                int varTypeTable = 0;
                j = this.readUnsignedShort(v);
                v += 2;
                while (j > 0) {
                    attrName = this.readUTF8(v, c);
                    if (attrName.equals("LocalVariableTable")) {
                        varTable = v + 6;
                    } else if (attrName.equals("LocalVariableTypeTable")) {
                        varTypeTable = v + 6;
                    }
                    v += 6 + this.readInt(v + 2);
                    --j;
                }
                v = codeStart;
                if (varTable != 0) {
                    int k;
                    if (varTypeTable != 0) {
                        k = this.readUnsignedShort(varTypeTable) * 3;
                        w = varTypeTable + 2;
                        int[] typeTable = new int[k];
                        while (k > 0) {
                            typeTable[--k] = w + 6;
                            typeTable[--k] = this.readUnsignedShort(w + 8);
                            typeTable[--k] = this.readUnsignedShort(w);
                            w += 10;
                        }
                    }
                    w = varTable + 2;
                    for (k = this.readUnsignedShort(varTable); k > 0; --k) {
                        int index = this.readUnsignedShort(w + 8);
                        mv.visitLocalVariable(this.readUTF8(w + 4, c), index);
                        w += 10;
                    }
                }
            }
            return u;
        }

        private int readUnsignedShort(int index) {
            byte[] b = this.b;
            return (b[index] & 0xFF) << 8 | b[index + 1] & 0xFF;
        }

        private int readInt(int index) {
            byte[] b = this.b;
            return (b[index] & 0xFF) << 24 | (b[index + 1] & 0xFF) << 16 | (b[index + 2] & 0xFF) << 8 | b[index + 3] & 0xFF;
        }

        private String readUTF8(int index, char[] buf) {
            int item = this.readUnsignedShort(index);
            String s = this.strings[item];
            if (s != null) {
                return s;
            }
            index = this.items[item];
            this.strings[item] = this.readUTF(index + 2, this.readUnsignedShort(index), buf);
            return this.strings[item];
        }

        private String readUTF(int index, int utfLen, char[] buf) {
            int endIndex = index + utfLen;
            byte[] b = this.b;
            int strLen = 0;
            int st = 0;
            int cc = 0;
            while (index < endIndex) {
                int c = b[index++];
                switch (st) {
                    case 0: {
                        if ((c &= 0xFF) < 128) {
                            buf[strLen++] = (char)c;
                            break;
                        }
                        if (c < 224 && c > 191) {
                            cc = (char)(c & 0x1F);
                            st = 1;
                            break;
                        }
                        cc = (char)(c & 0xF);
                        st = 2;
                        break;
                    }
                    case 1: {
                        buf[strLen++] = (char)(cc << 6 | c & 0x3F);
                        st = 0;
                        break;
                    }
                    case 2: {
                        cc = (char)(cc << 6 | c & 0x3F);
                        st = 1;
                    }
                }
            }
            return new String(buf, 0, strLen);
        }
    }

    private static class MethodCollector {
        private final int paramCount;
        private final int ignoreCount;
        private int currentParameter;
        private final StringBuffer result;
        private boolean debugInfoPresent;
        public static final String __PARANAMER_DATA = "visitLocalVariable java.lang.String,int name,index \n";

        private MethodCollector(int ignoreCount, int paramCount) {
            this.ignoreCount = ignoreCount;
            this.paramCount = paramCount;
            this.result = new StringBuffer();
            this.currentParameter = 0;
            this.debugInfoPresent = paramCount == 0;
        }

        public void visitLocalVariable(String name, int index) {
            if (index >= this.ignoreCount && index < this.ignoreCount + this.paramCount) {
                if (!name.equals("arg" + this.currentParameter)) {
                    this.debugInfoPresent = true;
                }
                this.result.append(',');
                this.result.append(name);
                ++this.currentParameter;
            }
        }

        private String getResult() {
            return this.result.length() != 0 ? this.result.substring(1) : "";
        }

        private boolean isDebugInfoPresent() {
            return this.debugInfoPresent;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class TypeCollector {
        private static final String COMMA = ",";
        private final String methodName;
        private final Class<?>[] parameterTypes;
        private final boolean throwExceptionIfMissing;
        private MethodCollector collector;
        public static final String __PARANAMER_DATA = "visitMethod int,java.lang.String,java.lang.String access,name,desc \n";

        private TypeCollector(String methodName, Class<?>[] parameterTypes, boolean throwExceptionIfMissing) {
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.throwExceptionIfMissing = throwExceptionIfMissing;
            this.collector = null;
        }

        public MethodCollector visitMethod(int access, String name, String desc) {
            if (this.collector != null) {
                return null;
            }
            if (!name.equals(this.methodName)) {
                return null;
            }
            Type[] argumentTypes = Type.getArgumentTypes(desc);
            int longOrDoubleQuantity = 0;
            for (Type t : argumentTypes) {
                if (!t.getClassName().equals("long") && !t.getClassName().equals("double")) continue;
                ++longOrDoubleQuantity;
            }
            int paramCount = argumentTypes.length;
            if (paramCount != this.parameterTypes.length) {
                return null;
            }
            for (int i = 0; i < argumentTypes.length; ++i) {
                if (this.correctTypeName(argumentTypes, i).equals(this.parameterTypes[i].getName())) continue;
                return null;
            }
            this.collector = new MethodCollector(Modifier.isStatic(access) ? 0 : 1, argumentTypes.length + longOrDoubleQuantity);
            return this.collector;
        }

        private String correctTypeName(Type[] argumentTypes, int i) {
            String s = argumentTypes[i].getClassName();
            String braces = "";
            while (s.endsWith("[]")) {
                braces = braces + "[";
                s = s.substring(0, s.length() - 2);
            }
            if (!braces.equals("")) {
                s = primitives.containsKey(s) ? braces + (String)primitives.get(s) : braces + "L" + s + ";";
            }
            return s;
        }

        private String[] getParameterNamesForMethod() {
            if (this.collector == null) {
                return Paranamer.EMPTY_NAMES;
            }
            if (!this.collector.isDebugInfoPresent()) {
                if (this.throwExceptionIfMissing) {
                    throw new ParameterNamesNotFoundException("Parameter names not found for " + this.methodName);
                }
                return Paranamer.EMPTY_NAMES;
            }
            return this.collector.getResult().split(COMMA);
        }
    }
}

