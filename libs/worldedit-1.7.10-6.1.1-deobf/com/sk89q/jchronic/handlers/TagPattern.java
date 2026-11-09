/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.handlers.HandlerPattern;
import com.sk89q.jchronic.tags.Tag;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TagPattern
extends HandlerPattern {
    private Class<? extends Tag> _tagClass;

    public TagPattern(Class<? extends Tag> tagClass) {
        this(tagClass, false);
    }

    public TagPattern(Class<? extends Tag> tagClass, boolean optional) {
        super(optional);
        this._tagClass = tagClass;
    }

    public Class<? extends Tag> getTagClass() {
        return this._tagClass;
    }

    public String toString() {
        return "[TagPattern: tagClass = " + this._tagClass + "]";
    }
}

