/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.ForgeWorldEdit;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;

public class KeyHandler {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static KeyBinding mainKey = new KeyBinding("WorldEdit Reference", 38, "WorldEdit");

    public KeyHandler() {
        ClientRegistry.registerKeyBinding((KeyBinding)mainKey);
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent evt) {
        if (KeyHandler.mc.thePlayer != null && KeyHandler.mc.theWorld != null && mainKey.isPressed()) {
            KeyHandler.mc.thePlayer.openGui((Object)ForgeWorldEdit.inst, 0, (World)KeyHandler.mc.theWorld, 0, 0, 0);
        }
    }
}

