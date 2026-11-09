/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionPointEvent;
import com.sk89q.worldedit.internal.cui.SelectionPolygonEvent;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.polyhedron.Triangle;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;

public class ConvexPolyhedralRegionSelector
extends com.sk89q.worldedit.regions.ConvexPolyhedralRegionSelector
implements RegionSelector,
CUIRegion {
    private final transient ConvexPolyhedralRegion region;
    private transient BlockVector pos1;

    public ConvexPolyhedralRegionSelector() {
        this((World)null);
    }

    public ConvexPolyhedralRegionSelector(@Nullable World world) {
        this.region = new ConvexPolyhedralRegion(world);
    }

    public ConvexPolyhedralRegionSelector(RegionSelector oldSelector) {
        Preconditions.checkNotNull((Object)oldSelector);
        if (oldSelector instanceof ConvexPolyhedralRegionSelector) {
            ConvexPolyhedralRegionSelector convexPolyhedralRegionSelector = (ConvexPolyhedralRegionSelector)oldSelector;
            this.pos1 = convexPolyhedralRegionSelector.pos1;
            this.region = new ConvexPolyhedralRegion(convexPolyhedralRegionSelector.region);
        } else {
            Region oldRegion;
            try {
                oldRegion = oldSelector.getRegion();
            }
            catch (IncompleteRegionException e) {
                this.region = new ConvexPolyhedralRegion(oldSelector.getIncompleteRegion().getWorld());
                return;
            }
            int minY = oldRegion.getMinimumPoint().getBlockY();
            int maxY = oldRegion.getMaximumPoint().getBlockY();
            this.region = new ConvexPolyhedralRegion(oldRegion.getWorld());
            for (BlockVector2D pt : new ArrayList<BlockVector2D>(oldRegion.polygonize(Integer.MAX_VALUE))) {
                this.region.addVertex(pt.toVector(minY));
                this.region.addVertex(pt.toVector(maxY));
            }
            this.learnChanges();
        }
    }

    @Override
    @Nullable
    public World getWorld() {
        return this.region.getWorld();
    }

    @Override
    public void setWorld(@Nullable World world) {
        this.region.setWorld(world);
    }

    @Override
    public boolean selectPrimary(Vector position, SelectorLimits limits) {
        Preconditions.checkNotNull((Object)position);
        this.clear();
        this.pos1 = position.toBlockVector();
        return this.region.addVertex(position);
    }

    @Override
    public boolean selectSecondary(Vector position, SelectorLimits limits) {
        Preconditions.checkNotNull((Object)position);
        Optional<Integer> vertexLimit = limits.getPolyhedronVertexLimit();
        if (vertexLimit.isPresent() && this.region.getVertices().size() > (Integer)vertexLimit.get()) {
            return false;
        }
        return this.region.addVertex(position);
    }

    @Override
    public BlockVector getPrimaryPosition() throws IncompleteRegionException {
        return this.pos1;
    }

    @Override
    public Region getRegion() throws IncompleteRegionException {
        if (!this.region.isDefined()) {
            throw new IncompleteRegionException();
        }
        return this.region;
    }

    @Override
    public Region getIncompleteRegion() {
        return this.region;
    }

    @Override
    public boolean isDefined() {
        return this.region.isDefined();
    }

    @Override
    public int getArea() {
        return this.region.getArea();
    }

    @Override
    public void learnChanges() {
        this.pos1 = this.region.getVertices().iterator().next().toBlockVector();
    }

    @Override
    public void clear() {
        this.region.clear();
    }

    @Override
    public String getTypeName() {
        return "Convex Polyhedron";
    }

    @Override
    public List<String> getInformationLines() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add("Vertices: " + this.region.getVertices().size());
        ret.add("Triangles: " + this.region.getTriangles().size());
        return ret;
    }

    @Override
    public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        Preconditions.checkNotNull((Object)pos);
        session.describeCUI(player);
        player.print("Started new selection with vertex " + pos + ".");
    }

    @Override
    public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        Preconditions.checkNotNull((Object)pos);
        session.describeCUI(player);
        player.print("Added vertex " + pos + " to the selection.");
    }

    @Override
    public void explainRegionAdjust(Actor player, LocalSession session) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        session.describeCUI(player);
    }

    @Override
    public int getProtocolVersion() {
        return 3;
    }

    @Override
    public String getTypeID() {
        return "polyhedron";
    }

    @Override
    public void describeCUI(LocalSession session, Actor player) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        Collection<Vector> vertices = this.region.getVertices();
        Collection<Triangle> triangles = this.region.getTriangles();
        HashMap<Vector, Integer> vertexIds = new HashMap<Vector, Integer>(vertices.size());
        int lastVertexId = -1;
        for (Vector vertex : vertices) {
            vertexIds.put(vertex, ++lastVertexId);
            session.dispatchCUIEvent(player, new SelectionPointEvent(lastVertexId, vertex, this.getArea()));
        }
        for (Triangle triangle : triangles) {
            int[] v = new int[3];
            for (int i = 0; i < 3; ++i) {
                v[i] = (Integer)vertexIds.get(triangle.getVertex(i));
            }
            session.dispatchCUIEvent(player, new SelectionPolygonEvent(v));
        }
    }

    @Override
    public String getLegacyTypeID() {
        return "cuboid";
    }

    @Override
    public void describeLegacyCUI(LocalSession session, Actor player) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        if (this.isDefined()) {
            session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.region.getMinimumPoint(), this.getArea()));
            session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.region.getMaximumPoint(), this.getArea()));
        }
    }
}

