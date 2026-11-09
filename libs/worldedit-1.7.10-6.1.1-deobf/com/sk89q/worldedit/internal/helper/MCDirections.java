/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.helper;

import com.sk89q.worldedit.util.Direction;

public final class MCDirections {
    private MCDirections() {
    }

    public static Direction fromHanging(int i) {
        switch (i) {
            case 0: {
                return Direction.SOUTH;
            }
            case 1: {
                return Direction.WEST;
            }
            case 2: {
                return Direction.NORTH;
            }
            case 3: {
                return Direction.EAST;
            }
        }
        return Direction.NORTH;
    }

    public static int toHanging(Direction direction) {
        switch (direction) {
            case SOUTH: {
                return 0;
            }
            case WEST: {
                return 1;
            }
            case NORTH: {
                return 2;
            }
            case EAST: {
                return 3;
            }
        }
        return 0;
    }

    public static int fromLegacyHanging(byte i) {
        switch (i) {
            case 0: {
                return 2;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 0;
            }
        }
        return 3;
    }

    public static byte toLegacyHanging(int i) {
        switch (i) {
            case 0: {
                return 2;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 0;
            }
        }
        return 3;
    }
}

