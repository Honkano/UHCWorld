/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Chronic;
import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.IHandler;
import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.tags.Scalar;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SRPHandler
implements IHandler {
    public Span handle(List<Token> tokens, Span span, Options options) {
        int distance = (Integer)tokens.get(0).getTag(Scalar.class).getType();
        Repeater repeater = tokens.get(1).getTag(Repeater.class);
        Pointer.PointerType pointer = (Pointer.PointerType)((Object)tokens.get(2).getTag(Pointer.class).getType());
        return repeater.getOffset(span, distance, pointer);
    }

    @Override
    public Span handle(List<Token> tokens, Options options) {
        Repeater repeater = tokens.get(1).getTag(Repeater.class);
        Span span = Chronic.parse("this second", new Options(options.getNow(), false));
        return this.handle(tokens, span, options);
    }
}

