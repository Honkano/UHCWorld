/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.math.transform;

import com.sk89q.worldedit.Vector;

public interface Transform {
    public boolean isIdentity();

    public Vector apply(Vector var1);

    public Transform inverse();

    public Transform combine(Transform var1);
}

