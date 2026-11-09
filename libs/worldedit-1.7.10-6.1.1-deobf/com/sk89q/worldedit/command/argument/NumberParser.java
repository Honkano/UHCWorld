/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.argument;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.Collections;
import java.util.List;

public class NumberParser
implements CommandExecutor<Number> {
    private final String name;
    private final String description;
    private final String defaultSuggestion;

    public NumberParser(String name, String description) {
        this(name, description, null);
    }

    public NumberParser(String name, String description, String defaultSuggestion) {
        this.name = name;
        this.description = description;
        this.defaultSuggestion = defaultSuggestion;
    }

    @Override
    public Number call(CommandArgs args, CommandLocals locals) throws CommandException {
        try {
            String next = args.next();
            try {
                return Double.parseDouble(next);
            }
            catch (NumberFormatException ignored) {
                throw new CommandException("The value for <" + this.name + "> should be a number. '" + next + "' is not a number.");
            }
        }
        catch (MissingArgumentException e) {
            throw new CommandException("Missing value for <" + this.name + "> (try a number).");
        }
    }

    @Override
    public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
        String value = args.next();
        return value.isEmpty() && this.defaultSuggestion != null ? Lists.newArrayList((Object[])new String[]{this.defaultSuggestion}) : Collections.emptyList();
    }

    @Override
    public String getUsage() {
        return "<" + this.name + ">";
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return true;
    }
}

