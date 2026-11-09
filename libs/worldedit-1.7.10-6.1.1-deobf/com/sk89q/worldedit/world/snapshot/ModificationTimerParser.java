/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.snapshot;

import com.sk89q.worldedit.world.snapshot.SnapshotDateParser;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ModificationTimerParser
implements SnapshotDateParser {
    @Override
    public Calendar detectDate(File file) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(file.lastModified());
        return cal;
    }
}

