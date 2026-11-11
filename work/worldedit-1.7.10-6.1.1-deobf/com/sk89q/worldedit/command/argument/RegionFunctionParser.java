/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.argument;

import com.sk89q.worldedit.command.argument.ItemUseParser;
import com.sk89q.worldedit.command.argument.ReplaceParser;
import com.sk89q.worldedit.command.argument.TreeGeneratorParser;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.util.command.composition.BranchingCommand;

public class RegionFunctionParser
extends BranchingCommand<Contextual<? extends RegionFunction>> {
    public RegionFunctionParser() {
        super("functionTpe");
        this.putOption(new TreeGeneratorParser("treeType"), "forest", new String[0]);
        this.putOption(new ItemUseParser(), "item", new String[0]);
        this.putOption(new ReplaceParser(), "set", new String[0]);
    }

    @Override
    public String getDescription() {
        return "Choose a block function";
    }
}

