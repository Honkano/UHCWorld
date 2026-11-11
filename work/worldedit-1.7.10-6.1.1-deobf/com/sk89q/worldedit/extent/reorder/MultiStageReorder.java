/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extent.reorder;

import com.google.common.collect.Iterators;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.reorder.ReorderingExtent;
import com.sk89q.worldedit.function.operation.BlockMapEntryPlacer;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.OperationQueue;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.util.collection.TupleArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiStageReorder
extends AbstractDelegateExtent
implements ReorderingExtent {
    private TupleArrayList<BlockVector, BaseBlock> stage1 = new TupleArrayList();
    private TupleArrayList<BlockVector, BaseBlock> stage2 = new TupleArrayList();
    private TupleArrayList<BlockVector, BaseBlock> stage3 = new TupleArrayList();
    private boolean enabled;

    public MultiStageReorder(Extent extent, boolean enabled) {
        super(extent);
        this.enabled = enabled;
    }

    public MultiStageReorder(Extent extent) {
        this(extent, true);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
        BaseBlock lazyBlock = this.getLazyBlock(location);
        if (!this.enabled) {
            return super.setBlock(location, block);
        }
        if (BlockType.shouldPlaceLast(block.getType())) {
            this.stage2.put(location.toBlockVector(), block);
            return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
        }
        if (BlockType.shouldPlaceFinal(block.getType())) {
            this.stage3.put(location.toBlockVector(), block);
            return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
        }
        if (BlockType.shouldPlaceLast(lazyBlock.getType())) {
            super.setBlock(location, new BaseBlock(0));
            return super.setBlock(location, block);
        }
        this.stage1.put(location.toBlockVector(), block);
        return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
    }

    @Override
    public Operation commitBefore() {
        return new OperationQueue(new BlockMapEntryPlacer(this.getExtent(), Iterators.concat(this.stage1.iterator(), this.stage2.iterator())), new Stage3Committer());
    }

    private class Stage3Committer
    implements Operation {
        private Stage3Committer() {
        }

        @Override
        public Operation resume(RunContext run) throws WorldEditException {
            Extent extent = MultiStageReorder.this.getExtent();
            HashSet<BlockVector> blocks = new HashSet<BlockVector>();
            HashMap blockTypes = new HashMap();
            for (Map.Entry entry : MultiStageReorder.this.stage3) {
                BlockVector pt = (BlockVector)entry.getKey();
                blocks.add(pt);
                blockTypes.put(pt, entry.getValue());
            }
            while (!blocks.isEmpty()) {
                int data;
                int type;
                PlayerDirection attachment;
                BlockVector current = (BlockVector)blocks.iterator().next();
                if (!blocks.contains(current)) continue;
                LinkedList<BlockVector> walked = new LinkedList<BlockVector>();
                block6: do {
                    walked.addFirst(current);
                    assert (blockTypes.containsKey(current));
                    BaseBlock baseBlock = (BaseBlock)blockTypes.get(current);
                    type = baseBlock.getType();
                    data = baseBlock.getData();
                    switch (type) {
                        case 64: 
                        case 71: {
                            BlockVector upperBlock;
                            if ((data & 8) != 0 || !blocks.contains(upperBlock = current.add(0, 1, 0).toBlockVector()) || walked.contains(upperBlock)) continue block6;
                            walked.addFirst(upperBlock);
                            break;
                        }
                        case 27: 
                        case 28: 
                        case 66: 
                        case 157: {
                            BlockVector lowerBlock = current.add(0, -1, 0).toBlockVector();
                            if (!blocks.contains(lowerBlock) || walked.contains(lowerBlock)) continue block6;
                            walked.addFirst(lowerBlock);
                        }
                    }
                } while ((attachment = BlockType.getAttachment(type, data)) != null && blocks.contains(current = current.add(attachment.vector()).toBlockVector()) && !walked.contains(current));
                for (BlockVector pt : walked) {
                    extent.setBlock(pt, (BaseBlock)blockTypes.get(pt));
                    blocks.remove(pt);
                }
            }
            MultiStageReorder.this.stage1.clear();
            MultiStageReorder.this.stage2.clear();
            MultiStageReorder.this.stage3.clear();
            return null;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void addStatusMessages(List<String> messages) {
        }
    }
}

