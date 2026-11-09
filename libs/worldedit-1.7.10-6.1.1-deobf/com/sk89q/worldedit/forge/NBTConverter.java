/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

final class NBTConverter {
    private NBTConverter() {
    }

    public static NBTBase toNative(Tag tag) {
        if (tag instanceof IntArrayTag) {
            return NBTConverter.toNative((IntArrayTag)tag);
        }
        if (tag instanceof ListTag) {
            return NBTConverter.toNative((ListTag)tag);
        }
        if (tag instanceof LongTag) {
            return NBTConverter.toNative((LongTag)tag);
        }
        if (tag instanceof StringTag) {
            return NBTConverter.toNative((StringTag)tag);
        }
        if (tag instanceof IntTag) {
            return NBTConverter.toNative((IntTag)tag);
        }
        if (tag instanceof ByteTag) {
            return NBTConverter.toNative((ByteTag)tag);
        }
        if (tag instanceof ByteArrayTag) {
            return NBTConverter.toNative((ByteArrayTag)tag);
        }
        if (tag instanceof CompoundTag) {
            return NBTConverter.toNative((CompoundTag)tag);
        }
        if (tag instanceof FloatTag) {
            return NBTConverter.toNative((FloatTag)tag);
        }
        if (tag instanceof ShortTag) {
            return NBTConverter.toNative((ShortTag)tag);
        }
        if (tag instanceof DoubleTag) {
            return NBTConverter.toNative((DoubleTag)tag);
        }
        throw new IllegalArgumentException("Can't convert tag of type " + tag.getClass().getCanonicalName());
    }

    public static NBTTagIntArray toNative(IntArrayTag tag) {
        int[] value = tag.getValue();
        return new NBTTagIntArray(Arrays.copyOf(value, value.length));
    }

    public static NBTTagList toNative(ListTag tag) {
        NBTTagList list = new NBTTagList();
        Iterator iterator = tag.getValue().iterator();
        while (iterator.hasNext()) {
            Tag child = (Tag)iterator.next();
            if (child instanceof EndTag) continue;
            list.appendTag(NBTConverter.toNative(child));
        }
        return list;
    }

    public static NBTTagLong toNative(LongTag tag) {
        return new NBTTagLong(tag.getValue().longValue());
    }

    public static NBTTagString toNative(StringTag tag) {
        return new NBTTagString(tag.getValue());
    }

    public static NBTTagInt toNative(IntTag tag) {
        return new NBTTagInt(tag.getValue().intValue());
    }

    public static NBTTagByte toNative(ByteTag tag) {
        return new NBTTagByte(tag.getValue().byteValue());
    }

    public static NBTTagByteArray toNative(ByteArrayTag tag) {
        byte[] value = tag.getValue();
        return new NBTTagByteArray(Arrays.copyOf(value, value.length));
    }

    public static NBTTagCompound toNative(CompoundTag tag) {
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry child : tag.getValue().entrySet()) {
            compound.setTag((String)child.getKey(), NBTConverter.toNative((Tag)child.getValue()));
        }
        return compound;
    }

    public static NBTTagFloat toNative(FloatTag tag) {
        return new NBTTagFloat(tag.getValue().floatValue());
    }

    public static NBTTagShort toNative(ShortTag tag) {
        return new NBTTagShort(tag.getValue().shortValue());
    }

    public static NBTTagDouble toNative(DoubleTag tag) {
        return new NBTTagDouble(tag.getValue().doubleValue());
    }

    public static Tag fromNative(NBTBase other) {
        if (other instanceof NBTTagIntArray) {
            return NBTConverter.fromNative((NBTTagIntArray)other);
        }
        if (other instanceof NBTTagList) {
            return NBTConverter.fromNative((NBTTagList)other);
        }
        if (other instanceof NBTTagEnd) {
            return NBTConverter.fromNative((NBTTagEnd)other);
        }
        if (other instanceof NBTTagLong) {
            return NBTConverter.fromNative((NBTTagLong)other);
        }
        if (other instanceof NBTTagString) {
            return NBTConverter.fromNative((NBTTagString)other);
        }
        if (other instanceof NBTTagInt) {
            return NBTConverter.fromNative((NBTTagInt)other);
        }
        if (other instanceof NBTTagByte) {
            return NBTConverter.fromNative((NBTTagByte)other);
        }
        if (other instanceof NBTTagByteArray) {
            return NBTConverter.fromNative((NBTTagByteArray)other);
        }
        if (other instanceof NBTTagCompound) {
            return NBTConverter.fromNative((NBTTagCompound)other);
        }
        if (other instanceof NBTTagFloat) {
            return NBTConverter.fromNative((NBTTagFloat)other);
        }
        if (other instanceof NBTTagShort) {
            return NBTConverter.fromNative((NBTTagShort)other);
        }
        if (other instanceof NBTTagDouble) {
            return NBTConverter.fromNative((NBTTagDouble)other);
        }
        throw new IllegalArgumentException("Can't convert other of type " + other.getClass().getCanonicalName());
    }

    public static IntArrayTag fromNative(NBTTagIntArray other) {
        int[] value = other.func_150302_c();
        return new IntArrayTag(Arrays.copyOf(value, value.length));
    }

    public static ListTag fromNative(NBTTagList other) {
        other = (NBTTagList)other.copy();
        ArrayList<Tag> list = new ArrayList<Tag>();
        Class listClass = StringTag.class;
        int tags = other.tagCount();
        for (int i = 0; i < tags; ++i) {
            Tag child = NBTConverter.fromNative(other.removeTag(0));
            list.add(child);
            listClass = child.getClass();
        }
        return new ListTag(listClass, list);
    }

    public static EndTag fromNative(NBTTagEnd other) {
        return new EndTag();
    }

    public static LongTag fromNative(NBTTagLong other) {
        return new LongTag(other.func_150291_c());
    }

    public static StringTag fromNative(NBTTagString other) {
        return new StringTag(other.func_150285_a_());
    }

    public static IntTag fromNative(NBTTagInt other) {
        return new IntTag(other.func_150287_d());
    }

    public static ByteTag fromNative(NBTTagByte other) {
        return new ByteTag(other.func_150290_f());
    }

    public static ByteArrayTag fromNative(NBTTagByteArray other) {
        byte[] value = other.func_150292_c();
        return new ByteArrayTag(Arrays.copyOf(value, value.length));
    }

    public static CompoundTag fromNative(NBTTagCompound other) {
        Set tags = other.func_150296_c();
        HashMap<String, Tag> map = new HashMap<String, Tag>();
        for (String tagName : tags) {
            map.put(tagName, NBTConverter.fromNative(other.getTag(tagName)));
        }
        return new CompoundTag(map);
    }

    public static FloatTag fromNative(NBTTagFloat other) {
        return new FloatTag(other.func_150288_h());
    }

    public static ShortTag fromNative(NBTTagShort other) {
        return new ShortTag(other.func_150289_e());
    }

    public static DoubleTag fromNative(NBTTagDouble other) {
        return new DoubleTag(other.func_150286_g());
    }
}

