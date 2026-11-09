/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command.composition;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import com.sk89q.worldedit.util.command.composition.ParameterCommand;
import java.util.List;

public abstract class SimpleCommand<T>
extends ParameterCommand<T> {
    @Override
    public final List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
        List<Object> suggestions = Lists.newArrayList();
        boolean seenParameter = false;
        for (CommandExecutor<?> parameter : this.getParameters()) {
            try {
                suggestions = parameter.getSuggestions(args, locals);
                seenParameter = true;
            }
            catch (MissingArgumentException e) {
                if (seenParameter) {
                    return suggestions;
                }
                throw e;
            }
            if (args.position() != args.size()) continue;
            return suggestions;
        }
        return suggestions;
    }
}

