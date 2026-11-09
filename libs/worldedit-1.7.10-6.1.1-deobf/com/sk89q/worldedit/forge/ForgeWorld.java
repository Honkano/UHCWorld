/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.LazyBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.forge.ForgeEntity;
import com.sk89q.worldedit.forge.ForgeWorldData;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.forge.NBTConverter;
import com.sk89q.worldedit.forge.TileEntityBaseBlock;
import com.sk89q.worldedit.forge.TileEntityUtils;
import com.sk89q.worldedit.forge.WorldEditFakePlayer;
import com.sk89q.worldedit.internal.Constants;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.AbstractWorld;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.WorldData;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ForgeWorld
extends AbstractWorld {
    private static final Logger logger = Logger.getLogger(ForgeWorld.class.getCanonicalName());
    private static final Random random = new Random();
    private final WeakReference<World> worldRef;

    ForgeWorld(World world) {
        Preconditions.checkNotNull((Object)world);
        this.worldRef = new WeakReference<World>(world);
    }

    public World getWorldChecked() throws WorldEditException {
        World world = (World)this.worldRef.get();
        if (world != null) {
            return world;
        }
        throw new WorldReferenceLostException("The reference to the world was lost (i.e. the world may have been unloaded)");
    }

    public World getWorld() {
        World world = (World)this.worldRef.get();
        if (world != null) {
            return world;
        }
        throw new RuntimeException("The reference to the world was lost (i.e. the world may have been unloaded)");
    }

    @Override
    public String getName() {
        return this.getWorld().getWorldInfo().getWorldName();
    }

    @Override
    public boolean useItem(Vector position, BaseItem item, Direction face) {
        Item nativeItem = Item.getItemById((int)item.getType());
        ItemStack stack = new ItemStack(nativeItem, 1, (int)item.getData());
        World world = this.getWorld();
        return stack.tryPlaceItemIntoWorld((EntityPlayer)new WorldEditFakePlayer((WorldServer)world), world, position.getBlockX(), position.getBlockY(), position.getBlockZ(), ForgeAdapter.adapt(face), 0.0f, 0.0f, 0.0f);
    }

    @Override
    public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
        CompoundTag tag;
        boolean successful;
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkNotNull((Object)block);
        World world = this.getWorldChecked();
        int x = position.getBlockX();
        int y = position.getBlockY();
        int z = position.getBlockZ();
        Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
        int previousId = 0;
        if (notifyAndLight) {
            previousId = Block.getIdFromBlock((Block)chunk.getBlock(x & 0xF, y, z & 0xF));
        }
        if ((successful = chunk.func_150807_a(x & 0xF, y, z & 0xF, Block.getBlockById((int)block.getId()), block.getData())) && (tag = block.getNbtData()) != null) {
            NBTTagCompound nativeTag = NBTConverter.toNative(tag);
            nativeTag.setString("id", block.getNbtId());
            TileEntityUtils.setTileEntity(this.getWorld(), position, nativeTag);
        }
        if (notifyAndLight) {
            world.func_147451_t(x, y, z);
            world.markBlockForUpdate(x, y, z);
            world.notifyBlockChange(x, y, z, Block.getBlockById((int)previousId));
            Block mcBlock = Block.getBlockById((int)previousId);
            if (mcBlock != null && mcBlock.hasComparatorInputOverride()) {
                world.func_147453_f(x, y, z, Block.getBlockById((int)block.getId()));
            }
        }
        return successful;
    }

    @Override
    public int getBlockLightLevel(Vector position) {
        Preconditions.checkNotNull((Object)position);
        return this.getWorld().getBlockLightValue(position.getBlockX(), position.getBlockY(), position.getBlockZ());
    }

    @Override
    public boolean clearContainerBlockContents(Vector position) {
        Preconditions.checkNotNull((Object)position);
        TileEntity tile = this.getWorld().getTileEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        if (tile instanceof IInventory) {
            IInventory inv = (IInventory)tile;
            int size = inv.getSizeInventory();
            for (int i = 0; i < size; ++i) {
                inv.setInventorySlotContents(i, null);
            }
            return true;
        }
        return false;
    }

    @Override
    public BaseBiome getBiome(Vector2D position) {
        Preconditions.checkNotNull((Object)position);
        return new BaseBiome(this.getWorld().getBiomeGenForCoords((int)position.getBlockX(), (int)position.getBlockZ()).biomeID);
    }

    @Override
    public boolean setBiome(Vector2D position, BaseBiome biome) {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkNotNull((Object)biome);
        Chunk chunk = this.getWorld().getChunkFromBlockCoords(position.getBlockX(), position.getBlockZ());
        if (chunk != null && chunk.isChunkLoaded) {
            chunk.getBiomeArray()[(position.getBlockZ() & 0xF) << 4 | position.getBlockX() & 0xF] = (byte)biome.getId();
            return true;
        }
        return false;
    }

    @Override
    public void dropItem(Vector position, BaseItemStack item) {
        Preconditions.checkNotNull((Object)position);
        Preconditions.checkNotNull((Object)item);
        if (item.getType() == 0) {
            return;
        }
        EntityItem entity = new EntityItem(this.getWorld(), position.getX(), position.getY(), position.getZ(), ForgeWorldEdit.toForgeItemStack(item));
        entity.delayBeforeCanPickup = 10;
        this.getWorld().spawnEntityInWorld((Entity)entity);
    }

    @Override
    public boolean regenerate(Region region, EditSession editSession) {
        BaseBlock[] history = new BaseBlock[256 * (this.getMaxY() + 1)];
        for (Vector2D chunk : region.getChunks()) {
            Vector pt;
            int x;
            Vector min = new Vector(chunk.getBlockX() * 16, 0, chunk.getBlockZ() * 16);
            for (x = 0; x < 16; ++x) {
                for (int y = 0; y < this.getMaxY() + 1; ++y) {
                    for (int z = 0; z < 16; ++z) {
                        pt = min.add(x, y, z);
                        int index = y * 16 * 16 + z * 16 + x;
                        history[index] = editSession.getBlock(pt);
                    }
                }
            }
            try {
                Field p;
                Field lc;
                Field m;
                Field u;
                Set<Vector2D> chunks = region.getChunks();
                IChunkProvider provider = this.getWorld().getChunkProvider();
                if (!(provider instanceof ChunkProviderServer)) {
                    return false;
                }
                ChunkProviderServer chunkServer = (ChunkProviderServer)provider;
                try {
                    u = ChunkProviderServer.class.getDeclaredField("field_73248_b");
                }
                catch (NoSuchFieldException e) {
                    u = ChunkProviderServer.class.getDeclaredField("chunksToUnload");
                }
                u.setAccessible(true);
                Set unloadQueue = (Set)u.get(chunkServer);
                try {
                    m = ChunkProviderServer.class.getDeclaredField("field_73244_f");
                }
                catch (NoSuchFieldException e) {
                    m = ChunkProviderServer.class.getDeclaredField("loadedChunkHashMap");
                }
                m.setAccessible(true);
                LongHashMap loadedMap = (LongHashMap)m.get(chunkServer);
                try {
                    lc = ChunkProviderServer.class.getDeclaredField("field_73245_g");
                }
                catch (NoSuchFieldException e) {
                    lc = ChunkProviderServer.class.getDeclaredField("loadedChunks");
                }
                lc.setAccessible(true);
                List loaded = (List)lc.get(chunkServer);
                try {
                    p = ChunkProviderServer.class.getDeclaredField("field_73246_d");
                }
                catch (NoSuchFieldException e) {
                    p = ChunkProviderServer.class.getDeclaredField("currentChunkProvider");
                }
                p.setAccessible(true);
                IChunkProvider chunkProvider = (IChunkProvider)p.get(chunkServer);
                for (Vector2D coord : chunks) {
                    Chunk mcChunk;
                    long pos = ChunkCoordIntPair.chunkXZ2Int((int)coord.getBlockX(), (int)coord.getBlockZ());
                    if (chunkServer.chunkExists(coord.getBlockX(), coord.getBlockZ())) {
                        mcChunk = chunkServer.loadChunk(coord.getBlockX(), coord.getBlockZ());
                        mcChunk.onChunkUnload();
                    }
                    unloadQueue.remove(pos);
                    loadedMap.remove(pos);
                    mcChunk = chunkProvider.provideChunk(coord.getBlockX(), coord.getBlockZ());
                    loadedMap.add(pos, (Object)mcChunk);
                    loaded.add(mcChunk);
                    if (mcChunk == null) continue;
                    mcChunk.onChunkLoad();
                    mcChunk.populateChunk(chunkProvider, chunkProvider, coord.getBlockX(), coord.getBlockZ());
                }
            }
            catch (Throwable t) {
                logger.log(Level.WARNING, "Failed to generate chunk", t);
                return false;
            }
            for (x = 0; x < 16; ++x) {
                for (int y = 0; y < this.getMaxY() + 1; ++y) {
                    for (int z = 0; z < 16; ++z) {
                        pt = min.add(x, y, z);
                        int index = y * 16 * 16 + z * 16 + x;
                        if (!region.contains(pt)) {
                            editSession.smartSetBlock(pt, history[index]);
                            continue;
                        }
                        editSession.rememberChange(pt, history[index], editSession.rawGetBlock(pt));
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    private static WorldGenerator createWorldGenerator(TreeGenerator.TreeType type) {
        switch (type) {
            case TREE: {
                return new WorldGenTrees(true);
            }
            case BIG_TREE: {
                return new WorldGenBigTree(true);
            }
            case REDWOOD: {
                return new WorldGenTaiga2(true);
            }
            case TALL_REDWOOD: {
                return new WorldGenTaiga1();
            }
            case BIRCH: {
                return new WorldGenForest(true, false);
            }
            case JUNGLE: {
                return new WorldGenMegaJungle(true, 10, 20, 3, 3);
            }
            case SMALL_JUNGLE: {
                return new WorldGenTrees(true, 4 + random.nextInt(7), 3, 3, false);
            }
            case SHORT_JUNGLE: {
                return new WorldGenTrees(true, 4 + random.nextInt(7), 3, 3, true);
            }
            case JUNGLE_BUSH: {
                return new WorldGenShrub(3, 0);
            }
            case RED_MUSHROOM: {
                return new WorldGenBigMushroom(1);
            }
            case BROWN_MUSHROOM: {
                return new WorldGenBigMushroom(0);
            }
            case SWAMP: {
                return new WorldGenSwamp();
            }
            case ACACIA: {
                return new WorldGenSavannaTree(true);
            }
            case DARK_OAK: {
                return new WorldGenCanopyTree(true);
            }
            case MEGA_REDWOOD: {
                return new WorldGenMegaPineTree(false, random.nextBoolean());
            }
            case TALL_BIRCH: {
                return new WorldGenForest(true, true);
            }
        }
        return null;
    }

    @Override
    public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector position) throws MaxChangedBlocksException {
        WorldGenerator generator = ForgeWorld.createWorldGenerator(type);
        return generator != null ? generator.generate(this.getWorld(), random, position.getBlockX(), position.getBlockY(), position.getBlockZ()) : false;
    }

    @Override
    public WorldData getWorldData() {
        return ForgeWorldData.getInstance();
    }

    @Override
    public boolean isValidBlockType(int id) {
        return id == 0 || Block.getBlockById((int)id) != null;
    }

    @Override
    public BaseBlock getBlock(Vector position) {
        World world = this.getWorld();
        int id = Block.getIdFromBlock((Block)world.getBlock(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
        int data = world.getBlockMetadata(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        TileEntity tile = this.getWorld().getTileEntity(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        if (tile != null) {
            return new TileEntityBaseBlock(id, data, tile);
        }
        return new BaseBlock(id, data);
    }

    @Override
    public BaseBlock getLazyBlock(Vector position) {
        World world = this.getWorld();
        int id = Block.getIdFromBlock((Block)world.getBlock(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
        int data = world.getBlockMetadata(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        return new LazyBlock(id, data, this, position);
    }

    @Override
    public int hashCode() {
        return this.getWorld().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof ForgeWorld) {
            ForgeWorld other = (ForgeWorld)o;
            World otherWorld = (World)other.worldRef.get();
            World thisWorld = (World)this.worldRef.get();
            return otherWorld != null && thisWorld != null && otherWorld.equals(thisWorld);
        }
        if (o instanceof com.sk89q.worldedit.world.World) {
            return ((com.sk89q.worldedit.world.World)o).getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public List<? extends com.sk89q.worldedit.entity.Entity> getEntities(Region region) {
        ArrayList<ForgeEntity> entities = new ArrayList<ForgeEntity>();
        World world = this.getWorld();
        List ents = world.loadedEntityList;
        for (Entity entity : ents) {
            if (!region.contains(new Vector(entity.posX, entity.posY, entity.posZ))) continue;
            entities.add(new ForgeEntity(entity));
        }
        return entities;
    }

    @Override
    public List<? extends com.sk89q.worldedit.entity.Entity> getEntities() {
        ArrayList<ForgeEntity> entities = new ArrayList<ForgeEntity>();
        for (Object entity : this.getWorld().loadedEntityList) {
            entities.add(new ForgeEntity((Entity)entity));
        }
        return entities;
    }

    @Override
    @Nullable
    public com.sk89q.worldedit.entity.Entity createEntity(Location location, BaseEntity entity) {
        World world = this.getWorld();
        Entity createdEntity = EntityList.createEntityByName((String)entity.getTypeId(), (World)world);
        if (createdEntity != null) {
            CompoundTag nativeTag = entity.getNbtData();
            if (nativeTag != null) {
                NBTTagCompound tag = NBTConverter.toNative(entity.getNbtData());
                for (String name : Constants.NO_COPY_ENTITY_NBT_FIELDS) {
                    tag.removeTag(name);
                }
                createdEntity.readFromNBT(tag);
            }
            createdEntity.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            world.spawnEntityInWorld(createdEntity);
            return new ForgeEntity(createdEntity);
        }
        return null;
    }

    private static class WorldReferenceLostException
    extends WorldEditException {
        private WorldReferenceLostException(String message) {
            super(message);
        }
    }
}

