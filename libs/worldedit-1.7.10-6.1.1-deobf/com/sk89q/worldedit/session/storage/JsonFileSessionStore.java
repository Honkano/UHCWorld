/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.session.storage;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.session.storage.SessionStore;
import com.sk89q.worldedit.util.gson.GsonUtil;
import com.sk89q.worldedit.util.io.Closer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileSessionStore
implements SessionStore {
    private static final Logger log = Logger.getLogger(JsonFileSessionStore.class.getCanonicalName());
    private final Gson gson;
    private final File dir;

    public JsonFileSessionStore(File dir) {
        Preconditions.checkNotNull((Object)dir);
        if (!dir.isDirectory() && !dir.mkdirs()) {
            log.log(Level.WARNING, "Failed to create directory '" + dir.getPath() + "' for sessions");
        }
        this.dir = dir;
        GsonBuilder builder = GsonUtil.createBuilder();
        this.gson = builder.create();
    }

    private File getPath(UUID id) {
        Preconditions.checkNotNull((Object)id);
        return new File(this.dir, id + ".json");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public LocalSession load(UUID id) throws IOException {
        File file = this.getPath(id);
        Closer closer = Closer.create();
        try {
            FileReader fr = closer.register(new FileReader(file));
            BufferedReader br = closer.register(new BufferedReader(fr));
            LocalSession localSession = (LocalSession)this.gson.fromJson((Reader)br, LocalSession.class);
            return localSession;
        }
        catch (JsonParseException e) {
            throw new IOException(e);
        }
        catch (FileNotFoundException e) {
            LocalSession localSession = new LocalSession();
            return localSession;
        }
        finally {
            try {
                closer.close();
            }
            catch (IOException iOException) {}
        }
    }

    @Override
    public void save(UUID id, LocalSession session) throws IOException {
        File finalFile = this.getPath(id);
        File tempFile = new File(finalFile.getParentFile(), finalFile.getName() + ".tmp");
        Closer closer = Closer.create();
        try {
            FileWriter fr = closer.register(new FileWriter(tempFile));
            BufferedWriter bw = closer.register(new BufferedWriter(fr));
            this.gson.toJson((Object)session, (Appendable)bw);
        }
        catch (JsonIOException e) {
            throw new IOException(e);
        }
        finally {
            try {
                closer.close();
            }
            catch (IOException iOException) {}
        }
        if (finalFile.exists() && !finalFile.delete()) {
            log.log(Level.WARNING, "Failed to delete " + finalFile.getPath() + " so the .tmp file can replace it");
        }
        if (!tempFile.renameTo(finalFile)) {
            log.log(Level.WARNING, "Failed to rename temporary session file to " + finalFile.getPath());
        }
    }
}

