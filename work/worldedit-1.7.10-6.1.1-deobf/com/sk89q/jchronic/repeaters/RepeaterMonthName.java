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
public class RepeaterMonthName
extends Repeater<MonthName> {
    private static final Pattern JAN_PATTERN = Pattern.compile("^jan\\.?(uary)?$");
    private static final Pattern FEB_PATTERN = Pattern.compile("^feb\\.?(ruary)?$");
    private static final Pattern MAR_PATTERN = Pattern.compile("^mar\\.?(ch)?$");
    private static final Pattern APR_PATTERN = Pattern.compile("^apr\\.?(il)?$");
    private static final Pattern MAY_PATTERN = Pattern.compile("^may$");
    private static final Pattern JUN_PATTERN = Pattern.compile("^jun\\.?e?$");
    private static final Pattern JUL_PATTERN = Pattern.compile("^jul\\.?y?$");
    private static final Pattern AUG_PATTERN = Pattern.compile("^aug\\.?(ust)?$");
    private static final Pattern SEP_PATTERN = Pattern.compile("^sep\\.?(t\\.?|tember)?$");
    private static final Pattern OCT_PATTERN = Pattern.compile("^oct\\.?(ober)?$");
    private static final Pattern NOV_PATTERN = Pattern.compile("^nov\\.?(ember)?$");
    private static final Pattern DEC_PATTERN = Pattern.compile("^dec\\.?(ember)?$");
    private static final int MONTH_SECONDS = 2592000;
    private Calendar _currentMonthBegin;

    public RepeaterMonthName(MonthName type) {
        super(type);
    }

    public int getIndex() {
        return ((MonthName)((Object)this.getType())).ordinal();
    }

    @Override
    protected Span _nextSpan(Pointer.PointerType pointer) {
        if (this._currentMonthBegin == null) {
            int targetMonth = ((MonthName)((Object)this.getType())).ordinal();
            int nowMonth = this.getNow().get(2) + 1;
            if (pointer == Pointer.PointerType.FUTURE) {
                if (nowMonth < targetMonth) {
                    this._currentMonthBegin = Time.y(this.getNow(), targetMonth);
                } else if (nowMonth > targetMonth) {
                    this._currentMonthBegin = Time.cloneAndAdd(Time.y(this.getNow(), targetMonth), 1, 1L);
                }
            } else if (pointer == Pointer.PointerType.NONE) {
                if (nowMonth <= targetMonth) {
                    this._currentMonthBegin = Time.y(this.getNow(), targetMonth);
                } else if (nowMonth > targetMonth) {
                    this._currentMonthBegin = Time.cloneAndAdd(Time.y(this.getNow(), targetMonth), 1, 1L);
                }
            } else if (pointer == Pointer.PointerType.PAST) {
                if (nowMonth > targetMonth) {
                    this._currentMonthBegin = Time.y(this.getNow(), targetMonth);
                } else if (nowMonth < targetMonth) {
                    this._currentMonthBegin = Time.cloneAndAdd(Time.y(this.getNow(), targetMonth), 1, -1L);
                }
            } else {
                throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
            }
            if (this._currentMonthBegin == null) {
                throw new IllegalStateException("Current month should be set by now.");
            }
        } else if (pointer == Pointer.PointerType.FUTURE) {
            this._currentMonthBegin = Time.cloneAndAdd(this._currentMonthBegin, 1, 1L);
        } else if (pointer == Pointer.PointerType.PAST) {
            this._currentMonthBegin = Time.cloneAndAdd(this._currentMonthBegin, 1, -1L);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return new Span(this._currentMonthBegin, 2, 1L);
    }

    @Override
    protected Span _thisSpan(Pointer.PointerType pointer) {
        Span span;
        if (pointer == Pointer.PointerType.PAST) {
            span = this.nextSpan(pointer);
        } else if (pointer == Pointer.PointerType.FUTURE || pointer == Pointer.PointerType.NONE) {
            span = this.nextSpan(Pointer.PointerType.NONE);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return span;
    }

    @Override
    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    public int getWidth() {
        return 2592000;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + "-monthname-" + this.getType();
    }

    public static RepeaterMonthName scan(Token token) {
        HashMap<Pattern, MonthName> scanner = new HashMap<Pattern, MonthName>();
        scanner.put(JAN_PATTERN, MonthName.JANUARY);
        scanner.put(FEB_PATTERN, MonthName.FEBRUARY);
        scanner.put(MAR_PATTERN, MonthName.MARCH);
        scanner.put(APR_PATTERN, MonthName.APRIL);
        scanner.put(MAY_PATTERN, MonthName.MAY);
        scanner.put(JUN_PATTERN, MonthName.JUNE);
        scanner.put(JUL_PATTERN, MonthName.JULY);
        scanner.put(AUG_PATTERN, MonthName.AUGUST);
        scanner.put(SEP_PATTERN, MonthName.SEPTEMBER);
        scanner.put(OCT_PATTERN, MonthName.OCTOBER);
        scanner.put(NOV_PATTERN, MonthName.NOVEMBER);
        scanner.put(DEC_PATTERN, MonthName.DECEMBER);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new RepeaterMonthName((MonthName)((Object)scanner.get(scannerItem)));
        }
        return null;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum MonthName {
        _ZERO_MONTH,
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER;

    }
}

