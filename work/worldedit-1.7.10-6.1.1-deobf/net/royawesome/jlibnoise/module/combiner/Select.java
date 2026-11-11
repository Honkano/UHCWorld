/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module.combiner;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Select
extends Module {
    public static final double DEFAULT_SELECT_EDGE_FALLOFF = 0.0;
    public static final double DEFAULT_SELECT_LOWER_BOUND = -1.0;
    public static final double DEFAULT_SELECT_UPPER_BOUND = 1.0;
    double edgeFalloff = 0.0;
    double lowerBound = -1.0;
    double upperBound = 1.0;

    public Select() {
        super(3);
    }

    public Module getControlModule() {
        if (this.SourceModule == null || this.SourceModule[2] == null) {
            throw new NoModuleException();
        }
        return this.SourceModule[2];
    }

    public void setControlModule(Module m) {
        if (m == null) {
            throw new IllegalArgumentException("the module cannot be null");
        }
        this.SourceModule[2] = m;
    }

    public double getEdgeFalloff() {
        return this.edgeFalloff;
    }

    public void setEdgeFalloff(double edgeFalloff) {
        double boundSize = this.upperBound - this.lowerBound;
        this.edgeFalloff = edgeFalloff > boundSize / 2.0 ? boundSize / 2.0 : edgeFalloff;
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public void setBounds(double upper, double lower) {
        if (lower > upper) {
            throw new IllegalArgumentException("lower must be less than upper");
        }
        this.lowerBound = lower;
        this.upperBound = upper;
        this.setEdgeFalloff(this.edgeFalloff);
    }

    public int GetSourceModuleCount() {
        return 3;
    }

    public double GetValue(double x, double y, double z) {
        if (this.SourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (this.SourceModule[1] == null) {
            throw new NoModuleException();
        }
        if (this.SourceModule[2] == null) {
            throw new NoModuleException();
        }
        double controlValue = this.SourceModule[2].GetValue(x, y, z);
        if (this.edgeFalloff > 0.0) {
            if (controlValue < this.lowerBound - this.edgeFalloff) {
                return this.SourceModule[0].GetValue(x, y, z);
            }
            if (controlValue < this.lowerBound + this.edgeFalloff) {
                double lowerCurve = this.lowerBound - this.edgeFalloff;
                double upperCurve = this.lowerBound + this.edgeFalloff;
                double alpha = Utils.SCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.LinearInterp(this.SourceModule[0].GetValue(x, y, z), this.SourceModule[1].GetValue(x, y, z), alpha);
            }
            if (controlValue < this.upperBound - this.edgeFalloff) {
                return this.SourceModule[1].GetValue(x, y, z);
            }
            if (controlValue < this.upperBound + this.edgeFalloff) {
                double lowerCurve = this.upperBound - this.edgeFalloff;
                double upperCurve = this.upperBound + this.edgeFalloff;
                double alpha = Utils.SCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.LinearInterp(this.SourceModule[1].GetValue(x, y, z), this.SourceModule[0].GetValue(x, y, z), alpha);
            }
            return this.SourceModule[0].GetValue(x, y, z);
        }
        if (controlValue < this.lowerBound || controlValue > this.upperBound) {
            return this.SourceModule[0].GetValue(x, y, z);
        }
        return this.SourceModule[1].GetValue(x, y, z);
    }
}

