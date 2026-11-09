/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RepeaterSeasonName
extends Repeater<Object> {
    public RepeaterSeasonName(Object type) {
        super(type);
    }

    @Override
    protected Span _nextSpan(Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    protected Span _thisSpan(Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    public int getWidth() {
        return 7862400;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + "-season-" + this.getType();
    }
}

