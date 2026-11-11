/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module.combiner;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Power
extends Module {
    public Power() {
        super(2);
    }

    public int GetSourceModuleCount() {
        return 2;
    }

    public double GetValue(double x, double y, double z) {
        if (this.SourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (this.SourceModule[1] == null) {
            throw new NoModuleException();
        }
        return Math.pow(this.SourceModule[0].GetValue(x, y, z), this.SourceModule[1].GetValue(x, y, z));
    }
}

