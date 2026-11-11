/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.TileEntityBlock;
import com.sk89q.worldedit.forge.NBTConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBaseBlock
extends BaseBlock
implements TileEntityBlock {
    public TileEntityBaseBlock(int type, int data, TileEntity tile) {
        super(type, data);
        this.setNbtData(NBTConverter.fromNative(TileEntityBaseBlock.copyNbtData(tile)));
    }

    private static NBTTagCompound copyNbtData(TileEntity tile) {
        NBTTagCompound tag = new NBTTagCompound();
        tile.writeToNBT(tag);
        return tag;
    }
}

