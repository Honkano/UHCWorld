/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.composition;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.SimpleDescription;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.argument.UnusedArgumentsException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.List;

public class LegacyCommandAdapter
implements CommandCallable {
    private final CommandExecutor<?> executor;

    private LegacyCommandAdapter(CommandExecutor<?> executor) {
        this.executor = executor;
    }

    @Override
    public final Object call(String arguments, CommandLocals locals, String[] parentCommands) throws CommandException {
        CommandArgs args = new CommandArgs.Parser().parse(arguments);
        if (args.hasNext() && args.uncheckedPeek().equals("-?")) {
            throw new CommandException(this.executor.getUsage());
        }
        Object ret = this.executor.call(args, locals);
        try {
            args.requireAllConsumed();
        }
        catch (UnusedArgumentsException e) {
            throw new CommandException(e.getMessage());
        }
        return ret;
    }

    @Override
    public Description getDescription() {
        return new SimpleDescription().setDescription(this.executor.getDescription()).overrideUsage(this.executor.getUsage());
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return this.executor.testPermission(locals);
    }

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        CommandArgs args = new CommandArgs.Parser().setUsingHangingArguments(true).parse(arguments);
        try {
            return this.executor.getSuggestions(args, locals);
        }
        catch (MissingArgumentException e) {
            return Lists.newArrayList();
        }
    }

    public static LegacyCommandAdapter adapt(CommandExecutor<?> executor) {
        return new LegacyCommandAdapter(executor);
    }
}

