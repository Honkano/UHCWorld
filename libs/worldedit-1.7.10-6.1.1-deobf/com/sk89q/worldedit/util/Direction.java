/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util;

import com.sk89q.worldedit.Vector;
import javax.annotation.Nullable;

public enum Direction {
    NORTH(new Vector(0, 0, -1), Flag.CARDINAL),
    EAST(new Vector(1, 0, 0), Flag.CARDINAL),
    SOUTH(new Vector(0, 0, 1), Flag.CARDINAL),
    WEST(new Vector(-1, 0, 0), Flag.CARDINAL),
    UP(new Vector(0, 1, 0), Flag.UPRIGHT),
    DOWN(new Vector(0, -1, 0), Flag.UPRIGHT),
    NORTHEAST(new Vector(1, 0, -1), Flag.ORDINAL),
    NORTHWEST(new Vector(-1, 0, -1), Flag.ORDINAL),
    SOUTHEAST(new Vector(1, 0, 1), Flag.ORDINAL),
    SOUTHWEST(new Vector(-1, 0, 1), Flag.ORDINAL),
    WEST_NORTHWEST(new Vector(-Math.cos(0.39269908169872414), 0.0, Math.sin(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    WEST_SOUTHWEST(new Vector(-Math.cos(0.39269908169872414), 0.0, Math.sin(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    NORTH_NORTHWEST(new Vector(Math.sin(0.39269908169872414), 0.0, -Math.cos(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    NORTH_NORTHEAST(new Vector(Math.sin(0.39269908169872414), 0.0, -Math.cos(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    EAST_NORTHEAST(new Vector(Math.cos(0.39269908169872414), 0.0, Math.sin(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    EAST_SOUTHEAST(new Vector(Math.cos(0.39269908169872414), 0.0, Math.sin(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    SOUTH_SOUTHEAST(new Vector(Math.sin(0.39269908169872414), 0.0, Math.cos(0.39269908169872414)), Flag.SECONDARY_ORDINAL),
    SOUTH_SOUTHWEST(new Vector(Math.sin(0.39269908169872414), 0.0, Math.cos(0.39269908169872414)), Flag.SECONDARY_ORDINAL);

    private final Vector direction;
    private final int flags;

    private Direction(Vector vector, int flags) {
        this.direction = vector.normalize();
        this.flags = flags;
    }

    public boolean isCardinal() {
        return (this.flags & Flag.CARDINAL) > 0;
    }

    public boolean isOrdinal() {
        return (this.flags & Flag.ORDINAL) > 0;
    }

    public boolean isSecondaryOrdinal() {
        return (this.flags & Flag.SECONDARY_ORDINAL) > 0;
    }

    public boolean isUpright() {
        return (this.flags & Flag.UPRIGHT) > 0;
    }

    public Vector toVector() {
        return this.direction;
    }

    @Nullable
    public static Direction findClosest(Vector vector, int flags) {
        if ((flags & Flag.UPRIGHT) == 0) {
            vector = vector.setY(0);
        }
        vector = vector.normalize();
        Direction closest = null;
        double closestDot = -2.0;
        for (Direction direction : Direction.values()) {
            double dot;
            if ((~flags & direction.flags) > 0 || !((dot = direction.toVector().dot(vector)) >= closestDot)) continue;
            closest = direction;
            closestDot = dot;
        }
        return closest;
    }

    public static final class Flag {
        public static int CARDINAL = 1;
        public static int ORDINAL = 2;
        public static int SECONDARY_ORDINAL = 4;
        public static int UPRIGHT = 8;
        public static int ALL = CARDINAL | ORDINAL | SECONDARY_ORDINAL | UPRIGHT;

        private Flag() {
        }
    }
}

