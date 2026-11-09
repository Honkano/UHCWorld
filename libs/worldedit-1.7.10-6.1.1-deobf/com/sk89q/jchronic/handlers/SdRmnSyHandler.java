/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.handlers.RmnSdSyHandler;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.LinkedList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SdRmnSyHandler
extends RmnSdSyHandler {
    @Override
    public Span handle(List<Token> tokens, Options options) {
        LinkedList<Token> newTokens = new LinkedList<Token>();
        newTokens.add(tokens.get(1));
        newTokens.add(tokens.get(0));
        newTokens.add(tokens.get(2));
        newTokens.addAll(tokens.subList(3, tokens.size()));
        return super.handle(newTokens, options);
    }
}

