/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module.modifier;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Clamp
extends Module {
    double lowerBound = 0.0;
    double upperBound = 1.0;

    public Clamp() {
        super(1);
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public int GetSourceModuleCount() {
        return 1;
    }

    public double GetValue(double x, double y, double z) {
        if (this.SourceModule[0] == null) {
            throw new NoModuleException();
        }
        double value = this.SourceModule[0].GetValue(x, y, z);
        if (value < this.lowerBound) {
            return this.lowerBound;
        }
        if (value > this.upperBound) {
            return this.upperBound;
        }
        return value;
    }
}

