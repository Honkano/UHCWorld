/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.VectorFace;
import com.sk89q.worldedit.WorldVector;

@Deprecated
public class WorldVectorFace
extends WorldVector {
    private VectorFace face;

    public WorldVectorFace(LocalWorld world, double x, double y, double z, VectorFace face) {
        super(world, x, y, z);
        this.face = face;
    }

    public WorldVectorFace(LocalWorld world, int x, int y, int z, VectorFace face) {
        super(world, x, y, z);
        this.face = face;
    }

    public WorldVectorFace(LocalWorld world, float x, float y, float z, VectorFace face) {
        super(world, x, y, z);
        this.face = face;
    }

    public WorldVectorFace(LocalWorld world, Vector pt, VectorFace face) {
        super(world, pt);
        this.face = face;
    }

    public WorldVectorFace(LocalWorld world, VectorFace face) {
        super(world);
        this.face = face;
    }

    public VectorFace getFace() {
        return this.face;
    }

    public WorldVector getFaceVector() {
        return new WorldVector(this.getWorld(), this.getBlockX() - this.face.getModX(), this.getBlockY() - this.face.getModY(), this.getBlockZ() - this.face.getModZ());
    }

    public static WorldVectorFace getWorldVectorFace(LocalWorld world, Vector vector, Vector face) {
        if (vector == null || face == null) {
            return null;
        }
        int x1 = vector.getBlockX();
        int y1 = vector.getBlockY();
        int z1 = vector.getBlockZ();
        int modX = x1 - face.getBlockX();
        int modY = y1 - face.getBlockY();
        int modZ = z1 - face.getBlockZ();
        modX = modX > 0 ? 1 : (modX < 0 ? -1 : 0);
        modY = modY > 0 ? 1 : (modY < 0 ? -1 : 0);
        modZ = modZ > 0 ? 1 : (modZ < 0 ? -1 : 0);
        return new WorldVectorFace(world, x1, y1, z1, VectorFace.fromMods(modX, modY, modZ));
    }
}

