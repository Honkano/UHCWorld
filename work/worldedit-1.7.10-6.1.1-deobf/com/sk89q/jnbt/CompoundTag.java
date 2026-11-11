/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CompoundTag
extends Tag {
    private final Map<String, Tag> value;

    public CompoundTag(Map<String, Tag> value) {
        this.value = Collections.unmodifiableMap(value);
    }

    public boolean containsKey(String key) {
        return this.value.containsKey(key);
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    public CompoundTag setValue(Map<String, Tag> value) {
        return new CompoundTag(value);
    }

    public CompoundTagBuilder createBuilder() {
        return new CompoundTagBuilder(new HashMap<String, Tag>(this.value));
    }

    public byte[] getByteArray(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteArrayTag) {
            return ((ByteArrayTag)tag).getValue();
        }
        return new byte[0];
    }

    public byte getByte(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag) {
            return ((ByteTag)tag).getValue();
        }
        return 0;
    }

    public double getDouble(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof DoubleTag) {
            return ((DoubleTag)tag).getValue();
        }
        return 0.0;
    }

    public double asDouble(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag) {
            return ((ByteTag)tag).getValue().byteValue();
        }
        if (tag instanceof ShortTag) {
            return ((ShortTag)tag).getValue().shortValue();
        }
        if (tag instanceof IntTag) {
            return ((IntTag)tag).getValue().intValue();
        }
        if (tag instanceof LongTag) {
            return ((LongTag)tag).getValue().longValue();
        }
        if (tag instanceof FloatTag) {
            return ((FloatTag)tag).getValue().floatValue();
        }
        if (tag instanceof DoubleTag) {
            return ((DoubleTag)tag).getValue();
        }
        return 0.0;
    }

    public float getFloat(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof FloatTag) {
            return ((FloatTag)tag).getValue().floatValue();
        }
        return 0.0f;
    }

    public int[] getIntArray(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof IntArrayTag) {
            return ((IntArrayTag)tag).getValue();
        }
        return new int[0];
    }

    public int getInt(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof IntTag) {
            return ((IntTag)tag).getValue();
        }
        return 0;
    }

    public int asInt(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag) {
            return ((ByteTag)tag).getValue().byteValue();
        }
        if (tag instanceof ShortTag) {
            return ((ShortTag)tag).getValue().shortValue();
        }
        if (tag instanceof IntTag) {
            return ((IntTag)tag).getValue();
        }
        if (tag instanceof LongTag) {
            return ((LongTag)tag).getValue().intValue();
        }
        if (tag instanceof FloatTag) {
            return ((FloatTag)tag).getValue().intValue();
        }
        if (tag instanceof DoubleTag) {
            return ((DoubleTag)tag).getValue().intValue();
        }
        return 0;
    }

    public List<Tag> getList(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ListTag) {
            return ((ListTag)tag).getValue();
        }
        return Collections.emptyList();
    }

    public ListTag getListTag(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ListTag) {
            return (ListTag)tag;
        }
        return new ListTag(StringTag.class, Collections.emptyList());
    }

    public <T extends Tag> List<T> getList(String key, Class<T> listType) {
        Tag tag = this.value.get(key);
        if (tag instanceof ListTag) {
            ListTag listTag = (ListTag)tag;
            if (listTag.getType().equals(listType)) {
                return listTag.getValue();
            }
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public long getLong(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof LongTag) {
            return ((LongTag)tag).getValue();
        }
        return 0L;
    }

    public long asLong(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ByteTag) {
            return ((ByteTag)tag).getValue().byteValue();
        }
        if (tag instanceof ShortTag) {
            return ((ShortTag)tag).getValue().shortValue();
        }
        if (tag instanceof IntTag) {
            return ((IntTag)tag).getValue().intValue();
        }
        if (tag instanceof LongTag) {
            return ((LongTag)tag).getValue();
        }
        if (tag instanceof FloatTag) {
            return ((FloatTag)tag).getValue().longValue();
        }
        if (tag instanceof DoubleTag) {
            return ((DoubleTag)tag).getValue().longValue();
        }
        return 0L;
    }

    public short getShort(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof ShortTag) {
            return ((ShortTag)tag).getValue();
        }
        return 0;
    }

    public String getString(String key) {
        Tag tag = this.value.get(key);
        if (tag instanceof StringTag) {
            return ((StringTag)tag).getValue();
        }
        return "";
    }

    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound").append(": ").append(this.value.size()).append(" entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : this.value.entrySet()) {
            bldr.append("   ").append(entry.getValue().toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }
}

