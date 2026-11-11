/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.entity.metadata.EntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;

public class ForgeEntityType
implements EntityType {
    private final Entity entity;

    public ForgeEntityType(Entity entity) {
        Preconditions.checkNotNull((Object)entity);
        this.entity = entity;
    }

    @Override
    public boolean isPlayerDerived() {
        return this.entity instanceof EntityPlayer;
    }

    @Override
    public boolean isProjectile() {
        return this.entity instanceof EntityEnderEye || this.entity instanceof IProjectile;
    }

    @Override
    public boolean isItem() {
        return this.entity instanceof EntityItem;
    }

    @Override
    public boolean isFallingBlock() {
        return this.entity instanceof EntityFallingBlock;
    }

    @Override
    public boolean isPainting() {
        return this.entity instanceof EntityPainting;
    }

    @Override
    public boolean isItemFrame() {
        return this.entity instanceof EntityItemFrame;
    }

    @Override
    public boolean isBoat() {
        return this.entity instanceof EntityBoat;
    }

    @Override
    public boolean isMinecart() {
        return this.entity instanceof EntityMinecart;
    }

    @Override
    public boolean isTNT() {
        return this.entity instanceof EntityTNTPrimed;
    }

    @Override
    public boolean isExperienceOrb() {
        return this.entity instanceof EntityXPOrb;
    }

    @Override
    public boolean isLiving() {
        return this.entity instanceof EntityLiving;
    }

    @Override
    public boolean isAnimal() {
        return this.entity instanceof IAnimals;
    }

    @Override
    public boolean isAmbient() {
        return this.entity instanceof EntityAmbientCreature;
    }

    @Override
    public boolean isNPC() {
        return this.entity instanceof INpc || this.entity instanceof IMerchant;
    }

    @Override
    public boolean isGolem() {
        return this.entity instanceof EntityGolem;
    }

    @Override
    public boolean isTamed() {
        return this.entity instanceof EntityTameable && ((EntityTameable)this.entity).isTamed();
    }

    @Override
    public boolean isTagged() {
        return this.entity instanceof EntityLiving && ((EntityLiving)this.entity).hasCustomNameTag();
    }

    @Override
    public boolean isArmorStand() {
        return false;
    }
}

