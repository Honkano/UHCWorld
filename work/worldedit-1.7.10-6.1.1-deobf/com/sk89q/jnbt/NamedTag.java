/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.Tag;

public class NamedTag {
    private final String name;
    private final Tag tag;

    public NamedTag(String name, Tag tag) {
        Preconditions.checkNotNull((Object)name);
        Preconditions.checkNotNull((Object)tag);
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public Tag getTag() {
        return this.tag;
    }
}

