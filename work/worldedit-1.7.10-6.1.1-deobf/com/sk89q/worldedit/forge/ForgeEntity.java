/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.forge.ForgeEntityType;
import com.sk89q.worldedit.forge.NBTConverter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.NullWorld;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

class ForgeEntity
implements com.sk89q.worldedit.entity.Entity {
    private final WeakReference<Entity> entityRef;

    ForgeEntity(Entity entity) {
        Preconditions.checkNotNull((Object)entity);
        this.entityRef = new WeakReference<Entity>(entity);
    }

    @Override
    public BaseEntity getState() {
        Entity entity = (Entity)this.entityRef.get();
        if (entity != null) {
            String id = EntityList.getEntityString((Entity)entity);
            if (id != null) {
                NBTTagCompound tag = new NBTTagCompound();
                entity.writeToNBT(tag);
                return new BaseEntity(id, NBTConverter.fromNative(tag));
            }
            return null;
        }
        return null;
    }

    @Override
    public Location getLocation() {
        Entity entity = (Entity)this.entityRef.get();
        if (entity != null) {
            Vector position = new Vector(entity.posX, entity.posY, entity.posZ);
            float yaw = entity.rotationYaw;
            float pitch = entity.rotationPitch;
            return new Location((Extent)ForgeAdapter.adapt(entity.worldObj), position, yaw, pitch);
        }
        return new Location(NullWorld.getInstance());
    }

    @Override
    public Extent getExtent() {
        Entity entity = (Entity)this.entityRef.get();
        if (entity != null) {
            return ForgeAdapter.adapt(entity.worldObj);
        }
        return NullWorld.getInstance();
    }

    @Override
    public boolean remove() {
        Entity entity = (Entity)this.entityRef.get();
        if (entity != null) {
            entity.setDead();
        }
        return true;
    }

    @Override
    @Nullable
    public <T> T getFacet(Class<? extends T> cls) {
        Entity entity = (Entity)this.entityRef.get();
        if (entity != null) {
            if (EntityType.class.isAssignableFrom(cls)) {
                return (T)new ForgeEntityType(entity);
            }
            return null;
        }
        return null;
    }
}

