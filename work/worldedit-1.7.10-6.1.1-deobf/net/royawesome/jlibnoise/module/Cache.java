/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Cache
extends Module {
    double cachedValue;
    boolean isCached = false;
    double xCache;
    double yCache;
    double zCache;

    public Cache() {
        super(1);
    }

    public int GetSourceModuleCount() {
        return 1;
    }

    public void SetSourceModule(int index, Module sourceModule) {
        super.SetSourceModule(index, sourceModule);
        this.isCached = false;
    }

    public double GetValue(double x, double y, double z) {
        if (this.SourceModule[0] == null) {
            throw new NoModuleException();
        }
        if (!this.isCached || x != this.xCache || y != this.yCache || z != this.zCache) {
            this.cachedValue = this.SourceModule[0].GetValue(x, y, z);
            this.xCache = x;
            this.yCache = y;
            this.zCache = z;
        }
        this.isCached = true;
        return this.cachedValue;
    }
}

