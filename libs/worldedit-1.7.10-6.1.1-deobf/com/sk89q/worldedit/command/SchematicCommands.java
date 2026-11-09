/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.command.FlattenedClipboardTransform;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.parametric.Optional;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class SchematicCommands {
    private static final int SCHEMATICS_PER_PAGE = 9;
    private static final Logger log = Logger.getLogger(SchematicCommands.class.getCanonicalName());
    private final WorldEdit worldEdit;

    public SchematicCommands(WorldEdit worldEdit) {
        Preconditions.checkNotNull((Object)worldEdit);
        this.worldEdit = worldEdit;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Command(aliases={"load"}, usage="[<format>] <filename>", desc="Load a schematic into your clipboard", min=1, max=2)
    @Deprecated
    @CommandPermissions(value={"worldedit.clipboard.load", "worldedit.schematic.load"})
    public void load(Player player, LocalSession session, @Optional(value={"schematic"}) String formatName, String filename) throws FilenameException {
        LocalConfiguration config = this.worldEdit.getConfiguration();
        File dir = this.worldEdit.getWorkingDirectoryFile(config.saveDir);
        File f = this.worldEdit.getSafeOpenFile(player, dir, filename, "schematic", "schematic");
        if (!f.exists()) {
            player.printError("Schematic " + filename + " does not exist!");
            return;
        }
        ClipboardFormat format = ClipboardFormat.findByAlias(formatName);
        if (format == null) {
            player.printError("Unknown schematic format: " + formatName);
            return;
        }
        Closer closer = Closer.create();
        try {
            FileInputStream fis = closer.register(new FileInputStream(f));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            ClipboardReader reader = format.getReader(bis);
            WorldData worldData = player.getWorld().getWorldData();
            Clipboard clipboard = reader.read(player.getWorld().getWorldData());
            session.setClipboard(new ClipboardHolder(clipboard, worldData));
            log.info(player.getName() + " loaded " + f.getCanonicalPath());
            player.print(filename + " loaded. Paste it with //paste");
        }
        catch (IOException e) {
            player.printError("Schematic could not read or it does not exist: " + e.getMessage());
            log.log(Level.WARNING, "Failed to load a saved clipboard", e);
        }
        finally {
            try {
                closer.close();
            }
            catch (IOException iOException) {}
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Command(aliases={"save"}, usage="[<format>] <filename>", desc="Save a schematic into your clipboard", min=1, max=2)
    @Deprecated
    @CommandPermissions(value={"worldedit.clipboard.save", "worldedit.schematic.save"})
    public void save(Player player, LocalSession session, @Optional(value={"schematic"}) String formatName, String filename) throws CommandException, WorldEditException {
        Clipboard target;
        LocalConfiguration config = this.worldEdit.getConfiguration();
        File dir = this.worldEdit.getWorkingDirectoryFile(config.saveDir);
        File f = this.worldEdit.getSafeSaveFile(player, dir, filename, "schematic", "schematic");
        ClipboardFormat format = ClipboardFormat.findByAlias(formatName);
        if (format == null) {
            player.printError("Unknown schematic format: " + formatName);
            return;
        }
        ClipboardHolder holder = session.getClipboard();
        Clipboard clipboard = holder.getClipboard();
        Transform transform = holder.getTransform();
        if (!transform.isIdentity()) {
            FlattenedClipboardTransform result = FlattenedClipboardTransform.transform(clipboard, transform, holder.getWorldData());
            target = new BlockArrayClipboard(result.getTransformedRegion());
            target.setOrigin(clipboard.getOrigin());
            Operations.completeLegacy(result.copyTo(target));
        } else {
            target = clipboard;
        }
        Closer closer = Closer.create();
        try {
            File parent = f.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new CommandException("Could not create folder for schematics!");
            }
            FileOutputStream fos = closer.register(new FileOutputStream(f));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(format.getWriter(bos));
            writer.write(target, holder.getWorldData());
            log.info(player.getName() + " saved " + f.getCanonicalPath());
            player.print(filename + " saved.");
        }
        catch (IOException e) {
            player.printError("Schematic could not written: " + e.getMessage());
            log.log(Level.WARNING, "Failed to write a saved clipboard", e);
        }
        finally {
            try {
                closer.close();
            }
            catch (IOException iOException) {}
        }
    }

    @Command(aliases={"delete", "d"}, usage="<filename>", desc="Delete a saved schematic", help="Delete a schematic from the schematic list", min=1, max=1)
    @CommandPermissions(value={"worldedit.schematic.delete"})
    public void delete(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
        LocalConfiguration config = this.worldEdit.getConfiguration();
        String filename = args.getString(0);
        File dir = this.worldEdit.getWorkingDirectoryFile(config.saveDir);
        File f = this.worldEdit.getSafeSaveFile(player, dir, filename, "schematic", "schematic");
        if (!f.exists()) {
            player.printError("Schematic " + filename + " does not exist!");
            return;
        }
        if (!f.delete()) {
            player.printError("Deletion of " + filename + " failed! Maybe it is read-only.");
            return;
        }
        player.print(filename + " has been deleted.");
    }

    @Command(aliases={"formats", "listformats", "f"}, desc="List available formats", max=0)
    @CommandPermissions(value={"worldedit.schematic.formats"})
    public void formats(Actor actor) throws WorldEditException {
        actor.print("Available clipboard formats (Name: Lookup names)");
        boolean first = true;
        for (ClipboardFormat format : ClipboardFormat.values()) {
            StringBuilder builder = new StringBuilder();
            builder.append(format.name()).append(": ");
            for (String lookupName : format.getAliases()) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append(lookupName);
                first = false;
            }
            first = true;
            actor.print(builder.toString());
        }
    }

    @Command(aliases={"list", "all", "ls"}, desc="List saved schematics", min=0, max=1, flags="dnp", help="List all schematics in the schematics directory\n -d sorts by date, oldest first\n -n sorts by date, newest first\n -p <page> prints the requested page\n")
    @CommandPermissions(value={"worldedit.schematic.list"})
    public void list(Actor actor, CommandContext args, @Switch(value=112) @Optional(value={"1"}) int page) throws WorldEditException {
        File dir = this.worldEdit.getWorkingDirectoryFile(this.worldEdit.getConfiguration().saveDir);
        List<File> fileList = this.allFiles(dir);
        if (fileList.isEmpty()) {
            actor.printError("No schematics found.");
            return;
        }
        File[] files = new File[fileList.size()];
        fileList.toArray(files);
        int pageCount = files.length / 9 + 1;
        if (page < 1) {
            actor.printError("Page must be at least 1");
            return;
        }
        if (page > pageCount) {
            actor.printError("Page must be less than " + (pageCount + 1));
            return;
        }
        final int sortType = args.hasFlag('d') ? -1 : (args.hasFlag('n') ? 1 : 0);
        Arrays.sort(files, new Comparator<File>(){

            @Override
            public int compare(File f1, File f2) {
                int res;
                if (sortType == 0) {
                    int p = f1.getParent().compareTo(f2.getParent());
                    res = p == 0 ? f1.getName().compareTo(f2.getName()) : p;
                } else {
                    res = Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    if (sortType == 1) {
                        res = -res;
                    }
                }
                return res;
            }
        });
        List<String> schematics = this.listFiles(this.worldEdit.getConfiguration().saveDir, files);
        int offset = (page - 1) * 9;
        actor.print("Available schematics (Filename: Format) [" + page + "/" + pageCount + "]:");
        StringBuilder build = new StringBuilder();
        int limit = Math.min(offset + 9, schematics.size());
        int i = offset;
        while (i < limit) {
            build.append(schematics.get(i));
            if (++i == limit) continue;
            build.append("\n");
        }
        actor.print(build.toString());
    }

    private List<File> allFiles(File root) {
        File[] files = root.listFiles();
        if (files == null) {
            return null;
        }
        ArrayList<File> fileList = new ArrayList<File>();
        for (File f : files) {
            if (f.isDirectory()) {
                List<File> subFiles = this.allFiles(f);
                if (subFiles == null) continue;
                fileList.addAll(subFiles);
                continue;
            }
            fileList.add(f);
        }
        return fileList;
    }

    private List<String> listFiles(String prefix, File[] files) {
        if (prefix == null) {
            prefix = "";
        }
        ArrayList<String> result = new ArrayList<String>();
        for (File file : files) {
            StringBuilder build = new StringBuilder();
            build.append("\u00a72");
            ClipboardFormat format = ClipboardFormat.findByFile(file);
            boolean inRoot = file.getParentFile().getName().equals(prefix);
            build.append(inRoot ? file.getName() : file.getPath().split(Pattern.quote(prefix + File.separator))[1]).append(": ").append(format == null ? "Unknown" : format.name());
            result.add(build.toString());
        }
        return result;
    }
}

