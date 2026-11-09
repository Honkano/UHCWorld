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
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

public final class NBTOutputStream
implements Closeable {
    private final DataOutputStream os;

    public NBTOutputStream(OutputStream os) throws IOException {
        this.os = new DataOutputStream(os);
    }

    public void writeNamedTag(String name, Tag tag) throws IOException {
        Preconditions.checkNotNull((Object)name);
        Preconditions.checkNotNull((Object)tag);
        int type = NBTUtils.getTypeCode(tag.getClass());
        byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
        this.os.writeByte(type);
        this.os.writeShort(nameBytes.length);
        this.os.write(nameBytes);
        if (type == 0) {
            throw new IOException("Named TAG_End not permitted.");
        }
        this.writeTagPayload(tag);
    }

    private void writeTagPayload(Tag tag) throws IOException {
        int type = NBTUtils.getTypeCode(tag.getClass());
        switch (type) {
            case 0: {
                this.writeEndTagPayload((EndTag)tag);
                break;
            }
            case 1: {
                this.writeByteTagPayload((ByteTag)tag);
                break;
            }
            case 2: {
                this.writeShortTagPayload((ShortTag)tag);
                break;
            }
            case 3: {
                this.writeIntTagPayload((IntTag)tag);
                break;
            }
            case 4: {
                this.writeLongTagPayload((LongTag)tag);
                break;
            }
            case 5: {
                this.writeFloatTagPayload((FloatTag)tag);
                break;
            }
            case 6: {
                this.writeDoubleTagPayload((DoubleTag)tag);
                break;
            }
            case 7: {
                this.writeByteArrayTagPayload((ByteArrayTag)tag);
                break;
            }
            case 8: {
                this.writeStringTagPayload((StringTag)tag);
                break;
            }
            case 9: {
                this.writeListTagPayload((ListTag)tag);
                break;
            }
            case 10: {
                this.writeCompoundTagPayload((CompoundTag)tag);
                break;
            }
            case 11: {
                this.writeIntArrayTagPayload((IntArrayTag)tag);
                break;
            }
            default: {
                throw new IOException("Invalid tag type: " + type + ".");
            }
        }
    }

    private void writeByteTagPayload(ByteTag tag) throws IOException {
        this.os.writeByte(tag.getValue().byteValue());
    }

    private void writeByteArrayTagPayload(ByteArrayTag tag) throws IOException {
        byte[] bytes = tag.getValue();
        this.os.writeInt(bytes.length);
        this.os.write(bytes);
    }

    private void writeCompoundTagPayload(CompoundTag tag) throws IOException {
        for (Map.Entry entry : tag.getValue().entrySet()) {
            this.writeNamedTag((String)entry.getKey(), (Tag)entry.getValue());
        }
        this.os.writeByte(0);
    }

    private void writeListTagPayload(ListTag tag) throws IOException {
        Class<? extends Tag> clazz = tag.getType();
        Object tags = tag.getValue();
        int size = tags.size();
        this.os.writeByte(NBTUtils.getTypeCode(clazz));
        this.os.writeInt(size);
        Iterator iterator = tags.iterator();
        while (iterator.hasNext()) {
            Tag tag1 = (Tag)iterator.next();
            this.writeTagPayload(tag1);
        }
    }

    private void writeStringTagPayload(StringTag tag) throws IOException {
        byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
        this.os.writeShort(bytes.length);
        this.os.write(bytes);
    }

    private void writeDoubleTagPayload(DoubleTag tag) throws IOException {
        this.os.writeDouble(tag.getValue());
    }

    private void writeFloatTagPayload(FloatTag tag) throws IOException {
        this.os.writeFloat(tag.getValue().floatValue());
    }

    private void writeLongTagPayload(LongTag tag) throws IOException {
        this.os.writeLong(tag.getValue());
    }

    private void writeIntTagPayload(IntTag tag) throws IOException {
        this.os.writeInt(tag.getValue());
    }

    private void writeShortTagPayload(ShortTag tag) throws IOException {
        this.os.writeShort(tag.getValue().shortValue());
    }

    private void writeEndTagPayload(EndTag tag) {
    }

    private void writeIntArrayTagPayload(IntArrayTag tag) throws IOException {
        int[] data = tag.getValue();
        this.os.writeInt(data.length);
        for (int aData : data) {
            this.os.writeInt(aData);
        }
    }

    @Override
    public void close() throws IOException {
        this.os.close();
    }
}

