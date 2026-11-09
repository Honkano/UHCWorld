/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.RepeaterUnit;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import java.util.Calendar;

public class RepeaterDay
extends RepeaterUnit {
    public static final int DAY_SECONDS = 86400;
    private Calendar _currentDayStart;

    protected Span _nextSpan(Pointer.PointerType pointer) {
        if (this._currentDayStart == null) {
            this._currentDayStart = Time.ymd(this.getNow());
        }
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        this._currentDayStart.add(5, direction);
        return new Span(this._currentDayStart, 5, 1L);
    }

    protected Span _thisSpan(Pointer.PointerType pointer) {
        Calendar dayEnd;
        Calendar dayBegin;
        if (pointer == Pointer.PointerType.FUTURE) {
            dayBegin = Time.cloneAndAdd(Time.ymdh(this.getNow()), 10, 1L);
            dayEnd = Time.cloneAndAdd(Time.ymd(this.getNow()), 5, 1L);
        } else if (pointer == Pointer.PointerType.PAST) {
            dayBegin = Time.ymd(this.getNow());
            dayEnd = Time.ymdh(this.getNow());
        } else if (pointer == Pointer.PointerType.NONE) {
            dayBegin = Time.ymd(this.getNow());
            dayEnd = Time.cloneAndAdd(Time.ymdh(this.getNow()), 13, 86400L);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return new Span(dayBegin, dayEnd);
    }

    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        return span.add(direction * amount * 86400);
    }

    public int getWidth() {
        return 86400;
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-day";
    }
}

