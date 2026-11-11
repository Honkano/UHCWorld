/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module.source;

import net.royawesome.jlibnoise.MathHelper;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.module.Module;

public class Checkerboard
extends Module {
    public Checkerboard() {
        super(0);
    }

    public int GetSourceModuleCount() {
        return 0;
    }

    public double GetValue(double x, double y, double z) {
        int iz;
        int iy;
        int ix = MathHelper.floor(Utils.MakeInt32Range(x));
        return (ix & 1 ^ (iy = MathHelper.floor(Utils.MakeInt32Range(y))) & 1 ^ (iz = MathHelper.floor(Utils.MakeInt32Range(z))) & 1) != 0 ? -1.0 : 1.0;
    }
}

