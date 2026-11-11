/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.schematic;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class SchematicFormat {
    private static final Map<String, SchematicFormat> SCHEMATIC_FORMATS = new HashMap<String, SchematicFormat>();
    public static final SchematicFormat MCEDIT = new MCEditSchematicFormat();
    private final String name;
    private final String[] lookupNames;

    public static Set<SchematicFormat> getFormats() {
        return Collections.unmodifiableSet(new HashSet<SchematicFormat>(SCHEMATIC_FORMATS.values()));
    }

    public static SchematicFormat getFormat(String lookupName) {
        return SCHEMATIC_FORMATS.get(lookupName.toLowerCase());
    }

    public static SchematicFormat getFormat(File file) {
        if (!file.isFile()) {
            return null;
        }
        for (SchematicFormat format : SCHEMATIC_FORMATS.values()) {
            if (!format.isOfFormat(file)) continue;
            return format;
        }
        return null;
    }

    protected SchematicFormat(String name, String ... lookupNames) {
        this.name = name;
        ArrayList<String> registeredLookupNames = new ArrayList<String>(lookupNames.length);
        for (int i = 0; i < lookupNames.length; ++i) {
            if (i != 0 && SCHEMATIC_FORMATS.containsKey(lookupNames[i].toLowerCase())) continue;
            SCHEMATIC_FORMATS.put(lookupNames[i].toLowerCase(), this);
            registeredLookupNames.add(lookupNames[i].toLowerCase());
        }
        this.lookupNames = registeredLookupNames.toArray(new String[registeredLookupNames.size()]);
    }

    public String getName() {
        return this.name;
    }

    public String[] getLookupNames() {
        return this.lookupNames;
    }

    public BaseBlock getBlockForId(int id, short data) {
        switch (id) {
            default: 
        }
        BaseBlock block = new BaseBlock(id, data);
        return block;
    }

    public abstract CuboidClipboard load(File var1) throws IOException, DataException;

    public abstract void save(CuboidClipboard var1, File var2) throws IOException, DataException;

    public abstract boolean isOfFormat(File var1);
}

