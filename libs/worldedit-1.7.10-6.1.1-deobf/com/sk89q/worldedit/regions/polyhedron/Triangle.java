/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.polyhedron;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.polyhedron.Edge;

public class Triangle {
    private String tag = "Triangle";
    private final Vector[] vertices;
    private final Vector normal;
    private final double b;

    public Triangle(Vector v0, Vector v1, Vector v2) {
        Preconditions.checkNotNull((Object)v0);
        Preconditions.checkNotNull((Object)v1);
        Preconditions.checkNotNull((Object)v2);
        this.vertices = new Vector[]{v0, v1, v2};
        this.normal = v1.subtract(v0).cross(v2.subtract(v0)).normalize();
        this.b = Math.max(Math.max(this.normal.dot(v0), this.normal.dot(v1)), this.normal.dot(v2));
    }

    public Vector getVertex(int index) {
        return this.vertices[index];
    }

    public Edge getEdge(int index) {
        if (index == this.vertices.length - 1) {
            return new Edge(this.vertices[index], this.vertices[0]);
        }
        return new Edge(this.vertices[index], this.vertices[index + 1]);
    }

    public boolean below(Vector pt) {
        Preconditions.checkNotNull((Object)pt);
        return this.normal.dot(pt) < this.b;
    }

    public boolean above(Vector pt) {
        Preconditions.checkNotNull((Object)pt);
        return this.normal.dot(pt) > this.b;
    }

    public Triangle tag(String tag) {
        Preconditions.checkNotNull((Object)tag);
        this.tag = tag;
        return this;
    }

    public String toString() {
        return this.tag + "(" + this.vertices[0] + "," + this.vertices[1] + "," + this.vertices[2] + ")";
    }
}

