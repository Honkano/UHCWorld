/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class CompoundTagBuilder {
    private final Map<String, Tag> entries;

    CompoundTagBuilder() {
        this.entries = new HashMap<String, Tag>();
    }

    CompoundTagBuilder(Map<String, Tag> value) {
        Preconditions.checkNotNull(value);
        this.entries = value;
    }

    public CompoundTagBuilder put(String key, Tag value) {
        Preconditions.checkNotNull((Object)key);
        Preconditions.checkNotNull((Object)value);
        this.entries.put(key, value);
        return this;
    }

    public CompoundTagBuilder putByteArray(String key, byte[] value) {
        return this.put(key, new ByteArrayTag(value));
    }

    public CompoundTagBuilder putByte(String key, byte value) {
        return this.put(key, new ByteTag(value));
    }

    public CompoundTagBuilder putDouble(String key, double value) {
        return this.put(key, new DoubleTag(value));
    }

    public CompoundTagBuilder putFloat(String key, float value) {
        return this.put(key, new FloatTag(value));
    }

    public CompoundTagBuilder putIntArray(String key, int[] value) {
        return this.put(key, new IntArrayTag(value));
    }

    public CompoundTagBuilder putInt(String key, int value) {
        return this.put(key, new IntTag(value));
    }

    public CompoundTagBuilder putLong(String key, long value) {
        return this.put(key, new LongTag(value));
    }

    public CompoundTagBuilder putShort(String key, short value) {
        return this.put(key, new ShortTag(value));
    }

    public CompoundTagBuilder putString(String key, String value) {
        return this.put(key, new StringTag(value));
    }

    public CompoundTagBuilder putAll(Map<String, ? extends Tag> value) {
        Preconditions.checkNotNull(value);
        for (Map.Entry<String, ? extends Tag> entry : value.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public CompoundTag build() {
        return new CompoundTag(new HashMap<String, Tag>(this.entries));
    }

    public static CompoundTagBuilder create() {
        return new CompoundTagBuilder();
    }
}

