/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.CommandMapping;
import java.util.Comparator;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public final class PrimaryAliasComparator
implements Comparator<CommandMapping> {
    public static final PrimaryAliasComparator INSTANCE = new PrimaryAliasComparator(null);
    @Nullable
    private final Pattern removalPattern;

    public PrimaryAliasComparator(@Nullable Pattern removalPattern) {
        this.removalPattern = removalPattern;
    }

    private String clean(String alias) {
        if (this.removalPattern != null) {
            return this.removalPattern.matcher(alias).replaceAll("");
        }
        return alias;
    }

    @Override
    public int compare(CommandMapping o1, CommandMapping o2) {
        return this.clean(o1.getPrimaryAlias()).compareTo(this.clean(o2.getPrimaryAlias()));
    }
}

