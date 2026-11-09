/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

public interface ExpressionEnvironment {
    public int getBlockType(double var1, double var3, double var5);

    public int getBlockData(double var1, double var3, double var5);

    public int getBlockTypeAbs(double var1, double var3, double var5);

    public int getBlockDataAbs(double var1, double var3, double var5);

    public int getBlockTypeRel(double var1, double var3, double var5);

    public int getBlockDataRel(double var1, double var3, double var5);
}

