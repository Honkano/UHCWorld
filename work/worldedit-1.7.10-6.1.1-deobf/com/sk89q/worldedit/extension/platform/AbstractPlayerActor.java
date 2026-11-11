/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.NotABlockException;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.WorldVectorFace;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.util.TargetBlock;
import com.sk89q.worldedit.util.auth.AuthorizationException;
import java.io.File;

public abstract class AbstractPlayerActor
implements Actor,
Player,
Cloneable {
    @Override
    public final Extent getExtent() {
        return this.getWorld();
    }

    private static PlayerDirection getDirection(double rot) {
        if (0.0 <= rot && rot < 22.5) {
            return PlayerDirection.SOUTH;
        }
        if (22.5 <= rot && rot < 67.5) {
            return PlayerDirection.SOUTH_WEST;
        }
        if (67.5 <= rot && rot < 112.5) {
            return PlayerDirection.WEST;
        }
        if (112.5 <= rot && rot < 157.5) {
            return PlayerDirection.NORTH_WEST;
        }
        if (157.5 <= rot && rot < 202.5) {
            return PlayerDirection.NORTH;
        }
        if (202.5 <= rot && rot < 247.5) {
            return PlayerDirection.NORTH_EAST;
        }
        if (247.5 <= rot && rot < 292.5) {
            return PlayerDirection.EAST;
        }
        if (292.5 <= rot && rot < 337.5) {
            return PlayerDirection.SOUTH_EAST;
        }
        if (337.5 <= rot && rot < 360.0) {
            return PlayerDirection.SOUTH;
        }
        return null;
    }

    @Override
    public boolean isHoldingPickAxe() {
        int item = this.getItemInHand();
        return item == 257 || item == 270 || item == 274 || item == 278 || item == 285;
    }

    @Override
    public void findFreePosition(WorldVector searchPos) {
        int y;
        LocalWorld world = searchPos.getWorld();
        int x = searchPos.getBlockX();
        int origY = y = Math.max(0, searchPos.getBlockY());
        int z = searchPos.getBlockZ();
        int free = 0;
        while (y <= world.getMaxY() + 2) {
            free = BlockType.canPassThrough(world.getBlock(new Vector(x, y, z))) ? (int)((byte)(free + 1)) : 0;
            if (free == 2) {
                if (y - 1 != origY) {
                    Vector pos = new Vector(x, y - 2, z);
                    int id = world.getBlockType(pos);
                    int data = world.getBlockData(pos);
                    this.setPosition(new Vector((double)x + 0.5, (double)(y - 2) + BlockType.centralTopLimit(id, data), (double)z + 0.5));
                }
                return;
            }
            ++y;
        }
    }

    @Override
    public void setOnGround(WorldVector searchPos) {
        LocalWorld world = searchPos.getWorld();
        int x = searchPos.getBlockX();
        int z = searchPos.getBlockZ();
        for (int y = Math.max(0, searchPos.getBlockY()); y >= 0; --y) {
            int data;
            Vector pos = new Vector(x, y, z);
            int id = world.getBlockType(pos);
            if (BlockType.canPassThrough(id, data = world.getBlockData(pos))) continue;
            this.setPosition(new Vector((double)x + 0.5, (double)y + BlockType.centralTopLimit(id, data), (double)z + 0.5));
            return;
        }
    }

    @Override
    public void findFreePosition() {
        this.findFreePosition(this.getBlockIn());
    }

    @Override
    public boolean ascendLevel() {
        WorldVector pos = this.getBlockIn();
        int x = pos.getBlockX();
        int z = pos.getBlockZ();
        LocalWorld world = pos.getWorld();
        int free = 0;
        int spots = 0;
        for (int y = Math.max(0, pos.getBlockY()); y <= world.getMaxY() + 2; ++y) {
            free = BlockType.canPassThrough(world.getBlock(new Vector(x, y, z))) ? (int)((byte)(free + 1)) : 0;
            if (free != 2 || (spots = (int)((byte)(spots + 1))) != 2) continue;
            Vector platform = new Vector(x, y - 2, z);
            BaseBlock block = world.getBlock(platform);
            int type = block.getId();
            if (type == 10 || type == 11) {
                return false;
            }
            this.setPosition(platform.add(0.5, BlockType.centralTopLimit(block), 0.5));
            return true;
        }
        return false;
    }

    @Override
    public boolean descendLevel() {
        WorldVector pos = this.getBlockIn();
        int x = pos.getBlockX();
        int z = pos.getBlockZ();
        LocalWorld world = pos.getWorld();
        int free = 0;
        for (int y = Math.max(0, pos.getBlockY() - 1); y >= 1; --y) {
            free = BlockType.canPassThrough(world.getBlock(new Vector(x, y, z))) ? (int)((byte)(free + 1)) : 0;
            if (free != 2) continue;
            while (y >= 0) {
                Vector platform = new Vector(x, y, z);
                BaseBlock block = world.getBlock(platform);
                int type = block.getId();
                if (type != 0 && type != 10 && type != 11) {
                    this.setPosition(platform.add(0.5, BlockType.centralTopLimit(block), 0.5));
                    return true;
                }
                --y;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean ascendToCeiling(int clearance) {
        return this.ascendToCeiling(clearance, true);
    }

    @Override
    public boolean ascendToCeiling(int clearance, boolean alwaysGlass) {
        int y;
        WorldVector pos = this.getBlockIn();
        int x = pos.getBlockX();
        int initialY = Math.max(0, pos.getBlockY());
        int z = pos.getBlockZ();
        LocalWorld world = this.getPosition().getWorld();
        if (world.getBlockType(new Vector(x, y, z)) != 0) {
            return false;
        }
        for (y = Math.max(0, pos.getBlockY() + 2); y <= world.getMaxY(); ++y) {
            if (BlockType.canPassThrough(world.getBlock(new Vector(x, y, z)))) continue;
            int platformY = Math.max(initialY, y - 3 - clearance);
            this.floatAt(x, platformY + 1, z, alwaysGlass);
            return true;
        }
        return false;
    }

    @Override
    public boolean ascendUpwards(int distance) {
        return this.ascendUpwards(distance, true);
    }

    @Override
    public boolean ascendUpwards(int distance, boolean alwaysGlass) {
        WorldVector pos = this.getBlockIn();
        int x = pos.getBlockX();
        int initialY = Math.max(0, pos.getBlockY());
        int z = pos.getBlockZ();
        int maxY = Math.min(this.getWorld().getMaxY() + 1, initialY + distance);
        LocalWorld world = this.getPosition().getWorld();
        for (int y = Math.max(0, pos.getBlockY() + 1); y <= world.getMaxY() + 2 && BlockType.canPassThrough(world.getBlock(new Vector(x, y, z))) && y <= maxY + 1; ++y) {
            if (y != maxY + 1) continue;
            this.floatAt(x, y - 1, z, alwaysGlass);
            return true;
        }
        return false;
    }

    @Override
    public void floatAt(int x, int y, int z, boolean alwaysGlass) {
        this.getPosition().getWorld().setBlockType(new Vector(x, y - 1, z), 20);
        this.setPosition(new Vector((double)x + 0.5, (double)y, (double)z + 0.5));
    }

    @Override
    public WorldVector getBlockIn() {
        WorldVector pos = this.getPosition();
        return WorldVector.toBlockPoint(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public WorldVector getBlockOn() {
        WorldVector pos = this.getPosition();
        return WorldVector.toBlockPoint(pos.getWorld(), pos.getX(), pos.getY() - 1.0, pos.getZ());
    }

    @Override
    public WorldVector getBlockTrace(int range, boolean useLastBlock) {
        TargetBlock tb = new TargetBlock(this, range, 0.2);
        return useLastBlock ? tb.getAnyTargetBlock() : tb.getTargetBlock();
    }

    @Override
    public WorldVectorFace getBlockTraceFace(int range, boolean useLastBlock) {
        TargetBlock tb = new TargetBlock(this, range, 0.2);
        return useLastBlock ? tb.getAnyTargetBlockFace() : tb.getTargetBlockFace();
    }

    @Override
    public WorldVector getBlockTrace(int range) {
        return this.getBlockTrace(range, false);
    }

    @Override
    public WorldVector getSolidBlockTrace(int range) {
        TargetBlock tb = new TargetBlock(this, range, 0.2);
        return tb.getSolidTargetBlock();
    }

    @Override
    public PlayerDirection getCardinalDirection() {
        return this.getCardinalDirection(0);
    }

    @Override
    public PlayerDirection getCardinalDirection(int yawOffset) {
        if (this.getPitch() > 67.5) {
            return PlayerDirection.DOWN;
        }
        if (this.getPitch() < -67.5) {
            return PlayerDirection.UP;
        }
        double rot = (this.getYaw() + (double)yawOffset) % 360.0;
        if (rot < 0.0) {
            rot += 360.0;
        }
        return AbstractPlayerActor.getDirection(rot);
    }

    @Override
    public BaseBlock getBlockInHand() throws WorldEditException {
        int typeId = this.getItemInHand();
        if (!this.getWorld().isValidBlockType(typeId)) {
            throw new NotABlockException(typeId);
        }
        return new BaseBlock(typeId);
    }

    @Override
    public boolean passThroughForwardWall(int range) {
        BlockWorldVector block;
        int searchDist = 0;
        TargetBlock hitBlox = new TargetBlock(this, range, 0.2);
        LocalWorld world = this.getPosition().getWorld();
        boolean firstBlock = true;
        int freeToFind = 2;
        boolean inFree = false;
        while ((block = hitBlox.getNextBlock()) != null) {
            boolean free = BlockType.canPassThrough(world.getBlock(block));
            if (firstBlock) {
                firstBlock = false;
                if (!free) {
                    --freeToFind;
                    continue;
                }
            }
            if (++searchDist > 20) {
                return false;
            }
            if (inFree != free && free) {
                --freeToFind;
            }
            if (freeToFind == 0) {
                this.setOnGround(block);
                return true;
            }
            inFree = free;
        }
        return false;
    }

    @Override
    public void setPosition(Vector pos) {
        this.setPosition(pos, (float)this.getPitch(), (float)this.getYaw());
    }

    @Override
    public File openFileOpenDialog(String[] extensions) {
        this.printError("File dialogs are not supported in your environment.");
        return null;
    }

    @Override
    public File openFileSaveDialog(String[] extensions) {
        this.printError("File dialogs are not supported in your environment.");
        return null;
    }

    @Override
    public boolean canDestroyBedrock() {
        return this.hasPermission("worldedit.override.bedrock");
    }

    @Override
    public void dispatchCUIEvent(CUIEvent event) {
    }

    public boolean equals(Object other) {
        if (!(other instanceof LocalPlayer)) {
            return false;
        }
        LocalPlayer other2 = (LocalPlayer)other;
        return other2.getName().equals(this.getName());
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public void checkPermission(String permission) throws AuthorizationException {
        if (!this.hasPermission(permission)) {
            throw new AuthorizationException();
        }
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean hasCreativeMode() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Not supported");
    }

    @Override
    public boolean remove() {
        return false;
    }
}

