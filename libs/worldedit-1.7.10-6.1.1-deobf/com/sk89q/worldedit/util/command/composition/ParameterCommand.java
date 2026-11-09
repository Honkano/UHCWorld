/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.composition;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import com.sk89q.worldedit.util.command.composition.FlagParser;
import java.util.ArrayList;
import java.util.List;

public abstract class ParameterCommand<T>
implements CommandExecutor<T> {
    private final List<CommandExecutor<?>> parameters = Lists.newArrayList();
    private final FlagParser flagParser = new FlagParser();

    public ParameterCommand() {
        this.addParameter(this.flagParser);
    }

    protected List<CommandExecutor<?>> getParameters() {
        return this.parameters;
    }

    public <E extends CommandExecutor<?>> E addParameter(E executor) {
        this.parameters.add(executor);
        return executor;
    }

    public <E> FlagParser.Flag<E> addFlag(char flag, CommandExecutor<E> executor) {
        return this.flagParser.registerFlag(flag, executor);
    }

    protected FlagParser getFlagParser() {
        return this.flagParser;
    }

    @Override
    public final String getUsage() {
        ArrayList parts = Lists.newArrayList();
        for (CommandExecutor<?> executor : this.parameters) {
            String usage = executor.getUsage();
            if (usage.isEmpty()) continue;
            parts.add(executor.getUsage());
        }
        return Joiner.on((String)" ").join((Iterable)parts);
    }

    @Override
    public final boolean testPermission(CommandLocals locals) {
        for (CommandExecutor<?> executor : this.parameters) {
            if (executor.testPermission(locals)) continue;
            return false;
        }
        return this.testPermission0(locals);
    }

    protected abstract boolean testPermission0(CommandLocals var1);
}

