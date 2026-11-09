/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.ChangeSetExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.MaskingExtent;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.extent.buffer.ForgetfulExtentBuffer;
import com.sk89q.worldedit.extent.cache.LastAccessExtentCache;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.extent.inventory.BlockBagExtent;
import com.sk89q.worldedit.extent.reorder.MultiStageReorder;
import com.sk89q.worldedit.extent.validation.BlockChangeLimiter;
import com.sk89q.worldedit.extent.validation.DataValidatorExtent;
import com.sk89q.worldedit.extent.world.BlockQuirkExtent;
import com.sk89q.worldedit.extent.world.ChunkLoadingExtent;
import com.sk89q.worldedit.extent.world.FastModeExtent;
import com.sk89q.worldedit.extent.world.SurvivalModeExtent;
import com.sk89q.worldedit.function.GroundFunction;
import com.sk89q.worldedit.function.RegionMaskingFilter;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.block.Counter;
import com.sk89q.worldedit.function.block.Naturalizer;
import com.sk89q.worldedit.function.generator.GardenPatchGenerator;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.BoundedHeightMask;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.FuzzyBlockMask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.function.mask.MaskUnion;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.mask.NoiseFilter2D;
import com.sk89q.worldedit.function.mask.RegionMask;
import com.sk89q.worldedit.function.operation.ChangeSetExecutor;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.OperationQueue;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Patterns;
import com.sk89q.worldedit.function.util.RegionOffset;
import com.sk89q.worldedit.function.visitor.DownwardVisitor;
import com.sk89q.worldedit.function.visitor.LayerVisitor;
import com.sk89q.worldedit.function.visitor.NonRisingVisitor;
import com.sk89q.worldedit.function.visitor.RecursiveVisitor;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.history.UndoContext;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.changeset.BlockOptimizedHistory;
import com.sk89q.worldedit.history.changeset.ChangeSet;
import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.math.interpolation.KochanekBartelsInterpolation;
import com.sk89q.worldedit.math.interpolation.Node;
import com.sk89q.worldedit.math.noise.RandomNoise;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.FlatRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.regions.shape.ArbitraryBiomeShape;
import com.sk89q.worldedit.regions.shape.ArbitraryShape;
import com.sk89q.worldedit.regions.shape.RegionShape;
import com.sk89q.worldedit.regions.shape.WorldEditExpressionEnvironment;
import com.sk89q.worldedit.util.Countable;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.collection.DoubleArrayList;
import com.sk89q.worldedit.util.eventbus.EventBus;
import com.sk89q.worldedit.world.NullWorld;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BaseBiome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class EditSession
implements Extent {
    private static final Logger log = Logger.getLogger(EditSession.class.getCanonicalName());
    protected final World world;
    private final ChangeSet changeSet = new BlockOptimizedHistory();
    @Nullable
    private FastModeExtent fastModeExtent;
    private final SurvivalModeExtent survivalExtent;
    @Nullable
    private ChunkLoadingExtent chunkLoadingExtent;
    @Nullable
    private LastAccessExtentCache cacheExtent;
    @Nullable
    private BlockQuirkExtent quirkExtent;
    @Nullable
    private DataValidatorExtent validator;
    private final BlockBagExtent blockBagExtent;
    private final MultiStageReorder reorderExtent;
    @Nullable
    private ChangeSetExtent changeSetExtent;
    private final MaskingExtent maskingExtent;
    private final BlockChangeLimiter changeLimiter;
    private final Extent bypassReorderHistory;
    private final Extent bypassHistory;
    private final Extent bypassNone;
    private com.sk89q.worldedit.function.mask.Mask oldMask;
    private static final Vector[] recurseDirections = new Vector[]{PlayerDirection.NORTH.vector(), PlayerDirection.EAST.vector(), PlayerDirection.SOUTH.vector(), PlayerDirection.WEST.vector(), PlayerDirection.UP.vector(), PlayerDirection.DOWN.vector()};

    @Deprecated
    public EditSession(LocalWorld world, int maxBlocks) {
        this(world, maxBlocks, null);
    }

    @Deprecated
    public EditSession(LocalWorld world, int maxBlocks, @Nullable BlockBag blockBag) {
        this(WorldEdit.getInstance().getEventBus(), world, maxBlocks, blockBag, new EditSessionEvent(world, null, maxBlocks, null));
    }

    EditSession(EventBus eventBus, World world, int maxBlocks, @Nullable BlockBag blockBag, EditSessionEvent event) {
        Preconditions.checkNotNull((Object)eventBus);
        Preconditions.checkArgument((maxBlocks >= -1 ? 1 : 0) != 0, (Object)"maxBlocks >= -1 required");
        Preconditions.checkNotNull((Object)event);
        this.world = world;
        if (world != null) {
            Extent extent = this.fastModeExtent = new FastModeExtent(world, false);
            this.survivalExtent = new SurvivalModeExtent(extent, world);
            extent = this.survivalExtent;
            this.quirkExtent = new BlockQuirkExtent(extent, world);
            extent = this.quirkExtent;
            this.chunkLoadingExtent = new ChunkLoadingExtent(extent, world);
            extent = this.chunkLoadingExtent;
            this.cacheExtent = new LastAccessExtentCache(extent);
            extent = this.cacheExtent;
            extent = this.wrapExtent(extent, eventBus, event, Stage.BEFORE_CHANGE);
            this.validator = new DataValidatorExtent(extent, world);
            extent = this.validator;
            this.blockBagExtent = new BlockBagExtent(extent, blockBag);
            extent = this.blockBagExtent;
            this.reorderExtent = new MultiStageReorder(extent, false);
            extent = this.reorderExtent;
            extent = this.wrapExtent(extent, eventBus, event, Stage.BEFORE_REORDER);
            this.changeSetExtent = new ChangeSetExtent(extent, this.changeSet);
            extent = this.changeSetExtent;
            this.maskingExtent = new MaskingExtent(extent, Masks.alwaysTrue());
            extent = this.maskingExtent;
            this.changeLimiter = new BlockChangeLimiter(extent, maxBlocks);
            extent = this.changeLimiter;
            extent = this.wrapExtent(extent, eventBus, event, Stage.BEFORE_HISTORY);
            this.bypassReorderHistory = this.blockBagExtent;
            this.bypassHistory = this.reorderExtent;
            this.bypassNone = extent;
        } else {
            Extent extent = new NullExtent();
            this.survivalExtent = new SurvivalModeExtent(extent, NullWorld.getInstance());
            extent = this.survivalExtent;
            this.blockBagExtent = new BlockBagExtent(extent, blockBag);
            extent = this.blockBagExtent;
            this.reorderExtent = new MultiStageReorder(extent, false);
            extent = this.reorderExtent;
            this.maskingExtent = new MaskingExtent(extent, Masks.alwaysTrue());
            extent = this.maskingExtent;
            this.changeLimiter = new BlockChangeLimiter(extent, maxBlocks);
            extent = this.changeLimiter;
            this.bypassReorderHistory = extent;
            this.bypassHistory = extent;
            this.bypassNone = extent;
        }
    }

    private Extent wrapExtent(Extent extent, EventBus eventBus, EditSessionEvent event, Stage stage) {
        event = event.clone(stage);
        event.setExtent(extent);
        eventBus.post(event);
        return event.getExtent();
    }

    public World getWorld() {
        return this.world;
    }

    public ChangeSet getChangeSet() {
        return this.changeSet;
    }

    public int getBlockChangeLimit() {
        return this.changeLimiter.getLimit();
    }

    public void setBlockChangeLimit(int limit) {
        this.changeLimiter.setLimit(limit);
    }

    public boolean isQueueEnabled() {
        return this.reorderExtent.isEnabled();
    }

    public void enableQueue() {
        this.reorderExtent.setEnabled(true);
    }

    public void disableQueue() {
        if (this.isQueueEnabled()) {
            this.flushQueue();
        }
        this.reorderExtent.setEnabled(true);
    }

    public com.sk89q.worldedit.function.mask.Mask getMask() {
        return this.oldMask;
    }

    public void setMask(com.sk89q.worldedit.function.mask.Mask mask) {
        this.oldMask = mask;
        if (mask == null) {
            this.maskingExtent.setMask(Masks.alwaysTrue());
        } else {
            this.maskingExtent.setMask(mask);
        }
    }

    @Deprecated
    public void setMask(Mask mask) {
        if (mask == null) {
            this.setMask((com.sk89q.worldedit.function.mask.Mask)null);
        } else {
            this.setMask(Masks.wrap(mask));
        }
    }

    public SurvivalModeExtent getSurvivalExtent() {
        return this.survivalExtent;
    }

    public void setFastMode(boolean enabled) {
        if (this.fastModeExtent != null) {
            this.fastModeExtent.setEnabled(enabled);
        }
    }

    public boolean hasFastMode() {
        return this.fastModeExtent != null && this.fastModeExtent.isEnabled();
    }

    public BlockBag getBlockBag() {
        return this.blockBagExtent.getBlockBag();
    }

    public void setBlockBag(BlockBag blockBag) {
        this.blockBagExtent.setBlockBag(blockBag);
    }

    public Map<Integer, Integer> popMissingBlocks() {
        return this.blockBagExtent.popMissing();
    }

    public int getBlockChangeCount() {
        return this.changeSet.size();
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        return this.bypassNone.getBiome(position);
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        return this.bypassNone.setBiome(position, biome);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        return this.world.getLazyBlock(position);
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        return this.world.getBlock(position);
    }

    @Deprecated
    public int getBlockType(Vector position) {
        return this.world.getBlockType(position);
    }

    @Deprecated
    public int getBlockData(Vector position) {
        return this.world.getBlockData(position);
    }

    @Deprecated
    public BaseBlock rawGetBlock(Vector position) {
        return this.getBlock(position);
    }

    public int getHighestTerrainBlock(int x, int z, int minY, int maxY) {
        return this.getHighestTerrainBlock(x, z, minY, maxY, false);
    }

    public int getHighestTerrainBlock(int x, int z, int minY, int maxY, boolean naturalOnly) {
        for (int y = maxY; y >= minY; --y) {
            Vector pt = new Vector(x, y, z);
            int id = this.getBlockType(pt);
            int data = this.getBlockData(pt);
            if (!(naturalOnly ? BlockType.isNaturalTerrainBlock(id, data) : !BlockType.canPassThrough(id, data))) continue;
            return y;
        }
        return minY;
    }

    public boolean setBlock(Vector position, BaseBlock block, Stage stage) throws WorldEditException {
        switch (stage) {
            case BEFORE_HISTORY: {
                return this.bypassNone.setBlock(position, block);
            }
            case BEFORE_CHANGE: {
                return this.bypassHistory.setBlock(position, block);
            }
            case BEFORE_REORDER: {
                return this.bypassReorderHistory.setBlock(position, block);
            }
        }
        throw new RuntimeException("New enum entry added that is unhandled here");
    }

    public boolean rawSetBlock(Vector position, BaseBlock block) {
        try {
            return this.setBlock(position, block, Stage.BEFORE_CHANGE);
        }
        catch (WorldEditException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    public boolean smartSetBlock(Vector position, BaseBlock block) {
        try {
            return this.setBlock(position, block, Stage.BEFORE_REORDER);
        }
        catch (WorldEditException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block) throws MaxChangedBlocksException {
        try {
            return this.setBlock(position, block, Stage.BEFORE_HISTORY);
        }
        catch (MaxChangedBlocksException e) {
            throw e;
        }
        catch (WorldEditException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    public boolean setBlock(Vector position, Pattern pattern) throws MaxChangedBlocksException {
        return this.setBlock(position, pattern.next(position));
    }

    private int setBlocks(Set<Vector> vset, Pattern pattern) throws MaxChangedBlocksException {
        int affected = 0;
        for (Vector v : vset) {
            affected += this.setBlock(v, pattern) ? 1 : 0;
        }
        return affected;
    }

    public boolean setChanceBlockIfAir(Vector position, BaseBlock block, double probability) throws MaxChangedBlocksException {
        return Math.random() <= probability && this.setBlockIfAir(position, block);
    }

    @Deprecated
    public boolean setBlockIfAir(Vector position, BaseBlock block) throws MaxChangedBlocksException {
        return this.getBlock(position).isAir() && this.setBlock(position, block);
    }

    @Override
    @Nullable
    public Entity createEntity(Location location, BaseEntity entity) {
        return this.bypassNone.createEntity(location, entity);
    }

    @Deprecated
    public void rememberChange(Vector position, BaseBlock existing, BaseBlock block) {
        this.changeSet.add(new BlockChange(position.toBlockVector(), existing, block));
    }

    public void undo(EditSession editSession) {
        UndoContext context = new UndoContext();
        context.setExtent(editSession.bypassHistory);
        Operations.completeBlindly(ChangeSetExecutor.createUndo(this.changeSet, context));
        editSession.flushQueue();
    }

    public void redo(EditSession editSession) {
        UndoContext context = new UndoContext();
        context.setExtent(editSession.bypassHistory);
        Operations.completeBlindly(ChangeSetExecutor.createRedo(this.changeSet, context));
        editSession.flushQueue();
    }

    public int size() {
        return this.getBlockChangeCount();
    }

    @Override
    public Vector getMinimumPoint() {
        return this.getWorld().getMinimumPoint();
    }

    @Override
    public Vector getMaximumPoint() {
        return this.getWorld().getMaximumPoint();
    }

    @Override
    public List<? extends Entity> getEntities(Region region) {
        return this.bypassNone.getEntities(region);
    }

    @Override
    public List<? extends Entity> getEntities() {
        return this.bypassNone.getEntities();
    }

    public void flushQueue() {
        Operations.completeBlindly(this.commit());
    }

    @Override
    @Nullable
    public Operation commit() {
        return this.bypassNone.commit();
    }

    public int countBlock(Region region, Set<Integer> searchIDs) {
        HashSet<BaseBlock> passOn = new HashSet<BaseBlock>();
        for (Integer i : searchIDs) {
            passOn.add(new BaseBlock(i, -1));
        }
        return this.countBlocks(region, passOn);
    }

    public int countBlocks(Region region, Set<BaseBlock> searchBlocks) {
        FuzzyBlockMask mask = new FuzzyBlockMask((Extent)this, searchBlocks);
        Counter count = new Counter();
        RegionMaskingFilter filter = new RegionMaskingFilter(mask, count);
        RegionVisitor visitor = new RegionVisitor(region, filter);
        Operations.completeBlindly(visitor);
        return count.getCount();
    }

    public int fillXZ(Vector origin, BaseBlock block, double radius, int depth, boolean recursive) throws MaxChangedBlocksException {
        return this.fillXZ(origin, new SingleBlockPattern(block), radius, depth, recursive);
    }

    public int fillXZ(Vector origin, Pattern pattern, double radius, int depth, boolean recursive) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)origin);
        Preconditions.checkNotNull((Object)pattern);
        Preconditions.checkArgument((radius >= 0.0 ? 1 : 0) != 0, (Object)"radius >= 0");
        Preconditions.checkArgument((depth >= 1 ? 1 : 0) != 0, (Object)"depth >= 1");
        MaskIntersection mask = new MaskIntersection(new RegionMask(new EllipsoidRegion(null, origin, new Vector(radius, radius, radius))), new BoundedHeightMask(Math.max(origin.getBlockY() - depth + 1, 0), Math.min(this.getWorld().getMaxY(), origin.getBlockY())), Masks.negate(new ExistingBlockMask(this)));
        BlockReplace replace = new BlockReplace(this, Patterns.wrap(pattern));
        RecursiveVisitor visitor = recursive ? new RecursiveVisitor(mask, replace) : new DownwardVisitor(mask, replace, origin.getBlockY());
        visitor.visit(origin);
        Operations.completeLegacy(visitor);
        return visitor.getAffected();
    }

    public int removeAbove(Vector position, int apothem, int height) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkArgument((apothem >= 1 ? 1 : 0) != 0, (Object)"apothem >= 1");
        Preconditions.checkArgument((height >= 1 ? 1 : 0) != 0, (Object)"height >= 1");
        CuboidRegion region = new CuboidRegion(this.getWorld(), position.add(-apothem + 1, 0, -apothem + 1), position.add(apothem - 1, height - 1, apothem - 1));
        SingleBlockPattern pattern = new SingleBlockPattern(new BaseBlock(0));
        return this.setBlocks((Region)region, (Pattern)pattern);
    }

    public int removeBelow(Vector position, int apothem, int height) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkArgument((apothem >= 1 ? 1 : 0) != 0, (Object)"apothem >= 1");
        Preconditions.checkArgument((height >= 1 ? 1 : 0) != 0, (Object)"height >= 1");
        CuboidRegion region = new CuboidRegion(this.getWorld(), position.add(-apothem + 1, 0, -apothem + 1), position.add(apothem - 1, -height + 1, apothem - 1));
        SingleBlockPattern pattern = new SingleBlockPattern(new BaseBlock(0));
        return this.setBlocks((Region)region, (Pattern)pattern);
    }

    public int removeNear(Vector position, int blockType, int apothem) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkArgument((apothem >= 1 ? 1 : 0) != 0, (Object)"apothem >= 1");
        FuzzyBlockMask mask = new FuzzyBlockMask((Extent)this, new BaseBlock(blockType, -1));
        Vector adjustment = new Vector(1, 1, 1).multiply(apothem - 1);
        CuboidRegion region = new CuboidRegion(this.getWorld(), position.add(adjustment.multiply(-1)), position.add(adjustment));
        SingleBlockPattern pattern = new SingleBlockPattern(new BaseBlock(0));
        return this.replaceBlocks((Region)region, mask, (Pattern)pattern);
    }

    public int setBlocks(Region region, BaseBlock block) throws MaxChangedBlocksException {
        return this.setBlocks(region, (Pattern)new SingleBlockPattern(block));
    }

    public int setBlocks(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        BlockReplace replace = new BlockReplace(this, Patterns.wrap(pattern));
        RegionVisitor visitor = new RegionVisitor(region, replace);
        Operations.completeLegacy(visitor);
        return visitor.getAffected();
    }

    public int replaceBlocks(Region region, Set<BaseBlock> filter, BaseBlock replacement) throws MaxChangedBlocksException {
        return this.replaceBlocks(region, filter, (Pattern)new SingleBlockPattern(replacement));
    }

    public int replaceBlocks(Region region, Set<BaseBlock> filter, Pattern pattern) throws MaxChangedBlocksException {
        AbstractExtentMask mask = filter == null ? new ExistingBlockMask(this) : new FuzzyBlockMask((Extent)this, filter);
        return this.replaceBlocks(region, mask, pattern);
    }

    public int replaceBlocks(Region region, com.sk89q.worldedit.function.mask.Mask mask, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)mask);
        Preconditions.checkNotNull((Object)pattern);
        BlockReplace replace = new BlockReplace(this, Patterns.wrap(pattern));
        RegionMaskingFilter filter = new RegionMaskingFilter(mask, replace);
        RegionVisitor visitor = new RegionVisitor(region, filter);
        Operations.completeLegacy(visitor);
        return visitor.getAffected();
    }

    public int center(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        Vector center = region.getCenter();
        CuboidRegion centerRegion = new CuboidRegion(this.getWorld(), new Vector((int)center.getX(), (int)center.getY(), (int)center.getZ()), (Vector)center.toBlockVector());
        return this.setBlocks((Region)centerRegion, pattern);
    }

    public int makeCuboidFaces(Region region, BaseBlock block) throws MaxChangedBlocksException {
        return this.makeCuboidFaces(region, new SingleBlockPattern(block));
    }

    public int makeCuboidFaces(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        CuboidRegion cuboid = CuboidRegion.makeCuboid(region);
        Region faces = cuboid.getFaces();
        return this.setBlocks(faces, pattern);
    }

    public int makeFaces(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        if (region instanceof CuboidRegion) {
            return this.makeCuboidFaces(region, pattern);
        }
        return new RegionShape(region).generate(this, pattern, true);
    }

    public int makeCuboidWalls(Region region, BaseBlock block) throws MaxChangedBlocksException {
        return this.makeCuboidWalls(region, new SingleBlockPattern(block));
    }

    public int makeCuboidWalls(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        CuboidRegion cuboid = CuboidRegion.makeCuboid(region);
        Region faces = cuboid.getWalls();
        return this.setBlocks(faces, pattern);
    }

    public int makeWalls(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        if (region instanceof CuboidRegion) {
            return this.makeCuboidWalls(region, pattern);
        }
        final int minY = region.getMinimumPoint().getBlockY();
        final int maxY = region.getMaximumPoint().getBlockY();
        RegionShape shape = new RegionShape(region){

            @Override
            protected BaseBlock getMaterial(int x, int y, int z, BaseBlock defaultMaterial) {
                if (y > maxY || y < minY) {
                    return defaultMaterial;
                }
                return super.getMaterial(x, y, z, defaultMaterial);
            }
        };
        return shape.generate(this, pattern, true);
    }

    public int overlayCuboidBlocks(Region region, BaseBlock block) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)block);
        return this.overlayCuboidBlocks(region, new SingleBlockPattern(block));
    }

    public int overlayCuboidBlocks(Region region, Pattern pattern) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)pattern);
        BlockReplace replace = new BlockReplace(this, Patterns.wrap(pattern));
        RegionOffset offset = new RegionOffset(new Vector(0, 1, 0), replace);
        GroundFunction ground = new GroundFunction(new ExistingBlockMask(this), offset);
        LayerVisitor visitor = new LayerVisitor(Regions.asFlatRegion(region), Regions.minimumBlockY(region), Regions.maximumBlockY(region), ground);
        Operations.completeLegacy(visitor);
        return ground.getAffected();
    }

    public int naturalizeCuboidBlocks(Region region) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Naturalizer naturalizer = new Naturalizer(this);
        FlatRegion flatRegion = Regions.asFlatRegion(region);
        LayerVisitor visitor = new LayerVisitor(flatRegion, Regions.minimumBlockY(region), Regions.maximumBlockY(region), naturalizer);
        Operations.completeLegacy(visitor);
        return naturalizer.getAffected();
    }

    public int stackCuboidRegion(Region region, Vector dir, int count, boolean copyAir) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)dir);
        Preconditions.checkArgument((count >= 1 ? 1 : 0) != 0, (Object)"count >= 1 required");
        Vector size = region.getMaximumPoint().subtract(region.getMinimumPoint()).add(1, 1, 1);
        Vector to = region.getMinimumPoint();
        ForwardExtentCopy copy = new ForwardExtentCopy(this, region, this, to);
        copy.setRepetitions(count);
        copy.setTransform(new AffineTransform().translate(dir.multiply(size)));
        if (!copyAir) {
            copy.setSourceMask(new ExistingBlockMask(this));
        }
        Operations.completeLegacy(copy);
        return copy.getAffected();
    }

    public int moveRegion(Region region, Vector dir, int distance, boolean copyAir, BaseBlock replacement) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)region);
        Preconditions.checkNotNull((Object)dir);
        Preconditions.checkArgument((distance >= 1 ? 1 : 0) != 0, (Object)"distance >= 1 required");
        Vector to = region.getMinimumPoint();
        BlockPattern pattern = replacement != null ? new BlockPattern(replacement) : new BlockPattern(new BaseBlock(0));
        BlockReplace remove = new BlockReplace(this, pattern);
        ForgetfulExtentBuffer buffer = new ForgetfulExtentBuffer(this, new RegionMask(region));
        ForwardExtentCopy copy = new ForwardExtentCopy(this, region, buffer, to);
        copy.setTransform(new AffineTransform().translate(dir.multiply(distance)));
        copy.setSourceFunction(remove);
        copy.setRemovingEntities(true);
        if (!copyAir) {
            copy.setSourceMask(new ExistingBlockMask(this));
        }
        BlockReplace replace = new BlockReplace(this, buffer);
        RegionVisitor visitor = new RegionVisitor(buffer.asRegion(), replace);
        OperationQueue operation = new OperationQueue(copy, visitor);
        Operations.completeLegacy(operation);
        return copy.getAffected();
    }

    public int moveCuboidRegion(Region region, Vector dir, int distance, boolean copyAir, BaseBlock replacement) throws MaxChangedBlocksException {
        return this.moveRegion(region, dir, distance, copyAir, replacement);
    }

    public int drainArea(Vector origin, double radius) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)origin);
        Preconditions.checkArgument((radius >= 0.0 ? 1 : 0) != 0, (Object)"radius >= 0 required");
        MaskIntersection mask = new MaskIntersection(new BoundedHeightMask(0, this.getWorld().getMaxY()), new RegionMask(new EllipsoidRegion(null, origin, new Vector(radius, radius, radius))), this.getWorld().createLiquidMask());
        BlockReplace replace = new BlockReplace(this, new BlockPattern(new BaseBlock(0)));
        RecursiveVisitor visitor = new RecursiveVisitor(mask, replace);
        for (BlockVector position : CuboidRegion.fromCenter(origin, 1)) {
            if (!mask.test(position)) continue;
            visitor.visit(position);
        }
        Operations.completeLegacy(visitor);
        return visitor.getAffected();
    }

    public int fixLiquid(Vector origin, double radius, int moving, int stationary) throws MaxChangedBlocksException {
        Preconditions.checkNotNull((Object)origin);
        Preconditions.checkArgument((radius >= 0.0 ? 1 : 0) != 0, (Object)"radius >= 0 required");
        BlockMask liquidMask = new BlockMask((Extent)this, new BaseBlock(moving, -1), new BaseBlock(stationary, -1));
        MaskUnion blockMask = new MaskUnion(liquidMask, new BlockMask((Extent)this, new BaseBlock(0)));
        MaskIntersection mask = new MaskIntersection(new BoundedHeightMask(0, Math.min(origin.getBlockY(), this.getWorld().getMaxY())), new RegionMask(new EllipsoidRegion(null, origin, new Vector(radius, radius, radius))), blockMask);
        BlockReplace replace = new BlockReplace(this, new BlockPattern(new BaseBlock(stationary)));
        NonRisingVisitor visitor = new NonRisingVisitor(mask, replace);
        for (BlockVector position : CuboidRegion.fromCenter(origin, 1)) {
            if (!liquidMask.test(position)) continue;
            visitor.visit(position);
        }
        Operations.completeLegacy(visitor);
        return visitor.getAffected();
    }

    public int makeCylinder(Vector pos, Pattern block, double radius, int height, boolean filled) throws MaxChangedBlocksException {
        return this.makeCylinder(pos, block, radius, radius, height, filled);
    }

    public int makeCylinder(Vector pos, Pattern block, double radiusX, double radiusZ, int height, boolean filled) throws MaxChangedBlocksException {
        int affected = 0;
        radiusX += 0.5;
        radiusZ += 0.5;
        if (height == 0) {
            return 0;
        }
        if (height < 0) {
            height = -height;
            pos = pos.subtract(0, height, 0);
        }
        if (pos.getBlockY() < 0) {
            pos = pos.setY(0);
        } else if (pos.getBlockY() + height - 1 > this.world.getMaxY()) {
            height = this.world.getMaxY() - pos.getBlockY() + 1;
        }
        double invRadiusX = 1.0 / radiusX;
        double invRadiusZ = 1.0 / radiusZ;
        int ceilRadiusX = (int)Math.ceil(radiusX);
        int ceilRadiusZ = (int)Math.ceil(radiusZ);
        double nextXn = 0.0;
        block0: for (int x = 0; x <= ceilRadiusX; ++x) {
            double xn = nextXn;
            nextXn = (double)(x + 1) * invRadiusX;
            double nextZn = 0.0;
            for (int z = 0; z <= ceilRadiusZ; ++z) {
                double zn = nextZn;
                nextZn = (double)(z + 1) * invRadiusZ;
                double distanceSq = EditSession.lengthSq(xn, zn);
                if (distanceSq > 1.0) {
                    if (z != 0) continue block0;
                    break block0;
                }
                if (!filled && EditSession.lengthSq(nextXn, zn) <= 1.0 && EditSession.lengthSq(xn, nextZn) <= 1.0) continue;
                for (int y = 0; y < height; ++y) {
                    if (this.setBlock(pos.add(x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(-x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(x, y, -z), block)) {
                        ++affected;
                    }
                    if (!this.setBlock(pos.add(-x, y, -z), block)) continue;
                    ++affected;
                }
            }
        }
        return affected;
    }

    public int makeSphere(Vector pos, Pattern block, double radius, boolean filled) throws MaxChangedBlocksException {
        return this.makeSphere(pos, block, radius, radius, radius, filled);
    }

    public int makeSphere(Vector pos, Pattern block, double radiusX, double radiusY, double radiusZ, boolean filled) throws MaxChangedBlocksException {
        int affected = 0;
        double invRadiusX = 1.0 / (radiusX += 0.5);
        double invRadiusY = 1.0 / (radiusY += 0.5);
        double invRadiusZ = 1.0 / (radiusZ += 0.5);
        int ceilRadiusX = (int)Math.ceil(radiusX);
        int ceilRadiusY = (int)Math.ceil(radiusY);
        int ceilRadiusZ = (int)Math.ceil(radiusZ);
        double nextXn = 0.0;
        block0: for (int x = 0; x <= ceilRadiusX; ++x) {
            double xn = nextXn;
            nextXn = (double)(x + 1) * invRadiusX;
            double nextYn = 0.0;
            block1: for (int y = 0; y <= ceilRadiusY; ++y) {
                double yn = nextYn;
                nextYn = (double)(y + 1) * invRadiusY;
                double nextZn = 0.0;
                for (int z = 0; z <= ceilRadiusZ; ++z) {
                    double zn = nextZn;
                    nextZn = (double)(z + 1) * invRadiusZ;
                    double distanceSq = EditSession.lengthSq(xn, yn, zn);
                    if (distanceSq > 1.0) {
                        if (z != 0) continue block1;
                        if (y != 0) continue block0;
                        break block0;
                    }
                    if (!filled && EditSession.lengthSq(nextXn, yn, zn) <= 1.0 && EditSession.lengthSq(xn, nextYn, zn) <= 1.0 && EditSession.lengthSq(xn, yn, nextZn) <= 1.0) continue;
                    if (this.setBlock(pos.add(x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(-x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(x, -y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(x, y, -z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(-x, -y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(x, -y, -z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(pos.add(-x, y, -z), block)) {
                        ++affected;
                    }
                    if (!this.setBlock(pos.add(-x, -y, -z), block)) continue;
                    ++affected;
                }
            }
        }
        return affected;
    }

    public int makePyramid(Vector position, Pattern block, int size, boolean filled) throws MaxChangedBlocksException {
        int affected = 0;
        int height = size;
        for (int y = 0; y <= height; ++y) {
            --size;
            for (int x = 0; x <= size; ++x) {
                for (int z = 0; z <= size; ++z) {
                    if ((!filled || z > size || x > size) && z != size && x != size) continue;
                    if (this.setBlock(position.add(x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(position.add(-x, y, z), block)) {
                        ++affected;
                    }
                    if (this.setBlock(position.add(x, y, -z), block)) {
                        ++affected;
                    }
                    if (!this.setBlock(position.add(-x, y, -z), block)) continue;
                    ++affected;
                }
            }
        }
        return affected;
    }

    public int thaw(Vector position, double radius) throws MaxChangedBlocksException {
        int affected = 0;
        double radiusSq = radius * radius;
        int ox = position.getBlockX();
        int oy = position.getBlockY();
        int oz = position.getBlockZ();
        BaseBlock air = new BaseBlock(0);
        BaseBlock water = new BaseBlock(9);
        int ceilRadius = (int)Math.ceil(radius);
        for (int x = ox - ceilRadius; x <= ox + ceilRadius; ++x) {
            block6: for (int z = oz - ceilRadius; z <= oz + ceilRadius; ++z) {
                if (new Vector(x, oy, z).distanceSq(position) > radiusSq) continue;
                block7: for (int y = this.world.getMaxY(); y >= 1; --y) {
                    Vector pt = new Vector(x, y, z);
                    int id = this.getBlockType(pt);
                    switch (id) {
                        case 79: {
                            if (!this.setBlock(pt, water)) continue block6;
                            ++affected;
                            continue block6;
                        }
                        case 78: {
                            if (!this.setBlock(pt, air)) continue block6;
                            ++affected;
                            continue block6;
                        }
                        case 0: {
                            continue block7;
                        }
                        default: {
                            continue block6;
                        }
                    }
                }
            }
        }
        return affected;
    }

    public int simulateSnow(Vector position, double radius) throws MaxChangedBlocksException {
        int affected = 0;
        double radiusSq = radius * radius;
        int ox = position.getBlockX();
        int oy = position.getBlockY();
        int oz = position.getBlockZ();
        BaseBlock ice = new BaseBlock(79);
        BaseBlock snow = new BaseBlock(78);
        int ceilRadius = (int)Math.ceil(radius);
        for (int x = ox - ceilRadius; x <= ox + ceilRadius; ++x) {
            block1: for (int z = oz - ceilRadius; z <= oz + ceilRadius; ++z) {
                if (new Vector(x, oy, z).distanceSq(position) > radiusSq) continue;
                for (int y = this.world.getMaxY(); y >= 1; --y) {
                    Vector pt = new Vector(x, y, z);
                    int id = this.getBlockType(pt);
                    if (id == 0) continue;
                    if (id == 8 || id == 9) {
                        if (!this.setBlock(pt, ice)) continue block1;
                        ++affected;
                        continue block1;
                    }
                    if (BlockType.isTranslucent(id) || y == this.world.getMaxY() || !this.setBlock(pt.add(0, 1, 0), snow)) continue block1;
                    ++affected;
                    continue block1;
                }
            }
        }
        return affected;
    }

    @Deprecated
    public int green(Vector position, double radius) throws MaxChangedBlocksException {
        return this.green(position, radius, true);
    }

    public int green(Vector position, double radius, boolean onlyNormalDirt) throws MaxChangedBlocksException {
        int affected = 0;
        double radiusSq = radius * radius;
        int ox = position.getBlockX();
        int oy = position.getBlockY();
        int oz = position.getBlockZ();
        BaseBlock grass = new BaseBlock(2);
        int ceilRadius = (int)Math.ceil(radius);
        for (int x = ox - ceilRadius; x <= ox + ceilRadius; ++x) {
            block5: for (int z = oz - ceilRadius; z <= oz + ceilRadius; ++z) {
                if (new Vector(x, oy, z).distanceSq(position) > radiusSq) continue;
                block6: for (int y = this.world.getMaxY(); y >= 1; --y) {
                    Vector pt = new Vector(x, y, z);
                    int id = this.getBlockType(pt);
                    int data = this.getBlockData(pt);
                    switch (id) {
                        case 3: {
                            if (onlyNormalDirt && data != 0 || !this.setBlock(pt, grass)) continue block5;
                            ++affected;
                            continue block5;
                        }
                        case 8: 
                        case 9: 
                        case 10: 
                        case 11: {
                            continue block5;
                        }
                        default: {
                            if (!BlockType.canPassThrough(id, data)) continue block5;
                            continue block6;
                        }
                    }
                }
            }
        }
        return affected;
    }

    public int makePumpkinPatches(Vector position, int apothem) throws MaxChangedBlocksException {
        GardenPatchGenerator generator = new GardenPatchGenerator(this);
        generator.setPlant(GardenPatchGenerator.getPumpkinPattern());
        CuboidRegion region = new CuboidRegion(this.getWorld(), position.add(-apothem, -5, -apothem), position.add(apothem, 10, apothem));
        double density = 0.02;
        GroundFunction ground = new GroundFunction(new ExistingBlockMask(this), generator);
        LayerVisitor visitor = new LayerVisitor(region, Regions.minimumBlockY(region), Regions.maximumBlockY(region), ground);
        visitor.setMask(new NoiseFilter2D(new RandomNoise(), density));
        Operations.completeLegacy(visitor);
        return ground.getAffected();
    }

    public int makeForest(Vector basePosition, int size, double density, TreeGenerator treeGenerator) throws MaxChangedBlocksException {
        int affected = 0;
        for (int x = basePosition.getBlockX() - size; x <= basePosition.getBlockX() + size; ++x) {
            block1: for (int z = basePosition.getBlockZ() - size; z <= basePosition.getBlockZ() + size; ++z) {
                if (!this.getBlock(new Vector(x, basePosition.getBlockY(), z)).isAir() || Math.random() >= density) continue;
                for (int y = basePosition.getBlockY(); y >= basePosition.getBlockY() - 10; --y) {
                    int t = this.getBlock(new Vector(x, y, z)).getType();
                    if (t == 2 || t == 3) {
                        treeGenerator.generate(this, new Vector(x, y + 1, z));
                        ++affected;
                        continue block1;
                    }
                    if (t == 78) {
                        this.setBlock(new Vector(x, y, z), new BaseBlock(0));
                        continue;
                    }
                    if (t != 0) continue block1;
                }
            }
        }
        return affected;
    }

    public List<Countable<Integer>> getBlockDistribution(Region region) {
        ArrayList<Countable<Integer>> distribution = new ArrayList<Countable<Integer>>();
        HashMap<Integer, Countable<Integer>> map = new HashMap<Integer, Countable<Integer>>();
        if (region instanceof CuboidRegion) {
            Vector min = region.getMinimumPoint();
            Vector max = region.getMaximumPoint();
            int minX = min.getBlockX();
            int minY = min.getBlockY();
            int minZ = min.getBlockZ();
            int maxX = max.getBlockX();
            int maxY = max.getBlockY();
            int maxZ = max.getBlockZ();
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        Vector pt = new Vector(x, y, z);
                        int id = this.getBlockType(pt);
                        if (map.containsKey(id)) {
                            ((Countable)map.get(id)).increment();
                            continue;
                        }
                        Countable<Integer> c = new Countable<Integer>(id, 1);
                        map.put(id, c);
                        distribution.add(c);
                    }
                }
            }
        } else {
            for (Vector pt : region) {
                int id = this.getBlockType(pt);
                if (map.containsKey(id)) {
                    ((Countable)map.get(id)).increment();
                    continue;
                }
                Countable<Integer> c = new Countable<Integer>(id, 1);
                map.put(id, c);
            }
        }
        Collections.sort(distribution);
        return distribution;
    }

    public List<Countable<BaseBlock>> getBlockDistributionWithData(Region region) {
        ArrayList<Countable<BaseBlock>> distribution = new ArrayList<Countable<BaseBlock>>();
        HashMap<BaseBlock, Countable<BaseBlock>> map = new HashMap<BaseBlock, Countable<BaseBlock>>();
        if (region instanceof CuboidRegion) {
            Vector min = region.getMinimumPoint();
            Vector max = region.getMaximumPoint();
            int minX = min.getBlockX();
            int minY = min.getBlockY();
            int minZ = min.getBlockZ();
            int maxX = max.getBlockX();
            int maxY = max.getBlockY();
            int maxZ = max.getBlockZ();
            for (int x = minX; x <= maxX; ++x) {
                for (int y = minY; y <= maxY; ++y) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        Vector pt = new Vector(x, y, z);
                        BaseBlock blk = new BaseBlock(this.getBlockType(pt), this.getBlockData(pt));
                        if (map.containsKey(blk)) {
                            ((Countable)map.get(blk)).increment();
                            continue;
                        }
                        Countable<BaseBlock> c = new Countable<BaseBlock>(blk, 1);
                        map.put(blk, c);
                        distribution.add(c);
                    }
                }
            }
        } else {
            for (Vector pt : region) {
                BaseBlock blk = new BaseBlock(this.getBlockType(pt), this.getBlockData(pt));
                if (map.containsKey(blk)) {
                    ((Countable)map.get(blk)).increment();
                    continue;
                }
                Countable<BaseBlock> c = new Countable<BaseBlock>(blk, 1);
                map.put(blk, c);
            }
        }
        Collections.sort(distribution);
        return distribution;
    }

    public int makeShape(Region region, final Vector zero, final Vector unit, Pattern pattern, String expressionString, boolean hollow) throws ExpressionException, MaxChangedBlocksException {
        final Expression expression = Expression.compile(expressionString, "x", "y", "z", "type", "data");
        expression.optimize();
        final RValue typeVariable = expression.getVariable("type", false);
        final RValue dataVariable = expression.getVariable("data", false);
        final WorldEditExpressionEnvironment environment = new WorldEditExpressionEnvironment(this, unit, zero);
        expression.setEnvironment(environment);
        ArbitraryShape shape = new ArbitraryShape(region){

            @Override
            protected BaseBlock getMaterial(int x, int y, int z, BaseBlock defaultMaterial) {
                Vector current = new Vector(x, y, z);
                environment.setCurrentBlock(current);
                Vector scaled = current.subtract(zero).divide(unit);
                try {
                    double[] dArray = new double[]{scaled.getX(), scaled.getY(), scaled.getZ(), defaultMaterial.getType(), defaultMaterial.getData()};
                    if (expression.evaluate(dArray) <= 0.0) {
                        return null;
                    }
                    return new BaseBlock((int)typeVariable.getValue(), (int)dataVariable.getValue());
                }
                catch (Exception e) {
                    log.log(Level.WARNING, "Failed to create shape", e);
                    return null;
                }
            }
        };
        return shape.generate(this, pattern, hollow);
    }

    public int deformRegion(Region region, Vector zero, Vector unit, String expressionString) throws ExpressionException, MaxChangedBlocksException {
        BaseBlock material;
        Expression expression = Expression.compile(expressionString, "x", "y", "z");
        expression.optimize();
        RValue x = expression.getVariable("x", false);
        RValue y = expression.getVariable("y", false);
        RValue z = expression.getVariable("z", false);
        WorldEditExpressionEnvironment environment = new WorldEditExpressionEnvironment(this, unit, zero);
        expression.setEnvironment(environment);
        DoubleArrayList<BlockVector, BaseBlock> queue = new DoubleArrayList<BlockVector, BaseBlock>(false);
        for (BlockVector position : region) {
            Vector scaled = position.subtract(zero).divide(unit);
            expression.evaluate(scaled.getX(), scaled.getY(), scaled.getZ());
            BlockVector sourcePosition = environment.toWorld(x.getValue(), y.getValue(), z.getValue());
            material = new BaseBlock(this.world.getBlockType(sourcePosition), this.world.getBlockData(sourcePosition));
            queue.put(position, material);
        }
        int affected = 0;
        for (Map.Entry entry : queue) {
            BlockVector position = (BlockVector)entry.getKey();
            if (!this.setBlock((Vector)position, material = (BaseBlock)entry.getValue())) continue;
            ++affected;
        }
        return affected;
    }

    public int hollowOutRegion(Region region, int thickness, Pattern pattern) throws MaxChangedBlocksException {
        int affected = 0;
        HashSet<BlockVector> outside = new HashSet<BlockVector>();
        Vector min = region.getMinimumPoint();
        Vector max = region.getMaximumPoint();
        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                this.recurseHollow(region, new BlockVector(x, y, minZ), outside);
                this.recurseHollow(region, new BlockVector(x, y, maxZ), outside);
            }
        }
        for (int y = minY; y <= maxY; ++y) {
            for (int z = minZ; z <= maxZ; ++z) {
                this.recurseHollow(region, new BlockVector(minX, y, z), outside);
                this.recurseHollow(region, new BlockVector(maxX, y, z), outside);
            }
        }
        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                this.recurseHollow(region, new BlockVector(x, minY, z), outside);
                this.recurseHollow(region, new BlockVector(x, maxY, z), outside);
            }
        }
        for (int i = 1; i < thickness; ++i) {
            HashSet<BlockVector> newOutside = new HashSet<BlockVector>();
            block7: for (BlockVector position : region) {
                for (Vector recurseDirection : recurseDirections) {
                    BlockVector neighbor = position.add(recurseDirection).toBlockVector();
                    if (!outside.contains(neighbor)) continue;
                    newOutside.add(position);
                    continue block7;
                }
            }
            outside.addAll(newOutside);
        }
        block9: for (BlockVector position : region) {
            for (Vector recurseDirection : recurseDirections) {
                BlockVector neighbor = position.add(recurseDirection).toBlockVector();
                if (outside.contains(neighbor)) continue block9;
            }
            if (!this.setBlock((Vector)position, pattern.next(position))) continue;
            ++affected;
        }
        return affected;
    }

    public int drawLine(Pattern pattern, Vector pos1, Vector pos2, double radius, boolean filled) throws MaxChangedBlocksException {
        int domstep;
        int dz;
        int dy;
        Set<Vector> vset = new HashSet<Vector>();
        boolean notdrawn = true;
        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();
        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();
        int tipx = x1;
        int tipy = y1;
        int tipz = z1;
        int dx = Math.abs(x2 - x1);
        if (dx + (dy = Math.abs(y2 - y1)) + (dz = Math.abs(z2 - z1)) == 0) {
            vset.add(new Vector(tipx, tipy, tipz));
            notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dx && notdrawn) {
            for (domstep = 0; domstep <= dx; ++domstep) {
                tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                tipy = (int)Math.round((double)y1 + (double)domstep * (double)dy / (double)dx * (double)(y2 - y1 > 0 ? 1 : -1));
                tipz = (int)Math.round((double)z1 + (double)domstep * (double)dz / (double)dx * (double)(z2 - z1 > 0 ? 1 : -1));
                vset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dy && notdrawn) {
            for (domstep = 0; domstep <= dy; ++domstep) {
                tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                tipx = (int)Math.round((double)x1 + (double)domstep * (double)dx / (double)dy * (double)(x2 - x1 > 0 ? 1 : -1));
                tipz = (int)Math.round((double)z1 + (double)domstep * (double)dz / (double)dy * (double)(z2 - z1 > 0 ? 1 : -1));
                vset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dz && notdrawn) {
            for (domstep = 0; domstep <= dz; ++domstep) {
                tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                tipy = (int)Math.round((double)y1 + (double)domstep * (double)dy / (double)dz * (double)(y2 - y1 > 0 ? 1 : -1));
                tipx = (int)Math.round((double)x1 + (double)domstep * (double)dx / (double)dz * (double)(x2 - x1 > 0 ? 1 : -1));
                vset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        vset = EditSession.getBallooned(vset, radius);
        if (!filled) {
            vset = EditSession.getHollowed(vset);
        }
        return this.setBlocks(vset, pattern);
    }

    public int drawSpline(Pattern pattern, List<Vector> nodevectors, double tension, double bias, double continuity, double quality, double radius, boolean filled) throws MaxChangedBlocksException {
        Set<Vector> vset = new HashSet<Vector>();
        ArrayList<Node> nodes = new ArrayList<Node>(nodevectors.size());
        KochanekBartelsInterpolation interpol = new KochanekBartelsInterpolation();
        for (Vector nodevector : nodevectors) {
            Node n = new Node(nodevector);
            n.setTension(tension);
            n.setBias(bias);
            n.setContinuity(continuity);
            nodes.add(n);
        }
        interpol.setNodes(nodes);
        double splinelength = interpol.arcLength(0.0, 1.0);
        for (double loop = 0.0; loop <= 1.0; loop += 1.0 / splinelength / quality) {
            Vector tipv = interpol.getPosition(loop);
            int tipx = (int)Math.round(tipv.getX());
            int tipy = (int)Math.round(tipv.getY());
            int tipz = (int)Math.round(tipv.getZ());
            vset.add(new Vector(tipx, tipy, tipz));
        }
        vset = EditSession.getBallooned(vset, radius);
        if (!filled) {
            vset = EditSession.getHollowed(vset);
        }
        return this.setBlocks(vset, pattern);
    }

    private static double hypot(double ... pars) {
        double sum = 0.0;
        for (double d : pars) {
            sum += Math.pow(d, 2.0);
        }
        return Math.sqrt(sum);
    }

    private static Set<Vector> getBallooned(Set<Vector> vset, double radius) {
        HashSet<Vector> returnset = new HashSet<Vector>();
        int ceilrad = (int)Math.ceil(radius);
        for (Vector v : vset) {
            int tipx = v.getBlockX();
            int tipy = v.getBlockY();
            int tipz = v.getBlockZ();
            for (int loopx = tipx - ceilrad; loopx <= tipx + ceilrad; ++loopx) {
                for (int loopy = tipy - ceilrad; loopy <= tipy + ceilrad; ++loopy) {
                    for (int loopz = tipz - ceilrad; loopz <= tipz + ceilrad; ++loopz) {
                        double[] dArray = new double[]{loopx - tipx, loopy - tipy, loopz - tipz};
                        if (!(EditSession.hypot(dArray) <= radius)) continue;
                        returnset.add(new Vector(loopx, loopy, loopz));
                    }
                }
            }
        }
        return returnset;
    }

    private static Set<Vector> getHollowed(Set<Vector> vset) {
        HashSet<Vector> returnset = new HashSet<Vector>();
        for (Vector v : vset) {
            double z;
            double y;
            double x = v.getX();
            if (vset.contains(new Vector(x + 1.0, y = v.getY(), z = v.getZ())) && vset.contains(new Vector(x - 1.0, y, z)) && vset.contains(new Vector(x, y + 1.0, z)) && vset.contains(new Vector(x, y - 1.0, z)) && vset.contains(new Vector(x, y, z + 1.0)) && vset.contains(new Vector(x, y, z - 1.0))) continue;
            returnset.add(v);
        }
        return returnset;
    }

    private void recurseHollow(Region region, BlockVector origin, Set<BlockVector> outside) {
        LinkedList<BlockVector> queue = new LinkedList<BlockVector>();
        queue.addLast(origin);
        while (!queue.isEmpty()) {
            BlockVector current = (BlockVector)queue.removeFirst();
            if (!BlockType.canPassThrough(this.getBlockType(current), this.getBlockData(current)) || !outside.add(current) || !region.contains(current)) continue;
            for (Vector recurseDirection : recurseDirections) {
                queue.addLast(current.add(recurseDirection).toBlockVector());
            }
        }
    }

    public int makeBiomeShape(Region region, Vector zero, Vector unit, BaseBiome biomeType, String expressionString, boolean hollow) throws ExpressionException, MaxChangedBlocksException {
        final Vector2D zero2D = zero.toVector2D();
        final Vector2D unit2D = unit.toVector2D();
        final Expression expression = Expression.compile(expressionString, "x", "z");
        expression.optimize();
        EditSession editSession = this;
        final WorldEditExpressionEnvironment environment = new WorldEditExpressionEnvironment(editSession, unit, zero);
        expression.setEnvironment(environment);
        ArbitraryBiomeShape shape = new ArbitraryBiomeShape(region){

            @Override
            protected BaseBiome getBiome(int x, int z, BaseBiome defaultBiomeType) {
                Vector2D current = new Vector2D(x, z);
                environment.setCurrentBlock(current.toVector(0.0));
                Vector2D scaled = current.subtract(zero2D).divide(unit2D);
                try {
                    double[] dArray = new double[]{scaled.getX(), scaled.getZ()};
                    if (expression.evaluate(dArray) <= 0.0) {
                        return null;
                    }
                    return defaultBiomeType;
                }
                catch (Exception e) {
                    log.log(Level.WARNING, "Failed to create shape", e);
                    return null;
                }
            }
        };
        return shape.generate(this, biomeType, hollow);
    }

    private static double lengthSq(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    private static double lengthSq(double x, double z) {
        return x * x + z * z;
    }

    public static enum Stage {
        BEFORE_HISTORY,
        BEFORE_REORDER,
        BEFORE_CHANGE;

    }
}

