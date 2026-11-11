/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.MultiUserPlatform;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.PlatformManager;
import com.sk89q.worldedit.util.command.CommandCompleter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserCommandCompleter
implements CommandCompleter {
    private final PlatformManager platformManager;

    public UserCommandCompleter(PlatformManager platformManager) {
        Preconditions.checkNotNull((Object)platformManager);
        this.platformManager = platformManager;
    }

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        Platform platform = this.platformManager.queryCapability(Capability.USER_COMMANDS);
        if (platform instanceof MultiUserPlatform) {
            ArrayList<String> suggestions = new ArrayList<String>();
            Collection<Actor> users = ((MultiUserPlatform)platform).getConnectedUsers();
            for (Actor user : users) {
                if (!user.getName().toLowerCase().startsWith(arguments.toLowerCase().trim())) continue;
                suggestions.add(user.getName());
            }
            return suggestions;
        }
        return Collections.emptyList();
    }
}

