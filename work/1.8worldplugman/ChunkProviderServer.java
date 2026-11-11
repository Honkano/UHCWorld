/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockSand;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkRegionLoader;
import net.minecraft.server.v1_8_R3.CrashReport;
import net.minecraft.server.v1_8_R3.CrashReportSystemDetails;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.ExceptionWorldConflict;
import net.minecraft.server.v1_8_R3.IChunkLoader;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IProgressUpdate;
import net.minecraft.server.v1_8_R3.ReportedException;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;
import org.bukkit.craftbukkit.v1_8_R3.util.LongObjectHashMap;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.generator.BlockPopulator;

public class ChunkProviderServer
implements IChunkProvider {
    private static final Logger b = LogManager.getLogger();
    public LongHashSet unloadQueue = new LongHashSet();
    public Chunk emptyChunk;
    public IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    public boolean forceChunkLoad = false;
    public LongObjectHashMap<Chunk> chunks = new LongObjectHashMap();
    public WorldServer world;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider) {
        this.emptyChunk = new EmptyChunk(worldserver, 0, 0);
        this.world = worldserver;
        this.chunkLoader = ichunkloader;
        this.chunkProvider = ichunkprovider;
    }

    @Override
    public boolean isChunkLoaded(int i, int j) {
        return this.chunks.containsKey(LongHash.toLong(i, j));
    }

    public Collection a() {
        return this.chunks.values();
    }

    public void queueUnload(int i, int j) {
        if (this.world.worldProvider.e()) {
            if (!this.world.c(i, j)) {
                this.unloadQueue.add(i, j);
                Chunk c = this.chunks.get(LongHash.toLong(i, j));
                if (c != null) {
                    c.mustSave = true;
                }
            }
        } else {
            this.unloadQueue.add(i, j);
            Chunk c = this.chunks.get(LongHash.toLong(i, j));
            if (c != null) {
                c.mustSave = true;
            }
        }
    }

    public void b() {
        for (Chunk chunk : this.chunks.values()) {
            this.queueUnload(chunk.locX, chunk.locZ);
        }
    }

    public Chunk getChunkIfLoaded(int x, int z) {
        return this.chunks.get(LongHash.toLong(x, z));
    }

    public Chunk getChunkAt(int i, int j) {
        return this.getChunkAt(i, j, null);
    }

    public Chunk getChunkAt(int i, int j, Runnable runnable) {
        this.unloadQueue.remove(i, j);
        Chunk chunk = this.chunks.get(LongHash.toLong(i, j));
        ChunkRegionLoader loader = null;
        if (this.chunkLoader instanceof ChunkRegionLoader) {
            loader = (ChunkRegionLoader)this.chunkLoader;
        }
        if (chunk == null && loader != null && loader.chunkExists(this.world, i, j)) {
            if (runnable != null) {
                ChunkIOExecutor.queueChunkLoad(this.world, loader, this, i, j, runnable);
                return null;
            }
            chunk = ChunkIOExecutor.syncChunkLoad(this.world, loader, this, i, j);
        } else if (chunk == null) {
            chunk = this.originalGetChunkAt(i, j);
        }
        if (runnable != null) {
            runnable.run();
        }
        return chunk;
    }

    public Chunk originalGetChunkAt(int i, int j) {
        this.unloadQueue.remove(i, j);
        Chunk chunk = this.chunks.get(LongHash.toLong(i, j));
        boolean newChunk = false;
        if (chunk == null) {
            this.world.timings.syncChunkLoadTimer.startTiming();
            chunk = this.loadChunk(i, j);
            if (chunk == null) {
                if (this.chunkProvider == null) {
                    chunk = this.emptyChunk;
                } else {
                    try {
                        chunk = this.chunkProvider.getOrCreateChunk(i, j);
                    }
                    catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");
                        crashreportsystemdetails.a("Location", String.format("%d,%d", i, j));
                        crashreportsystemdetails.a("Position hash", LongHash.toLong(i, j));
                        crashreportsystemdetails.a("Generator", this.chunkProvider.getName());
                        throw new ReportedException(crashreport);
                    }
                }
                newChunk = true;
            }
            this.chunks.put(LongHash.toLong(i, j), chunk);
            chunk.addEntities();
            CraftServer server = this.world.getServer();
            if (server != null) {
                server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk, newChunk));
            }
            int x = -2;
            while (x < 3) {
                int z = -2;
                while (z < 3) {
                    Chunk neighbor;
                    if ((x != 0 || z != 0) && (neighbor = this.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z)) != null) {
                        neighbor.setNeighborLoaded(-x, -z);
                        chunk.setNeighborLoaded(x, z);
                    }
                    ++z;
                }
                ++x;
            }
            chunk.loadNearby(this, this, i, j);
            this.world.timings.syncChunkLoadTimer.stopTiming();
        }
        return chunk;
    }

    @Override
    public Chunk getOrCreateChunk(int i, int j) {
        Chunk chunk = this.chunks.get(LongHash.toLong(i, j));
        Chunk chunk2 = chunk == null ? (!this.world.ad() && !this.forceChunkLoad ? this.emptyChunk : this.getChunkAt(i, j)) : (chunk = chunk);
        if (chunk == this.emptyChunk) {
            return chunk;
        }
        if (i != chunk.locX || j != chunk.locZ) {
            b.error("Chunk (" + chunk.locX + ", " + chunk.locZ + ") stored at  (" + i + ", " + j + ") in world '" + this.world.getWorld().getName() + "'");
            b.error(chunk.getClass().getName());
            Throwable ex = new Throwable();
            ex.fillInStackTrace();
            ex.printStackTrace();
        }
        return chunk;
    }

    public Chunk loadChunk(int i, int j) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            Chunk chunk = this.chunkLoader.a(this.world, i, j);
            if (chunk != null) {
                chunk.setLastSaved(this.world.getTime());
                if (this.chunkProvider != null) {
                    this.world.timings.syncChunkLoadStructuresTimer.startTiming();
                    this.chunkProvider.recreateStructures(chunk, i, j);
                    this.world.timings.syncChunkLoadStructuresTimer.stopTiming();
                }
            }
            return chunk;
        }
        catch (Exception exception) {
            b.error("Couldn't load chunk", (Throwable)exception);
            return null;
        }
    }

    public void saveChunkNOP(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.b(this.world, chunk);
            }
            catch (Exception exception) {
                b.error("Couldn't save entities", (Throwable)exception);
            }
        }
    }

    public void saveChunk(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                chunk.setLastSaved(this.world.getTime());
                this.chunkLoader.a(this.world, chunk);
            }
            catch (IOException ioexception) {
                b.error("Couldn't save chunk", (Throwable)ioexception);
            }
            catch (ExceptionWorldConflict exceptionworldconflict) {
                b.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)exceptionworldconflict);
            }
        }
    }

    @Override
    public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
        Chunk chunk = this.getOrCreateChunk(i, j);
        if (!chunk.isDone()) {
            chunk.n();
            if (this.chunkProvider != null) {
                this.chunkProvider.getChunkAt(ichunkprovider, i, j);
                BlockSand.instaFall = true;
                Random random = new Random();
                random.setSeed(this.world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long)i * xRand + (long)j * zRand ^ this.world.getSeed());
                CraftWorld world = this.world.getWorld();
                if (world != null) {
                    this.world.populating = true;
                    try {
                        for (BlockPopulator populator : world.getPopulators()) {
                            populator.populate(world, random, chunk.bukkitChunk);
                        }
                    }
                    finally {
                        this.world.populating = false;
                    }
                }
                BlockSand.instaFall = false;
                this.world.getServer().getPluginManager().callEvent(new ChunkPopulateEvent(chunk.bukkitChunk));
                chunk.e();
            }
        }
    }

    @Override
    public boolean a(IChunkProvider ichunkprovider, Chunk chunk, int i, int j) {
        if (this.chunkProvider != null && this.chunkProvider.a(ichunkprovider, chunk, i, j)) {
            Chunk chunk1 = this.getOrCreateChunk(i, j);
            chunk1.e();
            return true;
        }
        return false;
    }

    @Override
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        int i = 0;
        for (Chunk chunk : this.chunks.values()) {
            if (flag) {
                this.saveChunkNOP(chunk);
            }
            if (!chunk.a(flag)) continue;
            this.saveChunk(chunk);
            chunk.f(false);
            if (++i != 24) continue;
        }
        return true;
    }

    @Override
    public void c() {
        if (this.chunkLoader != null) {
            this.chunkLoader.b();
        }
    }

    @Override
    public boolean unloadChunks() {
        if (!this.world.savingDisabled) {
            CraftServer server = this.world.getServer();
            int i = 0;
            while (i < 100 && !this.unloadQueue.isEmpty()) {
                long chunkcoordinates = this.unloadQueue.popFirst();
                Chunk chunk = this.chunks.get(chunkcoordinates);
                if (chunk != null) {
                    ChunkUnloadEvent event = new ChunkUnloadEvent(chunk.bukkitChunk);
                    server.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        if (chunk != null) {
                            chunk.removeEntities();
                            this.saveChunk(chunk);
                            this.saveChunkNOP(chunk);
                            this.chunks.remove(chunkcoordinates);
                        }
                        int x = -2;
                        while (x < 3) {
                            int z = -2;
                            while (z < 3) {
                                Chunk neighbor;
                                if ((x != 0 || z != 0) && (neighbor = this.getChunkIfLoaded(chunk.locX + x, chunk.locZ + z)) != null) {
                                    neighbor.setNeighborUnloaded(-x, -z);
                                    chunk.setNeighborUnloaded(x, z);
                                }
                                ++z;
                            }
                            ++x;
                        }
                    }
                }
                ++i;
            }
            if (this.chunkLoader != null) {
                this.chunkLoader.a();
            }
        }
        return this.chunkProvider.unloadChunks();
    }

    @Override
    public boolean canSave() {
        return !this.world.savingDisabled;
    }

    @Override
    public String getName() {
        return "ServerChunkCache: " + this.chunks.size() + " Drop: " + this.unloadQueue.size();
    }

    @Override
    public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType enumcreaturetype, BlockPosition blockposition) {
        return this.chunkProvider.getMobsFor(enumcreaturetype, blockposition);
    }

    @Override
    public BlockPosition findNearestMapFeature(World world, String s, BlockPosition blockposition) {
        return this.chunkProvider.findNearestMapFeature(world, s, blockposition);
    }

    @Override
    public int getLoadedChunks() {
        return this.chunks.size();
    }

    @Override
    public void recreateStructures(Chunk chunk, int i, int j) {
    }

    @Override
    public Chunk getChunkAt(BlockPosition blockposition) {
        return this.getOrCreateChunk(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }
}

