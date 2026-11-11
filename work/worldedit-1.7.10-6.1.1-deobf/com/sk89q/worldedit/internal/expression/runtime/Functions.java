/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.ExpressionEnvironment;
import com.sk89q.worldedit.internal.expression.runtime.Function;
import com.sk89q.worldedit.internal.expression.runtime.LValue;
import com.sk89q.worldedit.internal.expression.runtime.LValueFunction;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.math.noise.PerlinNoise;
import com.sk89q.worldedit.math.noise.RidgedMultiFractalNoise;
import com.sk89q.worldedit.math.noise.VoronoiNoise;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Functions {
    private static final Map<String, List<Overload>> functions = new HashMap<String, List<Overload>>();
    private static final Map<Integer, double[]> gmegabuf;
    private final Map<Integer, double[]> megabuf = new HashMap<Integer, double[]>();
    private static final Random random;
    private static final ThreadLocal<PerlinNoise> localPerlin;
    private static final ThreadLocal<VoronoiNoise> localVoronoi;
    private static final ThreadLocal<RidgedMultiFractalNoise> localRidgedMulti;

    public static Function getFunction(int position, String name, RValue ... args) throws NoSuchMethodException {
        Method getter = Functions.getMethod(name, false, args);
        try {
            Method setter = Functions.getMethod(name, true, args);
            return new LValueFunction(position, getter, setter, args);
        }
        catch (NoSuchMethodException e) {
            return new Function(position, getter, args);
        }
    }

    private static Method getMethod(String name, boolean isSetter, RValue ... args) throws NoSuchMethodException {
        List<Overload> overloads = functions.get(name);
        if (overloads != null) {
            for (Overload overload : overloads) {
                if (!overload.matches(isSetter, args)) continue;
                return overload.method;
            }
        }
        throw new NoSuchMethodException();
    }

    public static void addFunction(Method method) throws IllegalArgumentException {
        String methodName = method.getName();
        Overload overload = new Overload(method);
        List<Overload> overloads = functions.get(methodName);
        if (overloads == null) {
            overloads = new ArrayList<Overload>();
            functions.put(methodName, overloads);
        }
        overloads.add(overload);
    }

    public static double sin(RValue x) throws EvaluationException {
        return Math.sin(x.getValue());
    }

    public static double cos(RValue x) throws EvaluationException {
        return Math.cos(x.getValue());
    }

    public static double tan(RValue x) throws EvaluationException {
        return Math.tan(x.getValue());
    }

    public static double asin(RValue x) throws EvaluationException {
        return Math.asin(x.getValue());
    }

    public static double acos(RValue x) throws EvaluationException {
        return Math.acos(x.getValue());
    }

    public static double atan(RValue x) throws EvaluationException {
        return Math.atan(x.getValue());
    }

    public static double atan2(RValue y, RValue x) throws EvaluationException {
        return Math.atan2(y.getValue(), x.getValue());
    }

    public static double sinh(RValue x) throws EvaluationException {
        return Math.sinh(x.getValue());
    }

    public static double cosh(RValue x) throws EvaluationException {
        return Math.cosh(x.getValue());
    }

    public static double tanh(RValue x) throws EvaluationException {
        return Math.tanh(x.getValue());
    }

    public static double sqrt(RValue x) throws EvaluationException {
        return Math.sqrt(x.getValue());
    }

    public static double cbrt(RValue x) throws EvaluationException {
        return Math.cbrt(x.getValue());
    }

    public static double abs(RValue x) throws EvaluationException {
        return Math.abs(x.getValue());
    }

    public static double min(RValue a, RValue b) throws EvaluationException {
        return Math.min(a.getValue(), b.getValue());
    }

    public static double min(RValue a, RValue b, RValue c) throws EvaluationException {
        return Math.min(a.getValue(), Math.min(b.getValue(), c.getValue()));
    }

    public static double max(RValue a, RValue b) throws EvaluationException {
        return Math.max(a.getValue(), b.getValue());
    }

    public static double max(RValue a, RValue b, RValue c) throws EvaluationException {
        return Math.max(a.getValue(), Math.max(b.getValue(), c.getValue()));
    }

    public static double ceil(RValue x) throws EvaluationException {
        return Math.ceil(x.getValue());
    }

    public static double floor(RValue x) throws EvaluationException {
        return Math.floor(x.getValue());
    }

    public static double rint(RValue x) throws EvaluationException {
        return Math.rint(x.getValue());
    }

    public static double round(RValue x) throws EvaluationException {
        return Math.round(x.getValue());
    }

    public static double exp(RValue x) throws EvaluationException {
        return Math.exp(x.getValue());
    }

    public static double ln(RValue x) throws EvaluationException {
        return Math.log(x.getValue());
    }

    public static double log(RValue x) throws EvaluationException {
        return Math.log(x.getValue());
    }

    public static double log10(RValue x) throws EvaluationException {
        return Math.log10(x.getValue());
    }

    public static double rotate(LValue x, LValue y, RValue angle) throws EvaluationException {
        double f = angle.getValue();
        double cosF = Math.cos(f);
        double sinF = Math.sin(f);
        double xOld = x.getValue();
        double yOld = y.getValue();
        x.assign(xOld * cosF - yOld * sinF);
        y.assign(xOld * sinF + yOld * cosF);
        return 0.0;
    }

    public static double swap(LValue x, LValue y) throws EvaluationException {
        double tmp = x.getValue();
        x.assign(y.getValue());
        y.assign(tmp);
        return 0.0;
    }

    public Map<Integer, double[]> getMegabuf() {
        return this.megabuf;
    }

    private static double[] getSubBuffer(Map<Integer, double[]> megabuf, Integer key) {
        double[] ret = megabuf.get(key);
        if (ret == null) {
            ret = new double[1024];
            megabuf.put(key, ret);
        }
        return ret;
    }

    private static double getBufferItem(Map<Integer, double[]> megabuf, int index) {
        return Functions.getSubBuffer(megabuf, index & 0xFFFFFC00)[index & 0x3FF];
    }

    private static double setBufferItem(Map<Integer, double[]> megabuf, int index, double value) {
        double d = value;
        Functions.getSubBuffer(megabuf, (Integer)Integer.valueOf((int)(index & 0xFFFFFC00)))[index & 0x3FF] = d;
        return d;
    }

    @Function.Dynamic
    public static double gmegabuf(RValue index) throws EvaluationException {
        return Functions.getBufferItem(gmegabuf, (int)index.getValue());
    }

    @Function.Dynamic
    public static double gmegabuf(RValue index, double value) throws EvaluationException {
        return Functions.setBufferItem(gmegabuf, (int)index.getValue(), value);
    }

    @Function.Dynamic
    public static double megabuf(RValue index) throws EvaluationException {
        return Functions.getBufferItem(Expression.getInstance().getFunctions().megabuf, (int)index.getValue());
    }

    @Function.Dynamic
    public static double megabuf(RValue index, double value) throws EvaluationException {
        return Functions.setBufferItem(Expression.getInstance().getFunctions().megabuf, (int)index.getValue(), value);
    }

    @Function.Dynamic
    public static double closest(RValue x, RValue y, RValue z, RValue index, RValue count, RValue stride) throws EvaluationException {
        return Functions.findClosest(Expression.getInstance().getFunctions().megabuf, x.getValue(), y.getValue(), z.getValue(), (int)index.getValue(), (int)count.getValue(), (int)stride.getValue());
    }

    @Function.Dynamic
    public static double gclosest(RValue x, RValue y, RValue z, RValue index, RValue count, RValue stride) throws EvaluationException {
        return Functions.findClosest(gmegabuf, x.getValue(), y.getValue(), z.getValue(), (int)index.getValue(), (int)count.getValue(), (int)stride.getValue());
    }

    private static double findClosest(Map<Integer, double[]> megabuf, double x, double y, double z, int index, int count, int stride) {
        int closestIndex = -1;
        double minDistanceSquared = Double.MAX_VALUE;
        for (int i = 0; i < count; ++i) {
            double currentZ;
            double currentY;
            double currentX = Functions.getBufferItem(megabuf, index + 0) - x;
            double currentDistanceSquared = currentX * currentX + (currentY = Functions.getBufferItem(megabuf, index + 1) - y) * currentY + (currentZ = Functions.getBufferItem(megabuf, index + 2) - z) * currentZ;
            if (currentDistanceSquared < minDistanceSquared) {
                minDistanceSquared = currentDistanceSquared;
                closestIndex = index;
            }
            index += stride;
        }
        return closestIndex;
    }

    @Function.Dynamic
    public static double random() {
        return random.nextDouble();
    }

    @Function.Dynamic
    public static double randint(RValue max) throws EvaluationException {
        return random.nextInt((int)Math.floor(max.getValue()));
    }

    public static double perlin(RValue seed, RValue x, RValue y, RValue z, RValue frequency, RValue octaves, RValue persistence) throws EvaluationException {
        PerlinNoise perlin = localPerlin.get();
        try {
            perlin.setSeed((int)seed.getValue());
            perlin.setFrequency(frequency.getValue());
            perlin.setOctaveCount((int)octaves.getValue());
            perlin.setPersistence(persistence.getValue());
        }
        catch (IllegalArgumentException e) {
            throw new EvaluationException(0, "Perlin noise error: " + e.getMessage());
        }
        return perlin.noise(new Vector(x.getValue(), y.getValue(), z.getValue()));
    }

    public static double voronoi(RValue seed, RValue x, RValue y, RValue z, RValue frequency) throws EvaluationException {
        VoronoiNoise voronoi = localVoronoi.get();
        try {
            voronoi.setSeed((int)seed.getValue());
            voronoi.setFrequency(frequency.getValue());
        }
        catch (IllegalArgumentException e) {
            throw new EvaluationException(0, "Voronoi error: " + e.getMessage());
        }
        return voronoi.noise(new Vector(x.getValue(), y.getValue(), z.getValue()));
    }

    public static double ridgedmulti(RValue seed, RValue x, RValue y, RValue z, RValue frequency, RValue octaves) throws EvaluationException {
        RidgedMultiFractalNoise ridgedMulti = localRidgedMulti.get();
        try {
            ridgedMulti.setSeed((int)seed.getValue());
            ridgedMulti.setFrequency(frequency.getValue());
            ridgedMulti.setOctaveCount((int)octaves.getValue());
        }
        catch (IllegalArgumentException e) {
            throw new EvaluationException(0, "Ridged multi error: " + e.getMessage());
        }
        return ridgedMulti.noise(new Vector(x.getValue(), y.getValue(), z.getValue()));
    }

    private static double queryInternal(RValue type, RValue data, double typeId, double dataValue) throws EvaluationException {
        double ret;
        double d = ret = !(type.getValue() != -1.0 && typeId != type.getValue() || data.getValue() != -1.0 && dataValue != data.getValue()) ? 1.0 : 0.0;
        if (type instanceof LValue) {
            ((LValue)type).assign(typeId);
        }
        if (data instanceof LValue) {
            ((LValue)data).assign(dataValue);
        }
        return ret;
    }

    @Function.Dynamic
    public static double query(RValue x, RValue y, RValue z, RValue type, RValue data) throws EvaluationException {
        double xp = x.getValue();
        double yp = y.getValue();
        double zp = z.getValue();
        ExpressionEnvironment environment = Expression.getInstance().getEnvironment();
        double typeId = environment.getBlockType(xp, yp, zp);
        double dataValue = environment.getBlockData(xp, yp, zp);
        return Functions.queryInternal(type, data, typeId, dataValue);
    }

    @Function.Dynamic
    public static double queryAbs(RValue x, RValue y, RValue z, RValue type, RValue data) throws EvaluationException {
        double xp = x.getValue();
        double yp = y.getValue();
        double zp = z.getValue();
        ExpressionEnvironment environment = Expression.getInstance().getEnvironment();
        double typeId = environment.getBlockTypeAbs(xp, yp, zp);
        double dataValue = environment.getBlockDataAbs(xp, yp, zp);
        return Functions.queryInternal(type, data, typeId, dataValue);
    }

    @Function.Dynamic
    public static double queryRel(RValue x, RValue y, RValue z, RValue type, RValue data) throws EvaluationException {
        double xp = x.getValue();
        double yp = y.getValue();
        double zp = z.getValue();
        ExpressionEnvironment environment = Expression.getInstance().getEnvironment();
        double typeId = environment.getBlockTypeRel(xp, yp, zp);
        double dataValue = environment.getBlockDataRel(xp, yp, zp);
        return Functions.queryInternal(type, data, typeId, dataValue);
    }

    static {
        for (Method method : Functions.class.getMethods()) {
            try {
                Functions.addFunction(method);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        gmegabuf = new HashMap<Integer, double[]>();
        random = new Random();
        localPerlin = new ThreadLocal<PerlinNoise>(){

            @Override
            protected PerlinNoise initialValue() {
                return new PerlinNoise();
            }
        };
        localVoronoi = new ThreadLocal<VoronoiNoise>(){

            @Override
            protected VoronoiNoise initialValue() {
                return new VoronoiNoise();
            }
        };
        localRidgedMulti = new ThreadLocal<RidgedMultiFractalNoise>(){

            @Override
            protected RidgedMultiFractalNoise initialValue() {
                return new RidgedMultiFractalNoise();
            }
        };
    }

    private static class Overload {
        private final Method method;
        private final int mask;
        private final boolean isSetter;

        private Overload(Method method) throws IllegalArgumentException {
            Class<?>[] parameters;
            this.method = method;
            boolean isSetter = false;
            int accum = 0;
            for (Class<?> parameter : parameters = method.getParameterTypes()) {
                if (isSetter) {
                    throw new IllegalArgumentException("Method takes arguments that can't be cast to RValue.");
                }
                if (Double.TYPE.equals(parameter)) {
                    isSetter = true;
                    continue;
                }
                if (!RValue.class.isAssignableFrom(parameter)) {
                    throw new IllegalArgumentException("Method takes arguments that can't be cast to RValue.");
                }
                accum <<= 2;
                if (LValue.class.isAssignableFrom(parameter)) {
                    accum |= 3;
                    continue;
                }
                accum |= 1;
            }
            this.mask = accum;
            this.isSetter = isSetter;
        }

        public boolean matches(boolean isSetter, RValue ... args) {
            if (this.isSetter != isSetter) {
                return false;
            }
            if (this.method.getParameterTypes().length != args.length) {
                return false;
            }
            int accum = 0;
            for (RValue argument : args) {
                accum <<= 2;
                if (argument instanceof LValue) {
                    accum |= 3;
                    continue;
                }
                accum |= 1;
            }
            return (accum & this.mask) == this.mask;
        }
    }
}

