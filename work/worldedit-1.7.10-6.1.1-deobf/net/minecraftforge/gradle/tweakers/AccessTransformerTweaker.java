/*
 * Decompiled with CFR 0.152.
 */
package net.minecraftforge.gradle.tweakers;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class AccessTransformerTweaker
implements ITweaker {
    public void acceptOptions(List<String> list, File file, File file2, String string) {
    }

    public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
        launchClassLoader.registerTransformer("net.minecraftforge.gradle.GradleStartCommon$AccessTransformerTransformer");
    }

    public String getLaunchTarget() {
        return null;
    }

    public String[] getLaunchArguments() {
        return new String[0];
    }
}

