/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform.permission;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;

public class ActorSelectorLimits
implements SelectorLimits {
    private final LocalConfiguration configuration;
    private final Actor actor;

    public ActorSelectorLimits(LocalConfiguration configuration, Actor actor) {
        Preconditions.checkNotNull((Object)configuration);
        Preconditions.checkNotNull((Object)actor);
        this.configuration = configuration;
        this.actor = actor;
    }

    @Override
    public Optional<Integer> getPolygonVertexLimit() {
        int limit = this.actor.hasPermission("worldedit.limit.unrestricted") || this.configuration.maxPolygonalPoints < 0 ? this.configuration.defaultMaxPolygonalPoints : (this.configuration.defaultMaxPolygonalPoints < 0 ? this.configuration.maxPolygonalPoints : Math.min(this.configuration.defaultMaxPolygonalPoints, this.configuration.maxPolygonalPoints));
        if (limit > 0) {
            return Optional.of((Object)limit);
        }
        return Optional.absent();
    }

    @Override
    public Optional<Integer> getPolyhedronVertexLimit() {
        int limit = this.actor.hasPermission("worldedit.limit.unrestricted") || this.configuration.maxPolyhedronPoints < 0 ? this.configuration.defaultMaxPolyhedronPoints : (this.configuration.defaultMaxPolyhedronPoints < 0 ? this.configuration.maxPolyhedronPoints : Math.min(this.configuration.defaultMaxPolyhedronPoints, this.configuration.maxPolyhedronPoints));
        if (limit > 0) {
            return Optional.of((Object)limit);
        }
        return Optional.absent();
    }

    public static ActorSelectorLimits forActor(Actor actor) {
        return new ActorSelectorLimits(WorldEdit.getInstance().getConfiguration(), actor);
    }
}

