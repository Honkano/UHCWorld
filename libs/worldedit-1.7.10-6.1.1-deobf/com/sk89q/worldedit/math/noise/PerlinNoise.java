/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.math.noise.JLibNoiseGenerator;
import net.royawesome.jlibnoise.module.source.Perlin;

public class PerlinNoise
extends JLibNoiseGenerator<Perlin> {
    @Override
    protected Perlin createModule() {
        return new Perlin();
    }

    public double getFrequency() {
        return ((Perlin)this.getModule()).getFrequency();
    }

    public void setFrequency(double frequency) {
        ((Perlin)this.getModule()).setFrequency(frequency);
    }

    public double getLacunarity() {
        return ((Perlin)this.getModule()).getLacunarity();
    }

    public void setLacunarity(double lacunarity) {
        ((Perlin)this.getModule()).setLacunarity(lacunarity);
    }

    public int getOctaveCount() {
        return ((Perlin)this.getModule()).getOctaveCount();
    }

    public void setOctaveCount(int octaveCount) {
        ((Perlin)this.getModule()).setOctaveCount(octaveCount);
    }

    public void setPersistence(double persistence) {
        ((Perlin)this.getModule()).setPersistence(persistence);
    }

    public double getPersistence() {
        return ((Perlin)this.getModule()).getPersistence();
    }

    @Override
    public void setSeed(int seed) {
        ((Perlin)this.getModule()).setSeed(seed);
    }

    @Override
    public int getSeed() {
        return ((Perlin)this.getModule()).getSeed();
    }
}

