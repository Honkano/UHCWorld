/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.selector.limit;

import com.google.common.base.Optional;

public interface SelectorLimits {
    public Optional<Integer> getPolygonVertexLimit();

    public Optional<Integer> getPolyhedronVertexLimit();
}

