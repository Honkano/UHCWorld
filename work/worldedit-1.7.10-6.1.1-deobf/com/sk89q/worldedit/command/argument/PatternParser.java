/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.argument.StringParser;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;
import com.sk89q.worldedit.world.World;

public class PatternParser
extends SimpleCommand<Pattern> {
    private final StringParser stringParser;

    public PatternParser(String name) {
        this.stringParser = this.addParameter(new StringParser(name, "The pattern"));
    }

    @Override
    public Pattern call(CommandArgs args, CommandLocals locals) throws CommandException {
        Extent extent;
        String patternString = this.stringParser.call(args, locals);
        Actor actor = locals.get(Actor.class);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(actor);
        ParserContext parserContext = new ParserContext();
        parserContext.setActor(actor);
        if (actor instanceof Entity && (extent = ((Entity)((Object)actor)).getExtent()) instanceof World) {
            parserContext.setWorld((World)extent);
        }
        parserContext.setSession(session);
        try {
            return (Pattern)WorldEdit.getInstance().getPatternFactory().parseFromInput(patternString, parserContext);
        }
        catch (NoMatchException e) {
            throw new CommandException(e.getMessage(), e);
        }
        catch (InputParseException e) {
            throw new CommandException(e.getMessage(), e);
        }
    }

    @Override
    public String getDescription() {
        return "Choose a pattern";
    }

    @Override
    public boolean testPermission0(CommandLocals locals) {
        return true;
    }
}

