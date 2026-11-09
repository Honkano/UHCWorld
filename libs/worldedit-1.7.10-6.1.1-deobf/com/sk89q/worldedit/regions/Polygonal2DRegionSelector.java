/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.regions.AbstractLegacyRegionSelector;

@Deprecated
public abstract class Polygonal2DRegionSelector
extends AbstractLegacyRegionSelector
implements CUIRegion {
    public abstract int getPointCount();
}

