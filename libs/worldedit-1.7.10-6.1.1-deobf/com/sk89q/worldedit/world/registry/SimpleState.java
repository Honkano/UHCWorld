/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.SimpleStateValue;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;

class SimpleState
implements State {
    private Byte dataMask;
    private Map<String, SimpleStateValue> values;

    SimpleState() {
    }

    public Map<String, SimpleStateValue> valueMap() {
        return Collections.unmodifiableMap(this.values);
    }

    @Override
    @Nullable
    public StateValue getValue(BaseBlock block) {
        for (StateValue stateValue : this.values.values()) {
            if (!stateValue.isSet(block)) continue;
            return stateValue;
        }
        return null;
    }

    byte getDataMask() {
        return this.dataMask != null ? this.dataMask : (byte)15;
    }

    @Override
    public boolean hasDirection() {
        for (SimpleStateValue value : this.values.values()) {
            if (value.getDirection() == null) continue;
            return true;
        }
        return false;
    }

    void postDeserialization() {
        for (SimpleStateValue v : this.values.values()) {
            v.setState(this);
        }
    }
}

