/*
 * Decompiled with CFR 0.152.
 */
package net.royawesome.jlibnoise;

import java.awt.Color;

public class MathHelper {
    public static final double DBL_EPSILON = Double.longBitsToDouble(4372995238176751616L);
    public static final float FLT_EPSILON = Float.intBitsToFloat(0x34000000);
    public static final double PI = Math.PI;
    public static final double SQUARED_PI = Math.PI * Math.PI;
    public static final double HALF_PI = 1.5707963267948966;
    public static final double QUARTER_PI = 0.7853981633974483;
    public static final double TWO_PI = Math.PI * 2;
    public static final double THREE_PI_HALVES = 4.71238898038469;
    public static final double DEGTORAD = Math.PI / 180;
    public static final double RADTODEG = 57.29577951308232;
    public static final double SQRTOFTWO = Math.sqrt(2.0);
    public static final double HALF_SQRTOFTWO = 0.5 * SQRTOFTWO;
    private static final double sin_a = -0.4052847345693511;
    private static final double sin_b = 1.2732395447351628;
    private static final double sin_p = 0.225;
    private static final double asin_a = -0.048129527683101345;
    private static final double asin_b = -0.3438359939479152;
    private static final double asin_c = 0.9627618484259132;
    private static final double asin_d = 1.0013894086010704;
    private static final double atan_a = 0.280872;

    public static double lengthSquared(double ... values) {
        double rval = 0.0;
        for (double value : values) {
            rval += value * value;
        }
        return rval;
    }

    public static double length(double ... values) {
        return Math.sqrt(MathHelper.lengthSquared(values));
    }

    public static float getAngleDifference(float angle1, float angle2) {
        return Math.abs(MathHelper.wrapAngle(angle1 - angle2));
    }

    public static double getRadianDifference(double radian1, double radian2) {
        return Math.abs(MathHelper.wrapRadian(radian1 - radian2));
    }

    public static float wrapAngle(float angle) {
        if ((angle %= 360.0f) <= -180.0f) {
            return angle + 360.0f;
        }
        if (angle > 180.0f) {
            return angle - 360.0f;
        }
        return angle;
    }

    public static byte wrapByte(int value) {
        if ((value %= 256) < 0) {
            value += 256;
        }
        return (byte)value;
    }

    public static double wrapRadian(double radian) {
        if ((radian %= Math.PI * 2) <= -Math.PI) {
            return radian + Math.PI * 2;
        }
        if (radian > Math.PI) {
            return radian - Math.PI * 2;
        }
        return radian;
    }

    public static double round(double input, int decimals) {
        double p = Math.pow(10.0, decimals);
        return (double)Math.round(input * p) / p;
    }

    public static double lerp(double a, double b, double percent) {
        return (1.0 - percent) * a + percent * b;
    }

    public static float lerp(float a, float b, float percent) {
        return (1.0f - percent) * a + percent * b;
    }

    public static int lerp(int a, int b, double percent) {
        return (int)((1.0 - percent) * (double)a + percent * (double)b);
    }

    public static Color lerp(Color a, Color b, double percent) {
        int red = MathHelper.lerp(a.getRed(), b.getRed(), percent);
        int blue = MathHelper.lerp(a.getBlue(), b.getBlue(), percent);
        int green = MathHelper.lerp(a.getGreen(), b.getGreen(), percent);
        int alpha = MathHelper.lerp(a.getAlpha(), b.getAlpha(), percent);
        return new Color(red, green, blue, alpha);
    }

    public static Color blend(Color a, Color b) {
        int red = MathHelper.lerp(a.getRed(), b.getRed(), (double)a.getAlpha() / 255.0);
        int blue = MathHelper.lerp(a.getBlue(), b.getBlue(), (double)a.getAlpha() / 255.0);
        int green = MathHelper.lerp(a.getGreen(), b.getGreen(), (double)a.getAlpha() / 255.0);
        int alpha = MathHelper.lerp(a.getAlpha(), b.getAlpha(), (double)a.getAlpha() / 255.0);
        return new Color(red, green, blue, alpha);
    }

    public static double clamp(double value, double low, double high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static int clamp(int value, int low, int high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static final double cos(double x) {
        return MathHelper.sin(x + (x > 1.5707963267948966 ? -4.71238898038469 : 1.5707963267948966));
    }

    public static final double sin(double x) {
        x = -0.4052847345693511 * x * Math.abs(x) + 1.2732395447351628 * x;
        return 0.225 * (x * Math.abs(x) - x) + x;
    }

    public static final double tan(double x) {
        return MathHelper.sin(x) / MathHelper.cos(x);
    }

    public static final double asin(double x) {
        return x * (Math.abs(x) * (Math.abs(x) * -0.048129527683101345 + -0.3438359939479152) + 0.9627618484259132) + Math.signum(x) * (1.0013894086010704 - Math.sqrt(1.0 - x * x));
    }

    public static final double acos(double x) {
        return 1.5707963267948966 - MathHelper.asin(x);
    }

    public static final double atan(double x) {
        return Math.abs(x) < 1.0 ? x / (1.0 + 0.280872 * x * x) : Math.signum(x) * 1.5707963267948966 - x / (x * x + 0.280872);
    }

    public static final double inverseSqrt(double x) {
        double xhalves = 0.5 * x;
        x = Double.longBitsToDouble(6910469410427058090L - (Double.doubleToRawLongBits(x) >> 1));
        return x * (1.5 - xhalves * x * x);
    }

    public static final double sqrt(double x) {
        return x * MathHelper.inverseSqrt(x);
    }

    public static int floor(double x) {
        int y = (int)x;
        if (x < (double)y) {
            return y - 1;
        }
        return y;
    }

    public static int floor(float x) {
        int y = (int)x;
        if (x < (float)y) {
            return y - 1;
        }
        return y;
    }

    public static byte max(byte value1, byte value2) {
        return value1 > value2 ? value1 : value2;
    }

    public static int roundUpPow2(int x) {
        if (x <= 0) {
            return 1;
        }
        if (x > 0x40000000) {
            throw new IllegalArgumentException("Rounding " + x + " to the next highest power of two would exceed the int range");
        }
        --x;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return ++x;
    }

    public static Float castFloat(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Float.valueOf(((Number)o).floatValue());
        }
        try {
            return Float.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Byte castByte(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }
        try {
            return Byte.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Short castShort(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).shortValue();
        }
        try {
            return Short.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer castInt(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).intValue();
        }
        try {
            return Integer.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double castDouble(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        try {
            return Double.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long castLong(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return ((Number)o).longValue();
        }
        try {
            return Long.valueOf(o.toString());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean castBoolean(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return (Boolean)o;
        }
        if (o instanceof String) {
            try {
                return Boolean.parseBoolean((String)o);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public static int mean(int ... values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    public static double mean(double ... values) {
        double sum = 0.0;
        for (double v : values) {
            sum += v;
        }
        return sum / (double)values.length;
    }
}

