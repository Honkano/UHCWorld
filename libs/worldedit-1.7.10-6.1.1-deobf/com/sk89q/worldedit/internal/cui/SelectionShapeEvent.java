/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.cui;

import com.sk89q.worldedit.internal.cui.CUIEvent;

public class SelectionShapeEvent
implements CUIEvent {
    protected final String shapeName;

    public SelectionShapeEvent(String shapeName) {
        this.shapeName = shapeName;
    }

    @Override
    public String getTypeId() {
        return "s";
    }

    @Override
    public String[] getParameters() {
        return new String[]{this.shapeName};
    }
}

