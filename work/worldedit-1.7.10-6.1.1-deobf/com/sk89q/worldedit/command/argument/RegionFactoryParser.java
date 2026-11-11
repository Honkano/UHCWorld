/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.argument;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.regions.factory.CuboidRegionFactory;
import com.sk89q.worldedit.regions.factory.CylinderRegionFactory;
import com.sk89q.worldedit.regions.factory.RegionFactory;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldedit.util.command.argument.ArgumentUtils;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.List;

public class RegionFactoryParser
implements CommandExecutor<RegionFactory> {
    @Override
    public RegionFactory call(CommandArgs args, CommandLocals locals) throws CommandException {
        try {
            String type = args.next();
            if (type.equals("cuboid")) {
                return new CuboidRegionFactory();
            }
            if (type.equals("sphere")) {
                return new SphereRegionFactory();
            }
            if (type.equals("cyl") || type.equals("cylinder")) {
                return new CylinderRegionFactory(1.0);
            }
            throw new CommandException("Unknown shape type: " + type + " (try one of " + this.getUsage() + ")");
        }
        catch (MissingArgumentException e) {
            throw new CommandException("Missing shape type (try one of " + this.getUsage() + ")");
        }
    }

    @Override
    public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
        return ArgumentUtils.getMatchingSuggestions(Lists.newArrayList((Object[])new String[]{"cuboid", "sphere", "cyl"}), args.next());
    }

    @Override
    public String getUsage() {
        return "(cuboid | sphere | cyl)";
    }

    @Override
    public String getDescription() {
        return "Defines a region";
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return true;
    }
}

