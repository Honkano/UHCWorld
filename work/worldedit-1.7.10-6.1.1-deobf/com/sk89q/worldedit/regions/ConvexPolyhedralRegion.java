/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.regions.polyhedron.Edge;
import com.sk89q.worldedit.regions.polyhedron.Triangle;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class ConvexPolyhedralRegion
extends AbstractRegion {
    private final Set<Vector> vertices = new LinkedHashSet<Vector>();
    private final List<Triangle> triangles = new ArrayList<Triangle>();
    private final Set<Vector> vertexBacklog = new LinkedHashSet<Vector>();
    private Vector minimumPoint;
    private Vector maximumPoint;
    private Vector centerAccum = Vector.ZERO;
    private Triangle lastTriangle;

    public ConvexPolyhedralRegion(@Nullable World world) {
        super(world);
    }

    @Deprecated
    public ConvexPolyhedralRegion(LocalWorld world) {
        super(world);
    }

    public ConvexPolyhedralRegion(ConvexPolyhedralRegion region) {
        this(region.world);
        this.vertices.addAll(region.vertices);
        this.triangles.addAll(region.triangles);
        this.vertexBacklog.addAll(region.vertexBacklog);
        this.minimumPoint = region.minimumPoint;
        this.maximumPoint = region.maximumPoint;
        this.centerAccum = region.centerAccum;
        this.lastTriangle = region.lastTriangle;
    }

    public void clear() {
        this.vertices.clear();
        this.triangles.clear();
        this.vertexBacklog.clear();
        this.minimumPoint = null;
        this.maximumPoint = null;
        this.centerAccum = Vector.ZERO;
        this.lastTriangle = null;
    }

    public boolean addVertex(Vector vertex) {
        Preconditions.checkNotNull((Object)vertex);
        this.lastTriangle = null;
        if (this.vertices.contains(vertex)) {
            return false;
        }
        if (this.vertices.size() == 3) {
            if (this.vertexBacklog.contains(vertex)) {
                return false;
            }
            if (this.containsRaw(vertex)) {
                return this.vertexBacklog.add(vertex);
            }
        }
        this.vertices.add(vertex);
        this.centerAccum = this.centerAccum.add(vertex);
        if (this.minimumPoint == null) {
            this.minimumPoint = this.maximumPoint = vertex;
        } else {
            this.minimumPoint = Vector.getMinimum(this.minimumPoint, vertex);
            this.maximumPoint = Vector.getMaximum(this.maximumPoint, vertex);
        }
        switch (this.vertices.size()) {
            case 0: 
            case 1: 
            case 2: {
                return true;
            }
            case 3: {
                Vector[] v = this.vertices.toArray(new Vector[this.vertices.size()]);
                this.triangles.add(new Triangle(v[0], v[1], v[2]));
                this.triangles.add(new Triangle(v[0], v[2], v[1]));
                return true;
            }
        }
        LinkedHashSet<Edge> borderEdges = new LinkedHashSet<Edge>();
        Iterator<Triangle> it = this.triangles.iterator();
        while (it.hasNext()) {
            Triangle triangle = it.next();
            if (!triangle.above(vertex)) continue;
            it.remove();
            for (int i = 0; i < 3; ++i) {
                Edge edge = triangle.getEdge(i);
                if (borderEdges.remove(edge)) continue;
                borderEdges.add(edge);
            }
        }
        for (Edge edge : borderEdges) {
            this.triangles.add(edge.createTriangle(vertex));
        }
        if (!this.vertexBacklog.isEmpty()) {
            this.vertices.remove(vertex);
            ArrayList<Vector> vertexBacklog2 = new ArrayList<Vector>(this.vertexBacklog);
            this.vertexBacklog.clear();
            for (Vector vertex2 : vertexBacklog2) {
                this.addVertex(vertex2);
            }
            this.vertices.add(vertex);
        }
        return true;
    }

    public boolean isDefined() {
        return !this.triangles.isEmpty();
    }

    @Override
    public Vector getMinimumPoint() {
        return this.minimumPoint;
    }

    @Override
    public Vector getMaximumPoint() {
        return this.maximumPoint;
    }

    @Override
    public Vector getCenter() {
        return this.centerAccum.divide(this.vertices.size());
    }

    @Override
    public void expand(Vector ... changes) throws RegionOperationException {
    }

    @Override
    public void contract(Vector ... changes) throws RegionOperationException {
    }

    @Override
    public void shift(Vector change) throws RegionOperationException {
        ConvexPolyhedralRegion.shiftCollection(this.vertices, change);
        ConvexPolyhedralRegion.shiftCollection(this.vertexBacklog, change);
        for (int i = 0; i < this.triangles.size(); ++i) {
            Triangle triangle = this.triangles.get(i);
            Vector v0 = change.add(triangle.getVertex(0));
            Vector v1 = change.add(triangle.getVertex(1));
            Vector v2 = change.add(triangle.getVertex(2));
            this.triangles.set(i, new Triangle(v0, v1, v2));
        }
        this.minimumPoint = change.add(this.minimumPoint);
        this.maximumPoint = change.add(this.maximumPoint);
        this.centerAccum = change.multiply(this.vertices.size()).add(this.centerAccum);
        this.lastTriangle = null;
    }

    private static void shiftCollection(Collection<Vector> collection, Vector change) {
        ArrayList<Vector> tmp = new ArrayList<Vector>(collection);
        collection.clear();
        for (Vector vertex : tmp) {
            collection.add(change.add(vertex));
        }
    }

    @Override
    public boolean contains(Vector position) {
        if (!this.isDefined()) {
            return false;
        }
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();
        Vector min = this.getMinimumPoint();
        Vector max = this.getMaximumPoint();
        if (x < min.getBlockX()) {
            return false;
        }
        if (x > max.getBlockX()) {
            return false;
        }
        if (y < min.getBlockY()) {
            return false;
        }
        if (y > max.getBlockY()) {
            return false;
        }
        if (z < min.getBlockZ()) {
            return false;
        }
        if (z > max.getBlockZ()) {
            return false;
        }
        return this.containsRaw(position);
    }

    private boolean containsRaw(Vector pt) {
        if (this.lastTriangle != null && this.lastTriangle.above(pt)) {
            return false;
        }
        for (Triangle triangle : this.triangles) {
            if (this.lastTriangle == triangle || !triangle.above(pt)) continue;
            this.lastTriangle = triangle;
            return false;
        }
        return true;
    }

    public Collection<Vector> getVertices() {
        if (this.vertexBacklog.isEmpty()) {
            return this.vertices;
        }
        ArrayList<Vector> ret = new ArrayList<Vector>(this.vertices);
        ret.addAll(this.vertexBacklog);
        return ret;
    }

    public Collection<Triangle> getTriangles() {
        return this.triangles;
    }

    @Override
    public AbstractRegion clone() {
        return new ConvexPolyhedralRegion(this);
    }
}

