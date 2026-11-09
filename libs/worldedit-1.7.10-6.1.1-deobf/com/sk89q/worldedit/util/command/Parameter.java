/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.command;

public interface Parameter {
    public String getName();

    public Character getFlag();

    public boolean isValueFlag();

    public boolean isOptional();

    public String[] getDefaultValue();
}

