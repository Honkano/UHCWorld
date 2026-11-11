/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util;

import javax.annotation.Nullable;

public interface Faceted {
    @Nullable
    public <T> T getFacet(Class<? extends T> var1);
}

