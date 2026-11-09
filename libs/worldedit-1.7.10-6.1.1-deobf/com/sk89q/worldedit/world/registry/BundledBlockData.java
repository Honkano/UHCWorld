/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.util.gson.VectorAdapter;
import com.sk89q.worldedit.world.registry.SimpleBlockMaterial;
import com.sk89q.worldedit.world.registry.SimpleState;
import com.sk89q.worldedit.world.registry.State;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class BundledBlockData {
    private static final Logger log = Logger.getLogger(BundledBlockData.class.getCanonicalName());
    private static final BundledBlockData INSTANCE = new BundledBlockData();
    private final Map<String, BlockEntry> idMap = new HashMap<String, BlockEntry>();
    private final Map<Integer, BlockEntry> legacyMap = new HashMap<Integer, BlockEntry>();

    private BundledBlockData() {
        try {
            this.loadFromResource();
        }
        catch (IOException e) {
            log.log(Level.WARNING, "Failed to load the built-in block registry", e);
        }
    }

    private void loadFromResource() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Vector.class, (Object)new VectorAdapter());
        Gson gson = gsonBuilder.create();
        URL url = BundledBlockData.class.getResource("blocks.json");
        if (url == null) {
            throw new IOException("Could not find blocks.json");
        }
        String data = Resources.toString((URL)url, (Charset)Charset.defaultCharset());
        List entries = (List)gson.fromJson(data, new TypeToken<List<BlockEntry>>(){}.getType());
        for (BlockEntry entry : entries) {
            entry.postDeserialization();
            this.idMap.put(entry.id, entry);
            this.legacyMap.put(entry.legacyId, entry);
        }
    }

    @Nullable
    private BlockEntry findById(String id) {
        return this.idMap.get(id);
    }

    @Nullable
    private BlockEntry findById(int id) {
        return this.legacyMap.get(id);
    }

    @Nullable
    public Integer toLegacyId(String id) {
        BlockEntry entry = this.findById(id);
        if (entry != null) {
            return entry.legacyId;
        }
        return null;
    }

    @Nullable
    public BlockMaterial getMaterialById(int id) {
        BlockEntry entry = this.findById(id);
        if (entry != null) {
            return entry.material;
        }
        return null;
    }

    @Nullable
    public Map<String, ? extends State> getStatesById(int id) {
        BlockEntry entry = this.findById(id);
        if (entry != null) {
            return entry.states;
        }
        return null;
    }

    public static BundledBlockData getInstance() {
        return INSTANCE;
    }

    private static class BlockEntry {
        private int legacyId;
        private String id;
        private String unlocalizedName;
        private List<String> aliases;
        private Map<String, SimpleState> states = new HashMap<String, SimpleState>();
        private SimpleBlockMaterial material = new SimpleBlockMaterial();

        private BlockEntry() {
        }

        void postDeserialization() {
            for (SimpleState state : this.states.values()) {
                state.postDeserialization();
            }
        }
    }
}

