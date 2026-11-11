/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

public interface BlockMaterial {
    public boolean isRenderedAsNormalBlock();

    public boolean isFullCube();

    public boolean isOpaque();

    public boolean isPowerSource();

    public boolean isLiquid();

    public boolean isSolid();

    public float getHardness();

    public float getResistance();

    public float getSlipperiness();

    public boolean isGrassBlocking();

    public float getAmbientOcclusionLightValue();

    public int getLightOpacity();

    public int getLightValue();

    public boolean isFragileWhenPushed();

    public boolean isUnpushable();

    public boolean isAdventureModeExempt();

    public boolean isTicksRandomly();

    public boolean isUsingNeighborLight();

    public boolean isMovementBlocker();

    public boolean isBurnable();

    public boolean isToolRequired();

    public boolean isReplacedDuringPlacement();
}

