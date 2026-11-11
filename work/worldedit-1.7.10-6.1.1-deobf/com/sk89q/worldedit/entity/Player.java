/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.entity;

import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.WorldVectorFace;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.world.World;

public interface Player
extends Entity,
Actor {
    public World getWorld();

    public boolean isHoldingPickAxe();

    public PlayerDirection getCardinalDirection(int var1);

    public int getItemInHand();

    public BaseBlock getBlockInHand() throws WorldEditException;

    public void giveItem(int var1, int var2);

    public BlockBag getInventoryBlockBag();

    public boolean hasCreativeMode();

    public void findFreePosition(WorldVector var1);

    public void setOnGround(WorldVector var1);

    public void findFreePosition();

    public boolean ascendLevel();

    public boolean descendLevel();

    public boolean ascendToCeiling(int var1);

    public boolean ascendToCeiling(int var1, boolean var2);

    public boolean ascendUpwards(int var1);

    public boolean ascendUpwards(int var1, boolean var2);

    public void floatAt(int var1, int var2, int var3, boolean var4);

    public WorldVector getBlockIn();

    public WorldVector getBlockOn();

    public WorldVector getBlockTrace(int var1, boolean var2);

    public WorldVectorFace getBlockTraceFace(int var1, boolean var2);

    public WorldVector getBlockTrace(int var1);

    public WorldVector getSolidBlockTrace(int var1);

    public PlayerDirection getCardinalDirection();

    @Deprecated
    public WorldVector getPosition();

    @Deprecated
    public double getPitch();

    @Deprecated
    public double getYaw();

    public boolean passThroughForwardWall(int var1);

    public void setPosition(Vector var1, float var2, float var3);

    public void setPosition(Vector var1);
}

