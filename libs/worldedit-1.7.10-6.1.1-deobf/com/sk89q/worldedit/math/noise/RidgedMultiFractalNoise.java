/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.math.noise.JLibNoiseGenerator;
import net.royawesome.jlibnoise.module.source.RidgedMulti;

public class RidgedMultiFractalNoise
extends JLibNoiseGenerator<RidgedMulti> {
    @Override
    protected RidgedMulti createModule() {
        return new RidgedMulti();
    }

    public double getFrequency() {
        return ((RidgedMulti)this.getModule()).getFrequency();
    }

    public void setFrequency(double frequency) {
        ((RidgedMulti)this.getModule()).setFrequency(frequency);
    }

    public double getLacunarity() {
        return ((RidgedMulti)this.getModule()).getLacunarity();
    }

    public void setLacunarity(double lacunarity) {
        ((RidgedMulti)this.getModule()).setLacunarity(lacunarity);
    }

    public int getOctaveCount() {
        return ((RidgedMulti)this.getModule()).getOctaveCount();
    }

    public void setOctaveCount(int octaveCount) {
        ((RidgedMulti)this.getModule()).setOctaveCount(octaveCount);
    }

    @Override
    public void setSeed(int seed) {
        ((RidgedMulti)this.getModule()).setSeed(seed);
    }

    @Override
    public int getSeed() {
        return ((RidgedMulti)this.getModule()).getSeed();
    }
}

