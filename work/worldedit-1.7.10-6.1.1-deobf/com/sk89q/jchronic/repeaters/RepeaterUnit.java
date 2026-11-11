/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.repeaters.Repeater;
import com.sk89q.jchronic.utils.Token;
import java.util.HashMap;
import java.util.regex.Pattern;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class RepeaterUnit
extends Repeater<Object> {
    private static final Pattern YEAR_PATTERN = Pattern.compile("^years?$");
    private static final Pattern SEASON_PATTERN = Pattern.compile("^seasons?$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^months?$");
    private static final Pattern FORTNIGHT_PATTERN = Pattern.compile("^fortnights?$");
    private static final Pattern WEEK_PATTERN = Pattern.compile("^weeks?$");
    private static final Pattern WEEKEND_PATTERN = Pattern.compile("^weekends?$");
    private static final Pattern DAY_PATTERN = Pattern.compile("^days?$");
    private static final Pattern HOUR_PATTERN = Pattern.compile("^hours?$");
    private static final Pattern MINUTE_PATTERN = Pattern.compile("^minutes?$");
    private static final Pattern SECOND_PATTERN = Pattern.compile("^seconds?$");

    public RepeaterUnit() {
        super(null);
    }

    public static RepeaterUnit scan(Token token) {
        try {
            HashMap<Pattern, UnitName> scanner = new HashMap<Pattern, UnitName>();
            scanner.put(YEAR_PATTERN, UnitName.YEAR);
            scanner.put(SEASON_PATTERN, UnitName.SEASON);
            scanner.put(MONTH_PATTERN, UnitName.MONTH);
            scanner.put(FORTNIGHT_PATTERN, UnitName.FORTNIGHT);
            scanner.put(WEEK_PATTERN, UnitName.WEEK);
            scanner.put(WEEKEND_PATTERN, UnitName.WEEKEND);
            scanner.put(DAY_PATTERN, UnitName.DAY);
            scanner.put(HOUR_PATTERN, UnitName.HOUR);
            scanner.put(MINUTE_PATTERN, UnitName.MINUTE);
            scanner.put(SECOND_PATTERN, UnitName.SECOND);
            for (Pattern scannerItem : scanner.keySet()) {
                if (!scannerItem.matcher(token.getWord()).matches()) continue;
                UnitName unitNameEnum = (UnitName)((Object)scanner.get(scannerItem));
                String unitName = unitNameEnum.name();
                String capitalizedUnitName = String.valueOf(unitName.substring(0, 1)) + unitName.substring(1).toLowerCase();
                String repeaterClassName = "com.sk89q.jchronic.repeaters.Repeater" + capitalizedUnitName;
                RepeaterUnit repeater = Class.forName(repeaterClassName).asSubclass(RepeaterUnit.class).newInstance();
                return repeater;
            }
            return null;
        }
        catch (Throwable t) {
            throw new RuntimeException("Failed to create RepeaterUnit.", t);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum UnitName {
        YEAR,
        SEASON,
        MONTH,
        FORTNIGHT,
        WEEK,
        WEEKEND,
        DAY,
        HOUR,
        MINUTE,
        SECOND;

    }
}

