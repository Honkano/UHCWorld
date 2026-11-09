/*
 * Decompiled with CFR 0.152.
 */
package com.thoughtworks.paranamer;

import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class DefaultParanamer
implements Paranamer {
    private static final String COMMA = ",";
    private static final String SPACE = " ";
    public static final String __PARANAMER_DATA = "v1.0 \nlookupParameterNames java.lang.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \ngetParameterTypeName java.lang.Class cls\n";

    @Override
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return this.lookupParameterNames(methodOrConstructor, true);
    }

    @Override
    public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
        Class<?>[] types = null;
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
        String parameterTypeNames = DefaultParanamer.getParameterTypeNamesCSV(types);
        String[] names = DefaultParanamer.getParameterNames(declaringClass, parameterTypeNames, name + SPACE);
        if (names == null) {
            if (throwExceptionIfMissing) {
                throw new ParameterNamesNotFoundException("No parameter names found for class '" + declaringClass + "', methodOrCtor " + name + " and parameter types " + parameterTypeNames);
            }
            return Paranamer.EMPTY_NAMES;
        }
        return names;
    }

    private static String[] getParameterNames(Class<?> declaringClass, String parameterTypes, String prefix) {
        String data = DefaultParanamer.getParameterListResource(declaringClass);
        String line = DefaultParanamer.findFirstMatchingLine(data, prefix + parameterTypes + SPACE);
        String[] parts = line.split(SPACE);
        if (parts.length == 3 && parts[1].equals(parameterTypes)) {
            String parameterNames = parts[2];
            return parameterNames.split(COMMA);
        }
        return Paranamer.EMPTY_NAMES;
    }

    static String getParameterTypeNamesCSV(Class<?>[] parameterTypes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < parameterTypes.length; ++i) {
            sb.append(DefaultParanamer.getParameterTypeName(parameterTypes[i]));
            if (i >= parameterTypes.length - 1) continue;
            sb.append(COMMA);
        }
        return sb.toString();
    }

    private static String getParameterListResource(Class<?> declaringClass) {
        try {
            Field field = declaringClass.getDeclaredField("__PARANAMER_DATA");
            if (!Modifier.isStatic(field.getModifiers()) || !field.getType().equals(String.class)) {
                return null;
            }
            return (String)field.get(null);
        }
        catch (NoSuchFieldException e) {
            return null;
        }
        catch (IllegalAccessException e) {
            return null;
        }
    }

    private static String findFirstMatchingLine(String data, String prefix) {
        int iy;
        if (data == null) {
            return "";
        }
        int ix = data.indexOf(prefix);
        if (ix >= 0 && (iy = data.indexOf("\n", ix)) > 0) {
            return data.substring(ix, iy);
        }
        return "";
    }

    private static String getParameterTypeName(Class<?> cls) {
        String parameterTypeNameName = cls.getName();
        parameterTypeNameName = parameterTypeNameName.replace("[J", "[Xlong").replace("[I", "[Xint").replace("[Z", "[Xboolean").replace("[S", "[Xshort").replace("[F", "[Xfloat").replace("[D", "[Xdouble").replace("[B", "[Xbyte").replace("[C", "[Xchar");
        int arrayNestingDepth = 0;
        int ix = parameterTypeNameName.indexOf("[");
        while (ix > -1) {
            ++arrayNestingDepth;
            parameterTypeNameName = parameterTypeNameName.replaceFirst("(\\[\\w)|(\\[)", "");
            ix = parameterTypeNameName.indexOf("[");
        }
        parameterTypeNameName = parameterTypeNameName.replaceFirst(";", "");
        for (int k = 0; k < arrayNestingDepth; ++k) {
            parameterTypeNameName = parameterTypeNameName + "[]";
        }
        return parameterTypeNameName;
    }
}

