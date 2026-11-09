/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.util.Identifiable;
import com.sk89q.worldedit.util.auth.Subject;
import java.io.File;

public interface Actor
extends Identifiable,
SessionOwner,
Subject {
    public String getName();

    public void printRaw(String var1);

    public void printDebug(String var1);

    public void print(String var1);

    public void printError(String var1);

    public boolean canDestroyBedrock();

    public boolean isPlayer();

    public File openFileOpenDialog(String[] var1);

    public File openFileSaveDialog(String[] var1);

    public void dispatchCUIEvent(CUIEvent var1);
}

