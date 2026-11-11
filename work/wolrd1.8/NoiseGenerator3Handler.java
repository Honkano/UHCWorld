/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import java.util.Random;

public class NoiseGenerator3Handler {
    private static int[][] e = new int[][]{{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0}, {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1}, {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    public static final double a = Math.sqrt(3.0);
    private int[] f = new int[512];
    public double b;
    public double c;
    public double d;
    private static final double g = 0.5 * (a - 1.0);
    private static final double h = (3.0 - a) / 6.0;

    public NoiseGenerator3Handler() {
        this(new Random());
    }

    public NoiseGenerator3Handler(Random random) {
        int n;
        this.b = random.nextDouble() * 256.0;
        this.c = random.nextDouble() * 256.0;
        this.d = random.nextDouble() * 256.0;
        for (n = 0; n < 256; ++n) {
            this.f[n] = n;
        }
        for (n = 0; n < 256; ++n) {
            int n2 = random.nextInt(256 - n) + n;
            int n3 = this.f[n];
            this.f[n] = this.f[n2];
            this.f[n2] = n3;
            this.f[n + 256] = this.f[n];
        }
    }

    private static int a(double d) {
        return d > 0.0 ? (int)d : (int)d - 1;
    }

    private static double a(int[] nArray, double d, double d2) {
        return (double)nArray[0] * d + (double)nArray[1] * d2;
    }

    public double a(double d, double d2) {
        double d3;
        double d4;
        double d5;
        int n;
        int n2;
        double d6;
        double d7;
        double d8;
        int n3;
        double d9;
        double d10 = 0.5 * (a - 1.0);
        double d11 = (d + d2) * d10;
        int n4 = NoiseGenerator3Handler.a(d + d11);
        double d12 = (double)n4 - (d9 = (double)(n4 + (n3 = NoiseGenerator3Handler.a(d2 + d11))) * (d8 = (3.0 - a) / 6.0));
        double d13 = d - d12;
        if (d13 > (d7 = d2 - (d6 = (double)n3 - d9))) {
            n2 = 1;
            n = 0;
        } else {
            n2 = 0;
            n = 1;
        }
        double d14 = d13 - (double)n2 + d8;
        double d15 = d7 - (double)n + d8;
        double d16 = d13 - 1.0 + 2.0 * d8;
        double d17 = d7 - 1.0 + 2.0 * d8;
        int n5 = n4 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = this.f[n5 + this.f[n6]] % 12;
        int n8 = this.f[n5 + n2 + this.f[n6 + n]] % 12;
        int n9 = this.f[n5 + 1 + this.f[n6 + 1]] % 12;
        double d18 = 0.5 - d13 * d13 - d7 * d7;
        if (d18 < 0.0) {
            d5 = 0.0;
        } else {
            d18 *= d18;
            d5 = d18 * d18 * NoiseGenerator3Handler.a(e[n7], d13, d7);
        }
        double d19 = 0.5 - d14 * d14 - d15 * d15;
        if (d19 < 0.0) {
            d4 = 0.0;
        } else {
            d19 *= d19;
            d4 = d19 * d19 * NoiseGenerator3Handler.a(e[n8], d14, d15);
        }
        double d20 = 0.5 - d16 * d16 - d17 * d17;
        if (d20 < 0.0) {
            d3 = 0.0;
        } else {
            d20 *= d20;
            d3 = d20 * d20 * NoiseGenerator3Handler.a(e[n9], d16, d17);
        }
        return 70.0 * (d5 + d4 + d3);
    }

    public void a(double[] dArray, double d, double d2, int n, int n2, double d3, double d4, double d5) {
        int n3 = 0;
        for (int i = 0; i < n2; ++i) {
            double d6 = (d2 + (double)i) * d4 + this.c;
            for (int j = 0; j < n; ++j) {
                double d7;
                double d8;
                double d9;
                int n4;
                int n5;
                double d10;
                double d11;
                int n6;
                double d12;
                double d13 = (d + (double)j) * d3 + this.b;
                double d14 = (d13 + d6) * g;
                int n7 = NoiseGenerator3Handler.a(d13 + d14);
                double d15 = (double)n7 - (d12 = (double)(n7 + (n6 = NoiseGenerator3Handler.a(d6 + d14))) * h);
                double d16 = d13 - d15;
                if (d16 > (d11 = d6 - (d10 = (double)n6 - d12))) {
                    n5 = 1;
                    n4 = 0;
                } else {
                    n5 = 0;
                    n4 = 1;
                }
                double d17 = d16 - (double)n5 + h;
                double d18 = d11 - (double)n4 + h;
                double d19 = d16 - 1.0 + 2.0 * h;
                double d20 = d11 - 1.0 + 2.0 * h;
                int n8 = n7 & 0xFF;
                int n9 = n6 & 0xFF;
                int n10 = this.f[n8 + this.f[n9]] % 12;
                int n11 = this.f[n8 + n5 + this.f[n9 + n4]] % 12;
                int n12 = this.f[n8 + 1 + this.f[n9 + 1]] % 12;
                double d21 = 0.5 - d16 * d16 - d11 * d11;
                if (d21 < 0.0) {
                    d9 = 0.0;
                } else {
                    d21 *= d21;
                    d9 = d21 * d21 * NoiseGenerator3Handler.a(e[n10], d16, d11);
                }
                double d22 = 0.5 - d17 * d17 - d18 * d18;
                if (d22 < 0.0) {
                    d8 = 0.0;
                } else {
                    d22 *= d22;
                    d8 = d22 * d22 * NoiseGenerator3Handler.a(e[n11], d17, d18);
                }
                double d23 = 0.5 - d19 * d19 - d20 * d20;
                if (d23 < 0.0) {
                    d7 = 0.0;
                } else {
                    d23 *= d23;
                    d7 = d23 * d23 * NoiseGenerator3Handler.a(e[n12], d19, d20);
                }
                int n13 = n3++;
                dArray[n13] = dArray[n13] + 70.0 * (d9 + d8 + d7) * d5;
            }
        }
    }
}

