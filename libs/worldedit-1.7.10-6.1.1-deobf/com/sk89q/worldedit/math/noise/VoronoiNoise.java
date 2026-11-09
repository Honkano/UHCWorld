/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.math.noise.JLibNoiseGenerator;
import net.royawesome.jlibnoise.module.source.Voronoi;

public class VoronoiNoise
extends JLibNoiseGenerator<Voronoi> {
    @Override
    protected Voronoi createModule() {
        return new Voronoi();
    }

    public double getFrequency() {
        return ((Voronoi)this.getModule()).getFrequency();
    }

    public void setFrequency(double frequency) {
        ((Voronoi)this.getModule()).setFrequency(frequency);
    }

    @Override
    public void setSeed(int seed) {
        ((Voronoi)this.getModule()).setSeed(seed);
    }

    @Override
    public int getSeed() {
        return ((Voronoi)this.getModule()).getSeed();
    }
}

