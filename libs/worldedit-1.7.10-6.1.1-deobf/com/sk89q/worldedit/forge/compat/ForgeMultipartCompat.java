/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge.compat;

import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ForgeMultipartCompat {
    public TileEntity overrideTileEntity(World var1, @Nullable NBTTagCompound var2, TileEntity var3);

    public void sendDescPacket(World var1, TileEntity var2);
}

