/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.session;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.platform.ConfigurationLoadEvent;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.session.storage.JsonFileSessionStore;
import com.sk89q.worldedit.session.storage.SessionStore;
import com.sk89q.worldedit.session.storage.VoidStore;
import com.sk89q.worldedit.util.concurrency.EvenMoreExecutors;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class SessionManager {
    public static int EXPIRATION_GRACE = 600000;
    private static final int FLUSH_PERIOD = 30000;
    private static final ListeningExecutorService executorService = MoreExecutors.listeningDecorator((ExecutorService)EvenMoreExecutors.newBoundedCachedThreadPool(0, 1, 5));
    private static final Logger log = Logger.getLogger(SessionManager.class.getCanonicalName());
    private final Timer timer = new Timer();
    private final WorldEdit worldEdit;
    private final Map<UUID, SessionHolder> sessions = new HashMap<UUID, SessionHolder>();
    private SessionStore store = new VoidStore();

    public SessionManager(WorldEdit worldEdit) {
        Preconditions.checkNotNull((Object)worldEdit);
        this.worldEdit = worldEdit;
        worldEdit.getEventBus().register(this);
        this.timer.schedule((TimerTask)new SessionTracker(), 30000L, 30000L);
    }

    public synchronized boolean contains(SessionOwner owner) {
        Preconditions.checkNotNull((Object)owner);
        return this.sessions.containsKey(this.getKey(owner));
    }

    @Nullable
    public synchronized LocalSession findByName(String name) {
        Preconditions.checkNotNull((Object)name);
        for (SessionHolder holder : this.sessions.values()) {
            String test = holder.key.getName();
            if (test == null || !name.equals(test)) continue;
            return holder.session;
        }
        return null;
    }

    @Nullable
    public synchronized LocalSession getIfPresent(SessionOwner owner) {
        Preconditions.checkNotNull((Object)owner);
        SessionHolder stored = this.sessions.get(this.getKey(owner));
        if (stored != null) {
            return stored.session;
        }
        return null;
    }

    public synchronized LocalSession get(SessionOwner owner) {
        Preconditions.checkNotNull((Object)owner);
        LocalSession session = this.getIfPresent(owner);
        LocalConfiguration config = this.worldEdit.getConfiguration();
        SessionKey sessionKey = owner.getSessionKey();
        if (session == null) {
            try {
                session = this.store.load(this.getKey(sessionKey));
                session.postLoad();
            }
            catch (IOException e) {
                log.log(Level.WARNING, "Failed to load saved session", e);
                session = new LocalSession();
            }
            session.setConfiguration(config);
            session.setBlockChangeLimit(config.defaultChangeLimit);
            if (sessionKey.isActive()) {
                this.sessions.put(this.getKey(owner), new SessionHolder(sessionKey, session));
            }
        }
        int currentChangeLimit = session.getBlockChangeLimit();
        if (!owner.hasPermission("worldedit.limit.unrestricted") && config.maxChangeLimit > -1) {
            if (config.defaultChangeLimit < 0) {
                if (currentChangeLimit < 0 || currentChangeLimit > config.maxChangeLimit) {
                    session.setBlockChangeLimit(config.maxChangeLimit);
                }
            } else {
                int maxChangeLimit = config.maxChangeLimit;
                if (currentChangeLimit == -1 || currentChangeLimit > maxChangeLimit) {
                    session.setBlockChangeLimit(maxChangeLimit);
                }
            }
        }
        session.setUseInventory(config.useInventory && (!config.useInventoryOverride || !owner.hasPermission("worldedit.inventory.unrestricted") && (!config.useInventoryCreativeOverride || owner instanceof Player && !((Player)owner).hasCreativeMode())));
        return session;
    }

    private ListenableFuture<?> commit(final Map<SessionKey, LocalSession> sessions) {
        Preconditions.checkNotNull(sessions);
        if (sessions.isEmpty()) {
            return Futures.immediateFuture(sessions);
        }
        return executorService.submit((Callable)new Callable<Object>(){

            @Override
            public Object call() throws Exception {
                IOException exception = null;
                for (Map.Entry entry : sessions.entrySet()) {
                    SessionKey key = (SessionKey)entry.getKey();
                    if (!key.isPersistent()) continue;
                    try {
                        SessionManager.this.store.save(SessionManager.this.getKey(key), (LocalSession)entry.getValue());
                    }
                    catch (IOException e) {
                        log.log(Level.WARNING, "Failed to write session for UUID " + SessionManager.this.getKey(key), e);
                        exception = e;
                    }
                }
                if (exception != null) {
                    throw exception;
                }
                return sessions;
            }
        });
    }

    protected UUID getKey(SessionOwner owner) {
        return this.getKey(owner.getSessionKey());
    }

    protected UUID getKey(SessionKey key) {
        String forcedKey = System.getProperty("worldedit.session.uuidOverride");
        if (forcedKey != null) {
            return UUID.fromString(forcedKey);
        }
        return key.getUniqueId();
    }

    public synchronized void remove(SessionOwner owner) {
        Preconditions.checkNotNull((Object)owner);
        this.sessions.remove(this.getKey(owner));
    }

    public synchronized void clear() {
        this.sessions.clear();
    }

    @Subscribe
    public void onConfigurationLoad(ConfigurationLoadEvent event) {
        LocalConfiguration config = event.getConfiguration();
        File dir = new File(config.getWorkingDirectory(), "sessions");
        this.store = new JsonFileSessionStore(dir);
    }

    private class SessionTracker
    extends TimerTask {
        private SessionTracker() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            SessionManager sessionManager = SessionManager.this;
            synchronized (sessionManager) {
                long now = System.currentTimeMillis();
                Iterator it = SessionManager.this.sessions.values().iterator();
                HashMap<SessionKey, LocalSession> saveQueue = new HashMap<SessionKey, LocalSession>();
                while (it.hasNext()) {
                    SessionHolder stored = (SessionHolder)it.next();
                    if (stored.key.isActive()) {
                        stored.lastActive = now;
                        if (!stored.session.compareAndResetDirty()) continue;
                        saveQueue.put(stored.key, stored.session);
                        continue;
                    }
                    if (now - stored.lastActive <= (long)EXPIRATION_GRACE) continue;
                    if (stored.session.compareAndResetDirty()) {
                        saveQueue.put(stored.key, stored.session);
                    }
                    it.remove();
                }
                if (!saveQueue.isEmpty()) {
                    SessionManager.this.commit(saveQueue);
                }
            }
        }
    }

    private static class SessionHolder {
        private final SessionKey key;
        private final LocalSession session;
        private long lastActive = System.currentTimeMillis();

        private SessionHolder(SessionKey key, LocalSession session) {
            this.key = key;
            this.session = session;
        }
    }
}

