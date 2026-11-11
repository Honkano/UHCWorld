/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.registry.SimpleState;
import com.sk89q.worldedit.world.registry.StateValue;

class SimpleStateValue
implements StateValue {
    private SimpleState state;
    private Byte data;
    private Vector direction;

    SimpleStateValue() {
    }

    void setState(SimpleState state) {
        this.state = state;
    }

    @Override
    public boolean isSet(BaseBlock block) {
        return this.data != null && (block.getData() & this.state.getDataMask()) == this.data;
    }

    @Override
    public boolean set(BaseBlock block) {
        if (this.data != null) {
            block.setData(block.getData() & ~this.state.getDataMask() | this.data);
            return true;
        }
        return false;
    }

    @Override
    public Vector getDirection() {
        return this.direction;
    }
}

