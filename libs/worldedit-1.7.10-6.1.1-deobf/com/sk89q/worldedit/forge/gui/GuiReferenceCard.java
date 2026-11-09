/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiReferenceCard
extends GuiScreen {
    private GuiButton closeButton;
    private int backgroundWidth = 256;
    private int backgroundHeight = 256;

    public void initGui() {
        this.closeButton = new GuiButton(0, (this.width - this.backgroundWidth + 100) / 2, (this.height + this.backgroundHeight - 60) / 2, this.backgroundWidth - 100, 20, "Close");
        this.buttonList.add(this.closeButton);
    }

    public void drawScreen(int mouseX, int mouseY, float par3) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2 - this.closeButton.height;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.renderEngine.bindTexture(new ResourceLocation("worldedit", "textures/gui/reference.png"));
        this.drawTexturedModalRect(x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        super.drawScreen(mouseX, mouseY, par3);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.thePlayer.closeScreen();
        }
    }

    public boolean doesGuiPauseGame() {
        return true;
    }
}

