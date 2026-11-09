/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.RegionFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombinedRegionFunction
implements RegionFunction {
    private final List<RegionFunction> functions = new ArrayList<RegionFunction>();

    public CombinedRegionFunction() {
    }

    public CombinedRegionFunction(Collection<RegionFunction> functions) {
        Preconditions.checkNotNull(functions);
        this.functions.addAll(functions);
    }

    public CombinedRegionFunction(RegionFunction ... function) {
        this(Arrays.asList((Object[])Preconditions.checkNotNull((Object)function)));
    }

    public void add(Collection<RegionFunction> functions) {
        Preconditions.checkNotNull(functions);
        this.functions.addAll(functions);
    }

    public void add(RegionFunction ... function) {
        this.add(Arrays.asList((Object[])Preconditions.checkNotNull((Object)function)));
    }

    @Override
    public boolean apply(Vector position) throws WorldEditException {
        boolean ret = false;
        for (RegionFunction function : this.functions) {
            if (!function.apply(position)) continue;
            ret = true;
        }
        return ret;
    }
}

