/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.IHandler;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Ordinal;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Time;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class ORRHandler
implements IHandler {
    public Span handle(List<Token> tokens, Span outerSpan, Options options) {
        Repeater repeater = tokens.get(1).getTag(Repeater.class);
        repeater.setStart(Time.cloneAndAdd(outerSpan.getBeginCalendar(), 13, -1L));
        Integer ordinalValue = (Integer)tokens.get(0).getTag(Ordinal.class).getType();
        Span span = null;
        int i = 0;
        while (i < ordinalValue) {
            span = repeater.nextSpan(Pointer.PointerType.FUTURE);
            if (span.getBegin() > outerSpan.getEnd()) {
                span = null;
                break;
            }
            ++i;
        }
        return span;
    }
}

