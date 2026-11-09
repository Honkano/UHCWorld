/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public abstract class Module {
    protected Module[] SourceModule = null;

    public Module(int sourceModuleCount) {
        if (sourceModuleCount > 0) {
            this.SourceModule = new Module[sourceModuleCount];
            for (int i = 0; i < sourceModuleCount; ++i) {
                this.SourceModule[i] = null;
            }
        } else {
            this.SourceModule = null;
        }
    }

    public Module getSourceModule(int index) {
        if (index >= this.GetSourceModuleCount() || index < 0 || this.SourceModule[index] == null) {
            throw new NoModuleException();
        }
        return this.SourceModule[index];
    }

    public void SetSourceModule(int index, Module sourceModule) {
        if (this.SourceModule == null) {
            return;
        }
        if (index >= this.GetSourceModuleCount() || index < 0) {
            throw new IllegalArgumentException("Index must be between 0 and GetSourceMoudleCount()");
        }
        this.SourceModule[index] = sourceModule;
    }

    public abstract int GetSourceModuleCount();

    public abstract double GetValue(double var1, double var3, double var5);
}

