/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.IHandler;
import com.sk89q.jchronic.tags.ScalarDay;
import com.sk89q.jchronic.tags.ScalarMonth;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.Calendar;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SmSdHandler
implements IHandler {
    @Override
    public Span handle(List<Token> tokens, Options options) {
        int month = (Integer)tokens.get(0).getTag(ScalarMonth.class).getType();
        int day = (Integer)tokens.get(1).getTag(ScalarDay.class).getType();
        Calendar start = Time.construct(options.getNow().get(1), month, day);
        Calendar end = Time.cloneAndAdd(start, 5, 1L);
        return new Span(start, end);
    }
}

