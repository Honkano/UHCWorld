/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.entity.metadata;

public interface EntityType {
    public boolean isPlayerDerived();

    public boolean isProjectile();

    public boolean isItem();

    public boolean isFallingBlock();

    public boolean isPainting();

    public boolean isItemFrame();

    public boolean isBoat();

    public boolean isMinecart();

    public boolean isTNT();

    public boolean isExperienceOrb();

    public boolean isLiving();

    public boolean isAnimal();

    public boolean isAmbient();

    public boolean isNPC();

    public boolean isGolem();

    public boolean isTamed();

    public boolean isTagged();

    public boolean isArmorStand();
}

