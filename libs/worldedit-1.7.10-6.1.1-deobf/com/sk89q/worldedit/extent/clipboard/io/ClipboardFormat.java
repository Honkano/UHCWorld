/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.clipboard.io;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.extent.clipboard.io.SchematicReader;
import com.sk89q.worldedit.extent.clipboard.io.SchematicWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nullable;

public enum ClipboardFormat {
    SCHEMATIC(new String[]{"mcedit", "mce", "schematic"}){

        @Override
        public ClipboardReader getReader(InputStream inputStream) throws IOException {
            NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(inputStream));
            return new SchematicReader(nbtStream);
        }

        @Override
        public ClipboardWriter getWriter(OutputStream outputStream) throws IOException {
            NBTOutputStream nbtStream = new NBTOutputStream(new GZIPOutputStream(outputStream));
            return new SchematicWriter(nbtStream);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isFormat(File file) {
            boolean bl;
            FilterInputStream str = null;
            try {
                str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
                if ((((DataInputStream)str).readByte() & 0xFF) != 10) {
                    boolean bl2 = false;
                    return bl2;
                }
                byte[] nameBytes = new byte[((DataInputStream)str).readShort() & 0xFFFF];
                ((DataInputStream)str).readFully(nameBytes);
                String name = new String(nameBytes, NBTConstants.CHARSET);
                bl = name.equals("Schematic");
            }
            catch (IOException e) {
                boolean bl3 = false;
                return bl3;
            }
            finally {
                if (str != null) {
                    try {
                        str.close();
                    }
                    catch (IOException iOException) {}
                }
            }
            return bl;
        }
    };

    private static final Map<String, ClipboardFormat> aliasMap;
    private final String[] aliases;

    private ClipboardFormat(String ... aliases) {
        this.aliases = aliases;
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(this.aliases)));
    }

    public abstract ClipboardReader getReader(InputStream var1) throws IOException;

    public abstract ClipboardWriter getWriter(OutputStream var1) throws IOException;

    public abstract boolean isFormat(File var1);

    @Nullable
    public static ClipboardFormat findByAlias(String alias) {
        Preconditions.checkNotNull((Object)alias);
        return aliasMap.get(alias.toLowerCase().trim());
    }

    @Nullable
    public static ClipboardFormat findByFile(File file) {
        Preconditions.checkNotNull((Object)file);
        for (ClipboardFormat format : EnumSet.allOf(ClipboardFormat.class)) {
            if (!format.isFormat(file)) continue;
            return format;
        }
        return null;
    }

    static {
        aliasMap = new HashMap<String, ClipboardFormat>();
        for (ClipboardFormat format : EnumSet.allOf(ClipboardFormat.class)) {
            for (String key : format.aliases) {
                aliasMap.put(key, format);
            }
        }
    }
}

