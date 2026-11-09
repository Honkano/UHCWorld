/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.repeaters.RepeaterDayName;
import com.sk89q.jchronic.repeaters.RepeaterDayPortion;
import com.sk89q.jchronic.repeaters.RepeaterMonthName;
import com.sk89q.jchronic.repeaters.RepeaterTime;
import com.sk89q.jchronic.repeaters.RepeaterUnit;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class Repeater<T>
extends Tag<T>
implements Comparable<Repeater<?>> {
    public Repeater(T type) {
        super(type);
    }

    public static List<Token> scan(List<Token> tokens) {
        return Repeater.scan(tokens, new Options());
    }

    public static List<Token> scan(List<Token> tokens, Options options) {
        for (Token token : tokens) {
            Repeater t = RepeaterMonthName.scan(token);
            if (t != null) {
                token.tag(t);
            }
            if ((t = RepeaterDayName.scan(token)) != null) {
                token.tag(t);
            }
            if ((t = RepeaterDayPortion.scan(token)) != null) {
                token.tag(t);
            }
            if ((t = RepeaterTime.scan(token, tokens, options)) != null) {
                token.tag(t);
            }
            if ((t = RepeaterUnit.scan(token)) == null) continue;
            token.tag(t);
        }
        return tokens;
    }

    @Override
    public int compareTo(Repeater<?> other) {
        return Integer.valueOf(this.getWidth()).compareTo(other.getWidth());
    }

    public abstract int getWidth();

    public Span nextSpan(Pointer.PointerType pointer) {
        if (this.getNow() == null) {
            throw new IllegalStateException("Start point must be set before calling #next");
        }
        return this._nextSpan(pointer);
    }

    protected abstract Span _nextSpan(Pointer.PointerType var1);

    public Span thisSpan(Pointer.PointerType pointer) {
        if (this.getNow() == null) {
            throw new IllegalStateException("Start point must be set before calling #this");
        }
        return this._thisSpan(pointer);
    }

    protected abstract Span _thisSpan(Pointer.PointerType var1);

    public abstract Span getOffset(Span var1, int var2, Pointer.PointerType var3);

    public String toString() {
        return "repeater";
    }
}

