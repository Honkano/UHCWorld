/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.List;
import javax.annotation.Nullable;

public interface RegionSelector {
    @Nullable
    public World getWorld();

    public void setWorld(@Nullable World var1);

    public boolean selectPrimary(Vector var1, SelectorLimits var2);

    public boolean selectSecondary(Vector var1, SelectorLimits var2);

    public void explainPrimarySelection(Actor var1, LocalSession var2, Vector var3);

    public void explainSecondarySelection(Actor var1, LocalSession var2, Vector var3);

    public void explainRegionAdjust(Actor var1, LocalSession var2);

    public BlockVector getPrimaryPosition() throws IncompleteRegionException;

    public Region getRegion() throws IncompleteRegionException;

    public Region getIncompleteRegion();

    public boolean isDefined();

    public int getArea();

    public void learnChanges();

    public void clear();

    public String getTypeName();

    public List<String> getInformationLines();
}

