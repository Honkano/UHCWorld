/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.tags;

import java.util.Calendar;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Tag<T> {
    private T _type;
    private Calendar _now;

    public Tag(T type) {
        this._type = type;
    }

    public Calendar getNow() {
        return this._now;
    }

    public void setType(T type) {
        this._type = type;
    }

    public T getType() {
        return this._type;
    }

    public void setStart(Calendar s) {
        this._now = s;
    }
}

