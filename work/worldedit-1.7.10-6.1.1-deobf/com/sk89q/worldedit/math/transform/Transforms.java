/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.transform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.util.Location;

public final class Transforms {
    private Transforms() {
    }

    public static Location transform(Location location, Transform transform) {
        Preconditions.checkNotNull((Object)location);
        Preconditions.checkNotNull((Object)transform);
        return new Location(location.getExtent(), transform.apply(location.toVector()), location.getDirection());
    }
}

