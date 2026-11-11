/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.polyhedron;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.polyhedron.Triangle;

public class Edge {
    private final Vector start;
    private final Vector end;

    public Edge(Vector start, Vector end) {
        Preconditions.checkNotNull((Object)start);
        Preconditions.checkNotNull((Object)end);
        this.start = start;
        this.end = end;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge otherEdge = (Edge)other;
        if (this.start == otherEdge.end && this.end == otherEdge.start) {
            return true;
        }
        return this.end == otherEdge.end && this.start == otherEdge.start;
    }

    public int hashCode() {
        return this.start.hashCode() ^ this.end.hashCode();
    }

    public String toString() {
        return "(" + this.start + "," + this.end + ")";
    }

    public Triangle createTriangle(Vector vertex) {
        Preconditions.checkNotNull((Object)vertex);
        return new Triangle(this.start, this.end, vertex);
    }

    public Triangle createTriangle2(Vector vertex) {
        Preconditions.checkNotNull((Object)vertex);
        return new Triangle(this.start, vertex, this.end);
    }
}

