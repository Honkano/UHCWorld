/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util.io;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

public final class Closer
implements Closeable {
    private static final Logger logger = Logger.getLogger(Closer.class.getCanonicalName());
    private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
    @VisibleForTesting
    final Suppressor suppressor;
    private final Deque<Closeable> stack = new ArrayDeque<Closeable>(4);
    private final Deque<ZipFile> zipStack = new ArrayDeque<ZipFile>(4);
    private Throwable thrown;

    public static Closer create() {
        return new Closer(SUPPRESSOR);
    }

    @VisibleForTesting
    Closer(Suppressor suppressor) {
        this.suppressor = (Suppressor)Preconditions.checkNotNull((Object)suppressor);
    }

    public <C extends Closeable> C register(C closeable) {
        this.stack.push(closeable);
        return closeable;
    }

    public <Z extends ZipFile> Z register(Z zipFile) {
        this.zipStack.push(zipFile);
        return zipFile;
    }

    public RuntimeException rethrow(Throwable e) throws IOException {
        this.thrown = e;
        Throwables.propagateIfPossible((Throwable)e, IOException.class);
        throw Throwables.propagate((Throwable)e);
    }

    public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
        this.thrown = e;
        Throwables.propagateIfPossible((Throwable)e, IOException.class);
        Throwables.propagateIfPossible((Throwable)e, declaredType);
        throw Throwables.propagate((Throwable)e);
    }

    public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
        this.thrown = e;
        Throwables.propagateIfPossible((Throwable)e, IOException.class);
        Throwables.propagateIfPossible((Throwable)e, declaredType1, declaredType2);
        throw Throwables.propagate((Throwable)e);
    }

    @Override
    public void close() throws IOException {
        Throwable throwable = this.thrown;
        while (!this.stack.isEmpty()) {
            Closeable closeable = this.stack.pop();
            try {
                closeable.close();
            }
            catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                    continue;
                }
                this.suppressor.suppress(closeable, throwable, e);
            }
        }
        while (!this.zipStack.isEmpty()) {
            ZipFile zipFile = this.zipStack.pop();
            try {
                zipFile.close();
            }
            catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                    continue;
                }
                this.suppressor.suppress(zipFile, throwable, e);
            }
        }
        if (this.thrown == null && throwable != null) {
            Throwables.propagateIfPossible((Throwable)throwable, IOException.class);
            throw new AssertionError((Object)throwable);
        }
    }

    @VisibleForTesting
    static final class SuppressingSuppressor
    implements Suppressor {
        static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
        static final Method addSuppressed = SuppressingSuppressor.getAddSuppressed();

        SuppressingSuppressor() {
        }

        static boolean isAvailable() {
            return addSuppressed != null;
        }

        private static Method getAddSuppressed() {
            try {
                return Throwable.class.getMethod("addSuppressed", Throwable.class);
            }
            catch (Throwable e) {
                return null;
            }
        }

        @Override
        public void suppress(Object closeable, Throwable thrown, Throwable suppressed) {
            if (thrown == suppressed) {
                return;
            }
            try {
                addSuppressed.invoke((Object)thrown, suppressed);
            }
            catch (Throwable e) {
                LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
            }
        }
    }

    @VisibleForTesting
    static final class LoggingSuppressor
    implements Suppressor {
        static final LoggingSuppressor INSTANCE = new LoggingSuppressor();

        LoggingSuppressor() {
        }

        @Override
        public void suppress(Object closeable, Throwable thrown, Throwable suppressed) {
            logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
        }
    }

    @VisibleForTesting
    static interface Suppressor {
        public void suppress(Object var1, Throwable var2, Throwable var3);
    }
}

