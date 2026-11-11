/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import java.io.File;

public class ScriptingCommands {
    private final WorldEdit worldEdit;

    public ScriptingCommands(WorldEdit worldEdit) {
        Preconditions.checkNotNull((Object)worldEdit);
        this.worldEdit = worldEdit;
    }

    @Command(aliases={"cs"}, usage="<filename> [args...]", desc="Execute a CraftScript", min=1, max=-1)
    @CommandPermissions(value={"worldedit.scripting.execute"})
    @Logging(value=Logging.LogMode.ALL)
    public void execute(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
        String[] scriptArgs = args.getSlice(1);
        String name = args.getString(0);
        if (!player.hasPermission("worldedit.scripting.execute." + name)) {
            player.printError("You don't have permission to use that script.");
            return;
        }
        session.setLastScript(name);
        File dir = this.worldEdit.getWorkingDirectoryFile(this.worldEdit.getConfiguration().scriptsDir);
        File f = this.worldEdit.getSafeOpenFile(player, dir, name, "js", "js");
        this.worldEdit.runScript(player, f, scriptArgs);
    }

    @Command(aliases={".s"}, usage="[args...]", desc="Execute last CraftScript", min=0, max=-1)
    @CommandPermissions(value={"worldedit.scripting.execute"})
    @Logging(value=Logging.LogMode.ALL)
    public void executeLast(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
        String lastScript = session.getLastScript();
        if (!player.hasPermission("worldedit.scripting.execute." + lastScript)) {
            player.printError("You don't have permission to use that script.");
            return;
        }
        if (lastScript == null) {
            player.printError("Use /cs with a script name first.");
            return;
        }
        String[] scriptArgs = args.getSlice(0);
        File dir = this.worldEdit.getWorkingDirectoryFile(this.worldEdit.getConfiguration().scriptsDir);
        File f = this.worldEdit.getSafeOpenFile(player, dir, lastScript, "js", "js");
        this.worldEdit.runScript(player, f, scriptArgs);
    }
}

