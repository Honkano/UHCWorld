/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.RepeaterDayName;
import com.sk89q.jchronic.repeaters.RepeaterUnit;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import java.util.Calendar;

public class RepeaterWeek
extends RepeaterUnit {
    public static final int WEEK_SECONDS = 604800;
    public static final int WEEK_DAYS = 7;
    private Calendar _currentWeekStart;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected Span _nextSpan(Pointer.PointerType pointer) {
        if (this._currentWeekStart == null) {
            if (pointer == Pointer.PointerType.FUTURE) {
                RepeaterDayName sundayRepeater = new RepeaterDayName(RepeaterDayName.DayName.SUNDAY);
                sundayRepeater.setStart((Calendar)this.getNow().clone());
                Span nextSundaySpan = sundayRepeater.nextSpan(Pointer.PointerType.FUTURE);
                this._currentWeekStart = nextSundaySpan.getBeginCalendar();
                return new Span(this._currentWeekStart, 5, 7L);
            } else {
                if (pointer != Pointer.PointerType.PAST) throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
                RepeaterDayName sundayRepeater = new RepeaterDayName(RepeaterDayName.DayName.SUNDAY);
                sundayRepeater.setStart(Time.cloneAndAdd(this.getNow(), 5, 1L));
                sundayRepeater.nextSpan(Pointer.PointerType.PAST);
                Span lastSundaySpan = sundayRepeater.nextSpan(Pointer.PointerType.PAST);
                this._currentWeekStart = lastSundaySpan.getBeginCalendar();
            }
            return new Span(this._currentWeekStart, 5, 7L);
        } else {
            int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
            this._currentWeekStart.add(5, 7 * direction);
        }
        return new Span(this._currentWeekStart, 5, 7L);
    }

    protected Span _thisSpan(Pointer.PointerType pointer) {
        Span thisWeekSpan;
        if (pointer == Pointer.PointerType.FUTURE) {
            Calendar thisWeekStart = Time.cloneAndAdd(Time.ymdh(this.getNow()), 10, 1L);
            RepeaterDayName sundayRepeater = new RepeaterDayName(RepeaterDayName.DayName.SUNDAY);
            sundayRepeater.setStart((Calendar)this.getNow().clone());
            Span thisSundaySpan = sundayRepeater.thisSpan(Pointer.PointerType.FUTURE);
            Calendar thisWeekEnd = thisSundaySpan.getBeginCalendar();
            thisWeekSpan = new Span(thisWeekStart, thisWeekEnd);
        } else if (pointer == Pointer.PointerType.PAST) {
            Calendar thisWeekEnd = Time.ymdh(this.getNow());
            RepeaterDayName sundayRepeater = new RepeaterDayName(RepeaterDayName.DayName.SUNDAY);
            sundayRepeater.setStart((Calendar)this.getNow().clone());
            Span lastSundaySpan = sundayRepeater.nextSpan(Pointer.PointerType.PAST);
            Calendar thisWeekStart = lastSundaySpan.getBeginCalendar();
            thisWeekSpan = new Span(thisWeekStart, thisWeekEnd);
        } else if (pointer == Pointer.PointerType.NONE) {
            RepeaterDayName sundayRepeater = new RepeaterDayName(RepeaterDayName.DayName.SUNDAY);
            sundayRepeater.setStart((Calendar)this.getNow().clone());
            Span lastSundaySpan = sundayRepeater.nextSpan(Pointer.PointerType.PAST);
            Calendar thisWeekStart = lastSundaySpan.getBeginCalendar();
            Calendar thisWeekEnd = Time.cloneAndAdd(thisWeekStart, 5, 7L);
            thisWeekSpan = new Span(thisWeekStart, thisWeekEnd);
        } else {
            throw new IllegalArgumentException("Unable to handle pointer " + (Object)((Object)pointer) + ".");
        }
        return thisWeekSpan;
    }

    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
        return span.add(direction * amount * 604800);
    }

    public int getWidth() {
        return 604800;
    }

    public String toString() {
        return String.valueOf(super.toString()) + "-week";
    }
}

