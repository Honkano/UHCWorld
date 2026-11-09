/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.interpolation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.math.interpolation.Interpolation;
import com.sk89q.worldedit.math.interpolation.Node;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ReparametrisingInterpolation
implements Interpolation {
    private static final Logger log = Logger.getLogger(ReparametrisingInterpolation.class.getCanonicalName());
    private final Interpolation baseInterpolation;
    private double totalArcLength;
    private final TreeMap<Double, Double> cache = new TreeMap();

    public ReparametrisingInterpolation(Interpolation baseInterpolation) {
        Preconditions.checkNotNull((Object)baseInterpolation);
        this.baseInterpolation = baseInterpolation;
    }

    @Override
    public void setNodes(List<Node> nodes) {
        Preconditions.checkNotNull(nodes);
        this.baseInterpolation.setNodes(nodes);
        this.cache.clear();
        this.cache.put(0.0, 0.0);
        this.totalArcLength = this.baseInterpolation.arcLength(0.0, 1.0);
        this.cache.put(this.totalArcLength, 1.0);
    }

    public Interpolation getBaseInterpolation() {
        return this.baseInterpolation;
    }

    @Override
    public Vector getPosition(double position) {
        if (position > 1.0) {
            return null;
        }
        return this.baseInterpolation.getPosition(this.arcToParameter(position));
    }

    @Override
    public Vector get1stDerivative(double position) {
        if (position > 1.0) {
            return null;
        }
        return this.baseInterpolation.get1stDerivative(this.arcToParameter(position)).normalize().multiply(this.totalArcLength);
    }

    @Override
    public double arcLength(double positionA, double positionB) {
        return this.baseInterpolation.arcLength(this.arcToParameter(positionA), this.arcToParameter(positionB));
    }

    private double arcToParameter(double arc) {
        if (this.cache.isEmpty()) {
            throw new IllegalStateException("Must call setNodes first.");
        }
        if (arc > 1.0) {
            arc = 1.0;
        }
        Map.Entry<Double, Double> floorEntry = this.cache.floorEntry(arc *= this.totalArcLength);
        double leftArc = floorEntry.getKey();
        double leftParameter = floorEntry.getValue();
        if (leftArc == arc) {
            return leftParameter;
        }
        Map.Entry<Double, Double> ceilingEntry = this.cache.ceilingEntry(arc);
        if (ceilingEntry == null) {
            log.warning("Error in arcToParameter: no ceiling entry for " + arc + " found!");
            return 0.0;
        }
        double rightArc = ceilingEntry.getKey();
        double rightParameter = ceilingEntry.getValue();
        if (rightArc == arc) {
            return rightParameter;
        }
        return this.evaluate(arc, leftArc, leftParameter, rightArc, rightParameter);
    }

    private double evaluate(double arc, double leftArc, double leftParameter, double rightArc, double rightParameter) {
        double midParameter = 0.0;
        for (int i = 0; i < 10; ++i) {
            midParameter = (leftParameter + rightParameter) * 0.5;
            double midArc = this.baseInterpolation.arcLength(0.0, midParameter);
            this.cache.put(midArc, midParameter);
            if (midArc < leftArc) {
                return leftParameter;
            }
            if (midArc > rightArc) {
                return rightParameter;
            }
            if (Math.abs(midArc - arc) < 0.01) {
                return midParameter;
            }
            if (arc < midArc) {
                rightArc = midArc;
                rightParameter = midParameter;
                continue;
            }
            leftArc = midArc;
            leftParameter = midParameter;
        }
        return midParameter;
    }

    @Override
    public int getSegment(double position) {
        if (position > 1.0) {
            return Integer.MAX_VALUE;
        }
        return this.baseInterpolation.getSegment(this.arcToParameter(position));
    }
}

