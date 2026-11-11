/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.Handler;
import com.sk89q.jchronic.handlers.IHandler;
import com.sk89q.jchronic.repeaters.RepeaterMonthName;
import com.sk89q.jchronic.tags.ScalarDay;
import com.sk89q.jchronic.tags.ScalarYear;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RmnSdSyHandler
implements IHandler {
    @Override
    public Span handle(List<Token> tokens, Options options) {
        Span span;
        int month = ((RepeaterMonthName.MonthName)((Object)tokens.get(0).getTag(RepeaterMonthName.class).getType())).ordinal();
        int day = (Integer)tokens.get(1).getTag(ScalarDay.class).getType();
        int year = (Integer)tokens.get(2).getTag(ScalarYear.class).getType();
        try {
            List<Token> timeTokens = tokens.subList(3, tokens.size());
            Calendar dayStart = Time.construct(year, month, day);
            span = Handler.dayOrTime(dayStart, timeTokens, options);
        }
        catch (IllegalArgumentException e) {
            if (options.isDebug()) {
                e.printStackTrace(System.out);
            }
            span = null;
        }
        return span;
    }
}

