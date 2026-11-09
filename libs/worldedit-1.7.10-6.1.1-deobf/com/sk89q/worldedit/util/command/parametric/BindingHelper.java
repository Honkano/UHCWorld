/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.Binding;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.BindingMatch;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindingHelper
implements Binding {
    private final List<BoundMethod> bindings;
    private final Type[] types;

    public BindingHelper() {
        ArrayList<BoundMethod> bindings = new ArrayList<BoundMethod>();
        ArrayList types = new ArrayList();
        for (Method method : this.getClass().getMethods()) {
            BindingMatch info = method.getAnnotation(BindingMatch.class);
            if (info == null) continue;
            Class<? extends Annotation> classifier = null;
            if (!info.classifier().equals(Annotation.class)) {
                classifier = info.classifier();
                types.add(classifier);
            }
            for (Class<?> t : info.type()) {
                Class<?> type = null;
                if (!t.equals(Class.class)) {
                    type = t;
                    if (classifier == null) {
                        types.add(type);
                    }
                }
                if (type == null && classifier == null) {
                    throw new RuntimeException("A @BindingMatch needs either a type or classifier set");
                }
                BoundMethod handler = new BoundMethod(info, type, classifier, method);
                bindings.add(handler);
            }
        }
        Collections.sort(bindings);
        this.bindings = bindings;
        Type[] typesArray = new Type[types.size()];
        types.toArray(typesArray);
        this.types = typesArray;
    }

    private BoundMethod match(ParameterData parameter) {
        for (BoundMethod binding : this.bindings) {
            Annotation classifer = parameter.getClassifier();
            Type type = parameter.getType();
            if (!(binding.classifier != null ? classifer != null && classifer.annotationType().equals(binding.classifier) && (binding.type == null || binding.type.equals(type)) : binding.type.equals(type))) continue;
            return binding;
        }
        throw new RuntimeException("Unknown type");
    }

    @Override
    public Type[] getTypes() {
        return this.types;
    }

    @Override
    public int getConsumedCount(ParameterData parameter) {
        return this.match(parameter).annotation.consumedCount();
    }

    @Override
    public BindingBehavior getBehavior(ParameterData parameter) {
        return this.match(parameter).annotation.behavior();
    }

    @Override
    public Object bind(ParameterData parameter, ArgumentStack scoped, boolean onlyConsume) throws ParameterException, CommandException, InvocationTargetException {
        BoundMethod binding = this.match(parameter);
        ArrayList<Object> args = new ArrayList<Object>();
        args.add(scoped);
        if (binding.classifier != null) {
            args.add(parameter.getClassifier());
        }
        if (binding.annotation.provideModifiers()) {
            args.add(parameter.getModifiers());
        }
        if (onlyConsume && binding.annotation.behavior() == BindingBehavior.PROVIDES) {
            return null;
        }
        Object[] argsArray = new Object[args.size()];
        args.toArray(argsArray);
        try {
            return binding.method.invoke((Object)this, argsArray);
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Processing of classifier " + parameter.getClassifier() + " and type " + parameter.getType() + " failed for method\n" + binding.method + "\nbecause the parameters for that method are wrong", e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            if (e.getCause() instanceof ParameterException) {
                throw (ParameterException)e.getCause();
            }
            if (e.getCause() instanceof CommandException) {
                throw (CommandException)e.getCause();
            }
            throw e;
        }
    }

    @Override
    public List<String> getSuggestions(ParameterData parameter, String prefix) {
        return new ArrayList<String>();
    }

    private static class BoundMethod
    implements Comparable<BoundMethod> {
        private final BindingMatch annotation;
        private final Type type;
        private final Class<? extends Annotation> classifier;
        private final Method method;

        BoundMethod(BindingMatch annotation, Type type, Class<? extends Annotation> classifier, Method method) {
            this.annotation = annotation;
            this.type = type;
            this.classifier = classifier;
            this.method = method;
        }

        @Override
        public int compareTo(BoundMethod o) {
            if (this.classifier != null && o.classifier == null) {
                return -1;
            }
            if (this.classifier == null && o.classifier != null) {
                return 1;
            }
            if (this.classifier != null && o.classifier != null) {
                if (this.type != null && o.type == null) {
                    return -1;
                }
                if (this.type == null && o.type != null) {
                    return 1;
                }
                return 0;
            }
            return 0;
        }
    }
}

