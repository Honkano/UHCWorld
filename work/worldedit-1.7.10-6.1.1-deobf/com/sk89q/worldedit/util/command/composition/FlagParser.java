/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.composition;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class FlagParser
implements CommandExecutor<FlagData> {
    private final Map<Character, CommandExecutor<?>> flags = Maps.newHashMap();

    public <T> Flag<T> registerFlag(char flag, CommandExecutor<T> executor) {
        Flag ret = new Flag(flag);
        this.flags.put(Character.valueOf(flag), executor);
        return ret;
    }

    @Override
    public FlagData call(CommandArgs args, CommandLocals locals) throws CommandException {
        HashMap values = Maps.newHashMap();
        try {
            block2: while (true) {
                String next;
                if ((next = args.peek()).equals("--")) {
                    args.next();
                } else if (next.length() > 0 && next.charAt(0) == '-') {
                    args.next();
                    if (next.length() == 1) {
                        throw new CommandException("- must be followed by a flag (like -a), otherwise use -- before the - (i.e. /cmd -- - is a dash).");
                    }
                    int i = 1;
                    while (true) {
                        if (i >= next.length()) continue block2;
                        char flag = next.charAt(i);
                        CommandExecutor<?> executor = this.flags.get(Character.valueOf(flag));
                        if (executor == null) {
                            throw new CommandException("Unknown flag: -" + flag + " (try one of -" + Joiner.on((String)"").join(this.flags.keySet()) + " or put -- to skip flag parsing, i.e. /cmd -- -this begins with a dash).");
                        }
                        values.put(Character.valueOf(flag), executor.call(args, locals));
                        ++i;
                    }
                }
                break;
            }
        }
        catch (MissingArgumentException missingArgumentException) {
            // empty catch block
        }
        return new FlagData(values);
    }

    @Override
    public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
        List<String> suggestions;
        block5: {
            suggestions = Collections.emptyList();
            block0: while (true) {
                String next;
                if ((next = args.peek()).equals("--")) break block5;
                if (next.length() <= 0 || next.charAt(0) != '-') break;
                args.next();
                if (!args.hasNext()) {
                    ArrayList flagSuggestions = Lists.newArrayList();
                    for (Character flag : this.flags.keySet()) {
                        if (next.indexOf(flag.charValue()) >= 1) continue;
                        flagSuggestions.add(next + flag);
                    }
                    return flagSuggestions;
                }
                int i = 1;
                while (true) {
                    if (i >= next.length()) continue block0;
                    char flag = next.charAt(i);
                    CommandExecutor<?> executor = this.flags.get(Character.valueOf(flag));
                    if (executor == null) {
                        return suggestions;
                    }
                    suggestions = executor.getSuggestions(args, locals);
                    ++i;
                }
                break;
            }
            return suggestions;
        }
        args.next();
        return suggestions;
    }

    @Override
    public String getUsage() {
        ArrayList options = Lists.newArrayList();
        for (Map.Entry<Character, CommandExecutor<?>> entry : this.flags.entrySet()) {
            String usage = entry.getValue().getUsage();
            options.add("[-" + entry.getKey() + (!usage.isEmpty() ? " " + usage : "") + "]");
        }
        return Joiner.on((String)" ").join((Iterable)options);
    }

    @Override
    public String getDescription() {
        return "Read flags";
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        for (CommandExecutor<?> executor : this.flags.values()) {
            if (executor.testPermission(locals)) continue;
            return false;
        }
        return true;
    }

    public static final class Flag<T> {
        private final char flag;

        private Flag(char flag) {
            this.flag = flag;
        }

        @Nullable
        public T get(FlagData data) {
            return (T)data.get(this.flag);
        }

        public T get(FlagData data, T fallback) {
            T value = this.get(data);
            if (value == null) {
                return fallback;
            }
            return value;
        }
    }

    public static class FlagData {
        private final Map<Character, Object> data;

        private FlagData(Map<Character, Object> data) {
            this.data = data;
        }

        public int size() {
            return this.data.size();
        }

        public boolean isEmpty() {
            return this.data.isEmpty();
        }

        public Object get(char key) {
            return this.data.get(Character.valueOf(key));
        }

        public boolean containsKey(char key) {
            return this.data.containsKey(Character.valueOf(key));
        }
    }
}

