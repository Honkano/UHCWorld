/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionPointEvent;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class CuboidRegionSelector
extends com.sk89q.worldedit.regions.CuboidRegionSelector
implements RegionSelector,
CUIRegion {
    protected transient BlockVector position1;
    protected transient BlockVector position2;
    protected transient CuboidRegion region;

    public CuboidRegionSelector() {
        this((World)null);
    }

    public CuboidRegionSelector(@Nullable World world) {
        this.region = new CuboidRegion(world, new Vector(), new Vector());
    }

    public CuboidRegionSelector(RegionSelector oldSelector) {
        this(((RegionSelector)Preconditions.checkNotNull((Object)oldSelector)).getIncompleteRegion().getWorld());
        if (oldSelector instanceof CuboidRegionSelector) {
            CuboidRegionSelector cuboidRegionSelector = (CuboidRegionSelector)oldSelector;
            this.position1 = cuboidRegionSelector.position1;
            this.position2 = cuboidRegionSelector.position2;
        } else {
            Region oldRegion;
            try {
                oldRegion = oldSelector.getRegion();
            }
            catch (IncompleteRegionException e) {
                return;
            }
            this.position1 = oldRegion.getMinimumPoint().toBlockVector();
            this.position2 = oldRegion.getMaximumPoint().toBlockVector();
        }
        this.region.setPos1(this.position1);
        this.region.setPos2(this.position2);
    }

    public CuboidRegionSelector(@Nullable World world, Vector position1, Vector position2) {
        this(world);
        Preconditions.checkNotNull((Object)position1);
        Preconditions.checkNotNull((Object)position2);
        this.position1 = position1.toBlockVector();
        this.position2 = position2.toBlockVector();
        this.region.setPos1(position1);
        this.region.setPos2(position2);
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
        if (this.position1 != null && position.compareTo(this.position1) == 0) {
            return false;
        }
        this.position1 = position.toBlockVector();
        this.region.setPos1(this.position1);
        return true;
    }

    @Override
    public boolean selectSecondary(Vector position, SelectorLimits limits) {
        Preconditions.checkNotNull((Object)position);
        if (this.position2 != null && position.compareTo(this.position2) == 0) {
            return false;
        }
        this.position2 = position.toBlockVector();
        this.region.setPos2(this.position2);
        return true;
    }

    @Override
    public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        Preconditions.checkNotNull((Object)pos);
        if (this.position1 != null && this.position2 != null) {
            player.print("First position set to " + this.position1 + " (" + this.region.getArea() + ").");
        } else {
            player.print("First position set to " + this.position1 + ".");
        }
        session.dispatchCUIEvent(player, new SelectionPointEvent(0, pos, this.getArea()));
    }

    @Override
    public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        Preconditions.checkNotNull((Object)pos);
        if (this.position1 != null && this.position2 != null) {
            player.print("Second position set to " + this.position2 + " (" + this.region.getArea() + ").");
        } else {
            player.print("Second position set to " + this.position2 + ".");
        }
        session.dispatchCUIEvent(player, new SelectionPointEvent(1, pos, this.getArea()));
    }

    @Override
    public void explainRegionAdjust(Actor player, LocalSession session) {
        Preconditions.checkNotNull((Object)player);
        Preconditions.checkNotNull((Object)session);
        if (this.position1 != null) {
            session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.position1, this.getArea()));
        }
        if (this.position2 != null) {
            session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.position2, this.getArea()));
        }
    }

    @Override
    public BlockVector getPrimaryPosition() throws IncompleteRegionException {
        if (this.position1 == null) {
            throw new IncompleteRegionException();
        }
        return this.position1;
    }

    @Override
    public boolean isDefined() {
        return this.position1 != null && this.position2 != null;
    }

    @Override
    public CuboidRegion getRegion() throws IncompleteRegionException {
        if (this.position1 == null || this.position2 == null) {
            throw new IncompleteRegionException();
        }
        return this.region;
    }

    @Override
    public CuboidRegion getIncompleteRegion() {
        return this.region;
    }

    @Override
    public void learnChanges() {
        this.position1 = this.region.getPos1().toBlockVector();
        this.position2 = this.region.getPos2().toBlockVector();
    }

    @Override
    public void clear() {
        this.position1 = null;
        this.position2 = null;
    }

    @Override
    public String getTypeName() {
        return "cuboid";
    }

    @Override
    public List<String> getInformationLines() {
        ArrayList<String> lines = new ArrayList<String>();
        if (this.position1 != null) {
            lines.add("Position 1: " + this.position1);
        }
        if (this.position2 != null) {
            lines.add("Position 2: " + this.position2);
        }
        return lines;
    }

    @Override
    public int getArea() {
        if (this.position1 == null) {
            return -1;
        }
        if (this.position2 == null) {
            return -1;
        }
        return this.region.getArea();
    }

    @Override
    public void describeCUI(LocalSession session, Actor player) {
        if (this.position1 != null) {
            session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.position1, this.getArea()));
        }
        if (this.position2 != null) {
            session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.position2, this.getArea()));
        }
    }

    @Override
    public void describeLegacyCUI(LocalSession session, Actor player) {
        this.describeCUI(session, player);
    }

    @Override
    public int getProtocolVersion() {
        return 0;
    }

    @Override
    public String getTypeID() {
        return "cuboid";
    }

    @Override
    public String getLegacyTypeID() {
        return "cuboid";
    }
}

