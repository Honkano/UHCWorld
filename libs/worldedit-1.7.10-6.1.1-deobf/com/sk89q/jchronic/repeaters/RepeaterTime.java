/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.StringUtils;
import com.sk89q.jchronic.utils.Tick;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RepeaterTime
extends Repeater<Tick> {
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{1,2}(:?\\d{2})?([\\.:]?\\d{2})?$");
    private Calendar _currentTime;

    public RepeaterTime(String time) {
        super(null);
        Tick type;
        String t = time.replaceAll(":", "");
        int length = t.length();
        if (length <= 2) {
            int hours = Integer.parseInt(t);
            int hoursInSeconds = hours * 60 * 60;
            type = hours == 12 ? new Tick(0, true) : new Tick(hoursInSeconds, true);
        } else if (length == 3) {
            int hoursInSeconds = Integer.parseInt(t.substring(0, 1)) * 60 * 60;
            int minutesInSeconds = Integer.parseInt(t.substring(1)) * 60;
            type = new Tick(hoursInSeconds + minutesInSeconds, true);
        } else if (length == 4) {
            boolean ambiguous = time.contains(":") && Integer.parseInt(t.substring(0, 1)) != 0 && Integer.parseInt(t.substring(0, 2)) <= 12;
            int hours = Integer.parseInt(t.substring(0, 2));
            int hoursInSeconds = hours * 60 * 60;
            int minutesInSeconds = Integer.parseInt(t.substring(2)) * 60;
            type = hours == 12 ? new Tick(0 + minutesInSeconds, ambiguous) : new Tick(hoursInSeconds + minutesInSeconds, ambiguous);
        } else if (length == 5) {
            int hoursInSeconds = Integer.parseInt(t.substring(0, 1)) * 60 * 60;
            int minutesInSeconds = Integer.parseInt(t.substring(1, 3)) * 60;
            int seconds = Integer.parseInt(t.substring(3));
            type = new Tick(hoursInSeconds + minutesInSeconds + seconds, true);
        } else if (length == 6) {
            boolean ambiguous = time.contains(":") && Integer.parseInt(t.substring(0, 1)) != 0 && Integer.parseInt(t.substring(0, 2)) <= 12;
            int hours = Integer.parseInt(t.substring(0, 2));
            int hoursInSeconds = hours * 60 * 60;
            int minutesInSeconds = Integer.parseInt(t.substring(2, 4)) * 60;
            int seconds = Integer.parseInt(t.substring(4, 6));
            type = hours == 12 ? new Tick(0 + minutesInSeconds + seconds, ambiguous) : new Tick(hoursInSeconds + minutesInSeconds + seconds, ambiguous);
        } else {
            throw new IllegalArgumentException("Time cannot exceed six digits");
        }
        this.setType(type);
    }

    @Override
    protected Span _nextSpan(Pointer.PointerType pointer) {
        int halfDay = 43200;
        int fullDay = 86400;
        Calendar now = this.getNow();
        Tick tick = (Tick)this.getType();
        boolean first = false;
        if (this._currentTime == null) {
            first = true;
            Calendar midnight = Time.ymd(now);
            Calendar yesterdayMidnight = Time.cloneAndAdd(midnight, 13, -fullDay);
            Calendar tomorrowMidnight = Time.cloneAndAdd(midnight, 13, fullDay);
            boolean done = false;
            if (pointer == Pointer.PointerType.FUTURE) {
                if (tick.isAmbiguous()) {
                    LinkedList<Calendar> futureDates = new LinkedList<Calendar>();
                    futureDates.add(Time.cloneAndAdd(midnight, 13, tick.intValue()));
                    futureDates.add(Time.cloneAndAdd(midnight, 13, halfDay + tick.intValue()));
                    futureDates.add(Time.cloneAndAdd(tomorrowMidnight, 13, tick.intValue()));
                    for (Calendar futureDate : futureDates) {
                        if (!futureDate.after(now) && !futureDate.equals(now)) continue;
                        this._currentTime = futureDate;
                        done = true;
                        break;
                    }
                } else {
                    LinkedList<Calendar> futureDates = new LinkedList<Calendar>();
                    futureDates.add(Time.cloneAndAdd(midnight, 13, tick.intValue()));
                    futureDates.add(Time.cloneAndAdd(tomorrowMidnight, 13, tick.intValue()));
                    for (Calendar futureDate : futureDates) {
                        if (!futureDate.after(now) && !futureDate.equals(now)) continue;
                        this._currentTime = futureDate;
                        done = true;
                        break;
                    }
                }
            } else if (tick.isAmbiguous()) {
                LinkedList<Calendar> pastDates = new LinkedList<Calendar>();
                pastDates.add(Time.cloneAndAdd(midnight, 13, halfDay + tick.intValue()));
                pastDates.add(Time.cloneAndAdd(midnight, 13, tick.intValue()));
                pastDates.add(Time.cloneAndAdd(yesterdayMidnight, 13, tick.intValue() * 2));
                for (Calendar pastDate : pastDates) {
                    if (!pastDate.before(now) && !pastDate.equals(now)) continue;
                    this._currentTime = pastDate;
                    done = true;
                    break;
                }
            } else {
                LinkedList<Calendar> pastDates = new LinkedList<Calendar>();
                pastDates.add(Time.cloneAndAdd(midnight, 13, tick.intValue()));
                pastDates.add(Time.cloneAndAdd(yesterdayMidnight, 13, tick.intValue()));
                for (Calendar pastDate : pastDates) {
                    if (!pastDate.before(now) && !pastDate.equals(now)) continue;
                    this._currentTime = pastDate;
                    done = true;
                    break;
                }
            }
            if (!done && this._currentTime == null) {
                throw new IllegalStateException("Current time cannot be null at this point.");
            }
        }
        if (!first) {
            int increment = tick.isAmbiguous() ? halfDay : fullDay;
            int direction = pointer == Pointer.PointerType.FUTURE ? 1 : -1;
            this._currentTime.add(13, direction * increment);
        }
        return new Span(this._currentTime, Time.cloneAndAdd(this._currentTime, 13, this.getWidth()));
    }

    @Override
    protected Span _thisSpan(Pointer.PointerType pointer) {
        if (pointer == Pointer.PointerType.NONE) {
            pointer = Pointer.PointerType.FUTURE;
        }
        return this.nextSpan(pointer);
    }

    @Override
    public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
        throw new IllegalStateException("Not implemented.");
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public String toString() {
        return String.valueOf(super.toString()) + "-time-" + this.getType();
    }

    public static RepeaterTime scan(Token token, List<Token> tokens, Options options) {
        if (TIME_PATTERN.matcher(token.getWord()).matches()) {
            return new RepeaterTime(token.getWord());
        }
        Integer intStrValue = StringUtils.integerValue(token.getWord());
        if (intStrValue != null) {
            return new RepeaterTime(intStrValue.toString());
        }
        return null;
    }
}

