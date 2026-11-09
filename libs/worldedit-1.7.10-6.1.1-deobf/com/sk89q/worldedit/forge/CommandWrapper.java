/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.platform.CommandSuggestionEvent;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.util.command.CommandMapping;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandWrapper
extends CommandBase {
    private CommandMapping command;

    protected CommandWrapper(CommandMapping command) {
        this.command = command;
    }

    public String getCommandName() {
        return this.command.getPrimaryAlias();
    }

    public List<String> getCommandAliases() {
        return Arrays.asList(this.command.getAllAliases());
    }

    public void processCommand(ICommandSender var1, String[] var2) {
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] arguments) {
        if (sender instanceof EntityPlayerMP) {
            CommandSuggestionEvent event = new CommandSuggestionEvent(ForgeWorldEdit.inst.wrap((EntityPlayerMP)sender), this.command.getPrimaryAlias() + " " + Joiner.on((String)" ").join((Object[])arguments));
            WorldEdit.getInstance().getEventBus().post(event);
            return event.getSuggestions();
        }
        return super.addTabCompletionOptions(sender, arguments);
    }

    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.command.getPrimaryAlias() + " " + this.command.getDescription().getUsage();
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public int compareTo(@Nullable Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof ICommand) {
            return super.compareTo((ICommand)o);
        }
        return 0;
    }
}

