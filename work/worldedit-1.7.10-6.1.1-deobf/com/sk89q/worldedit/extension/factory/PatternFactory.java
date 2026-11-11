/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.factory.HashTagPatternParser;
import com.sk89q.worldedit.extension.factory.RandomPatternParser;
import com.sk89q.worldedit.extension.factory.SingleBlockPatternParser;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.registry.AbstractFactory;

public final class PatternFactory
extends AbstractFactory<Pattern> {
    public PatternFactory(WorldEdit worldEdit) {
        super(worldEdit);
        this.parsers.add(new HashTagPatternParser(worldEdit));
        this.parsers.add(new SingleBlockPatternParser(worldEdit));
        this.parsers.add(new RandomPatternParser(worldEdit));
    }
}

