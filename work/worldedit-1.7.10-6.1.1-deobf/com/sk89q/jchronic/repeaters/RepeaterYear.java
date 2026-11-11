/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.RepeaterUnit;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import java.util.Calendar;

public class RepeaterYear
extends RepeaterUnit {
    private Calendar _currentYearStart;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Span _nextSpan(Pointer.PointerType pointer) {
        if (this._currentYearStart == null) {
            if (pointer == Pointer.PointerType.FUTURE) {
                this._currentYearStart = Time.cloneAndAdd(Time.y(this.getNow()), 1, 1L);
                return new Span(this._currentYearStart, 1, 1L);
            } else {
                if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
                this._currentYearStart = Time.cloneAndAdd(Time.y(this.getNow()), 1, -1L);
            }
            return new Span(this._currentYearStart, 1, 1L);
        } else {
            int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
            this._currentYearStart.add(1, direction);
        }
        return new Span(this._currentYearStart, 1, 1L);
    }

    protected Span _thisSpan(Pointer.PointerType pointer) {
        Calendar yearEnd;
        Calendar yearStart;
        if (pointer == Pointer.PointerType.FUTURE) {
            yearStart = Time.cloneAndAdd(Time.ymd(this.getNow()), 5, 1L);
            yearEnd = Time.cloneAndAdd(Time.yJan1(this.getNow()), 1, 1L);
        } else if (pointer == Pointer.PointerType.PAST) {
            yearStart = Time.yJan1(this.getNow());
            yearEnd = Time.ymd(this.getNow());
        } else if (pointer == Pointer.PointerType.NONE) {
            yearStart = Time.yJan1(this.getNow());
            yearEnd = Time.cloneAndAdd(Time.yJan1(this.getNow()), 1, 1L);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return new Span(yearStart, yearEnd);
    }

    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        Calendar newBegin = Time.cloneAndAdd(span.getBeginCalendar(), 1, amount * direction);
        Calendar newEnd = Time.cloneAndAdd(span.getEndCalendar(), 1, amount * direction);
        return new Span(newBegin, newEnd);
    }

    public int getWidth() {
        return 31536000;
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-year";
    }
}

