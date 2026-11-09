/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.util.yaml;

import org.yaml.snakeyaml.DumperOptions;

public enum YAMLFormat {
    EXTENDED(DumperOptions.FlowStyle.BLOCK),
    COMPACT(DumperOptions.FlowStyle.AUTO);

    private final DumperOptions.FlowStyle style;

    private YAMLFormat(DumperOptions.FlowStyle style) {
        this.style = style;
    }

    public DumperOptions.FlowStyle getStyle() {
        return this.style;
    }
}

