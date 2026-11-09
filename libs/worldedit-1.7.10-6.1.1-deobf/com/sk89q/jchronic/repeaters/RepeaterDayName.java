/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RepeaterDayName
extends Repeater<DayName> {
    private static final Pattern MON_PATTERN = Pattern.compile("^m[ou]n(day)?$");
    private static final Pattern TUE_PATTERN = Pattern.compile("^t(ue|eu|oo|u|)s(day)?$");
    private static final Pattern TUE_PATTERN_1 = Pattern.compile("^tue$");
    private static final Pattern WED_PATTERN_1 = Pattern.compile("^we(dnes|nds|nns)day$");
    private static final Pattern WED_PATTERN_2 = Pattern.compile("^wed$");
    private static final Pattern THU_PATTERN_1 = Pattern.compile("^th(urs|ers)day$");
    private static final Pattern THU_PATTERN_2 = Pattern.compile("^thu$");
    private static final Pattern FRI_PATTERN = Pattern.compile("^fr[iy](day)?$");
    private static final Pattern SAT_PATTERN = Pattern.compile("^sat(t?[ue]rday)?$");
    private static final Pattern SUN_PATTERN = Pattern.compile("^su[nm](day)?$");
    public static final int DAY_SECONDS = 86400;
    private Calendar _currentDayStart;

    public RepeaterDayName(DayName type) {
        super(type);
    }

    @Override
    protected Span _nextSpan(Pointer.PointerType pointer) {
        int direction;
        int n = direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        if (this._currentDayStart == null) {
            this._currentDayStart = Time.ymd(this.getNow());
            this._currentDayStart.add(5, direction);
            int dayNum = ((DayName)((Object)this.getType())).ordinal();
            while (this._currentDayStart.get(7) - 1 != dayNum) {
                this._currentDayStart.add(5, direction);
            }
        } else {
            this._currentDayStart.add(5, direction * 7);
        }
        return new Span(this._currentDayStart, 5, 1L);
    }

    @Override
    protected Span _thisSpan(Pointer.PointerType pointer) {
        if (pointer == Pointer.PointerType.NONE) {
            pointer = Pointer.PointerType.FUTURE;
        }
        return super.nextSpan(pointer);
    }

    @Override
    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    public int getWidth() {
        return 86400;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + "-dayname-" + this.getType();
    }

    public static RepeaterDayName scan(Token token) {
        HashMap<Pattern, DayName> scanner = new HashMap<Pattern, DayName>();
        scanner.put(MON_PATTERN, DayName.MONDAY);
        scanner.put(TUE_PATTERN, DayName.TUESDAY);
        scanner.put(TUE_PATTERN_1, DayName.TUESDAY);
        scanner.put(WED_PATTERN_1, DayName.WEDNESDAY);
        scanner.put(WED_PATTERN_2, DayName.WEDNESDAY);
        scanner.put(THU_PATTERN_1, DayName.THURSDAY);
        scanner.put(THU_PATTERN_2, DayName.THURSDAY);
        scanner.put(FRI_PATTERN, DayName.FRIDAY);
        scanner.put(SAT_PATTERN, DayName.SATURDAY);
        scanner.put(SUN_PATTERN, DayName.SUNDAY);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new RepeaterDayName((DayName)((Object)scanner.get(scannerItem)));
        }
        return null;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum DayName {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

    }
}

