/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.util.PropertiesConfiguration;
import java.io.File;

public class ForgeConfiguration
extends PropertiesConfiguration {
    public boolean creativeEnable = false;
    public boolean cheatMode = false;

    public ForgeConfiguration(ForgeWorldEdit mod) {
        super(new File(mod.getWorkingDir() + File.separator + "worldedit.properties"));
    }

    @Override
    protected void loadExtra() {
        this.creativeEnable = this.getBool("use-in-creative", false);
        this.cheatMode = this.getBool("cheat-mode", false);
    }

    @Override
    public File getWorkingDirectory() {
        return ForgeWorldEdit.inst.getWorkingDir();
    }
}

