/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.parametric;

import com.google.common.primitives.Chars;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.InvalidUsageException;
import com.sk89q.worldedit.util.command.MissingParameterException;
import com.sk89q.worldedit.util.command.Parameter;
import com.sk89q.worldedit.util.command.SimpleDescription;
import com.sk89q.worldedit.util.command.SimpleParameter;
import com.sk89q.worldedit.util.command.UnconsumedParameterException;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.ContextArgumentStack;
import com.sk89q.worldedit.util.command.parametric.ExceptionConverter;
import com.sk89q.worldedit.util.command.parametric.InvokeHandler;
import com.sk89q.worldedit.util.command.parametric.InvokeListener;
import com.sk89q.worldedit.util.command.parametric.Optional;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import com.sk89q.worldedit.util.command.parametric.ParametricBuilder;
import com.sk89q.worldedit.util.command.parametric.ParametricException;
import com.sk89q.worldedit.util.command.parametric.StringArgumentStack;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ParametricCallable
implements CommandCallable {
    private final ParametricBuilder builder;
    private final Object object;
    private final Method method;
    private final ParameterData[] parameters;
    private final Set<Character> valueFlags = new HashSet<Character>();
    private final boolean anyFlags;
    private final Set<Character> legacyFlags = new HashSet<Character>();
    private final SimpleDescription description = new SimpleDescription();
    private final CommandPermissions commandPermissions;

    ParametricCallable(ParametricBuilder builder, Object object, Method method, Command definition) throws ParametricException {
        this.builder = builder;
        this.object = object;
        this.method = method;
        Annotation[][] annotations = method.getParameterAnnotations();
        String[] names = builder.getParanamer().lookupParameterNames(method, false);
        Type[] types = method.getGenericParameterTypes();
        this.parameters = new ParameterData[types.length];
        ArrayList<Parameter> userParameters = new ArrayList<Parameter>();
        int numOptional = 0;
        CommandPermissions permHint = method.getAnnotation(CommandPermissions.class);
        if (permHint != null) {
            this.description.setPermissions(Arrays.asList(permHint.value()));
        }
        for (int i = 0; i < types.length; ++i) {
            Type type = types[i];
            ParameterData parameter = new ParameterData();
            parameter.setType(type);
            parameter.setModifiers(annotations[i]);
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof Switch) {
                    parameter.setFlag(Character.valueOf(((Switch)annotation).value()), type != Boolean.TYPE);
                    continue;
                }
                if (annotation instanceof Optional) {
                    parameter.setOptional(true);
                    String[] value = ((Optional)annotation).value();
                    if (value.length <= 0) continue;
                    parameter.setDefaultValue(value);
                    continue;
                }
                if (parameter.getBinding() != null) continue;
                parameter.setBinding(builder.getBindings().get(annotation.annotationType()));
                parameter.setClassifier(annotation);
            }
            parameter.setName(names.length > 0 ? names[i] : ParametricCallable.generateName(type, parameter.getClassifier(), i));
            if (parameter.isValueFlag()) {
                this.valueFlags.add(parameter.getFlag());
            }
            if (parameter.getBinding() == null) {
                parameter.setBinding(builder.getBindings().get(type));
                if (parameter.getBinding() == null) {
                    throw new ParametricException("Don't know how to handle the parameter type '" + type + "' in\n" + method.toGenericString());
                }
            }
            parameter.validate(method, i + 1);
            if (parameter.isOptional() && parameter.getFlag() == null) {
                ++numOptional;
            } else if (numOptional > 0 && parameter.isNonFlagConsumer() && parameter.getConsumedCount() < 0) {
                throw new ParametricException("Found an parameter using the binding " + parameter.getBinding().getClass().getCanonicalName() + "\nthat does not know how many arguments it consumes, but " + "it follows an optional parameter\nMethod: " + method.toGenericString());
            }
            this.parameters[i] = parameter;
            if (!parameter.isUserInput()) continue;
            userParameters.add(parameter);
        }
        this.anyFlags = definition.anyFlags();
        this.legacyFlags.addAll(Chars.asList((char[])definition.flags().toCharArray()));
        this.description.setDescription(!definition.desc().isEmpty() ? definition.desc() : null);
        this.description.setHelp(!definition.help().isEmpty() ? definition.help() : null);
        this.description.overrideUsage(!definition.usage().isEmpty() ? definition.usage() : null);
        for (InvokeListener listener : builder.getInvokeListeners()) {
            listener.updateDescription(object, method, this.parameters, this.description);
        }
        this.description.setParameters(userParameters);
        this.commandPermissions = method.getAnnotation(CommandPermissions.class);
    }

    @Override
    public Object call(String stringArguments, CommandLocals locals, String[] parentCommands) throws CommandException {
        if (!this.testPermission(locals)) {
            throw new CommandPermissionsException();
        }
        String calledCommand = parentCommands.length > 0 ? parentCommands[parentCommands.length - 1] : "_";
        String[] split = CommandContext.split(calledCommand + " " + stringArguments);
        CommandContext context = new CommandContext(split, this.getValueFlags(), false, locals);
        if (context.hasFlag('?')) {
            throw new InvalidUsageException(null, this, true);
        }
        Object[] args = new Object[this.parameters.length];
        ContextArgumentStack arguments = new ContextArgumentStack(context);
        SimpleParameter parameter = null;
        try {
            ArrayList<InvokeHandler> handlers = new ArrayList<InvokeHandler>();
            for (InvokeListener listener : this.builder.getInvokeListeners()) {
                InvokeHandler handler = listener.createInvokeHandler();
                handlers.add(handler);
                handler.preProcess(this.object, this.method, this.parameters, context);
            }
            for (int i = 0; i < this.parameters.length; ++i) {
                parameter = this.parameters[i];
                if (this.mayConsumeArguments(i, arguments)) {
                    ArgumentStack usedArguments = ParametricCallable.getScopedContext(parameter, arguments);
                    try {
                        args[i] = ((ParameterData)parameter).getBinding().bind((ParameterData)parameter, usedArguments, false);
                    }
                    catch (MissingParameterException e) {
                        if (!parameter.isOptional()) {
                            throw e;
                        }
                        args[i] = this.getDefaultValue(i, arguments);
                    }
                    continue;
                }
                args[i] = this.getDefaultValue(i, arguments);
            }
            this.checkUnconsumed(arguments);
            for (InvokeHandler handler : handlers) {
                handler.preInvoke(this.object, this.method, this.parameters, args, context);
            }
            this.method.invoke(this.object, args);
            for (InvokeHandler handler : handlers) {
                handler.postInvoke(handler, this.method, this.parameters, args, context);
            }
        }
        catch (MissingParameterException e) {
            throw new InvalidUsageException("Too few parameters!", this);
        }
        catch (UnconsumedParameterException e) {
            throw new InvalidUsageException("Too many parameters! Unused parameters: " + e.getUnconsumed(), this);
        }
        catch (ParameterException e) {
            assert (parameter != null);
            String name = parameter.getName();
            throw new InvalidUsageException("For parameter '" + name + "': " + e.getMessage(), this);
        }
        catch (InvocationTargetException e) {
            for (ExceptionConverter converter : this.builder.getExceptionConverters()) {
                converter.convert(e.getCause());
            }
            throw new WrappedCommandException(e);
        }
        catch (IllegalArgumentException e) {
            throw new WrappedCommandException(e);
        }
        catch (CommandException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new WrappedCommandException(e);
        }
        return true;
    }

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        return this.builder.getDefaultCompleter().getSuggestions(arguments, locals);
    }

    public Set<Character> getValueFlags() {
        return this.valueFlags;
    }

    @Override
    public SimpleDescription getDescription() {
        return this.description;
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        if (this.commandPermissions != null) {
            for (String perm : this.commandPermissions.value()) {
                if (!this.builder.getAuthorizer().testPermission(locals, perm)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    private static ArgumentStack getScopedContext(Parameter parameter, ArgumentStack existing) {
        if (parameter.getFlag() != null) {
            CommandContext context = existing.getContext();
            if (parameter.isValueFlag()) {
                return new StringArgumentStack(context, context.getFlag(parameter.getFlag().charValue()), false);
            }
            String v = context.hasFlag(parameter.getFlag().charValue()) ? "true" : "false";
            return new StringArgumentStack(context, v, true);
        }
        return existing;
    }

    private boolean mayConsumeArguments(int i, ContextArgumentStack scoped) {
        CommandContext context = scoped.getContext();
        ParameterData parameter = this.parameters[i];
        if (parameter.isOptional()) {
            if (parameter.getFlag() != null) {
                return !parameter.isValueFlag() || context.hasFlag(parameter.getFlag().charValue());
            }
            int numberFree = context.argsLength() - scoped.position();
            for (int j = i; j < this.parameters.length; ++j) {
                if (!this.parameters[j].isNonFlagConsumer() || this.parameters[j].isOptional()) continue;
                numberFree -= this.parameters[j].getConsumedCount();
            }
            if (numberFree < 1) {
                return false;
            }
        }
        return true;
    }

    private Object getDefaultValue(int i, ContextArgumentStack scoped) throws ParameterException, CommandException, InvocationTargetException {
        CommandContext context = scoped.getContext();
        ParameterData parameter = this.parameters[i];
        String[] defaultValue = parameter.getDefaultValue();
        if (defaultValue != null) {
            try {
                return parameter.getBinding().bind(parameter, new StringArgumentStack(context, defaultValue, false), false);
            }
            catch (MissingParameterException e) {
                throw new ParametricException("The default value of the parameter using the binding " + parameter.getBinding().getClass() + " in the method\n" + this.method.toGenericString() + "\nis invalid");
            }
        }
        return null;
    }

    private void checkUnconsumed(ContextArgumentStack scoped) throws UnconsumedParameterException {
        CommandContext context = scoped.getContext();
        String unconsumedFlags = this.getUnusedFlags(context);
        String unconsumed = scoped.getUnconsumed();
        if (unconsumed != null) {
            throw new UnconsumedParameterException(unconsumed + " " + unconsumedFlags);
        }
        if (unconsumedFlags != null) {
            throw new UnconsumedParameterException(unconsumedFlags);
        }
    }

    private String getUnusedFlags(CommandContext context) {
        if (!this.anyFlags) {
            HashSet<Character> unusedFlags = null;
            for (char flag : context.getFlags()) {
                boolean found = false;
                if (this.legacyFlags.contains(Character.valueOf(flag))) break;
                for (ParameterData parameter : this.parameters) {
                    Character paramFlag = parameter.getFlag();
                    if (paramFlag == null || flag != paramFlag.charValue()) continue;
                    found = true;
                    break;
                }
                if (found) continue;
                if (unusedFlags == null) {
                    unusedFlags = new HashSet<Character>();
                }
                unusedFlags.add(Character.valueOf(flag));
            }
            if (unusedFlags != null) {
                StringBuilder builder = new StringBuilder();
                for (Character flag : unusedFlags) {
                    builder.append("-").append(flag).append(" ");
                }
                return builder.toString().trim();
            }
        }
        return null;
    }

    private static String generateName(Type type, Annotation classifier, int index) {
        if (classifier != null) {
            return classifier.annotationType().getSimpleName().toLowerCase();
        }
        if (type instanceof Class) {
            return ((Class)type).getSimpleName().toLowerCase();
        }
        return "unknown" + index;
    }
}

