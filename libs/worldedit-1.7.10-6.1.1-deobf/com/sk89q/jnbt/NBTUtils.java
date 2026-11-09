/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.EndTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.Map;

public final class NBTUtils {
    private NBTUtils() {
    }

    public static String getTypeName(Class<? extends Tag> clazz) {
        if (clazz.equals(ByteArrayTag.class)) {
            return "TAG_Byte_Array";
        }
        if (clazz.equals(ByteTag.class)) {
            return "TAG_Byte";
        }
        if (clazz.equals(CompoundTag.class)) {
            return "TAG_Compound";
        }
        if (clazz.equals(DoubleTag.class)) {
            return "TAG_Double";
        }
        if (clazz.equals(EndTag.class)) {
            return "TAG_End";
        }
        if (clazz.equals(FloatTag.class)) {
            return "TAG_Float";
        }
        if (clazz.equals(IntTag.class)) {
            return "TAG_Int";
        }
        if (clazz.equals(ListTag.class)) {
            return "TAG_List";
        }
        if (clazz.equals(LongTag.class)) {
            return "TAG_Long";
        }
        if (clazz.equals(ShortTag.class)) {
            return "TAG_Short";
        }
        if (clazz.equals(StringTag.class)) {
            return "TAG_String";
        }
        if (clazz.equals(IntArrayTag.class)) {
            return "TAG_Int_Array";
        }
        throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
    }

    public static int getTypeCode(Class<? extends Tag> clazz) {
        if (clazz.equals(ByteArrayTag.class)) {
            return 7;
        }
        if (clazz.equals(ByteTag.class)) {
            return 1;
        }
        if (clazz.equals(CompoundTag.class)) {
            return 10;
        }
        if (clazz.equals(DoubleTag.class)) {
            return 6;
        }
        if (clazz.equals(EndTag.class)) {
            return 0;
        }
        if (clazz.equals(FloatTag.class)) {
            return 5;
        }
        if (clazz.equals(IntTag.class)) {
            return 3;
        }
        if (clazz.equals(ListTag.class)) {
            return 9;
        }
        if (clazz.equals(LongTag.class)) {
            return 4;
        }
        if (clazz.equals(ShortTag.class)) {
            return 2;
        }
        if (clazz.equals(StringTag.class)) {
            return 8;
        }
        if (clazz.equals(IntArrayTag.class)) {
            return 11;
        }
        throw new IllegalArgumentException("Invalid tag classs (" + clazz.getName() + ").");
    }

    public static Class<? extends Tag> getTypeClass(int type) {
        switch (type) {
            case 0: {
                return EndTag.class;
            }
            case 1: {
                return ByteTag.class;
            }
            case 2: {
                return ShortTag.class;
            }
            case 3: {
                return IntTag.class;
            }
            case 4: {
                return LongTag.class;
            }
            case 5: {
                return FloatTag.class;
            }
            case 6: {
                return DoubleTag.class;
            }
            case 7: {
                return ByteArrayTag.class;
            }
            case 8: {
                return StringTag.class;
            }
            case 9: {
                return ListTag.class;
            }
            case 10: {
                return CompoundTag.class;
            }
            case 11: {
                return IntArrayTag.class;
            }
        }
        throw new IllegalArgumentException("Invalid tag type : " + type + ".");
    }

    public static Vector toVector(ListTag listTag) {
        Preconditions.checkNotNull((Object)listTag);
        return new Vector(listTag.asDouble(0), listTag.asDouble(1), listTag.asDouble(2));
    }

    public static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws InvalidFormatException {
        if (!items.containsKey(key)) {
            throw new InvalidFormatException("Missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new InvalidFormatException(key + " tag is not of tag type " + expected.getName());
        }
        return (T)((Tag)expected.cast(tag));
    }
}

