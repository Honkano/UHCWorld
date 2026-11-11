/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class WorldEditFakePlayer
extends FakePlayer {
    private static final GameProfile FAKE_GAME_PROFILE = new GameProfile(UUID.nameUUIDFromBytes("worldedit".getBytes()), "[WorldEdit]");

    public WorldEditFakePlayer(WorldServer world) {
        super(world, FAKE_GAME_PROFILE);
    }
}

