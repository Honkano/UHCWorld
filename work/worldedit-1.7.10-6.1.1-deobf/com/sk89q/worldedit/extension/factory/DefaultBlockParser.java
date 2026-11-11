/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.NotABlockException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.blocks.ClothColor;
import com.sk89q.worldedit.blocks.MobSpawnerBlock;
import com.sk89q.worldedit.blocks.NoteBlock;
import com.sk89q.worldedit.blocks.SignBlock;
import com.sk89q.worldedit.blocks.SkullBlock;
import com.sk89q.worldedit.blocks.metadata.MobType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.input.DisallowedUsageException;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.registry.InputParser;

class DefaultBlockParser
extends InputParser<BaseBlock> {
    protected DefaultBlockParser(WorldEdit worldEdit) {
        super(worldEdit);
    }

    private static BaseBlock getBlockInHand(Actor actor) throws InputParseException {
        if (actor instanceof Player) {
            try {
                return ((Player)actor).getBlockInHand();
            }
            catch (NotABlockException e) {
                throw new InputParseException("You're not holding a block!");
            }
            catch (WorldEditException e) {
                throw new InputParseException("Unknown error occurred: " + e.getMessage(), e);
            }
        }
        throw new InputParseException("The user is not a player!");
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public BaseBlock parseFromInput(String input, ParserContext context) throws InputParseException {
        input = input.replace("_", " ");
        input = input.replace(";", "|");
        blockAndExtraData = input.split("\\|");
        blockLocator = blockAndExtraData[0].split(":", 3);
        switch (blockLocator.length) {
            case 3: {
                typeAndData = new String[]{blockLocator[0] + ":" + blockLocator[1], blockLocator[2]};
                break;
            }
            default: {
                typeAndData = blockLocator;
            }
        }
        testId = typeAndData[0];
        blockId = -1;
        data = -1;
        parseDataValue = true;
        if ("hand".equalsIgnoreCase(testId)) {
            blockInHand = DefaultBlockParser.getBlockInHand(context.requireActor());
            if (blockInHand.getClass() != BaseBlock.class) {
                return blockInHand;
            }
            blockId = blockInHand.getId();
            blockType = BlockType.fromID(blockId);
            data = blockInHand.getData();
        } else if ("pos1".equalsIgnoreCase(testId)) {
            world = context.requireWorld();
            try {
                primaryPosition = context.requireSession().getRegionSelector(world).getPrimaryPosition();
            }
            catch (IncompleteRegionException e) {
                throw new InputParseException("Your selection is not complete.");
            }
            blockInHand = world.getBlock(primaryPosition);
            if (blockInHand.getClass() != BaseBlock.class) {
                return blockInHand;
            }
            blockId = blockInHand.getId();
            blockType = BlockType.fromID(blockId);
            data = blockInHand.getData();
        } else {
            block62: {
                try {
                    blockId = Integer.parseInt(testId);
                    blockType = BlockType.fromID(blockId);
                }
                catch (NumberFormatException e) {
                    blockType = BlockType.lookup(testId);
                    if (blockType != null) break block62;
                    t = this.worldEdit.getServer().resolveItem(testId);
                    if (t > 0) {
                        blockType = BlockType.fromID(t);
                        blockId = t;
                    }
                    if (blockLocator.length != 2 || (t = this.worldEdit.getServer().resolveItem(blockAndExtraData[0])) <= 0) break block62;
                    blockType = BlockType.fromID(t);
                    blockId = t;
                    typeAndData = new String[]{blockAndExtraData[0]};
                    testId = blockAndExtraData[0];
                }
            }
            if (blockId == -1 && blockType == null) {
                col = ClothColor.lookup(testId);
                if (col == null) {
                    throw new NoMatchException("Can't figure out what block '" + input + "' refers to");
                }
                blockType = BlockType.CLOTH;
                data = col.getID();
                parseDataValue = false;
            }
            if (blockId == -1) {
                blockId = blockType.getID();
            }
            if (!context.requireWorld().isValidBlockType(blockId)) {
                throw new NoMatchException("Does not match a valid block type: '" + input + "'");
            }
        }
        if (!context.isPreferringWildcard() && data == -1) {
            data = 0;
        }
        if (parseDataValue) {
            try {
                if (typeAndData.length > 1 && !typeAndData[1].isEmpty()) {
                    data = Integer.parseInt(typeAndData[1]);
                }
                if (data > 15) {
                    throw new NoMatchException("Invalid data value '" + typeAndData[1] + "'");
                }
                if (data < 0 && (context.isRestricted() || data != -1)) {
                    data = 0;
                }
            }
            catch (NumberFormatException e) {
                if (blockType == null) {
                    throw new NoMatchException("Unknown data value '" + typeAndData[1] + "'");
                }
                block8 : switch (1.$SwitchMap$com$sk89q$worldedit$blocks$BlockType[blockType.ordinal()]) {
                    case 9: 
                    case 10: 
                    case 11: {
                        col = ClothColor.lookup(typeAndData[1]);
                        if (col == null) {
                            throw new NoMatchException("Unknown wool color '" + typeAndData[1] + "'");
                        }
                        data = col.getID();
                        break;
                    }
                    case 12: 
                    case 13: {
                        dataType = BlockType.lookup(typeAndData[1]);
                        if (dataType == null) {
                            throw new NoMatchException("Unknown step type '" + typeAndData[1] + "'");
                        }
                        switch (1.$SwitchMap$com$sk89q$worldedit$blocks$BlockType[dataType.ordinal()]) {
                            case 1: {
                                data = 0;
                                break block8;
                            }
                            case 2: {
                                data = 1;
                                break block8;
                            }
                            case 3: {
                                data = 2;
                                break block8;
                            }
                            case 4: {
                                data = 3;
                                break block8;
                            }
                            case 5: {
                                data = 4;
                                break block8;
                            }
                            case 6: {
                                data = 5;
                                break block8;
                            }
                            case 7: {
                                data = 6;
                                break block8;
                            }
                            case 8: {
                                data = 7;
                                break block8;
                            }
                        }
                        throw new NoMatchException("Invalid step type '" + typeAndData[1] + "'");
                    }
                    default: {
                        throw new NoMatchException("Unknown data value '" + typeAndData[1] + "'");
                    }
                }
            }
        }
        actor = context.requireActor();
        if (context.isRestricted() && actor != null && !actor.hasPermission("worldedit.anyblock") && this.worldEdit.getConfiguration().disallowedBlocks.contains(blockId)) {
            throw new DisallowedUsageException("You are not allowed to use '" + input + "'");
        }
        if (blockType == null) {
            return new BaseBlock(blockId, data);
        }
        switch (1.$SwitchMap$com$sk89q$worldedit$blocks$BlockType[blockType.ordinal()]) {
            case 14: 
            case 15: {
                text = new String[]{blockAndExtraData.length > 1 ? blockAndExtraData[1] : "", blockAndExtraData.length > 2 ? blockAndExtraData[2] : "", blockAndExtraData.length > 3 ? blockAndExtraData[3] : "", blockAndExtraData.length > 4 ? blockAndExtraData[4] : ""};
                return new SignBlock(blockType.getID(), data, text);
            }
            case 16: {
                if (blockAndExtraData.length > 1) {
                    mobName = blockAndExtraData[1];
                    for (MobType mobType : MobType.values()) {
                        if (!mobType.getName().toLowerCase().equals(mobName.toLowerCase())) continue;
                        mobName = mobType.getName();
                        break;
                    }
                    if (!this.worldEdit.getServer().isValidMobType(mobName)) {
                        throw new NoMatchException("Unknown mob type '" + mobName + "'");
                    }
                    return new MobSpawnerBlock(data, mobName);
                }
                return new MobSpawnerBlock(data, MobType.PIG.getName());
            }
            case 17: {
                if (blockAndExtraData.length <= 1) {
                    return new NoteBlock(data, 0);
                }
                note = Byte.parseByte(blockAndExtraData[1]);
                if (note < 0 || note > 24) {
                    throw new InputParseException("Out of range note value: '" + blockAndExtraData[1] + "'");
                }
                return new NoteBlock(data, note);
            }
            case 18: {
                if (blockAndExtraData.length <= 1) {
                    return new SkullBlock(data);
                }
                rot = 0;
                type = "";
                try {
                    rot = Byte.parseByte(blockAndExtraData[1]);
                }
                catch (NumberFormatException e) {
                    type = blockAndExtraData[1];
                    if (blockAndExtraData.length <= 2) ** GOTO lbl162
                    try {
                        rot = Byte.parseByte(blockAndExtraData[2]);
                    }
                    catch (NumberFormatException e2) {
                        throw new InputParseException("Second part of skull metadata should be a number.");
                    }
                }
lbl162:
                // 3 sources

                skullType = 0;
                if (!type.isEmpty()) {
                    skullType = type.equalsIgnoreCase("skeleton") != false ? 0 : (type.equalsIgnoreCase("wither") != false ? 1 : (type.equalsIgnoreCase("zombie") != false ? 2 : (type.equalsIgnoreCase("creeper") != false ? 4 : 3)));
                }
                if (skullType == 3) {
                    return new SkullBlock(data, rot, type.replace(" ", "_"));
                }
                return new SkullBlock(data, (byte)skullType, rot);
            }
        }
        return new BaseBlock(blockId, data);
    }
}

