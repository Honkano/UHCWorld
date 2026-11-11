/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.registry.WorldData;

public interface World
extends Extent {
    public String getName();

    public int getMaxY();

    public boolean isValidBlockType(int var1);

    public boolean usesBlockData(int var1);

    public Mask createLiquidMask();

    public boolean useItem(Vector var1, BaseItem var2, Direction var3);

    @Deprecated
    public int getBlockType(Vector var1);

    @Deprecated
    public int getBlockData(Vector var1);

    public boolean setBlock(Vector var1, BaseBlock var2, boolean var3) throws WorldEditException;

    @Deprecated
    public boolean setBlockType(Vector var1, int var2);

    @Deprecated
    public void setBlockData(Vector var1, int var2);

    @Deprecated
    public boolean setTypeIdAndData(Vector var1, int var2, int var3);

    public int getBlockLightLevel(Vector var1);

    public boolean clearContainerBlockContents(Vector var1);

    public void dropItem(Vector var1, BaseItemStack var2, int var3);

    public void dropItem(Vector var1, BaseItemStack var2);

    public void simulateBlockMine(Vector var1);

    public boolean regenerate(Region var1, EditSession var2);

    public boolean generateTree(TreeGenerator.TreeType var1, EditSession var2, Vector var3) throws MaxChangedBlocksException;

    @Deprecated
    public boolean generateTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

    @Deprecated
    public boolean generateBigTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

    @Deprecated
    public boolean generateBirchTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

    @Deprecated
    public boolean generateRedwoodTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

    @Deprecated
    public boolean generateTallRedwoodTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

    public void checkLoadedChunk(Vector var1);

    public void fixAfterFastMode(Iterable<BlockVector2D> var1);

    public void fixLighting(Iterable<BlockVector2D> var1);

    public boolean playEffect(Vector var1, int var2, int var3);

    public boolean queueBlockBreakEffect(Platform var1, Vector var2, int var3, double var4);

    public WorldData getWorldData();

    public boolean equals(Object var1);

    public int hashCode();
}

