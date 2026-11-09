/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.RepeaterUnit;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import java.util.Calendar;

public class RepeaterMonth
extends RepeaterUnit {
    private static final int MONTH_SECONDS = 2592000;
    private Calendar _currentMonthStart;

    protected Span _nextSpan(Pointer.PointerType pointer) {
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        this._currentMonthStart = this._currentMonthStart == null ? Time.cloneAndAdd(Time.ym(this.getNow()), 2, direction) : Time.cloneAndAdd(this._currentMonthStart, 2, direction);
        return new Span(this._currentMonthStart, 2, 1L);
    }

    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        return new Span(Time.cloneAndAdd(span.getBeginCalendar(), 2, amount * direction), Time.cloneAndAdd(span.getEndCalendar(), 2, amount * direction));
    }

    protected Span _thisSpan(Pointer.PointerType pointer) {
        Calendar monthEnd;
        Calendar monthStart;
        if (pointer == Pointer.PointerType.FUTURE) {
            monthStart = Time.cloneAndAdd(Time.ymd(this.getNow()), 5, 1L);
            monthEnd = Time.cloneAndAdd(Time.ym(this.getNow()), 2, 1L);
        } else if (pointer == Pointer.PointerType.PAST) {
            monthStart = Time.ym(this.getNow());
            monthEnd = Time.ymd(this.getNow());
        } else if (pointer == Pointer.PointerType.NONE) {
            monthStart = Time.ym(this.getNow());
            monthEnd = Time.cloneAndAdd(Time.ym(this.getNow()), 2, 1L);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return new Span(monthStart, monthEnd);
    }

    public int getWidth() {
        return 2592000;
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-month";
    }
}

