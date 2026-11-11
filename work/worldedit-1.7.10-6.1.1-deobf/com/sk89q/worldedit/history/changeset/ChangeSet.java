/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.history.changeset;

import com.sk89q.worldedit.history.change.Change;
import java.util.Iterator;

public interface ChangeSet {
    public void add(Change var1);

    public Iterator<Change> backwardIterator();

    public Iterator<Change> forwardIterator();

    public int size();
}

