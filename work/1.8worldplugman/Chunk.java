/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockContainer;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.ChunkProviderDebug;
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.ChunkSnapshot;
import net.minecraft.server.v1_8_R3.CrashReport;
import net.minecraft.server.v1_8_R3.CrashReportSystemDetails;
import net.minecraft.server.v1_8_R3.EmptyChunk;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumCreatureType;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.EnumSkyBlock;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.IChunkProvider;
import net.minecraft.server.v1_8_R3.IContainer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.Material;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.ReportedException;
import net.minecraft.server.v1_8_R3.TileEntity;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldChunkManager;
import net.minecraft.server.v1_8_R3.WorldType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.HumanEntity;

public class Chunk {
    private static final Logger c = LogManager.getLogger();
    private final ChunkSection[] sections;
    private final byte[] e;
    private final int[] f;
    private final boolean[] g;
    private boolean h;
    public final World world;
    public final int[] heightMap;
    public final int locX;
    public final int locZ;
    private boolean k;
    public final Map<BlockPosition, TileEntity> tileEntities;
    public final List<Entity>[] entitySlices;
    private boolean done;
    private boolean lit;
    private boolean p;
    private boolean q;
    private boolean r;
    private long lastSaved;
    private int t;
    private long u;
    private int v = 4096;
    private ConcurrentLinkedQueue<BlockPosition> w;
    protected TObjectIntHashMap<Class> entityCount = new TObjectIntHashMap();
    private int neighbors = 4096;
    public org.bukkit.Chunk bukkitChunk;
    public boolean mustSave;

    public boolean areNeighborsLoaded(int radius) {
        switch (radius) {
            case 2: {
                return this.neighbors == 0x1FFFFFF;
            }
            case 1: {
                return (this.neighbors & 0x739C0) == 473536;
            }
        }
        throw new UnsupportedOperationException(String.valueOf(radius));
    }

    public void setNeighborLoaded(int x, int z) {
        this.neighbors |= 1 << x * 5 + 12 + z;
    }

    public void setNeighborUnloaded(int x, int z) {
        this.neighbors &= ~(1 << x * 5 + 12 + z);
    }

    public Chunk(World world, int i, int j) {
        this.sections = new ChunkSection[16];
        this.e = new byte[256];
        this.f = new int[256];
        this.g = new boolean[256];
        this.tileEntities = Maps.newHashMap();
        this.w = Queues.newConcurrentLinkedQueue();
        this.entitySlices = new List[16];
        this.world = world;
        this.locX = i;
        this.locZ = j;
        this.heightMap = new int[256];
        int k = 0;
        while (k < this.entitySlices.length) {
            this.entitySlices[k] = new UnsafeList<Entity>();
            ++k;
        }
        Arrays.fill(this.f, -999);
        Arrays.fill(this.e, (byte)-1);
        if (!(this instanceof EmptyChunk)) {
            this.bukkitChunk = new CraftChunk(this);
        }
    }

    public Chunk(World world, ChunkSnapshot chunksnapshot, int i, int j) {
        this(world, i, j);
        int short0 = 256;
        boolean flag = !world.worldProvider.o();
        int k = 0;
        while (k < 16) {
            int l = 0;
            while (l < 16) {
                int i1 = 0;
                while (i1 < short0) {
                    int j1 = k * short0 * 16 | l * short0 | i1;
                    IBlockData iblockdata = chunksnapshot.a(j1);
                    if (iblockdata.getBlock().getMaterial() != Material.AIR) {
                        int k1 = i1 >> 4;
                        if (this.sections[k1] == null) {
                            this.sections[k1] = new ChunkSection(k1 << 4, flag);
                        }
                        this.sections[k1].setType(k, i1 & 0xF, l, iblockdata);
                    }
                    ++i1;
                }
                ++l;
            }
            ++k;
        }
    }

    public boolean a(int i, int j) {
        return i == this.locX && j == this.locZ;
    }

    public int f(BlockPosition blockposition) {
        return this.b(blockposition.getX() & 0xF, blockposition.getZ() & 0xF);
    }

    public int b(int i, int j) {
        return this.heightMap[j << 4 | i];
    }

    public int g() {
        int i = this.sections.length - 1;
        while (i >= 0) {
            if (this.sections[i] != null) {
                return this.sections[i].getYPosition();
            }
            --i;
        }
        return 0;
    }

    public ChunkSection[] getSections() {
        return this.sections;
    }

    public void initLighting() {
        int i = this.g();
        this.t = Integer.MAX_VALUE;
        int j = 0;
        while (j < 16) {
            int k = 0;
            while (k < 16) {
                int l;
                this.f[j + (k << 4)] = -999;
                for (l = i + 16; l > 0; --l) {
                    if (this.e(j, l - 1, k) == 0) {
                        continue;
                    }
                    this.heightMap[k << 4 | j] = l;
                    if (l >= this.t) break;
                    this.t = l;
                    break;
                }
                if (!this.world.worldProvider.o()) {
                    l = 15;
                    int i1 = i + 16 - 1;
                    do {
                        ChunkSection chunksection;
                        int j1;
                        if ((j1 = this.e(j, i1, k)) == 0 && l != 15) {
                            j1 = 1;
                        }
                        if ((l -= j1) <= 0 || (chunksection = this.sections[i1 >> 4]) == null) continue;
                        chunksection.a(j, i1 & 0xF, k, l);
                        this.world.n(new BlockPosition((this.locX << 4) + j, i1, (this.locZ << 4) + k));
                    } while (--i1 > 0 && l > 0);
                }
                ++k;
            }
            ++j;
        }
        this.q = true;
    }

    private void d(int i, int j) {
        this.g[i + j * 16] = true;
        this.k = true;
    }

    private void h(boolean flag) {
        this.world.methodProfiler.a("recheckGaps");
        if (this.world.areChunksLoaded(new BlockPosition(this.locX * 16 + 8, 0, this.locZ * 16 + 8), 16)) {
            int i = 0;
            while (i < 16) {
                int j = 0;
                while (j < 16) {
                    if (this.g[i + j * 16]) {
                        this.g[i + j * 16] = false;
                        int k = this.b(i, j);
                        int l = this.locX * 16 + i;
                        int i1 = this.locZ * 16 + j;
                        int j1 = Integer.MAX_VALUE;
                        for (EnumDirection enumdirection : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
                            j1 = Math.min(j1, this.world.b(l + enumdirection.getAdjacentX(), i1 + enumdirection.getAdjacentZ()));
                        }
                        this.c(l, i1, j1);
                        for (EnumDirection enumdirection : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
                            this.c(l + enumdirection.getAdjacentX(), i1 + enumdirection.getAdjacentZ(), k);
                        }
                        if (flag) {
                            this.world.methodProfiler.b();
                            return;
                        }
                    }
                    ++j;
                }
                ++i;
            }
            this.k = false;
        }
        this.world.methodProfiler.b();
    }

    private void c(int i, int j, int k) {
        int l = this.world.getHighestBlockYAt(new BlockPosition(i, 0, j)).getY();
        if (l > k) {
            this.a(i, j, k, l + 1);
        } else if (l < k) {
            this.a(i, j, l, k + 1);
        }
    }

    private void a(int i, int j, int k, int l) {
        if (l > k && this.world.areChunksLoaded(new BlockPosition(i, 0, j), 16)) {
            int i1 = k;
            while (i1 < l) {
                this.world.c(EnumSkyBlock.SKY, new BlockPosition(i, i1, j));
                ++i1;
            }
            this.q = true;
        }
    }

    private void d(int i, int j, int k) {
        int l;
        int i1 = l = this.heightMap[k << 4 | i] & 0xFF;
        if (j > l) {
            i1 = j;
        }
        while (i1 > 0 && this.e(i, i1 - 1, k) == 0) {
            --i1;
        }
        if (i1 != l) {
            int i2;
            int l1;
            this.world.a(i + this.locX * 16, k + this.locZ * 16, i1, l);
            this.heightMap[k << 4 | i] = i1;
            int j1 = this.locX * 16 + i;
            int k1 = this.locZ * 16 + k;
            if (!this.world.worldProvider.o()) {
                ChunkSection chunksection;
                if (i1 < l) {
                    l1 = i1;
                    while (l1 < l) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.a(i, l1 & 0xF, k, 15);
                            this.world.n(new BlockPosition((this.locX << 4) + i, l1, (this.locZ << 4) + k));
                        }
                        ++l1;
                    }
                } else {
                    l1 = l;
                    while (l1 < i1) {
                        chunksection = this.sections[l1 >> 4];
                        if (chunksection != null) {
                            chunksection.a(i, l1 & 0xF, k, 0);
                            this.world.n(new BlockPosition((this.locX << 4) + i, l1, (this.locZ << 4) + k));
                        }
                        ++l1;
                    }
                }
                l1 = 15;
                while (i1 > 0 && l1 > 0) {
                    ChunkSection chunksection1;
                    if ((i2 = this.e(i, --i1, k)) == 0) {
                        i2 = 1;
                    }
                    if ((l1 -= i2) < 0) {
                        l1 = 0;
                    }
                    if ((chunksection1 = this.sections[i1 >> 4]) == null) continue;
                    chunksection1.a(i, i1 & 0xF, k, l1);
                }
            }
            l1 = this.heightMap[k << 4 | i];
            i2 = l;
            int j2 = l1;
            if (l1 < l) {
                i2 = l1;
                j2 = l;
            }
            if (l1 < this.t) {
                this.t = l1;
            }
            if (!this.world.worldProvider.o()) {
                for (EnumDirection enumdirection : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
                    this.a(j1 + enumdirection.getAdjacentX(), k1 + enumdirection.getAdjacentZ(), i2, j2);
                }
                this.a(j1, k1, i2, j2);
            }
            this.q = true;
        }
    }

    public int b(BlockPosition blockposition) {
        return this.getType(blockposition).p();
    }

    private int e(int i, int j, int k) {
        return this.getType(i, j, k).p();
    }

    private Block getType(int i, int j, int k) {
        ChunkSection chunksection;
        Block block = Blocks.AIR;
        if (j >= 0 && j >> 4 < this.sections.length && (chunksection = this.sections[j >> 4]) != null) {
            try {
                block = chunksection.b(i, j & 0xF, k);
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Getting block");
                throw new ReportedException(crashreport);
            }
        }
        return block;
    }

    public Block getTypeAbs(final int i, final int j, final int k) {
        try {
            return this.getType(i & 0xF, j, k & 0xF);
        }
        catch (ReportedException reportedexception) {
            CrashReportSystemDetails crashreportsystemdetails = reportedexception.a().a("Block being got");
            crashreportsystemdetails.a("Location", new Callable(){

                public String a() throws Exception {
                    return CrashReportSystemDetails.a(new BlockPosition(Chunk.this.locX * 16 + i, j, Chunk.this.locZ * 16 + k));
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            throw reportedexception;
        }
    }

    public Block getType(final BlockPosition blockposition) {
        try {
            return this.getType(blockposition.getX() & 0xF, blockposition.getY(), blockposition.getZ() & 0xF);
        }
        catch (ReportedException reportedexception) {
            CrashReportSystemDetails crashreportsystemdetails = reportedexception.a().a("Block being got");
            crashreportsystemdetails.a("Location", new Callable(){

                public String a() throws Exception {
                    return CrashReportSystemDetails.a(blockposition);
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            throw reportedexception;
        }
    }

    public IBlockData getBlockData(final BlockPosition blockposition) {
        if (this.world.G() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            IBlockData iblockdata = null;
            if (blockposition.getY() == 60) {
                iblockdata = Blocks.BARRIER.getBlockData();
            }
            if (blockposition.getY() == 70) {
                iblockdata = ChunkProviderDebug.b(blockposition.getX(), blockposition.getZ());
            }
            return iblockdata == null ? Blocks.AIR.getBlockData() : iblockdata;
        }
        try {
            ChunkSection chunksection;
            if (blockposition.getY() >= 0 && blockposition.getY() >> 4 < this.sections.length && (chunksection = this.sections[blockposition.getY() >> 4]) != null) {
                int i = blockposition.getX() & 0xF;
                int j = blockposition.getY() & 0xF;
                int k = blockposition.getZ() & 0xF;
                return chunksection.getType(i, j, k);
            }
            return Blocks.AIR.getBlockData();
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Getting block state");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");
            crashreportsystemdetails.a("Location", new Callable(){

                public String a() throws Exception {
                    return CrashReportSystemDetails.a(blockposition);
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private int g(int i, int j, int k) {
        if (j >> 4 >= this.sections.length) {
            return 0;
        }
        ChunkSection chunksection = this.sections[j >> 4];
        return chunksection != null ? chunksection.c(i, j & 0xF, k) : 0;
    }

    public int c(BlockPosition blockposition) {
        return this.g(blockposition.getX() & 0xF, blockposition.getY(), blockposition.getZ() & 0xF);
    }

    public IBlockData a(BlockPosition blockposition, IBlockData iblockdata) {
        TileEntity tileentity;
        int k;
        int l;
        int i = blockposition.getX() & 0xF;
        int j = blockposition.getY();
        if (j >= this.f[l = (k = blockposition.getZ() & 0xF) << 4 | i] - 1) {
            this.f[l] = -999;
        }
        int i1 = this.heightMap[l];
        IBlockData iblockdata1 = this.getBlockData(blockposition);
        if (iblockdata1 == iblockdata) {
            return null;
        }
        Block block = iblockdata.getBlock();
        Block block1 = iblockdata1.getBlock();
        ChunkSection chunksection = this.sections[j >> 4];
        boolean flag = false;
        if (chunksection == null) {
            if (block == Blocks.AIR) {
                return null;
            }
            ChunkSection chunkSection = new ChunkSection(j >> 4 << 4, !this.world.worldProvider.o());
            this.sections[j >> 4] = chunkSection;
            chunksection = chunkSection;
            flag = j >= i1;
        }
        chunksection.setType(i, j & 0xF, k, iblockdata);
        if (block1 != block) {
            if (!this.world.isClientSide) {
                block1.remove(this.world, blockposition, iblockdata1);
            } else if (block1 instanceof IContainer) {
                this.world.t(blockposition);
            }
        }
        if (chunksection.b(i, j & 0xF, k) != block) {
            return null;
        }
        if (flag) {
            this.initLighting();
        } else {
            int j1 = block.p();
            int k1 = block1.p();
            if (j1 > 0) {
                if (j >= i1) {
                    this.d(i, j + 1, k);
                }
            } else if (j == i1 - 1) {
                this.d(i, j, k);
            }
            if (j1 != k1 && (j1 < k1 || this.getBrightness(EnumSkyBlock.SKY, blockposition) > 0 || this.getBrightness(EnumSkyBlock.BLOCK, blockposition) > 0)) {
                this.d(i, k);
            }
        }
        if (block1 instanceof IContainer && (tileentity = this.a(blockposition, EnumTileEntityState.CHECK)) != null) {
            tileentity.E();
        }
        if (!(this.world.isClientSide || block1 == block || this.world.captureBlockStates && !(block instanceof BlockContainer))) {
            block.onPlace(this.world, blockposition, iblockdata);
        }
        if (block instanceof IContainer) {
            tileentity = this.a(blockposition, EnumTileEntityState.CHECK);
            if (tileentity == null) {
                tileentity = ((IContainer)((Object)block)).a(this.world, block.toLegacyData(iblockdata));
                this.world.setTileEntity(blockposition, tileentity);
            }
            if (tileentity != null) {
                tileentity.E();
            }
        }
        this.q = true;
        return iblockdata1;
    }

    public int getBrightness(EnumSkyBlock enumskyblock, BlockPosition blockposition) {
        int i = blockposition.getX() & 0xF;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 0xF;
        ChunkSection chunksection = this.sections[j >> 4];
        return chunksection == null ? (this.d(blockposition) ? enumskyblock.c : 0) : (enumskyblock == EnumSkyBlock.SKY ? (this.world.worldProvider.o() ? 0 : chunksection.d(i, j & 0xF, k)) : (enumskyblock == EnumSkyBlock.BLOCK ? chunksection.e(i, j & 0xF, k) : enumskyblock.c));
    }

    public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) {
        int j = blockposition.getX() & 0xF;
        int k = blockposition.getY();
        int l = blockposition.getZ() & 0xF;
        ChunkSection chunksection = this.sections[k >> 4];
        if (chunksection == null) {
            ChunkSection chunkSection = new ChunkSection(k >> 4 << 4, !this.world.worldProvider.o());
            this.sections[k >> 4] = chunkSection;
            chunksection = chunkSection;
            this.initLighting();
        }
        this.q = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            if (!this.world.worldProvider.o()) {
                chunksection.a(j, k & 0xF, l, i);
            }
        } else if (enumskyblock == EnumSkyBlock.BLOCK) {
            chunksection.b(j, k & 0xF, l, i);
        }
    }

    public int a(BlockPosition blockposition, int i) {
        int j = blockposition.getX() & 0xF;
        int k = blockposition.getY();
        int l = blockposition.getZ() & 0xF;
        ChunkSection chunksection = this.sections[k >> 4];
        if (chunksection == null) {
            return !this.world.worldProvider.o() && i < EnumSkyBlock.SKY.c ? EnumSkyBlock.SKY.c - i : 0;
        }
        int i1 = this.world.worldProvider.o() ? 0 : chunksection.d(j, k & 0xF, l);
        int j1 = chunksection.e(j, k & 0xF, l);
        if (j1 > (i1 -= i)) {
            i1 = j1;
        }
        return i1;
    }

    public void a(Entity entity) {
        EntityInsentient entityinsentient;
        int k;
        this.r = true;
        int i = MathHelper.floor(entity.locX / 16.0);
        int j = MathHelper.floor(entity.locZ / 16.0);
        if (i != this.locX || j != this.locZ) {
            Bukkit.getLogger().warning("Wrong location for " + entity + " in world '" + this.world.getWorld().getName() + "'!");
            Bukkit.getLogger().warning("Entity is at " + entity.locX + "," + entity.locZ + " (chunk " + i + "," + j + ") but was stored in chunk " + this.locX + "," + this.locZ);
            entity.die();
        }
        if ((k = MathHelper.floor(entity.locY / 16.0)) < 0) {
            k = 0;
        }
        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }
        entity.ad = true;
        entity.ae = this.locX;
        entity.af = k;
        entity.ag = this.locZ;
        this.entitySlices[k].add(entity);
        if (entity instanceof EntityInsentient && (entityinsentient = (EntityInsentient)entity).isTypeNotPersistent() && entityinsentient.isPersistent()) {
            return;
        }
        EnumCreatureType[] enumCreatureTypeArray = EnumCreatureType.values();
        int n = enumCreatureTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumCreatureType creatureType = enumCreatureTypeArray[n2];
            if (creatureType.a().isAssignableFrom(entity.getClass())) {
                this.entityCount.adjustOrPutValue(creatureType.a(), 1, 1);
            }
            ++n2;
        }
    }

    public void b(Entity entity) {
        this.a(entity, entity.af);
    }

    public void a(Entity entity, int i) {
        EntityInsentient entityinsentient;
        if (i < 0) {
            i = 0;
        }
        if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }
        this.entitySlices[i].remove(entity);
        if (entity instanceof EntityInsentient && (entityinsentient = (EntityInsentient)entity).isTypeNotPersistent() && entityinsentient.isPersistent()) {
            return;
        }
        EnumCreatureType[] enumCreatureTypeArray = EnumCreatureType.values();
        int n = enumCreatureTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumCreatureType creatureType = enumCreatureTypeArray[n2];
            if (creatureType.a().isAssignableFrom(entity.getClass())) {
                this.entityCount.adjustValue(creatureType.a(), -1);
            }
            ++n2;
        }
    }

    public boolean d(BlockPosition blockposition) {
        int k;
        int i = blockposition.getX() & 0xF;
        int j = blockposition.getY();
        return j >= this.heightMap[(k = blockposition.getZ() & 0xF) << 4 | i];
    }

    private TileEntity i(BlockPosition blockposition) {
        Block block = this.getType(blockposition);
        return !block.isTileEntity() ? null : ((IContainer)((Object)block)).a(this.world, this.c(blockposition));
    }

    public TileEntity a(BlockPosition blockposition, EnumTileEntityState chunk_enumtileentitystate) {
        TileEntity tileentity = null;
        if (this.world.captureBlockStates) {
            tileentity = this.world.capturedTileEntities.get(blockposition);
        }
        if (tileentity == null) {
            tileentity = this.tileEntities.get(blockposition);
        }
        if (tileentity == null) {
            if (chunk_enumtileentitystate == EnumTileEntityState.IMMEDIATE) {
                tileentity = this.i(blockposition);
                this.world.setTileEntity(blockposition, tileentity);
            } else if (chunk_enumtileentitystate == EnumTileEntityState.QUEUED) {
                this.w.add(blockposition);
            }
        } else if (tileentity.x()) {
            this.tileEntities.remove(blockposition);
            return null;
        }
        return tileentity;
    }

    public void a(TileEntity tileentity) {
        this.a(tileentity.getPosition(), tileentity);
        if (this.h) {
            this.world.a(tileentity);
        }
    }

    public void a(BlockPosition blockposition, TileEntity tileentity) {
        tileentity.a(this.world);
        tileentity.a(blockposition);
        if (this.getType(blockposition) instanceof IContainer) {
            if (this.tileEntities.containsKey(blockposition)) {
                this.tileEntities.get(blockposition).y();
            }
            tileentity.D();
            this.tileEntities.put(blockposition, tileentity);
        } else {
            System.out.println("Attempted to place a tile entity (" + tileentity + ") at " + tileentity.position.getX() + "," + tileentity.position.getY() + "," + tileentity.position.getZ() + " (" + (Object)((Object)CraftMagicNumbers.getMaterial(this.getType(blockposition))) + ") where there was no entity tile!");
            System.out.println("Chunk coordinates: " + this.locX * 16 + "," + this.locZ * 16);
            new Exception().printStackTrace();
        }
    }

    public void e(BlockPosition blockposition) {
        TileEntity tileentity;
        if (this.h && (tileentity = this.tileEntities.remove(blockposition)) != null) {
            tileentity.y();
        }
    }

    public void addEntities() {
        this.h = true;
        this.world.a(this.tileEntities.values());
        int i = 0;
        while (i < this.entitySlices.length) {
            for (Entity entity : this.entitySlices[i]) {
                entity.ah();
            }
            this.world.b(this.entitySlices[i]);
            ++i;
        }
    }

    public void removeEntities() {
        this.h = false;
        for (TileEntity tileentity : this.tileEntities.values()) {
            if (tileentity instanceof IInventory) {
                for (HumanEntity h : Lists.newArrayList(((IInventory)((Object)tileentity)).getViewers())) {
                    if (!(h instanceof CraftHumanEntity)) continue;
                    ((CraftHumanEntity)h).getHandle().closeInventory();
                }
            }
            this.world.b(tileentity);
        }
        int i = 0;
        while (i < this.entitySlices.length) {
            ArrayList<Entity> newList = Lists.newArrayList(this.entitySlices[i]);
            Iterator iter = newList.iterator();
            while (iter.hasNext()) {
                Entity entity = (Entity)iter.next();
                if (entity instanceof IInventory) {
                    for (HumanEntity h : Lists.newArrayList(((IInventory)((Object)entity)).getViewers())) {
                        if (!(h instanceof CraftHumanEntity)) continue;
                        ((CraftHumanEntity)h).getHandle().closeInventory();
                    }
                }
                if (!(entity instanceof EntityPlayer)) continue;
                iter.remove();
            }
            this.world.c(newList);
            ++i;
        }
    }

    public void e() {
        this.q = true;
    }

    public void a(Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, Predicate<? super Entity> predicate) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0) / 16.0);
        int j = MathHelper.floor((axisalignedbb.e + 2.0) / 16.0);
        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);
        int k = i;
        while (k <= j) {
            if (!this.entitySlices[k].isEmpty()) {
                for (Entity entity1 : this.entitySlices[k]) {
                    Entity[] aentity;
                    if (!entity1.getBoundingBox().b(axisalignedbb) || entity1 == entity) continue;
                    if (predicate == null || predicate.apply(entity1)) {
                        list.add(entity1);
                    }
                    if ((aentity = entity1.aB()) == null) continue;
                    int l = 0;
                    while (l < aentity.length) {
                        entity1 = aentity[l];
                        if (entity1 != entity && entity1.getBoundingBox().b(axisalignedbb) && (predicate == null || predicate.apply(entity1))) {
                            list.add(entity1);
                        }
                        ++l;
                    }
                }
            }
            ++k;
        }
    }

    public <T extends Entity> void a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0) / 16.0);
        int j = MathHelper.floor((axisalignedbb.e + 2.0) / 16.0);
        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);
        int k = i;
        while (k <= j) {
            for (Entity entity : this.entitySlices[k]) {
                if (!oclass.isInstance(entity) || !entity.getBoundingBox().b(axisalignedbb) || predicate != null && !predicate.apply(entity)) continue;
                list.add(entity);
            }
            ++k;
        }
    }

    public boolean a(boolean flag) {
        if (flag ? this.r && this.world.getTime() != this.lastSaved || this.q : this.r && this.world.getTime() >= this.lastSaved + (long)(MinecraftServer.getServer().autosavePeriod * 4)) {
            return true;
        }
        return this.q;
    }

    public Random a(long i) {
        return new Random(this.world.getSeed() + (long)(this.locX * this.locX * 4987142) + (long)(this.locX * 5947611) + (long)(this.locZ * this.locZ) * 4392871L + (long)(this.locZ * 389711) ^ i);
    }

    public boolean isEmpty() {
        return false;
    }

    public void loadNearby(IChunkProvider ichunkprovider, IChunkProvider ichunkprovider1, int i, int j) {
        Chunk chunk;
        this.world.timings.syncChunkLoadPostTimer.startTiming();
        boolean flag = ichunkprovider.isChunkLoaded(i, j - 1);
        boolean flag1 = ichunkprovider.isChunkLoaded(i + 1, j);
        boolean flag2 = ichunkprovider.isChunkLoaded(i, j + 1);
        boolean flag3 = ichunkprovider.isChunkLoaded(i - 1, j);
        boolean flag4 = ichunkprovider.isChunkLoaded(i - 1, j - 1);
        boolean flag5 = ichunkprovider.isChunkLoaded(i + 1, j + 1);
        boolean flag6 = ichunkprovider.isChunkLoaded(i - 1, j + 1);
        boolean flag7 = ichunkprovider.isChunkLoaded(i + 1, j - 1);
        if (flag1 && flag2 && flag5) {
            if (!this.done) {
                ichunkprovider.getChunkAt(ichunkprovider1, i, j);
            } else {
                ichunkprovider.a(ichunkprovider1, this, i, j);
            }
        }
        if (flag3 && flag2 && flag6) {
            chunk = ichunkprovider.getOrCreateChunk(i - 1, j);
            if (!chunk.done) {
                ichunkprovider.getChunkAt(ichunkprovider1, i - 1, j);
            } else {
                ichunkprovider.a(ichunkprovider1, chunk, i - 1, j);
            }
        }
        if (flag && flag1 && flag7) {
            chunk = ichunkprovider.getOrCreateChunk(i, j - 1);
            if (!chunk.done) {
                ichunkprovider.getChunkAt(ichunkprovider1, i, j - 1);
            } else {
                ichunkprovider.a(ichunkprovider1, chunk, i, j - 1);
            }
        }
        if (flag4 && flag && flag3) {
            chunk = ichunkprovider.getOrCreateChunk(i - 1, j - 1);
            if (!chunk.done) {
                ichunkprovider.getChunkAt(ichunkprovider1, i - 1, j - 1);
            } else {
                ichunkprovider.a(ichunkprovider1, chunk, i - 1, j - 1);
            }
        }
        this.world.timings.syncChunkLoadPostTimer.stopTiming();
    }

    public BlockPosition h(BlockPosition blockposition) {
        int i = blockposition.getX() & 0xF;
        int j = blockposition.getZ() & 0xF;
        int k = i | j << 4;
        BlockPosition blockposition1 = new BlockPosition(blockposition.getX(), this.f[k], blockposition.getZ());
        if (blockposition1.getY() == -999) {
            int l = this.g() + 15;
            blockposition1 = new BlockPosition(blockposition.getX(), l, blockposition.getZ());
            int i1 = -1;
            while (blockposition1.getY() > 0 && i1 == -1) {
                Block block = this.getType(blockposition1);
                Material material = block.getMaterial();
                if (!material.isSolid() && !material.isLiquid()) {
                    blockposition1 = blockposition1.down();
                    continue;
                }
                i1 = blockposition1.getY() + 1;
            }
            this.f[k] = i1;
        }
        return new BlockPosition(blockposition.getX(), this.f[k], blockposition.getZ());
    }

    public void b(boolean flag) {
        if (this.k && !this.world.worldProvider.o() && !flag) {
            this.h(this.world.isClientSide);
        }
        this.p = true;
        if (!this.lit && this.done && this.world.spigotConfig.randomLightUpdates) {
            this.n();
        }
        while (!this.w.isEmpty()) {
            BlockPosition blockposition = this.w.poll();
            if (this.a(blockposition, EnumTileEntityState.CHECK) != null || !this.getType(blockposition).isTileEntity()) continue;
            TileEntity tileentity = this.i(blockposition);
            this.world.setTileEntity(blockposition, tileentity);
            this.world.b(blockposition, blockposition);
        }
    }

    public boolean isReady() {
        return true;
    }

    public ChunkCoordIntPair j() {
        return new ChunkCoordIntPair(this.locX, this.locZ);
    }

    public boolean c(int i, int j) {
        if (i < 0) {
            i = 0;
        }
        if (j >= 256) {
            j = 255;
        }
        int k = i;
        while (k <= j) {
            ChunkSection chunksection = this.sections[k >> 4];
            if (chunksection != null && !chunksection.a()) {
                return false;
            }
            k += 16;
        }
        return true;
    }

    public void a(ChunkSection[] achunksection) {
        if (this.sections.length != achunksection.length) {
            c.warn("Could not set level chunk sections, array length is " + achunksection.length + " instead of " + this.sections.length);
        } else {
            int i = 0;
            while (i < this.sections.length) {
                this.sections[i] = achunksection[i];
                ++i;
            }
        }
    }

    public BiomeBase getBiome(BlockPosition blockposition, WorldChunkManager worldchunkmanager) {
        BiomeBase biomebase;
        int i = blockposition.getX() & 0xF;
        int j = blockposition.getZ() & 0xF;
        int k = this.e[j << 4 | i] & 0xFF;
        if (k == 255) {
            biomebase = worldchunkmanager.getBiome(blockposition, BiomeBase.PLAINS);
            k = biomebase.id;
            this.e[j << 4 | i] = (byte)(k & 0xFF);
        }
        return (biomebase = BiomeBase.getBiome(k)) == null ? BiomeBase.PLAINS : biomebase;
    }

    public byte[] getBiomeIndex() {
        return this.e;
    }

    public void a(byte[] abyte) {
        if (this.e.length != abyte.length) {
            c.warn("Could not set level chunk biomes, array length is " + abyte.length + " instead of " + this.e.length);
        } else {
            int i = 0;
            while (i < this.e.length) {
                this.e[i] = abyte[i];
                ++i;
            }
        }
    }

    public void l() {
        this.v = 0;
    }

    public void m() {
        BlockPosition blockposition = new BlockPosition(this.locX << 4, 0, this.locZ << 4);
        int i = 0;
        while (i < 8) {
            if (this.v >= 4096) {
                return;
            }
            int j = this.v % 16;
            int k = this.v / 16 % 16;
            int l = this.v / 256;
            ++this.v;
            int i1 = 0;
            while (i1 < 16) {
                boolean flag;
                BlockPosition blockposition1 = blockposition.a(k, (j << 4) + i1, l);
                boolean bl = flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;
                if (this.sections[j] == null && flag || this.sections[j] != null && this.sections[j].b(k, i1, l).getMaterial() == Material.AIR) {
                    EnumDirection[] aenumdirection = EnumDirection.values();
                    int j1 = aenumdirection.length;
                    int k1 = 0;
                    while (k1 < j1) {
                        EnumDirection enumdirection = aenumdirection[k1];
                        BlockPosition blockposition2 = blockposition1.shift(enumdirection);
                        if (this.world.getType(blockposition2).getBlock().r() > 0) {
                            this.world.x(blockposition2);
                        }
                        ++k1;
                    }
                    this.world.x(blockposition1);
                }
                ++i1;
            }
            ++i;
        }
    }

    public void n() {
        this.done = true;
        this.lit = true;
        BlockPosition blockposition = new BlockPosition(this.locX << 4, 0, this.locZ << 4);
        if (!this.world.worldProvider.o()) {
            if (this.world.areChunksLoadedBetween(blockposition.a(-1, 0, -1), blockposition.a(16, this.world.F(), 16))) {
                int i = 0;
                block0: while (i < 16) {
                    int j = 0;
                    while (j < 16) {
                        if (!this.e(i, j)) {
                            this.lit = false;
                            break block0;
                        }
                        ++j;
                    }
                    ++i;
                }
                if (this.lit) {
                    for (EnumDirection enumdirection : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
                        int k = enumdirection.c() == EnumDirection.EnumAxisDirection.POSITIVE ? 16 : 1;
                        this.world.getChunkAtWorldCoords(blockposition.shift(enumdirection, k)).a(enumdirection.opposite());
                    }
                    this.y();
                }
            } else {
                this.lit = false;
            }
        }
    }

    private void y() {
        int i = 0;
        while (i < this.g.length) {
            this.g[i] = true;
            ++i;
        }
        this.h(false);
    }

    private void a(EnumDirection enumdirection) {
        block4: {
            block7: {
                block6: {
                    block5: {
                        if (!this.done) break block4;
                        if (enumdirection != EnumDirection.EAST) break block5;
                        int i = 0;
                        while (i < 16) {
                            this.e(15, i);
                            ++i;
                        }
                        break block4;
                    }
                    if (enumdirection != EnumDirection.WEST) break block6;
                    int i = 0;
                    while (i < 16) {
                        this.e(0, i);
                        ++i;
                    }
                    break block4;
                }
                if (enumdirection != EnumDirection.SOUTH) break block7;
                int i = 0;
                while (i < 16) {
                    this.e(i, 15);
                    ++i;
                }
                break block4;
            }
            if (enumdirection != EnumDirection.NORTH) break block4;
            int i = 0;
            while (i < 16) {
                this.e(i, 0);
                ++i;
            }
        }
    }

    private boolean e(int i, int j) {
        int k = this.g();
        boolean flag = false;
        boolean flag1 = false;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition((this.locX << 4) + i, 0, (this.locZ << 4) + j);
        int l = k + 16 - 1;
        while (l > this.world.F() || l > 0 && !flag1) {
            blockposition_mutableblockposition.c(blockposition_mutableblockposition.getX(), l, blockposition_mutableblockposition.getZ());
            int i1 = this.b(blockposition_mutableblockposition);
            if (i1 == 255 && blockposition_mutableblockposition.getY() < this.world.F()) {
                flag1 = true;
            }
            if (!flag && i1 > 0) {
                flag = true;
            } else if (flag && i1 == 0 && !this.world.x(blockposition_mutableblockposition)) {
                return false;
            }
            --l;
        }
        l = blockposition_mutableblockposition.getY();
        while (l > 0) {
            blockposition_mutableblockposition.c(blockposition_mutableblockposition.getX(), l, blockposition_mutableblockposition.getZ());
            if (this.getType(blockposition_mutableblockposition).r() > 0) {
                this.world.x(blockposition_mutableblockposition);
            }
            --l;
        }
        return true;
    }

    public boolean o() {
        return this.h;
    }

    public World getWorld() {
        return this.world;
    }

    public int[] q() {
        return this.heightMap;
    }

    public void a(int[] aint) {
        if (this.heightMap.length != aint.length) {
            c.warn("Could not set level chunk heightmap, array length is " + aint.length + " instead of " + this.heightMap.length);
        } else {
            int i = 0;
            while (i < this.heightMap.length) {
                this.heightMap[i] = aint[i];
                ++i;
            }
        }
    }

    public Map<BlockPosition, TileEntity> getTileEntities() {
        return this.tileEntities;
    }

    public List<Entity>[] getEntitySlices() {
        return this.entitySlices;
    }

    public boolean isDone() {
        return this.done;
    }

    public void d(boolean flag) {
        this.done = flag;
    }

    public boolean u() {
        return this.lit;
    }

    public void e(boolean flag) {
        this.lit = flag;
    }

    public void f(boolean flag) {
        this.q = flag;
    }

    public void g(boolean flag) {
        this.r = flag;
    }

    public void setLastSaved(long i) {
        this.lastSaved = i;
    }

    public int v() {
        return this.t;
    }

    public long w() {
        return this.u;
    }

    public void c(long i) {
        this.u = i;
    }

    public static enum EnumTileEntityState {
        IMMEDIATE,
        QUEUED,
        CHECK;

    }
}

