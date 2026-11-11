/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.EnumRepeaterDayPortion;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Range;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class RepeaterDayPortion<T>
extends Repeater<T> {
    private static final Pattern AM_PATTERN = Pattern.compile("^ams?$");
    private static final Pattern PM_PATTERN = Pattern.compile("^pms?$");
    private static final Pattern MORNING_PATTERN = Pattern.compile("^mornings?$");
    private static final Pattern AFTERNOON_PATTERN = Pattern.compile("^afternoons?$");
    private static final Pattern EVENING_PATTERN = Pattern.compile("^evenings?$");
    private static final Pattern NIGHT_PATTERN = Pattern.compile("^(night|nite)s?$");
    private static final int FULL_DAY_SECONDS = 86400;
    private Range _range;
    private Span _currentSpan;

    public RepeaterDayPortion(T type) {
        super(type);
        this._range = this.createRange(type);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected Span _nextSpan(Pointer.PointerType pointer) {
        if (this._currentSpan == null) {
            Calendar rangeStart;
            long nowSeconds = (this.getNow().getTimeInMillis() - Time.ymd(this.getNow()).getTimeInMillis()) / 1000L;
            if (nowSeconds < this._range.getBegin()) {
                if (pointer == Pointer.PointerType.FUTURE) {
                    rangeStart = Time.cloneAndAdd(Time.ymd(this.getNow()), 13, this._range.getBegin());
                } else {
                    if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer type " + (Object)((Object)pointer));
                    rangeStart = Time.cloneAndAdd(Time.cloneAndAdd(Time.ymd(this.getNow()), 5, -1L), 13, this._range.getBegin());
                }
            } else if (nowSeconds > this._range.getBegin()) {
                if (pointer == Pointer.PointerType.FUTURE) {
                    rangeStart = Time.cloneAndAdd(Time.cloneAndAdd(Time.ymd(this.getNow()), 5, 1L), 13, this._range.getBegin());
                } else {
                    if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer type " + (Object)((Object)pointer));
                    rangeStart = Time.cloneAndAdd(Time.ymd(this.getNow()), 13, this._range.getBegin());
                }
            } else if (pointer == Pointer.PointerType.FUTURE) {
                rangeStart = Time.cloneAndAdd(Time.cloneAndAdd(Time.ymd(this.getNow()), 5, 1L), 13, this._range.getBegin());
            } else {
                if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer type " + (Object)((Object)pointer));
                rangeStart = Time.cloneAndAdd(Time.cloneAndAdd(Time.ymd(this.getNow()), 5, -1L), 13, this._range.getBegin());
            }
            this._currentSpan = new Span(rangeStart, Time.cloneAndAdd(rangeStart, 13, this._range.getWidth()));
            return this._currentSpan;
        } else if (pointer == Pointer.PointerType.FUTURE) {
            this._currentSpan = this._currentSpan.add(86400L);
            return this._currentSpan;
        } else {
            if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer type " + (Object)((Object)pointer));
            this._currentSpan = this._currentSpan.subtract(86400L);
        }
        return this._currentSpan;
    }

    @Override
    protected Span _thisSpan(Pointer.PointerType pointer) {
        Calendar rangeStart = Time.cloneAndAdd(Time.ymd(this.getNow()), 13, this._range.getBegin());
        this._currentSpan = new Span(rangeStart, Time.cloneAndAdd(rangeStart, 13, this._range.getWidth()));
        return this._currentSpan;
    }

    @Override
    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        this.setStart(span.getBeginCalendar());
        Span portionSpan = this.nextSpan(pointer);
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        portionSpan = portionSpan.add(direction * (amount - 1) * 86400);
        return portionSpan;
    }

    @Override
    public int getWidth() {
        if (this._range == null) {
            throw new IllegalStateException("Range has not been set");
        }
        int width = this._currentSpan != null ? (int)this._currentSpan.getWidth() : this._getWidth(this._range);
        return width;
    }

    protected abstract int _getWidth(Range var1);

    protected abstract Range createRange(T var1);

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + "-dayportion-" + this.getType();
    }

    public static EnumRepeaterDayPortion scan(Token token) {
        HashMap<Pattern, DayPortion> scanner = new HashMap<Pattern, DayPortion>();
        scanner.put(AM_PATTERN, DayPortion.AM);
        scanner.put(PM_PATTERN, DayPortion.PM);
        scanner.put(MORNING_PATTERN, DayPortion.MORNING);
        scanner.put(AFTERNOON_PATTERN, DayPortion.AFTERNOON);
        scanner.put(EVENING_PATTERN, DayPortion.EVENING);
        scanner.put(NIGHT_PATTERN, DayPortion.NIGHT);
        for (Pattern scannerItem : scanner.keySet()) {
            if (!scannerItem.matcher(token.getWord()).matches()) continue;
            return new EnumRepeaterDayPortion((DayPortion)((Object)scanner.get(scannerItem)));
        }
        return null;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum DayPortion {
        AM,
        PM,
        MORNING,
        AFTERNOON,
        EVENING,
        NIGHT;

    }
}

