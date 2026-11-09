/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.AbstractMask2D;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.session.request.Request;
import javax.annotation.Nullable;

public final class Masks {
    private static final AlwaysTrue ALWAYS_TRUE = new AlwaysTrue();
    private static final AlwaysFalse ALWAYS_FALSE = new AlwaysFalse();

    private Masks() {
    }

    public static Mask alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static Mask2D alwaysTrue2D() {
        return ALWAYS_TRUE;
    }

    public static Mask negate(final Mask mask) {
        if (mask instanceof AlwaysTrue) {
            return ALWAYS_FALSE;
        }
        if (mask instanceof AlwaysFalse) {
            return ALWAYS_TRUE;
        }
        Preconditions.checkNotNull((Object)mask);
        return new AbstractMask(){

            @Override
            public boolean test(Vector vector) {
                return !mask.test(vector);
            }

            @Override
            @Nullable
            public Mask2D toMask2D() {
                Mask2D mask2d = mask.toMask2D();
                if (mask2d != null) {
                    return Masks.negate(mask2d);
                }
                return null;
            }
        };
    }

    public static Mask2D negate(final Mask2D mask) {
        if (mask instanceof AlwaysTrue) {
            return ALWAYS_FALSE;
        }
        if (mask instanceof AlwaysFalse) {
            return ALWAYS_TRUE;
        }
        Preconditions.checkNotNull((Object)mask);
        return new AbstractMask2D(){

            @Override
            public boolean test(Vector2D vector) {
                return !mask.test(vector);
            }
        };
    }

    public static Mask asMask(final Mask2D mask) {
        return new AbstractMask(){

            @Override
            public boolean test(Vector vector) {
                return mask.test(vector.toVector2D());
            }

            @Override
            @Nullable
            public Mask2D toMask2D() {
                return mask;
            }
        };
    }

    @Deprecated
    public static Mask wrap(final com.sk89q.worldedit.masks.Mask mask, final EditSession editSession) {
        Preconditions.checkNotNull((Object)mask);
        return new AbstractMask(){

            @Override
            public boolean test(Vector vector) {
                return mask.matches(editSession, vector);
            }

            @Override
            @Nullable
            public Mask2D toMask2D() {
                return null;
            }
        };
    }

    public static Mask wrap(final com.sk89q.worldedit.masks.Mask mask) {
        Preconditions.checkNotNull((Object)mask);
        return new AbstractMask(){

            @Override
            public boolean test(Vector vector) {
                EditSession editSession = Request.request().getEditSession();
                return editSession != null && mask.matches(editSession, vector);
            }

            @Override
            @Nullable
            public Mask2D toMask2D() {
                return null;
            }
        };
    }

    public static com.sk89q.worldedit.masks.Mask wrap(final Mask mask) {
        Preconditions.checkNotNull((Object)mask);
        return new com.sk89q.worldedit.masks.AbstractMask(){

            @Override
            public boolean matches(EditSession editSession, Vector position) {
                Request.request().setEditSession(editSession);
                return mask.test(position);
            }
        };
    }

    private static class AlwaysFalse
    implements Mask,
    Mask2D {
        private AlwaysFalse() {
        }

        @Override
        public boolean test(Vector vector) {
            return false;
        }

        @Override
        public boolean test(Vector2D vector) {
            return false;
        }

        @Override
        @Nullable
        public Mask2D toMask2D() {
            return this;
        }
    }

    private static class AlwaysTrue
    implements Mask,
    Mask2D {
        private AlwaysTrue() {
        }

        @Override
        public boolean test(Vector vector) {
            return true;
        }

        @Override
        public boolean test(Vector2D vector) {
            return true;
        }

        @Override
        @Nullable
        public Mask2D toMask2D() {
            return this;
        }
    }
}

