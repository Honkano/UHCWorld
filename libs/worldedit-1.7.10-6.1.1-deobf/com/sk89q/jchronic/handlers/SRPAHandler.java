/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.Handler;
import com.sk89q.jchronic.handlers.SRPHandler;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SRPAHandler
extends SRPHandler {
    @Override
    public Span handle(List<Token> tokens, Options options) {
        Span anchorSpan = Handler.getAnchor(tokens.subList(3, tokens.size()), options);
        return super.handle(tokens, anchorSpan, options);
    }
}

