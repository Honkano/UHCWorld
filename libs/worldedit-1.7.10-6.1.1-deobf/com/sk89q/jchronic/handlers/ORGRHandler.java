/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.Handler;
import com.sk89q.jchronic.handlers.ORRHandler;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ORGRHandler
extends ORRHandler {
    @Override
    public Span handle(List<Token> tokens, Options options) {
        Span outerSpan = Handler.getAnchor(tokens.subList(2, 4), options);
        return this.handle(tokens.subList(0, 2), outerSpan, options);
    }
}

