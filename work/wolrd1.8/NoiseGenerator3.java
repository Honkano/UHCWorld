/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import java.util.Random;
import net.minecraft.server.v1_8_R3.NoiseGenerator;
import net.minecraft.server.v1_8_R3.NoiseGenerator3Handler;

public class NoiseGenerator3
extends NoiseGenerator {
    private NoiseGenerator3Handler[] a;
    private int b;

    public NoiseGenerator3(Random random, int n) {
        this.b = n;
        this.a = new NoiseGenerator3Handler[n];
        for (int i = 0; i < n; ++i) {
            this.a[i] = new NoiseGenerator3Handler(random);
        }
    }

    public double a(double d, double d2) {
        double d3 = 0.0;
        double d4 = 1.0;
        for (int i = 0; i < this.b; ++i) {
            d3 += this.a[i].a(d * d4, d2 * d4) / d4;
            d4 /= 2.0;
        }
        return d3;
    }

    public double[] a(double[] dArray, double d, double d2, int n, int n2, double d3, double d4, double d5) {
        return this.a(dArray, d, d2, n, n2, d3, d4, d5, 0.5);
    }

    public double[] a(double[] dArray, double d, double d2, int n, int n2, double d3, double d4, double d5, double d6) {
        if (dArray == null || dArray.length < n * n2) {
            dArray = new double[n * n2];
        } else {
            for (int i = 0; i < dArray.length; ++i) {
                dArray[i] = 0.0;
            }
        }
        double d7 = 1.0;
        double d8 = 1.0;
        for (int i = 0; i < this.b; ++i) {
            this.a[i].a(dArray, d, d2, n, n2, d3 * d8 * d7, d4 * d8 * d7, 0.55 / d7);
            d8 *= d5;
            d7 *= d6;
        }
        return dArray;
    }
}

