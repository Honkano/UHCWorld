/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.RegionSelector;

abstract class AbstractLegacyRegionSelector
implements RegionSelector {
    AbstractLegacyRegionSelector() {
    }

    @Deprecated
    public final void explainPrimarySelection(LocalPlayer player, LocalSession session, Vector position) {
        this.explainPrimarySelection((Actor)player, session, position);
    }

    @Deprecated
    public final void explainSecondarySelection(LocalPlayer player, LocalSession session, Vector position) {
        this.explainSecondarySelection((Actor)player, session, position);
    }

    @Deprecated
    public final void explainRegionAdjust(LocalPlayer player, LocalSession session) {
        this.explainRegionAdjust((Actor)player, session);
    }
}

