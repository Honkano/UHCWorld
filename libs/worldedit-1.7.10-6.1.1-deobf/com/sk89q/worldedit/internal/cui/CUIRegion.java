/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.cui;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;

public interface CUIRegion {
    public void describeCUI(LocalSession var1, Actor var2);

    public void describeLegacyCUI(LocalSession var1, Actor var2);

    public int getProtocolVersion();

    public String getTypeID();

    public String getLegacyTypeID();
}

