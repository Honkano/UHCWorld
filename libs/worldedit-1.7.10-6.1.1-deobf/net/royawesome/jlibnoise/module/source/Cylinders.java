/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module.source;

import net.royawesome.jlibnoise.MathHelper;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.module.Module;

public class Cylinders
extends Module {
    public static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;
    double frequency = 1.0;

    public Cylinders() {
        super(0);
    }

    public double getFrequency() {
        return this.frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public int GetSourceModuleCount() {
        return 0;
    }

    public double GetValue(double x, double y, double z) {
        double z1 = z;
        double x1 = x;
        double distFromCenter = MathHelper.sqrt((x1 *= this.frequency) * x1 + (z1 *= this.frequency) * z1);
        double distFromSmallerSphere = distFromCenter - (double)MathHelper.floor(distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        double nearestDist = Utils.GetMin(distFromSmallerSphere, distFromLargerSphere);
        return 1.0 - nearestDist * 4.0;
    }
}

