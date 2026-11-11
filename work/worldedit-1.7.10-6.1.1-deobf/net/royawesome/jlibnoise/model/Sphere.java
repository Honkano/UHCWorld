/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Sphere {
    Module module;

    public Sphere(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.module = module;
    }

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("module cannot be null");
        }
        this.module = module;
    }

    public double getValue(double lat, double log) {
        if (this.module == null) {
            throw new NoModuleException();
        }
        double[] vec = Utils.LatLonToXYZ(lat, log);
        return this.module.GetValue(vec[0], vec[1], vec[2]);
    }
}

