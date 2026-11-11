/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.forge;

import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.forge.ForgePlatform;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.forge.ThreadSafeCache;
import com.sk89q.worldedit.forge.WECUIPacketHandler;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ForgePlayer
extends AbstractPlayerActor {
    private final ForgePlatform platform;
    private final EntityPlayerMP player;

    protected ForgePlayer(ForgePlatform platform, EntityPlayerMP player) {
        this.platform = platform;
        this.player = player;
        ThreadSafeCache.getInstance().getOnlineIds().add(this.getUniqueId());
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueID();
    }

    @Override
    public int getItemInHand() {
        ItemStack is = this.player.getCurrentEquippedItem();
        return is == null ? 0 : Item.getIdFromItem((Item)is.getItem());
    }

    @Override
    public String getName() {
        return this.player.getCommandSenderName();
    }

    @Override
    public BaseEntity getState() {
        throw new UnsupportedOperationException("Cannot create a state from this object");
    }

    @Override
    public Location getLocation() {
        Vector position = new Vector(this.player.posX, this.player.posY, this.player.posZ);
        return new Location((Extent)ForgeWorldEdit.inst.getWorld(this.player.worldObj), position, this.player.cameraYaw, this.player.cameraPitch);
    }

    @Override
    public WorldVector getPosition() {
        return new WorldVector((LocalWorld)LocalWorldAdapter.adapt(ForgeWorldEdit.inst.getWorld(this.player.worldObj)), this.player.posX, this.player.posY, this.player.posZ);
    }

    @Override
    public World getWorld() {
        return ForgeWorldEdit.inst.getWorld(this.player.worldObj);
    }

    @Override
    public double getPitch() {
        return this.player.rotationPitch;
    }

    @Override
    public double getYaw() {
        return this.player.rotationYaw;
    }

    @Override
    public void giveItem(int type, int amt) {
        this.player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById((int)type), amt, 0));
    }

    @Override
    public void dispatchCUIEvent(CUIEvent event) {
        String[] params = event.getParameters();
        String send = event.getTypeId();
        if (params.length > 0) {
            send = send + "|" + StringUtil.joinString(params, "|");
        }
        S3FPacketCustomPayload packet = new S3FPacketCustomPayload("WECUI", send.getBytes(WECUIPacketHandler.UTF_8_CHARSET));
        this.player.playerNetServerHandler.sendPacket((Packet)packet);
    }

    @Override
    public void printRaw(String msg) {
        for (String part : msg.split("\n")) {
            this.player.addChatMessage((IChatComponent)new ChatComponentText(part));
        }
    }

    @Override
    public void printDebug(String msg) {
        for (String part : msg.split("\n")) {
            this.player.addChatMessage((IChatComponent)new ChatComponentText("\u00a77" + part));
        }
    }

    @Override
    public void print(String msg) {
        for (String part : msg.split("\n")) {
            this.player.addChatMessage((IChatComponent)new ChatComponentText("\u00a7d" + part));
        }
    }

    @Override
    public void printError(String msg) {
        for (String part : msg.split("\n")) {
            this.player.addChatMessage((IChatComponent)new ChatComponentText("\u00a7c" + part));
        }
    }

    @Override
    public void setPosition(Vector pos, float pitch, float yaw) {
        this.player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), pitch, yaw);
    }

    @Override
    public String[] getGroups() {
        return new String[0];
    }

    @Override
    public BlockBag getInventoryBlockBag() {
        return null;
    }

    @Override
    public boolean hasPermission(String perm) {
        return ForgeWorldEdit.inst.getPermissionsProvider().hasPermission(this.player, perm);
    }

    @Override
    @Nullable
    public <T> T getFacet(Class<? extends T> cls) {
        return null;
    }

    @Override
    public SessionKey getSessionKey() {
        return new SessionKeyImpl(this.player.getUniqueID(), this.player.getCommandSenderName());
    }

    private static class SessionKeyImpl
    implements SessionKey {
        private final UUID uuid;
        private final String name;

        private SessionKeyImpl(UUID uuid, String name) {
            this.uuid = uuid;
            this.name = name;
        }

        @Override
        public UUID getUniqueId() {
            return this.uuid;
        }

        @Override
        @Nullable
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isActive() {
            return ThreadSafeCache.getInstance().getOnlineIds().contains(this.uuid);
        }

        @Override
        public boolean isPersistent() {
            return true;
        }
    }
}

