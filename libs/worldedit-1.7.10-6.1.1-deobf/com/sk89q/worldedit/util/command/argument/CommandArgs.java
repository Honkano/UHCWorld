/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.argument;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.argument.UnusedArgumentsException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandArgs {
    private final List<String> arguments;
    private int position = 0;

    public CommandArgs(List<String> arguments) {
        this.arguments = arguments;
    }

    public CommandArgs(CommandArgs args) {
        this(Lists.newArrayList(args.arguments));
    }

    public boolean hasNext() {
        return this.position < this.arguments.size();
    }

    public String next() throws MissingArgumentException {
        try {
            return this.arguments.get(this.position++);
        }
        catch (IndexOutOfBoundsException ignored) {
            throw new MissingArgumentException("Too few arguments specified.");
        }
    }

    public String uncheckedNext() {
        if (this.hasNext()) {
            return this.arguments.get(this.position);
        }
        return null;
    }

    public String peek() throws MissingArgumentException {
        try {
            return this.arguments.get(this.position);
        }
        catch (IndexOutOfBoundsException ignored) {
            throw new MissingArgumentException("Too few arguments specified.");
        }
    }

    public String uncheckedPeek() {
        if (this.hasNext()) {
            return this.arguments.get(this.position);
        }
        return null;
    }

    public String remaining() throws MissingArgumentException {
        if (this.hasNext()) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            while (this.hasNext()) {
                if (!first) {
                    builder.append(" ");
                }
                builder.append(this.next());
                first = false;
            }
            return builder.toString();
        }
        throw new MissingArgumentException("Too few arguments specified.");
    }

    public String peekRemaining() throws MissingArgumentException {
        if (this.hasNext()) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            while (this.hasNext()) {
                if (!first) {
                    builder.append(" ");
                }
                builder.append(this.next());
                first = false;
            }
            return builder.toString();
        }
        throw new MissingArgumentException();
    }

    public int position() {
        return this.position;
    }

    public int size() {
        return this.arguments.size();
    }

    public void markConsumed() {
        this.position = this.arguments.size();
    }

    public void requireAllConsumed() throws UnusedArgumentsException {
        if (this.hasNext()) {
            StringBuilder builder = new StringBuilder();
            try {
                builder.append(this.peekRemaining());
            }
            catch (MissingArgumentException e) {
                throw new RuntimeException("This should not have happened", e);
            }
            throw new UnusedArgumentsException("There were unused arguments: " + builder);
        }
    }

    public static class Parser {
        private boolean usingHangingArguments = false;

        public boolean isUsingHangingArguments() {
            return this.usingHangingArguments;
        }

        public Parser setUsingHangingArguments(boolean usingHangingArguments) {
            this.usingHangingArguments = usingHangingArguments;
            return this;
        }

        public CommandArgs parse(String arguments) throws CommandException {
            CommandContext context = new CommandContext(CommandContext.split("_ " + arguments), Collections.<Character>emptySet(), false, null, false);
            ArrayList args = Lists.newArrayList();
            for (int i = 0; i < context.argsLength(); ++i) {
                args.add(context.getString(i));
            }
            if (this.isUsingHangingArguments() && (arguments.isEmpty() || arguments.endsWith(" "))) {
                args.add("");
            }
            return new CommandArgs(args);
        }
    }
}

