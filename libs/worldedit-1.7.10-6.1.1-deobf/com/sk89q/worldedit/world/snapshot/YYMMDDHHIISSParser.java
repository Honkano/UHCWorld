/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.snapshot;

import com.sk89q.worldedit.world.snapshot.SnapshotDateParser;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YYMMDDHHIISSParser
implements SnapshotDateParser {
    protected Pattern patt = Pattern.compile("([0-9]+)[^0-9]?([0-9]+)[^0-9]?([0-9]+)[^0-9]?([0-9]+)[^0-9]?([0-9]+)[^0-9]?([0-9]+)");

    @Override
    public Calendar detectDate(File file) {
        Matcher matcher = this.patt.matcher(file.getName());
        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            int hrs = Integer.parseInt(matcher.group(4));
            int min = Integer.parseInt(matcher.group(5));
            int sec = Integer.parseInt(matcher.group(6));
            GregorianCalendar calender = new GregorianCalendar();
            calender.set(year, month, day, hrs, min, sec);
            return calender;
        }
        return null;
    }
}

