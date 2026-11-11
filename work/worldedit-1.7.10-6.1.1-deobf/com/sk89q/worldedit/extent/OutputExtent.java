/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.world.biome.BaseBiome;
import javax.annotation.Nullable;

public interface OutputExtent {
    public boolean setBlock(Vector var1, BaseBlock var2) throws WorldEditException;

    public boolean setBiome(Vector2D var1, BaseBiome var2);

    @Nullable
    public Operation commit();
}

