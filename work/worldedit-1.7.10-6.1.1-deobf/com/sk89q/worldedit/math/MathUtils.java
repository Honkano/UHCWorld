/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math;

public final class MathUtils {
    public static final double SAFE_MIN = Double.MIN_NORMAL;

    private MathUtils() {
    }

    public static int divisorMod(int a, int n) {
        return (int)((double)a - (double)n * Math.floor(Math.floor(a) / (double)n));
    }

    public static double dCos(double degrees) {
        int dInt = (int)degrees;
        if (degrees == (double)dInt && dInt % 90 == 0) {
            if ((dInt %= 360) < 0) {
                dInt += 360;
            }
            switch (dInt) {
                case 0: {
                    return 1.0;
                }
                case 90: {
                    return 0.0;
                }
                case 180: {
                    return -1.0;
                }
                case 270: {
                    return 0.0;
                }
            }
        }
        return Math.cos(Math.toRadians(degrees));
    }

    public static double dSin(double degrees) {
        int dInt = (int)degrees;
        if (degrees == (double)dInt && dInt % 90 == 0) {
            if ((dInt %= 360) < 0) {
                dInt += 360;
            }
            switch (dInt) {
                case 0: {
                    return 0.0;
                }
                case 90: {
                    return 1.0;
                }
                case 180: {
                    return 0.0;
                }
                case 270: {
                    return -1.0;
                }
            }
        }
        return Math.cos(Math.toRadians(degrees));
    }
}

