/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.RepeaterDayPortion;
import com.sk89q.jchronic.utils.Range;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class IntegerRepeaterDayPortion
extends RepeaterDayPortion<Integer> {
    public IntegerRepeaterDayPortion(Integer type) {
        super(type);
    }

    @Override
    protected Range createRange(Integer type) {
        Range range = new Range(type * 60 * 60, (type + 12) * 60 * 60);
        return range;
    }

    @Override
    protected int _getWidth(Range range) {
        int width = 43200;
        return width;
    }
}

