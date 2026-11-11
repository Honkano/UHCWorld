/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.clipboard.io;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.IOException;

public interface ClipboardReader {
    public Clipboard read(WorldData var1) throws IOException;
}

