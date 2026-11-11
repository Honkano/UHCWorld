/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import java.lang.reflect.Constructor;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

final class TileEntityUtils {
    private TileEntityUtils() {
    }

    private static NBTTagCompound updateForSet(NBTTagCompound tag, Vector position) {
        Preconditions.checkNotNull((Object)tag);
        Preconditions.checkNotNull((Object)position);
        tag.setTag("x", (NBTBase)new NBTTagInt(position.getBlockX()));
        tag.setTag("y", (NBTBase)new NBTTagInt(position.getBlockY()));
        tag.setTag("z", (NBTBase)new NBTTagInt(position.getBlockZ()));
        return tag;
    }

    static void setTileEntity(World world, Vector position, Class<? extends TileEntity> clazz, @Nullable NBTTagCompound tag) {
        Preconditions.checkNotNull((Object)world);
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkNotNull(clazz);
        TileEntity tileEntity = TileEntityUtils.constructTileEntity(world, position, clazz);
        if (tileEntity == null) {
            return;
        }
        if (tag != null) {
            TileEntityUtils.updateForSet(tag, position);
            tileEntity.readFromNBT(tag);
        }
        tileEntity = ForgeWorldEdit.inst.getFMPCompat().overrideTileEntity(world, tag, tileEntity);
        TileEntityUtils.setTileEntity(world, position, tileEntity);
    }

    static void setTileEntity(World world, Vector position, @Nullable NBTTagCompound tag) {
        if (tag != null) {
            TileEntityUtils.updateForSet(tag, position);
            TileEntity tileEntity = TileEntityUtils.makeTileEntity(world, position, tag);
            if (tileEntity != null) {
                TileEntityUtils.setTileEntity(world, position, tileEntity);
            }
        }
    }

    private static TileEntity makeTileEntity(World world, Vector position, NBTTagCompound tag) {
        TileEntity normal = TileEntity.createAndLoadEntity((NBTTagCompound)tag);
        return ForgeWorldEdit.inst.getFMPCompat().overrideTileEntity(world, tag, normal);
    }

    private static void setTileEntity(World world, Vector position, TileEntity tileEntity) {
        world.setTileEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ(), tileEntity);
        ForgeWorldEdit.inst.getFMPCompat().sendDescPacket(world, tileEntity);
    }

    @Nullable
    static TileEntity constructTileEntity(World world, Vector position, Class<? extends TileEntity> clazz) {
        TileEntity genericTE;
        Constructor<? extends TileEntity> baseConstructor;
        try {
            baseConstructor = clazz.getConstructor(new Class[0]);
        }
        catch (Throwable e) {
            return null;
        }
        try {
            genericTE = baseConstructor.newInstance(new Object[0]);
        }
        catch (Throwable e) {
            return null;
        }
        return genericTE;
    }
}

