/*
 * Decompiled with CFR 0.152.
 */
package com.thoughtworks.paranamer;

import com.thoughtworks.paranamer.DefaultParanamer;
import com.thoughtworks.paranamer.NullParanamer;
import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.inject.Named;

public class AnnotationParanamer
implements Paranamer {
    public static final String __PARANAMER_DATA = "v1.0 \nlookupParameterNames java.lang.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";
    private final Paranamer fallback;

    public AnnotationParanamer() {
        this(new NullParanamer());
    }

    public AnnotationParanamer(Paranamer fallback) {
        this.fallback = fallback;
    }

    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return this.lookupParameterNames(methodOrConstructor, true);
    }

    public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
        Class<?>[] types = null;
        Class<Object> declaringClass = null;
        String name = null;
        Annotation[][] anns = null;
        if (methodOrCtor instanceof Method) {
            Method method = (Method)methodOrCtor;
            types = method.getParameterTypes();
            name = method.getName();
            declaringClass = method.getDeclaringClass();
            anns = method.getParameterAnnotations();
        } else {
            Constructor constructor = (Constructor)methodOrCtor;
            types = constructor.getParameterTypes();
            declaringClass = constructor.getDeclaringClass();
            name = "<init>";
            anns = constructor.getParameterAnnotations();
        }
        if (types.length == 0) {
            return EMPTY_NAMES;
        }
        String[] names = new String[types.length];
        boolean allDone = true;
        for (int i = 0; i < names.length; ++i) {
            for (int j = 0; j < anns[i].length; ++j) {
                Annotation ann = anns[i][j];
                if (!this.isNamed(ann)) continue;
                names[i] = this.getNamedValue(ann);
                break;
            }
            if (names[i] != null) continue;
            allDone = false;
        }
        if (!allDone) {
            allDone = true;
            String[] altNames = this.fallback.lookupParameterNames(methodOrCtor, false);
            if (altNames.length > 0) {
                for (int i = 0; i < names.length; ++i) {
                    if (names[i] != null) continue;
                    if (altNames[i] != null) {
                        names[i] = altNames[i];
                        continue;
                    }
                    allDone = false;
                }
            } else {
                allDone = false;
            }
        }
        if (!allDone) {
            if (throwExceptionIfMissing) {
                throw new ParameterNamesNotFoundException("One or more @Named annotations missing for class '" + declaringClass.getName() + "', methodOrCtor " + name + " and parameter types " + DefaultParanamer.getParameterTypeNamesCSV(types));
            }
            return Paranamer.EMPTY_NAMES;
        }
        return names;
    }

    protected String getNamedValue(Annotation ann) {
        if ("javax.inject.Named".equals(ann.annotationType().getName())) {
            return Jsr330Helper.getNamedValue(ann);
        }
        return null;
    }

    protected boolean isNamed(Annotation ann) {
        if ("javax.inject.Named".equals(ann.annotationType().getName())) {
            return Jsr330Helper.isNamed(ann);
        }
        return false;
    }

    public static class Jsr330Helper {
        public static final String __PARANAMER_DATA = "";

        private static boolean isNamed(Annotation ann) {
            return ann instanceof Named;
        }

        private static String getNamedValue(Annotation ann) {
            return ((Named)ann).value();
        }
    }
}

