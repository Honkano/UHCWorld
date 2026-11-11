/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.google.common.base.Preconditions;
import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.blocks.BaseItemStack;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public final class BlockType
extends Enum<BlockType> {
    public static final /* enum */ BlockType AIR;
    public static final /* enum */ BlockType STONE;
    public static final /* enum */ BlockType GRASS;
    public static final /* enum */ BlockType DIRT;
    public static final /* enum */ BlockType COBBLESTONE;
    public static final /* enum */ BlockType WOOD;
    public static final /* enum */ BlockType SAPLING;
    public static final /* enum */ BlockType BEDROCK;
    public static final /* enum */ BlockType WATER;
    public static final /* enum */ BlockType STATIONARY_WATER;
    public static final /* enum */ BlockType LAVA;
    public static final /* enum */ BlockType STATIONARY_LAVA;
    public static final /* enum */ BlockType SAND;
    public static final /* enum */ BlockType GRAVEL;
    public static final /* enum */ BlockType GOLD_ORE;
    public static final /* enum */ BlockType IRON_ORE;
    public static final /* enum */ BlockType COAL_ORE;
    public static final /* enum */ BlockType LOG;
    public static final /* enum */ BlockType LEAVES;
    public static final /* enum */ BlockType SPONGE;
    public static final /* enum */ BlockType GLASS;
    public static final /* enum */ BlockType LAPIS_LAZULI_ORE;
    public static final /* enum */ BlockType LAPIS_LAZULI;
    public static final /* enum */ BlockType DISPENSER;
    public static final /* enum */ BlockType SANDSTONE;
    public static final /* enum */ BlockType NOTE_BLOCK;
    public static final /* enum */ BlockType BED;
    public static final /* enum */ BlockType POWERED_RAIL;
    public static final /* enum */ BlockType DETECTOR_RAIL;
    public static final /* enum */ BlockType PISTON_STICKY_BASE;
    public static final /* enum */ BlockType WEB;
    public static final /* enum */ BlockType LONG_GRASS;
    public static final /* enum */ BlockType DEAD_BUSH;
    public static final /* enum */ BlockType PISTON_BASE;
    public static final /* enum */ BlockType PISTON_EXTENSION;
    public static final /* enum */ BlockType CLOTH;
    public static final /* enum */ BlockType PISTON_MOVING_PIECE;
    public static final /* enum */ BlockType YELLOW_FLOWER;
    public static final /* enum */ BlockType RED_FLOWER;
    public static final /* enum */ BlockType BROWN_MUSHROOM;
    public static final /* enum */ BlockType RED_MUSHROOM;
    public static final /* enum */ BlockType GOLD_BLOCK;
    public static final /* enum */ BlockType IRON_BLOCK;
    public static final /* enum */ BlockType DOUBLE_STEP;
    public static final /* enum */ BlockType STEP;
    public static final /* enum */ BlockType BRICK;
    public static final /* enum */ BlockType TNT;
    public static final /* enum */ BlockType BOOKCASE;
    public static final /* enum */ BlockType MOSSY_COBBLESTONE;
    public static final /* enum */ BlockType OBSIDIAN;
    public static final /* enum */ BlockType TORCH;
    public static final /* enum */ BlockType FIRE;
    public static final /* enum */ BlockType MOB_SPAWNER;
    public static final /* enum */ BlockType WOODEN_STAIRS;
    public static final /* enum */ BlockType CHEST;
    public static final /* enum */ BlockType REDSTONE_WIRE;
    public static final /* enum */ BlockType DIAMOND_ORE;
    public static final /* enum */ BlockType DIAMOND_BLOCK;
    public static final /* enum */ BlockType WORKBENCH;
    public static final /* enum */ BlockType CROPS;
    public static final /* enum */ BlockType SOIL;
    public static final /* enum */ BlockType FURNACE;
    public static final /* enum */ BlockType BURNING_FURNACE;
    public static final /* enum */ BlockType SIGN_POST;
    public static final /* enum */ BlockType WOODEN_DOOR;
    public static final /* enum */ BlockType LADDER;
    public static final /* enum */ BlockType MINECART_TRACKS;
    public static final /* enum */ BlockType COBBLESTONE_STAIRS;
    public static final /* enum */ BlockType WALL_SIGN;
    public static final /* enum */ BlockType LEVER;
    public static final /* enum */ BlockType STONE_PRESSURE_PLATE;
    public static final /* enum */ BlockType IRON_DOOR;
    public static final /* enum */ BlockType WOODEN_PRESSURE_PLATE;
    public static final /* enum */ BlockType REDSTONE_ORE;
    public static final /* enum */ BlockType GLOWING_REDSTONE_ORE;
    public static final /* enum */ BlockType REDSTONE_TORCH_OFF;
    public static final /* enum */ BlockType REDSTONE_TORCH_ON;
    public static final /* enum */ BlockType STONE_BUTTON;
    public static final /* enum */ BlockType SNOW;
    public static final /* enum */ BlockType ICE;
    public static final /* enum */ BlockType SNOW_BLOCK;
    public static final /* enum */ BlockType CACTUS;
    public static final /* enum */ BlockType CLAY;
    public static final /* enum */ BlockType SUGAR_CANE;
    public static final /* enum */ BlockType JUKEBOX;
    public static final /* enum */ BlockType FENCE;
    public static final /* enum */ BlockType PUMPKIN;
    public static final /* enum */ BlockType NETHERRACK;
    public static final /* enum */ BlockType SOUL_SAND;
    public static final /* enum */ BlockType GLOWSTONE;
    public static final /* enum */ BlockType PORTAL;
    public static final /* enum */ BlockType JACK_O_LANTERN;
    public static final /* enum */ BlockType CAKE;
    public static final /* enum */ BlockType REDSTONE_REPEATER_OFF;
    public static final /* enum */ BlockType REDSTONE_REPEATER_ON;
    @Deprecated
    public static final /* enum */ BlockType LOCKED_CHEST;
    public static final /* enum */ BlockType STAINED_GLASS;
    public static final /* enum */ BlockType TRAP_DOOR;
    public static final /* enum */ BlockType SILVERFISH_BLOCK;
    public static final /* enum */ BlockType STONE_BRICK;
    public static final /* enum */ BlockType RED_MUSHROOM_CAP;
    public static final /* enum */ BlockType BROWN_MUSHROOM_CAP;
    public static final /* enum */ BlockType IRON_BARS;
    public static final /* enum */ BlockType GLASS_PANE;
    public static final /* enum */ BlockType MELON_BLOCK;
    public static final /* enum */ BlockType PUMPKIN_STEM;
    public static final /* enum */ BlockType MELON_STEM;
    public static final /* enum */ BlockType VINE;
    public static final /* enum */ BlockType FENCE_GATE;
    public static final /* enum */ BlockType BRICK_STAIRS;
    public static final /* enum */ BlockType STONE_BRICK_STAIRS;
    public static final /* enum */ BlockType MYCELIUM;
    public static final /* enum */ BlockType LILY_PAD;
    public static final /* enum */ BlockType NETHER_BRICK;
    public static final /* enum */ BlockType NETHER_BRICK_FENCE;
    public static final /* enum */ BlockType NETHER_BRICK_STAIRS;
    public static final /* enum */ BlockType NETHER_WART;
    public static final /* enum */ BlockType ENCHANTMENT_TABLE;
    public static final /* enum */ BlockType BREWING_STAND;
    public static final /* enum */ BlockType CAULDRON;
    public static final /* enum */ BlockType END_PORTAL;
    public static final /* enum */ BlockType END_PORTAL_FRAME;
    public static final /* enum */ BlockType END_STONE;
    public static final /* enum */ BlockType DRAGON_EGG;
    public static final /* enum */ BlockType REDSTONE_LAMP_OFF;
    public static final /* enum */ BlockType REDSTONE_LAMP_ON;
    public static final /* enum */ BlockType DOUBLE_WOODEN_STEP;
    public static final /* enum */ BlockType WOODEN_STEP;
    public static final /* enum */ BlockType COCOA_PLANT;
    public static final /* enum */ BlockType SANDSTONE_STAIRS;
    public static final /* enum */ BlockType EMERALD_ORE;
    public static final /* enum */ BlockType ENDER_CHEST;
    public static final /* enum */ BlockType TRIPWIRE_HOOK;
    public static final /* enum */ BlockType TRIPWIRE;
    public static final /* enum */ BlockType EMERALD_BLOCK;
    public static final /* enum */ BlockType SPRUCE_WOOD_STAIRS;
    public static final /* enum */ BlockType BIRCH_WOOD_STAIRS;
    public static final /* enum */ BlockType JUNGLE_WOOD_STAIRS;
    public static final /* enum */ BlockType COMMAND_BLOCK;
    public static final /* enum */ BlockType BEACON;
    public static final /* enum */ BlockType COBBLESTONE_WALL;
    public static final /* enum */ BlockType FLOWER_POT;
    public static final /* enum */ BlockType CARROTS;
    public static final /* enum */ BlockType POTATOES;
    public static final /* enum */ BlockType WOODEN_BUTTON;
    public static final /* enum */ BlockType HEAD;
    public static final /* enum */ BlockType ANVIL;
    public static final /* enum */ BlockType TRAPPED_CHEST;
    public static final /* enum */ BlockType PRESSURE_PLATE_LIGHT;
    public static final /* enum */ BlockType PRESSURE_PLATE_HEAVY;
    public static final /* enum */ BlockType COMPARATOR_OFF;
    public static final /* enum */ BlockType COMPARATOR_ON;
    public static final /* enum */ BlockType DAYLIGHT_SENSOR;
    public static final /* enum */ BlockType REDSTONE_BLOCK;
    public static final /* enum */ BlockType QUARTZ_ORE;
    public static final /* enum */ BlockType HOPPER;
    public static final /* enum */ BlockType QUARTZ_BLOCK;
    public static final /* enum */ BlockType QUARTZ_STAIRS;
    public static final /* enum */ BlockType ACTIVATOR_RAIL;
    public static final /* enum */ BlockType DROPPER;
    public static final /* enum */ BlockType STAINED_CLAY;
    public static final /* enum */ BlockType STAINED_GLASS_PANE;
    public static final /* enum */ BlockType LEAVES2;
    public static final /* enum */ BlockType LOG2;
    public static final /* enum */ BlockType ACACIA_STAIRS;
    public static final /* enum */ BlockType DARK_OAK_STAIRS;
    public static final /* enum */ BlockType SLIME;
    public static final /* enum */ BlockType BARRIER;
    public static final /* enum */ BlockType IRON_TRAP_DOOR;
    public static final /* enum */ BlockType PRISMARINE;
    public static final /* enum */ BlockType SEA_LANTERN;
    public static final /* enum */ BlockType HAY_BLOCK;
    public static final /* enum */ BlockType CARPET;
    public static final /* enum */ BlockType HARDENED_CLAY;
    public static final /* enum */ BlockType COAL_BLOCK;
    public static final /* enum */ BlockType PACKED_ICE;
    public static final /* enum */ BlockType DOUBLE_PLANT;
    public static final /* enum */ BlockType STANDING_BANNER;
    public static final /* enum */ BlockType WALL_BANNER;
    public static final /* enum */ BlockType DAYLIGHT_SENSOR_INVERTED;
    public static final /* enum */ BlockType RED_SANDSTONE;
    public static final /* enum */ BlockType RED_SANDSTONE_STAIRS;
    public static final /* enum */ BlockType DOUBLE_STEP2;
    public static final /* enum */ BlockType STEP2;
    public static final /* enum */ BlockType SPRUCE_FENCE_GATE;
    public static final /* enum */ BlockType BIRCH_FENCE_GATE;
    public static final /* enum */ BlockType JUNGLE_FENCE_GATE;
    public static final /* enum */ BlockType DARK_OAK_FENCE_GATE;
    public static final /* enum */ BlockType ACACIA_FENCE_GATE;
    public static final /* enum */ BlockType SPRUCE_FENCE;
    public static final /* enum */ BlockType BIRCH_FENCE;
    public static final /* enum */ BlockType JUNGLE_FENCE;
    public static final /* enum */ BlockType DARK_OAK_FENCE;
    public static final /* enum */ BlockType ACACIA_FENCE;
    public static final /* enum */ BlockType SPRUCE_DOOR;
    public static final /* enum */ BlockType BIRCH_DOOR;
    public static final /* enum */ BlockType JUNGLE_DOOR;
    public static final /* enum */ BlockType ACACIA_DOOR;
    public static final /* enum */ BlockType DARK_OAK_DOOR;
    private static final Map<Integer, BlockType> ids;
    private static final Map<String, BlockType> lookup;
    private final int id;
    private final String name;
    private final String[] lookupKeys;
    private static final Map<Integer, BaseBlock> itemBlockMapping;
    private static final Map<Integer, BaseBlock> dataItemBlockMapping;
    private static final Set<Integer> shouldPlaceLast;
    private static final Set<Integer> shouldPlaceFinal;
    private static final Set<Integer> canPassThrough;
    private static final Map<Integer, Double> centralTopLimit;
    private static final Set<Integer> usesData;
    private static final Set<Integer> isContainerBlock;
    private static final Set<Integer> isRedstoneBlock;
    private static final Set<Integer> canTransferRedstone;
    private static final Set<Integer> isRedstoneSource;
    private static final Set<Integer> isRailBlock;
    private static final Set<Integer> isNaturalTerrainBlock;
    private static final Set<Integer> emitsLight;
    private static final Set<Integer> isTranslucent;
    private static final Map<Integer, BaseItem> dataBlockBagItems;
    private static final Map<Integer, BaseItem> nonDataBlockBagItems;
    private static final BaseItem doNotDestroy;
    private static final Random random;
    private static final Map<Integer, PlayerDirection> dataAttachments;
    private static final Map<Integer, PlayerDirection> nonDataAttachments;
    private static final /* synthetic */ BlockType[] $VALUES;

    public static BlockType[] values() {
        return (BlockType[])$VALUES.clone();
    }

    public static BlockType valueOf(String name) {
        return Enum.valueOf(BlockType.class, name);
    }

    private BlockType(int id, String name, String lookupKey) {
        this.id = id;
        this.name = name;
        this.lookupKeys = new String[]{lookupKey};
    }

    private BlockType(int id, String name, String ... lookupKeys) {
        this.id = id;
        this.name = name;
        this.lookupKeys = lookupKeys;
    }

    @Nullable
    public static BlockType fromID(int id) {
        return ids.get(id);
    }

    @Nullable
    public static BlockType lookup(String name) {
        return BlockType.lookup(name, true);
    }

    @Nullable
    public static BlockType lookup(String name, boolean fuzzy) {
        try {
            return BlockType.fromID(Integer.parseInt(name));
        }
        catch (NumberFormatException e) {
            return StringUtil.lookup(lookup, name, fuzzy);
        }
    }

    @Nullable
    public static BaseBlock getBlockForItem(int typeId, int data) {
        BaseBlock block = itemBlockMapping.get(typeId);
        if (block != null) {
            return block;
        }
        return dataItemBlockMapping.get(BlockType.typeDataKey(typeId, data));
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static boolean shouldPlaceLast(int id) {
        return shouldPlaceLast.contains(id);
    }

    public boolean shouldPlaceLast() {
        return shouldPlaceLast.contains(this.id);
    }

    public static boolean shouldPlaceFinal(int id) {
        return shouldPlaceFinal.contains(id);
    }

    public static boolean canPassThrough(int id) {
        return canPassThrough.contains(id);
    }

    public static boolean canPassThrough(int id, int data) {
        return canPassThrough.contains(-16 * id - data) || canPassThrough.contains(id);
    }

    public static boolean canPassThrough(BaseBlock block) {
        Preconditions.checkNotNull((Object)block);
        return BlockType.canPassThrough(block.getId(), block.getData());
    }

    public boolean canPassThrough() {
        return canPassThrough.contains(this.id);
    }

    public static double centralTopLimit(int id, int data) {
        if (centralTopLimit.containsKey(-16 * id - data)) {
            return centralTopLimit.get(-16 * id - data);
        }
        if (centralTopLimit.containsKey(id)) {
            return centralTopLimit.get(id);
        }
        return BlockType.canPassThrough(id) ? 0.0 : 1.0;
    }

    public static double centralTopLimit(BaseBlock block) {
        Preconditions.checkNotNull((Object)block);
        return BlockType.centralTopLimit(block.getId(), block.getData());
    }

    public double centralTopLimit() {
        if (centralTopLimit.containsKey(this.id)) {
            return centralTopLimit.get(this.id);
        }
        return BlockType.canPassThrough(this.id) ? 0.0 : 1.0;
    }

    public static boolean usesData(int id) {
        return usesData.contains(id);
    }

    public boolean usesData() {
        return usesData.contains(this.id);
    }

    public static boolean isContainerBlock(int id) {
        return isContainerBlock.contains(id);
    }

    public boolean isContainerBlock() {
        return isContainerBlock.contains(this.id);
    }

    public static boolean isRedstoneBlock(int id) {
        return isRedstoneBlock.contains(id);
    }

    public boolean isRedstoneBlock() {
        return isRedstoneBlock.contains(this.id);
    }

    public static boolean canTransferRedstone(int id) {
        return canTransferRedstone.contains(id);
    }

    public boolean canTransferRedstone() {
        return canTransferRedstone.contains(this.id);
    }

    public static boolean isRedstoneSource(int id) {
        return isRedstoneSource.contains(id);
    }

    public boolean isRedstoneSource() {
        return isRedstoneSource.contains(this.id);
    }

    public static boolean isRailBlock(int id) {
        return isRailBlock.contains(id);
    }

    public boolean isRailBlock() {
        return isRailBlock.contains(this.id);
    }

    @Deprecated
    public static boolean isNaturalTerrainBlock(int id) {
        return isNaturalTerrainBlock.contains(id);
    }

    public static boolean isNaturalTerrainBlock(int id, int data) {
        return isNaturalTerrainBlock.contains(-16 * id - data) || isNaturalTerrainBlock.contains(id);
    }

    public static boolean isNaturalTerrainBlock(BaseBlock block) {
        return BlockType.isNaturalTerrainBlock(block.getId(), block.getData());
    }

    public boolean isNaturalTerrainBlock() {
        return isNaturalTerrainBlock.contains(this.id);
    }

    public static boolean emitsLight(int id) {
        return emitsLight.contains(id);
    }

    public static boolean isTranslucent(int id) {
        return isTranslucent.contains(id);
    }

    @Nullable
    public static BaseItem getBlockBagItem(int type, int data) {
        BaseItem dropped = nonDataBlockBagItems.get(type);
        if (dropped != null) {
            return dropped;
        }
        dropped = dataBlockBagItems.get(BlockType.typeDataKey(type, data));
        if (dropped == null) {
            return new BaseItemStack(0, 0);
        }
        if (dropped == doNotDestroy) {
            return null;
        }
        return dropped;
    }

    private static void addIdentity(int type) {
        nonDataBlockBagItems.put(type, new BaseItem(type));
    }

    private static void addIdentities(int type, int maxData) {
        for (int data = 0; data < maxData; ++data) {
            dataBlockBagItems.put(BlockType.typeDataKey(type, data), new BaseItem(type, (short)data));
        }
    }

    @Deprecated
    public static int getDroppedBlock(int id) {
        BaseItem dropped = nonDataBlockBagItems.get(id);
        if (dropped == null) {
            return 0;
        }
        return dropped.getType();
    }

    public BaseItemStack getBlockDrop(short data) {
        return BlockType.getBlockDrop(this.id, data);
    }

    @Nullable
    public static BaseItemStack getBlockDrop(int id, short data) {
        switch (id) {
            case 1: {
                return new BaseItemStack(4);
            }
            case 2: {
                return new BaseItemStack(3);
            }
            case 13: {
                if (random.nextInt(10) == 0) {
                    return new BaseItemStack(318);
                }
                return new BaseItemStack(13);
            }
            case 16: {
                return new BaseItemStack(263);
            }
            case 18: {
                if (random.nextDouble() > 0.95) {
                    return new BaseItemStack(6, 1, data);
                }
                return null;
            }
            case 21: {
                return new BaseItemStack(351, random.nextInt(5) + 4, 4);
            }
            case 26: {
                return new BaseItemStack(355);
            }
            case 31: {
                if (random.nextInt(8) == 0) {
                    return new BaseItemStack(295);
                }
                return null;
            }
            case 43: {
                return new BaseItemStack(44, 2, data);
            }
            case 55: {
                return new BaseItemStack(331);
            }
            case 56: {
                return new BaseItemStack(264);
            }
            case 59: {
                if (data == 7) {
                    return new BaseItemStack(296);
                }
                return new BaseItemStack(295);
            }
            case 60: {
                return new BaseItemStack(3);
            }
            case 62: {
                return new BaseItemStack(61);
            }
            case 63: {
                return new BaseItemStack(323);
            }
            case 64: {
                return new BaseItemStack(324);
            }
            case 68: {
                return new BaseItemStack(323);
            }
            case 71: {
                return new BaseItemStack(330);
            }
            case 73: 
            case 74: {
                return new BaseItemStack(331, random.nextInt(2) + 4);
            }
            case 75: {
                return new BaseItemStack(76);
            }
            case 82: {
                return new BaseItemStack(337, 4);
            }
            case 83: {
                return new BaseItemStack(338);
            }
            case 89: {
                return new BaseItemStack(348, random.nextInt(3) + 2);
            }
            case 93: 
            case 94: {
                return new BaseItemStack(356);
            }
            case 99: {
                int store = random.nextInt(10);
                if (store == 0) {
                    return new BaseItemStack(39, 2);
                }
                if (store == 1) {
                    return new BaseItemStack(39);
                }
                return null;
            }
            case 100: {
                int store = random.nextInt(10);
                if (store == 0) {
                    return new BaseItemStack(40, 2);
                }
                if (store == 1) {
                    return new BaseItemStack(40);
                }
                return null;
            }
            case 103: {
                return new BaseItemStack(360, random.nextInt(5) + 3);
            }
            case 104: {
                return new BaseItemStack(361);
            }
            case 105: {
                return new BaseItemStack(362);
            }
            case 110: {
                return new BaseItemStack(3);
            }
            case 111: {
                return new BaseItemStack(111);
            }
            case 115: {
                return new BaseItemStack(372, random.nextInt(3) + 1);
            }
            case 117: {
                return new BaseItemStack(379);
            }
            case 118: {
                return new BaseItemStack(380);
            }
            case 124: {
                return new BaseItemStack(123);
            }
            case 125: {
                return new BaseItemStack(126, 2, data);
            }
            case 127: {
                return new BaseItemStack(351, data >= 2 ? 3 : 1, 3);
            }
            case 129: {
                return new BaseItemStack(388);
            }
            case 132: {
                return new BaseItemStack(287);
            }
            case 140: {
                return new BaseItemStack(390);
            }
            case 141: {
                return new BaseItemStack(391, random.nextInt(3) + 1);
            }
            case 142: {
                return new BaseItemStack(392, random.nextInt(3) + 1);
            }
            case 149: 
            case 150: {
                return new BaseItemStack(404);
            }
            case 153: {
                return new BaseItemStack(406);
            }
            case 155: {
                return new BaseItemStack(155, 1, data >= 2 ? (short)2 : (short)data);
            }
            case 17: {
                return new BaseItemStack(17, 1, (short)(data & 3));
            }
            case 170: {
                return new BaseItemStack(170);
            }
            case 53: 
            case 67: 
            case 108: 
            case 109: 
            case 114: 
            case 134: 
            case 135: 
            case 136: 
            case 156: {
                return new BaseItemStack(id);
            }
            case 0: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 20: 
            case 34: 
            case 47: 
            case 51: 
            case 52: 
            case 78: 
            case 79: 
            case 90: 
            case 97: 
            case 106: 
            case 119: 
            case 120: 
            case 144: 
            case 160: {
                return null;
            }
        }
        if (BlockType.usesData(id)) {
            return new BaseItemStack(id, 1, data);
        }
        return new BaseItemStack(id);
    }

    public static PlayerDirection getAttachment(int type, int data) {
        PlayerDirection direction = nonDataAttachments.get(type);
        if (direction != null) {
            return direction;
        }
        return dataAttachments.get(BlockType.typeDataKey(type, data));
    }

    private static int typeDataKey(int type, int data) {
        return type << 4 | data & 0xF;
    }

    private static void addCardinals(int type, int west, int north, int east, int south) {
        dataAttachments.put(BlockType.typeDataKey(type, west), PlayerDirection.WEST);
        dataAttachments.put(BlockType.typeDataKey(type, north), PlayerDirection.NORTH);
        dataAttachments.put(BlockType.typeDataKey(type, east), PlayerDirection.EAST);
        dataAttachments.put(BlockType.typeDataKey(type, south), PlayerDirection.SOUTH);
    }

    static {
        int offset;
        int data;
        AIR = new BlockType(0, "Air", "air");
        STONE = new BlockType(1, "Stone", "stone", "rock");
        GRASS = new BlockType(2, "Grass", "grass");
        DIRT = new BlockType(3, "Dirt", "dirt");
        COBBLESTONE = new BlockType(4, "Cobblestone", "cobblestone", "cobble");
        WOOD = new BlockType(5, "Wood", "wood", "woodplank", "plank", "woodplanks", "planks");
        SAPLING = new BlockType(6, "Sapling", "sapling", "seedling");
        BEDROCK = new BlockType(7, "Bedrock", "adminium", "bedrock");
        WATER = new BlockType(8, "Water", "watermoving", "movingwater", "flowingwater", "waterflowing");
        STATIONARY_WATER = new BlockType(9, "Water (stationary)", "water", "waterstationary", "stationarywater", "stillwater");
        LAVA = new BlockType(10, "Lava", "lavamoving", "movinglava", "flowinglava", "lavaflowing");
        STATIONARY_LAVA = new BlockType(11, "Lava (stationary)", "lava", "lavastationary", "stationarylava", "stilllava");
        SAND = new BlockType(12, "Sand", "sand");
        GRAVEL = new BlockType(13, "Gravel", "gravel");
        GOLD_ORE = new BlockType(14, "Gold ore", "goldore");
        IRON_ORE = new BlockType(15, "Iron ore", "ironore");
        COAL_ORE = new BlockType(16, "Coal ore", "coalore");
        LOG = new BlockType(17, "Log", "log", "tree", "pine", "oak", "birch", "redwood");
        LEAVES = new BlockType(18, "Leaves", "leaves", "leaf");
        SPONGE = new BlockType(19, "Sponge", "sponge");
        GLASS = new BlockType(20, "Glass", "glass");
        LAPIS_LAZULI_ORE = new BlockType(21, "Lapis lazuli ore", "lapislazuliore", "blueore", "lapisore");
        LAPIS_LAZULI = new BlockType(22, "Lapis lazuli", "lapislazuli", "lapislazuliblock", "bluerock");
        DISPENSER = new BlockType(23, "Dispenser", "dispenser");
        SANDSTONE = new BlockType(24, "Sandstone", "sandstone");
        NOTE_BLOCK = new BlockType(25, "Note block", "musicblock", "noteblock", "note", "music", "instrument");
        BED = new BlockType(26, "Bed", "bed");
        POWERED_RAIL = new BlockType(27, "Powered Rail", "poweredrail", "boosterrail", "poweredtrack", "boostertrack", "booster");
        DETECTOR_RAIL = new BlockType(28, "Detector Rail", "detectorrail", "detector");
        PISTON_STICKY_BASE = new BlockType(29, "Sticky Piston", "stickypiston");
        WEB = new BlockType(30, "Web", "web", "spiderweb");
        LONG_GRASS = new BlockType(31, "Long grass", "longgrass", "tallgrass");
        DEAD_BUSH = new BlockType(32, "Shrub", "deadbush", "shrub", "deadshrub", "tumbleweed");
        PISTON_BASE = new BlockType(33, "Piston", "piston");
        PISTON_EXTENSION = new BlockType(34, "Piston extension", "pistonextendsion", "pistonhead");
        CLOTH = new BlockType(35, "Wool", "cloth", "wool");
        PISTON_MOVING_PIECE = new BlockType(36, "Piston moving piece", "movingpiston");
        YELLOW_FLOWER = new BlockType(37, "Yellow flower", "yellowflower", "flower");
        RED_FLOWER = new BlockType(38, "Red rose", "redflower", "redrose", "rose");
        BROWN_MUSHROOM = new BlockType(39, "Brown mushroom", "brownmushroom", "mushroom");
        RED_MUSHROOM = new BlockType(40, "Red mushroom", "redmushroom");
        GOLD_BLOCK = new BlockType(41, "Gold block", "gold", "goldblock");
        IRON_BLOCK = new BlockType(42, "Iron block", "iron", "ironblock");
        DOUBLE_STEP = new BlockType(43, "Double step", "doubleslab", "doublestoneslab", "doublestep");
        STEP = new BlockType(44, "Step", "slab", "stoneslab", "step", "halfstep");
        BRICK = new BlockType(45, "Brick", "brick", "brickblock");
        TNT = new BlockType(46, "TNT", "tnt", "c4", "explosive");
        BOOKCASE = new BlockType(47, "Bookcase", "bookshelf", "bookshelves", "bookcase", "bookcases");
        MOSSY_COBBLESTONE = new BlockType(48, "Cobblestone (mossy)", "mossycobblestone", "mossstone", "mossystone", "mosscobble", "mossycobble", "moss", "mossy", "sossymobblecone");
        OBSIDIAN = new BlockType(49, "Obsidian", "obsidian");
        TORCH = new BlockType(50, "Torch", "torch", "light", "candle");
        FIRE = new BlockType(51, "Fire", "fire", "flame", "flames");
        MOB_SPAWNER = new BlockType(52, "Mob spawner", "mobspawner", "spawner");
        WOODEN_STAIRS = new BlockType(53, "Wooden stairs", "woodstair", "woodstairs", "woodenstair", "woodenstairs");
        CHEST = new BlockType(54, "Chest", "chest", "storage", "storagechest");
        REDSTONE_WIRE = new BlockType(55, "Redstone wire", "redstone", "redstoneblock");
        DIAMOND_ORE = new BlockType(56, "Diamond ore", "diamondore");
        DIAMOND_BLOCK = new BlockType(57, "Diamond block", "diamond", "diamondblock");
        WORKBENCH = new BlockType(58, "Workbench", "workbench", "table", "craftingtable", "crafting");
        CROPS = new BlockType(59, "Crops", "crops", "crop", "plant", "plants");
        SOIL = new BlockType(60, "Soil", "soil", "farmland");
        FURNACE = new BlockType(61, "Furnace", "furnace");
        BURNING_FURNACE = new BlockType(62, "Furnace (burning)", "burningfurnace", "litfurnace");
        SIGN_POST = new BlockType(63, "Sign post", "sign", "signpost");
        WOODEN_DOOR = new BlockType(64, "Wooden door", "wooddoor", "woodendoor", "door");
        LADDER = new BlockType(65, "Ladder", "ladder");
        MINECART_TRACKS = new BlockType(66, "Minecart tracks", "track", "tracks", "minecrattrack", "minecarttracks", "rails", "rail");
        COBBLESTONE_STAIRS = new BlockType(67, "Cobblestone stairs", "cobblestonestair", "cobblestonestairs", "cobblestair", "cobblestairs");
        WALL_SIGN = new BlockType(68, "Wall sign", "wallsign");
        LEVER = new BlockType(69, "Lever", "lever", "switch", "stonelever", "stoneswitch");
        STONE_PRESSURE_PLATE = new BlockType(70, "Stone pressure plate", "stonepressureplate", "stoneplate");
        IRON_DOOR = new BlockType(71, "Iron Door", "irondoor");
        WOODEN_PRESSURE_PLATE = new BlockType(72, "Wooden pressure plate", "woodpressureplate", "woodplate", "woodenpressureplate", "woodenplate", "plate", "pressureplate");
        REDSTONE_ORE = new BlockType(73, "Redstone ore", "redstoneore");
        GLOWING_REDSTONE_ORE = new BlockType(74, "Glowing redstone ore", "glowingredstoneore");
        REDSTONE_TORCH_OFF = new BlockType(75, "Redstone torch (off)", "redstonetorchoff", "rstorchoff");
        REDSTONE_TORCH_ON = new BlockType(76, "Redstone torch (on)", "redstonetorch", "redstonetorchon", "rstorchon", "redtorch");
        STONE_BUTTON = new BlockType(77, "Stone Button", "stonebutton", "button");
        SNOW = new BlockType(78, "Snow", "snow");
        ICE = new BlockType(79, "Ice", "ice");
        SNOW_BLOCK = new BlockType(80, "Snow block", "snowblock");
        CACTUS = new BlockType(81, "Cactus", "cactus", "cacti");
        CLAY = new BlockType(82, "Clay", "clay");
        SUGAR_CANE = new BlockType(83, "Reed", "reed", "cane", "sugarcane", "sugarcanes", "vine", "vines");
        JUKEBOX = new BlockType(84, "Jukebox", "jukebox", "stereo", "recordplayer");
        FENCE = new BlockType(85, "Fence", "fence");
        PUMPKIN = new BlockType(86, "Pumpkin", "pumpkin");
        NETHERRACK = new BlockType(87, "Netherrack", "redmossycobblestone", "redcobblestone", "redmosstone", "redcobble", "netherstone", "netherrack", "nether", "hellstone");
        SOUL_SAND = new BlockType(88, "Soul sand", "slowmud", "mud", "soulsand", "hellmud");
        GLOWSTONE = new BlockType(89, "Glowstone", "brittlegold", "glowstone", "lightstone", "brimstone", "australium");
        PORTAL = new BlockType(90, "Portal", "portal");
        JACK_O_LANTERN = new BlockType(91, "Pumpkin (on)", "pumpkinlighted", "pumpkinon", "litpumpkin", "jackolantern");
        CAKE = new BlockType(92, "Cake", "cake", "cakeblock");
        REDSTONE_REPEATER_OFF = new BlockType(93, "Redstone repeater (off)", "diodeoff", "redstonerepeater", "repeateroff", "delayeroff");
        REDSTONE_REPEATER_ON = new BlockType(94, "Redstone repeater (on)", "diodeon", "redstonerepeateron", "repeateron", "delayeron");
        LOCKED_CHEST = new BlockType(95, "thisblockisinvalidusedstainedglassinstead", new String[0]);
        STAINED_GLASS = new BlockType(95, "Stained Glass", "stainedglass");
        TRAP_DOOR = new BlockType(96, "Trap door", "trapdoor", "hatch", "floordoor");
        SILVERFISH_BLOCK = new BlockType(97, "Silverfish block", "silverfish", "silver");
        STONE_BRICK = new BlockType(98, "Stone brick", "stonebrick", "sbrick", "smoothstonebrick");
        RED_MUSHROOM_CAP = new BlockType(100, "Red mushroom cap", "giantmushroomred", "redgiantmushroom", "redmushroomcap");
        BROWN_MUSHROOM_CAP = new BlockType(99, "Brown mushroom cap", "giantmushroombrown", "browngiantmushoom", "brownmushroomcap");
        IRON_BARS = new BlockType(101, "Iron bars", "ironbars", "ironfence");
        GLASS_PANE = new BlockType(102, "Glass pane", "window", "glasspane", "glasswindow");
        MELON_BLOCK = new BlockType(103, "Melon (block)", "melonblock");
        PUMPKIN_STEM = new BlockType(104, "Pumpkin stem", "pumpkinstem");
        MELON_STEM = new BlockType(105, "Melon stem", "melonstem");
        VINE = new BlockType(106, "Vine", "vine", "vines", "creepers");
        FENCE_GATE = new BlockType(107, "Fence gate", "fencegate", "gate");
        BRICK_STAIRS = new BlockType(108, "Brick stairs", "brickstairs", "bricksteps");
        STONE_BRICK_STAIRS = new BlockType(109, "Stone brick stairs", "stonebrickstairs", "smoothstonebrickstairs");
        MYCELIUM = new BlockType(110, "Mycelium", "mycelium", "fungus", "mycel");
        LILY_PAD = new BlockType(111, "Lily pad", "lilypad", "waterlily");
        NETHER_BRICK = new BlockType(112, "Nether brick", "netherbrick");
        NETHER_BRICK_FENCE = new BlockType(113, "Nether brick fence", "netherbrickfence", "netherfence");
        NETHER_BRICK_STAIRS = new BlockType(114, "Nether brick stairs", "netherbrickstairs", "netherbricksteps", "netherstairs", "nethersteps");
        NETHER_WART = new BlockType(115, "Nether wart", "netherwart", "netherstalk");
        ENCHANTMENT_TABLE = new BlockType(116, "Enchantment table", "enchantmenttable", "enchanttable");
        BREWING_STAND = new BlockType(117, "Brewing Stand", "brewingstand");
        CAULDRON = new BlockType(118, "Cauldron", new String[0]);
        END_PORTAL = new BlockType(119, "End Portal", "endportal", "blackstuff", "airportal", "weirdblackstuff");
        END_PORTAL_FRAME = new BlockType(120, "End Portal Frame", "endportalframe", "airportalframe", "crystalblock");
        END_STONE = new BlockType(121, "End Stone", "endstone", "enderstone", "endersand");
        DRAGON_EGG = new BlockType(122, "Dragon Egg", "dragonegg", "dragons");
        REDSTONE_LAMP_OFF = new BlockType(123, "Redstone lamp (off)", "redstonelamp", "redstonelampoff", "rslamp", "rslampoff", "rsglow", "rsglowoff");
        REDSTONE_LAMP_ON = new BlockType(124, "Redstone lamp (on)", "redstonelampon", "rslampon", "rsglowon");
        DOUBLE_WOODEN_STEP = new BlockType(125, "Double wood step", "doublewoodslab", "doublewoodstep");
        WOODEN_STEP = new BlockType(126, "Wood step", "woodenslab", "woodslab", "woodstep", "woodhalfstep");
        COCOA_PLANT = new BlockType(127, "Cocoa plant", "cocoplant", "cocoaplant");
        SANDSTONE_STAIRS = new BlockType(128, "Sandstone stairs", "sandstairs", "sandstonestairs");
        EMERALD_ORE = new BlockType(129, "Emerald ore", "emeraldore");
        ENDER_CHEST = new BlockType(130, "Ender chest", "enderchest");
        TRIPWIRE_HOOK = new BlockType(131, "Tripwire hook", "tripwirehook");
        TRIPWIRE = new BlockType(132, "Tripwire", "tripwire", "string");
        EMERALD_BLOCK = new BlockType(133, "Emerald block", "emeraldblock", "emerald");
        SPRUCE_WOOD_STAIRS = new BlockType(134, "Spruce wood stairs", "sprucestairs", "sprucewoodstairs");
        BIRCH_WOOD_STAIRS = new BlockType(135, "Birch wood stairs", "birchstairs", "birchwoodstairs");
        JUNGLE_WOOD_STAIRS = new BlockType(136, "Jungle wood stairs", "junglestairs", "junglewoodstairs");
        COMMAND_BLOCK = new BlockType(137, "Command block", "commandblock", "cmdblock", "command", "cmd");
        BEACON = new BlockType(138, "Beacon", "beacon", "beaconblock");
        COBBLESTONE_WALL = new BlockType(139, "Cobblestone wall", "cobblestonewall", "cobblewall");
        FLOWER_POT = new BlockType(140, "Flower pot", "flowerpot", "plantpot", "pot");
        CARROTS = new BlockType(141, "Carrots", "carrots", "carrotsplant", "carrotsblock");
        POTATOES = new BlockType(142, "Potatoes", "potatoes", "potatoesblock");
        WOODEN_BUTTON = new BlockType(143, "Wooden button", "woodbutton", "woodenbutton");
        HEAD = new BlockType(144, "Head", "head", "skull");
        ANVIL = new BlockType(145, "Anvil", "anvil", "blacksmith");
        TRAPPED_CHEST = new BlockType(146, "Trapped Chest", "trappedchest", "redstonechest");
        PRESSURE_PLATE_LIGHT = new BlockType(147, "Weighted Pressure Plate (Light)", "lightpressureplate");
        PRESSURE_PLATE_HEAVY = new BlockType(148, "Weighted Pressure Plate (Heavy)", "heavypressureplate");
        COMPARATOR_OFF = new BlockType(149, "Redstone Comparator (inactive)", "redstonecomparator", "comparator");
        COMPARATOR_ON = new BlockType(150, "Redstone Comparator (active)", "redstonecomparatoron", "comparatoron");
        DAYLIGHT_SENSOR = new BlockType(151, "Daylight Sensor", "daylightsensor", "lightsensor", "daylightdetector");
        REDSTONE_BLOCK = new BlockType(152, "Block of Redstone", "redstoneblock", "blockofredstone");
        QUARTZ_ORE = new BlockType(153, "Nether Quartz Ore", "quartzore", "netherquartzore");
        HOPPER = new BlockType(154, "Hopper", "hopper");
        QUARTZ_BLOCK = new BlockType(155, "Block of Quartz", "quartzblock", "quartz");
        QUARTZ_STAIRS = new BlockType(156, "Quartz Stairs", "quartzstairs");
        ACTIVATOR_RAIL = new BlockType(157, "Activator Rail", "activatorrail", "tntrail", "activatortrack");
        DROPPER = new BlockType(158, "Dropper", "dropper");
        STAINED_CLAY = new BlockType(159, "Stained Clay", "stainedclay", "stainedhardenedclay");
        STAINED_GLASS_PANE = new BlockType(160, "Stained Glass Pane", "stainedglasspane");
        LEAVES2 = new BlockType(161, "Leaves", "leaves2", "acacialeaves", "darkoakleaves");
        LOG2 = new BlockType(162, "Log", "log2", "acacia", "darkoak");
        ACACIA_STAIRS = new BlockType(163, "Acacia Wood Stairs", "acaciawoodstairs", "acaciastairs");
        DARK_OAK_STAIRS = new BlockType(164, "Dark Oak Wood Stairs", "darkoakwoodstairs", "darkoakstairs");
        SLIME = new BlockType(165, "SLime", "slimeblock");
        BARRIER = new BlockType(166, "Barrier", "barrier", "wall", "worldborder", "edge");
        IRON_TRAP_DOOR = new BlockType(167, "Iron Trap Door", "irontrapdoor");
        PRISMARINE = new BlockType(168, "Prismarine", "prismarine");
        SEA_LANTERN = new BlockType(169, "Sea Lantern", "sealantern");
        HAY_BLOCK = new BlockType(170, "Hay Block", "hayblock", "haybale", "wheatbale");
        CARPET = new BlockType(171, "Carpet", "carpet");
        HARDENED_CLAY = new BlockType(172, "Hardened Clay", "hardenedclay", "hardclay");
        COAL_BLOCK = new BlockType(173, "Block of Coal", "coalblock", "blockofcoal");
        PACKED_ICE = new BlockType(174, "Packed Ice", "packedice", "hardice");
        DOUBLE_PLANT = new BlockType(175, "Large Flowers", "largeflowers", "doubleflowers");
        STANDING_BANNER = new BlockType(176, "Standing Banner", "standingbannear", "banner");
        WALL_BANNER = new BlockType(177, "Wall Banner", "wallbanner");
        DAYLIGHT_SENSOR_INVERTED = new BlockType(178, "Inverted Daylight Sensor", "inverteddaylight", "inverteddaylightsensor");
        RED_SANDSTONE = new BlockType(179, "Red Sandstone", "redsandstone");
        RED_SANDSTONE_STAIRS = new BlockType(180, "Red Sandstone Stairs", "redsandstonestairs");
        DOUBLE_STEP2 = new BlockType(181, "Double Step 2", "doublestep2", "doubleslab2", "doublestoneslab2", "doublestonestep2");
        STEP2 = new BlockType(182, "Step 2", "step2", "slab2", "stonestep2", "stoneslab2");
        SPRUCE_FENCE_GATE = new BlockType(183, "Spruce Fence Gate", "spurcefencegate");
        BIRCH_FENCE_GATE = new BlockType(184, "Birch Fence Gate", "birchfencegate");
        JUNGLE_FENCE_GATE = new BlockType(185, "Jungle Fence Gate", "junglefencegate");
        DARK_OAK_FENCE_GATE = new BlockType(186, "Dark Oak Fence Gate", "darkoakfencegate");
        ACACIA_FENCE_GATE = new BlockType(187, "Acacia Fence Gate", "acaciafencegate");
        SPRUCE_FENCE = new BlockType(188, "Spruce Fence", "sprucefence");
        BIRCH_FENCE = new BlockType(189, "Birch Fence", "birchfence");
        JUNGLE_FENCE = new BlockType(190, "Jungle Fence", "junglefence");
        DARK_OAK_FENCE = new BlockType(191, "Dark Oak Fence", "darkoakfence");
        ACACIA_FENCE = new BlockType(192, "Acacia Fence", "acaciafence");
        SPRUCE_DOOR = new BlockType(193, "Spruce Door", "sprucedoor");
        BIRCH_DOOR = new BlockType(194, "Birch Door", "birchdoor");
        JUNGLE_DOOR = new BlockType(195, "Jungle Door", "jungledoor");
        ACACIA_DOOR = new BlockType(196, "Acacia Door", "acaciadoor");
        DARK_OAK_DOOR = new BlockType(197, "Dark Oak Door", "darkoakdoor");
        $VALUES = new BlockType[]{AIR, STONE, GRASS, DIRT, COBBLESTONE, WOOD, SAPLING, BEDROCK, WATER, STATIONARY_WATER, LAVA, STATIONARY_LAVA, SAND, GRAVEL, GOLD_ORE, IRON_ORE, COAL_ORE, LOG, LEAVES, SPONGE, GLASS, LAPIS_LAZULI_ORE, LAPIS_LAZULI, DISPENSER, SANDSTONE, NOTE_BLOCK, BED, POWERED_RAIL, DETECTOR_RAIL, PISTON_STICKY_BASE, WEB, LONG_GRASS, DEAD_BUSH, PISTON_BASE, PISTON_EXTENSION, CLOTH, PISTON_MOVING_PIECE, YELLOW_FLOWER, RED_FLOWER, BROWN_MUSHROOM, RED_MUSHROOM, GOLD_BLOCK, IRON_BLOCK, DOUBLE_STEP, STEP, BRICK, TNT, BOOKCASE, MOSSY_COBBLESTONE, OBSIDIAN, TORCH, FIRE, MOB_SPAWNER, WOODEN_STAIRS, CHEST, REDSTONE_WIRE, DIAMOND_ORE, DIAMOND_BLOCK, WORKBENCH, CROPS, SOIL, FURNACE, BURNING_FURNACE, SIGN_POST, WOODEN_DOOR, LADDER, MINECART_TRACKS, COBBLESTONE_STAIRS, WALL_SIGN, LEVER, STONE_PRESSURE_PLATE, IRON_DOOR, WOODEN_PRESSURE_PLATE, REDSTONE_ORE, GLOWING_REDSTONE_ORE, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON, STONE_BUTTON, SNOW, ICE, SNOW_BLOCK, CACTUS, CLAY, SUGAR_CANE, JUKEBOX, FENCE, PUMPKIN, NETHERRACK, SOUL_SAND, GLOWSTONE, PORTAL, JACK_O_LANTERN, CAKE, REDSTONE_REPEATER_OFF, REDSTONE_REPEATER_ON, LOCKED_CHEST, STAINED_GLASS, TRAP_DOOR, SILVERFISH_BLOCK, STONE_BRICK, RED_MUSHROOM_CAP, BROWN_MUSHROOM_CAP, IRON_BARS, GLASS_PANE, MELON_BLOCK, PUMPKIN_STEM, MELON_STEM, VINE, FENCE_GATE, BRICK_STAIRS, STONE_BRICK_STAIRS, MYCELIUM, LILY_PAD, NETHER_BRICK, NETHER_BRICK_FENCE, NETHER_BRICK_STAIRS, NETHER_WART, ENCHANTMENT_TABLE, BREWING_STAND, CAULDRON, END_PORTAL, END_PORTAL_FRAME, END_STONE, DRAGON_EGG, REDSTONE_LAMP_OFF, REDSTONE_LAMP_ON, DOUBLE_WOODEN_STEP, WOODEN_STEP, COCOA_PLANT, SANDSTONE_STAIRS, EMERALD_ORE, ENDER_CHEST, TRIPWIRE_HOOK, TRIPWIRE, EMERALD_BLOCK, SPRUCE_WOOD_STAIRS, BIRCH_WOOD_STAIRS, JUNGLE_WOOD_STAIRS, COMMAND_BLOCK, BEACON, COBBLESTONE_WALL, FLOWER_POT, CARROTS, POTATOES, WOODEN_BUTTON, HEAD, ANVIL, TRAPPED_CHEST, PRESSURE_PLATE_LIGHT, PRESSURE_PLATE_HEAVY, COMPARATOR_OFF, COMPARATOR_ON, DAYLIGHT_SENSOR, REDSTONE_BLOCK, QUARTZ_ORE, HOPPER, QUARTZ_BLOCK, QUARTZ_STAIRS, ACTIVATOR_RAIL, DROPPER, STAINED_CLAY, STAINED_GLASS_PANE, LEAVES2, LOG2, ACACIA_STAIRS, DARK_OAK_STAIRS, SLIME, BARRIER, IRON_TRAP_DOOR, PRISMARINE, SEA_LANTERN, HAY_BLOCK, CARPET, HARDENED_CLAY, COAL_BLOCK, PACKED_ICE, DOUBLE_PLANT, STANDING_BANNER, WALL_BANNER, DAYLIGHT_SENSOR_INVERTED, RED_SANDSTONE, RED_SANDSTONE_STAIRS, DOUBLE_STEP2, STEP2, SPRUCE_FENCE_GATE, BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, DARK_OAK_FENCE_GATE, ACACIA_FENCE_GATE, SPRUCE_FENCE, BIRCH_FENCE, JUNGLE_FENCE, DARK_OAK_FENCE, ACACIA_FENCE, SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR};
        ids = new HashMap<Integer, BlockType>();
        lookup = new HashMap<String, BlockType>();
        for (BlockType type : EnumSet.allOf(BlockType.class)) {
            ids.put(type.id, type);
            for (String key : type.lookupKeys) {
                lookup.put(key, type);
            }
        }
        itemBlockMapping = new HashMap<Integer, BaseBlock>();
        dataItemBlockMapping = new HashMap<Integer, BaseBlock>();
        for (data = 0; data < 16; ++data) {
            dataItemBlockMapping.put(BlockType.typeDataKey(3, data), new BaseBlock(3, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(5, data), new BaseBlock(5, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(12, data), new BaseBlock(12, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(24, data), new BaseBlock(24, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(31, data), new BaseBlock(31, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(35, data), new BaseBlock(35, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(97, data), new BaseBlock(97, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(98, data), new BaseBlock(98, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(139, data), new BaseBlock(139, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(159, data), new BaseBlock(159, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(171, data), new BaseBlock(171, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(38, data), new BaseBlock(38, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(175, data), new BaseBlock(175, data));
            dataItemBlockMapping.put(BlockType.typeDataKey(95, data), new BaseBlock(95, data));
        }
        itemBlockMapping.put(259, new BaseBlock(51, -1));
        itemBlockMapping.put(287, new BaseBlock(132, -1));
        itemBlockMapping.put(295, new BaseBlock(59, -1));
        itemBlockMapping.put(323, new BaseBlock(63, -1));
        itemBlockMapping.put(324, new BaseBlock(64, -1));
        itemBlockMapping.put(326, new BaseBlock(9, -1));
        itemBlockMapping.put(327, new BaseBlock(11, -1));
        itemBlockMapping.put(330, new BaseBlock(71, -1));
        itemBlockMapping.put(331, new BaseBlock(55, -1));
        itemBlockMapping.put(338, new BaseBlock(83, -1));
        itemBlockMapping.put(355, new BaseBlock(26, -1));
        itemBlockMapping.put(356, new BaseBlock(93, -1));
        itemBlockMapping.put(361, new BaseBlock(104, -1));
        itemBlockMapping.put(362, new BaseBlock(105, -1));
        itemBlockMapping.put(372, new BaseBlock(115, -1));
        itemBlockMapping.put(379, new BaseBlock(117, -1));
        itemBlockMapping.put(380, new BaseBlock(118, -1));
        itemBlockMapping.put(390, new BaseBlock(140, -1));
        itemBlockMapping.put(391, new BaseBlock(141, -1));
        itemBlockMapping.put(392, new BaseBlock(142, -1));
        itemBlockMapping.put(404, new BaseBlock(149, -1));
        itemBlockMapping.put(425, new BaseBlock(176, -1));
        itemBlockMapping.put(427, new BaseBlock(193, -1));
        itemBlockMapping.put(428, new BaseBlock(194, -1));
        itemBlockMapping.put(429, new BaseBlock(195, -1));
        itemBlockMapping.put(430, new BaseBlock(196, -1));
        itemBlockMapping.put(431, new BaseBlock(197, -1));
        shouldPlaceLast = new HashSet<Integer>();
        shouldPlaceLast.add(6);
        shouldPlaceLast.add(26);
        shouldPlaceLast.add(27);
        shouldPlaceLast.add(28);
        shouldPlaceLast.add(31);
        shouldPlaceLast.add(32);
        shouldPlaceLast.add(34);
        shouldPlaceLast.add(37);
        shouldPlaceLast.add(38);
        shouldPlaceLast.add(39);
        shouldPlaceLast.add(40);
        shouldPlaceLast.add(50);
        shouldPlaceLast.add(51);
        shouldPlaceLast.add(55);
        shouldPlaceLast.add(59);
        shouldPlaceLast.add(65);
        shouldPlaceLast.add(66);
        shouldPlaceLast.add(69);
        shouldPlaceLast.add(70);
        shouldPlaceLast.add(72);
        shouldPlaceLast.add(75);
        shouldPlaceLast.add(76);
        shouldPlaceLast.add(77);
        shouldPlaceLast.add(78);
        shouldPlaceLast.add(90);
        shouldPlaceLast.add(93);
        shouldPlaceLast.add(94);
        shouldPlaceLast.add(96);
        shouldPlaceLast.add(106);
        shouldPlaceLast.add(111);
        shouldPlaceLast.add(115);
        shouldPlaceLast.add(33);
        shouldPlaceLast.add(29);
        shouldPlaceLast.add(34);
        shouldPlaceLast.add(36);
        shouldPlaceLast.add(127);
        shouldPlaceLast.add(131);
        shouldPlaceLast.add(132);
        shouldPlaceLast.add(140);
        shouldPlaceLast.add(141);
        shouldPlaceLast.add(142);
        shouldPlaceLast.add(143);
        shouldPlaceLast.add(145);
        shouldPlaceLast.add(147);
        shouldPlaceLast.add(148);
        shouldPlaceLast.add(149);
        shouldPlaceLast.add(150);
        shouldPlaceLast.add(157);
        shouldPlaceLast.add(167);
        shouldPlaceLast.add(171);
        shouldPlaceLast.add(175);
        shouldPlaceLast.add(178);
        shouldPlaceFinal = new HashSet<Integer>();
        shouldPlaceFinal.add(63);
        shouldPlaceFinal.add(64);
        shouldPlaceFinal.add(68);
        shouldPlaceFinal.add(71);
        shouldPlaceFinal.add(81);
        shouldPlaceFinal.add(83);
        shouldPlaceFinal.add(92);
        shouldPlaceFinal.add(34);
        shouldPlaceFinal.add(36);
        shouldPlaceFinal.add(176);
        shouldPlaceFinal.add(177);
        canPassThrough = new HashSet<Integer>();
        canPassThrough.add(0);
        canPassThrough.add(8);
        canPassThrough.add(9);
        canPassThrough.add(6);
        canPassThrough.add(27);
        canPassThrough.add(28);
        canPassThrough.add(30);
        canPassThrough.add(31);
        canPassThrough.add(32);
        canPassThrough.add(37);
        canPassThrough.add(38);
        canPassThrough.add(39);
        canPassThrough.add(40);
        canPassThrough.add(50);
        canPassThrough.add(51);
        canPassThrough.add(55);
        canPassThrough.add(59);
        canPassThrough.add(63);
        canPassThrough.add(65);
        canPassThrough.add(66);
        canPassThrough.add(68);
        canPassThrough.add(69);
        canPassThrough.add(70);
        canPassThrough.add(72);
        canPassThrough.add(75);
        canPassThrough.add(76);
        canPassThrough.add(77);
        canPassThrough.add(-1248);
        canPassThrough.add(-1256);
        canPassThrough.add(83);
        canPassThrough.add(90);
        canPassThrough.add(93);
        canPassThrough.add(94);
        canPassThrough.add(104);
        canPassThrough.add(105);
        canPassThrough.add(106);
        canPassThrough.add(115);
        canPassThrough.add(119);
        canPassThrough.add(131);
        canPassThrough.add(132);
        canPassThrough.add(141);
        canPassThrough.add(142);
        canPassThrough.add(143);
        canPassThrough.add(147);
        canPassThrough.add(148);
        canPassThrough.add(149);
        canPassThrough.add(150);
        canPassThrough.add(157);
        canPassThrough.add(167);
        canPassThrough.add(171);
        canPassThrough.add(175);
        canPassThrough.add(176);
        canPassThrough.add(177);
        centralTopLimit = new HashMap<Integer, Double>();
        centralTopLimit.put(26, 0.5625);
        centralTopLimit.put(117, 0.875);
        centralTopLimit.put(92, 0.4375);
        for (data = 6; data < 16; ++data) {
            centralTopLimit.put(-1472 - data, 0.0);
        }
        centralTopLimit.put(118, 0.3125);
        centralTopLimit.put(127, 0.75);
        centralTopLimit.put(116, 0.75);
        for (data = 0; data < 16; ++data) {
            if ((data & 4) != 0) {
                centralTopLimit.put(-1920 - data, 1.0);
            } else {
                centralTopLimit.put(-1920 - data, 0.8125);
            }
            centralTopLimit.put(-2304 - data, 0.75);
        }
        centralTopLimit.put(-2305, 0.5);
        centralTopLimit.put(-2313, 0.5);
        centralTopLimit.put(85, 1.5);
        for (data = 0; data < 8; ++data) {
            centralTopLimit.put(-704 - data, 0.5);
            centralTopLimit.put(-2016 - data, 0.5);
            centralTopLimit.put(-2912 - data, 0.5);
            centralTopLimit.put(-1248 - data, 0.125 * (double)data);
            centralTopLimit.put(-1248 - (data + 8), 0.125 * (double)data);
        }
        centralTopLimit.put(111, 0.015625);
        centralTopLimit.put(94, 0.125);
        centralTopLimit.put(93, 0.125);
        for (data = 0; data < 4; ++data) {
            centralTopLimit.put(-1536 - (data + 0), 0.1875);
            centralTopLimit.put(-1536 - (data + 4), 0.0);
            centralTopLimit.put(-1536 - (data + 8), 1.0);
            centralTopLimit.put(-1536 - (data + 12), 0.0);
            centralTopLimit.put(-1712 - (data + 0), 1.5);
            centralTopLimit.put(-1712 - (data + 4), 0.0);
            centralTopLimit.put(-1712 - (data + 8), 1.5);
            centralTopLimit.put(-1712 - (data + 12), 0.0);
        }
        centralTopLimit.put(88, 0.875);
        centralTopLimit.put(139, 1.5);
        centralTopLimit.put(140, 0.375);
        centralTopLimit.put(149, 0.125);
        centralTopLimit.put(150, 0.125);
        centralTopLimit.put(151, 0.375);
        centralTopLimit.put(154, 0.625);
        centralTopLimit.put(144, 0.75);
        centralTopLimit.put(96, 1.0);
        centralTopLimit.put(107, 1.5);
        usesData = new HashSet<Integer>();
        usesData.add(1);
        usesData.add(3);
        usesData.add(5);
        usesData.add(6);
        usesData.add(8);
        usesData.add(9);
        usesData.add(10);
        usesData.add(11);
        usesData.add(12);
        usesData.add(17);
        usesData.add(162);
        usesData.add(18);
        usesData.add(161);
        usesData.add(19);
        usesData.add(23);
        usesData.add(24);
        usesData.add(26);
        usesData.add(27);
        usesData.add(28);
        usesData.add(29);
        usesData.add(31);
        usesData.add(33);
        usesData.add(34);
        usesData.add(35);
        usesData.add(38);
        usesData.add(43);
        usesData.add(44);
        usesData.add(50);
        usesData.add(51);
        usesData.add(53);
        usesData.add(54);
        usesData.add(55);
        usesData.add(59);
        usesData.add(60);
        usesData.add(61);
        usesData.add(62);
        usesData.add(63);
        usesData.add(64);
        usesData.add(65);
        usesData.add(66);
        usesData.add(67);
        usesData.add(68);
        usesData.add(69);
        usesData.add(70);
        usesData.add(71);
        usesData.add(72);
        usesData.add(75);
        usesData.add(76);
        usesData.add(77);
        usesData.add(78);
        usesData.add(81);
        usesData.add(83);
        usesData.add(84);
        usesData.add(86);
        usesData.add(91);
        usesData.add(92);
        usesData.add(93);
        usesData.add(94);
        usesData.add(96);
        usesData.add(97);
        usesData.add(98);
        usesData.add(100);
        usesData.add(99);
        usesData.add(104);
        usesData.add(105);
        usesData.add(106);
        usesData.add(107);
        usesData.add(108);
        usesData.add(109);
        usesData.add(114);
        usesData.add(115);
        usesData.add(117);
        usesData.add(118);
        usesData.add(120);
        usesData.add(125);
        usesData.add(126);
        usesData.add(127);
        usesData.add(128);
        usesData.add(130);
        usesData.add(131);
        usesData.add(132);
        usesData.add(134);
        usesData.add(135);
        usesData.add(136);
        usesData.add(139);
        usesData.add(140);
        usesData.add(141);
        usesData.add(142);
        usesData.add(143);
        usesData.add(144);
        usesData.add(145);
        usesData.add(147);
        usesData.add(148);
        usesData.add(149);
        usesData.add(150);
        usesData.add(155);
        usesData.add(156);
        usesData.add(157);
        usesData.add(158);
        usesData.add(154);
        usesData.add(159);
        usesData.add(95);
        usesData.add(160);
        usesData.add(167);
        usesData.add(168);
        usesData.add(170);
        usesData.add(171);
        usesData.add(175);
        usesData.add(176);
        usesData.add(177);
        usesData.add(179);
        usesData.add(180);
        usesData.add(181);
        usesData.add(182);
        usesData.add(193);
        usesData.add(194);
        usesData.add(195);
        usesData.add(196);
        usesData.add(197);
        isContainerBlock = new HashSet<Integer>();
        isContainerBlock.add(23);
        isContainerBlock.add(61);
        isContainerBlock.add(62);
        isContainerBlock.add(54);
        isContainerBlock.add(117);
        isContainerBlock.add(146);
        isContainerBlock.add(154);
        isContainerBlock.add(158);
        isRedstoneBlock = new HashSet<Integer>();
        isRedstoneBlock.add(27);
        isRedstoneBlock.add(28);
        isRedstoneBlock.add(29);
        isRedstoneBlock.add(33);
        isRedstoneBlock.add(69);
        isRedstoneBlock.add(70);
        isRedstoneBlock.add(72);
        isRedstoneBlock.add(75);
        isRedstoneBlock.add(76);
        isRedstoneBlock.add(77);
        isRedstoneBlock.add(55);
        isRedstoneBlock.add(64);
        isRedstoneBlock.add(71);
        isRedstoneBlock.add(46);
        isRedstoneBlock.add(23);
        isRedstoneBlock.add(25);
        isRedstoneBlock.add(93);
        isRedstoneBlock.add(94);
        isRedstoneBlock.add(131);
        isRedstoneBlock.add(137);
        isRedstoneBlock.add(143);
        isRedstoneBlock.add(146);
        isRedstoneBlock.add(147);
        isRedstoneBlock.add(148);
        isRedstoneBlock.add(149);
        isRedstoneBlock.add(150);
        isRedstoneBlock.add(151);
        isRedstoneBlock.add(152);
        isRedstoneBlock.add(154);
        isRedstoneBlock.add(157);
        isRedstoneBlock.add(158);
        isRedstoneBlock.add(178);
        canTransferRedstone = new HashSet<Integer>();
        canTransferRedstone.add(75);
        canTransferRedstone.add(76);
        canTransferRedstone.add(55);
        canTransferRedstone.add(93);
        canTransferRedstone.add(94);
        canTransferRedstone.add(149);
        canTransferRedstone.add(150);
        isRedstoneSource = new HashSet<Integer>();
        isRedstoneSource.add(28);
        isRedstoneSource.add(75);
        isRedstoneSource.add(76);
        isRedstoneSource.add(69);
        isRedstoneSource.add(70);
        isRedstoneSource.add(72);
        isRedstoneSource.add(77);
        isRedstoneSource.add(131);
        isRedstoneSource.add(143);
        isRedstoneSource.add(147);
        isRedstoneSource.add(148);
        isRedstoneSource.add(151);
        isRedstoneSource.add(152);
        isRedstoneSource.add(178);
        isRailBlock = new HashSet<Integer>();
        isRailBlock.add(27);
        isRailBlock.add(28);
        isRailBlock.add(66);
        isRailBlock.add(157);
        isNaturalTerrainBlock = new HashSet<Integer>();
        isNaturalTerrainBlock.add(1);
        isNaturalTerrainBlock.add(2);
        isNaturalTerrainBlock.add(3);
        isNaturalTerrainBlock.add(7);
        isNaturalTerrainBlock.add(12);
        isNaturalTerrainBlock.add(13);
        isNaturalTerrainBlock.add(82);
        isNaturalTerrainBlock.add(110);
        isNaturalTerrainBlock.add(174);
        isNaturalTerrainBlock.add(159);
        isNaturalTerrainBlock.add(87);
        isNaturalTerrainBlock.add(88);
        isNaturalTerrainBlock.add(89);
        isNaturalTerrainBlock.add(153);
        isNaturalTerrainBlock.add(16);
        isNaturalTerrainBlock.add(15);
        isNaturalTerrainBlock.add(14);
        isNaturalTerrainBlock.add(21);
        isNaturalTerrainBlock.add(56);
        isNaturalTerrainBlock.add(73);
        isNaturalTerrainBlock.add(74);
        isNaturalTerrainBlock.add(129);
        emitsLight = new HashSet<Integer>();
        emitsLight.add(10);
        emitsLight.add(11);
        emitsLight.add(39);
        emitsLight.add(40);
        emitsLight.add(50);
        emitsLight.add(51);
        emitsLight.add(62);
        emitsLight.add(74);
        emitsLight.add(76);
        emitsLight.add(89);
        emitsLight.add(90);
        emitsLight.add(91);
        emitsLight.add(94);
        emitsLight.add(99);
        emitsLight.add(100);
        emitsLight.add(119);
        emitsLight.add(124);
        emitsLight.add(130);
        emitsLight.add(138);
        emitsLight.add(152);
        emitsLight.add(169);
        isTranslucent = new HashSet<Integer>();
        isTranslucent.add(0);
        isTranslucent.add(6);
        isTranslucent.add(8);
        isTranslucent.add(9);
        isTranslucent.add(18);
        isTranslucent.add(20);
        isTranslucent.add(26);
        isTranslucent.add(27);
        isTranslucent.add(28);
        isTranslucent.add(30);
        isTranslucent.add(31);
        isTranslucent.add(32);
        isTranslucent.add(34);
        isTranslucent.add(37);
        isTranslucent.add(38);
        isTranslucent.add(39);
        isTranslucent.add(40);
        isTranslucent.add(50);
        isTranslucent.add(51);
        isTranslucent.add(52);
        isTranslucent.add(53);
        isTranslucent.add(54);
        isTranslucent.add(55);
        isTranslucent.add(59);
        isTranslucent.add(63);
        isTranslucent.add(64);
        isTranslucent.add(65);
        isTranslucent.add(66);
        isTranslucent.add(67);
        isTranslucent.add(68);
        isTranslucent.add(69);
        isTranslucent.add(70);
        isTranslucent.add(71);
        isTranslucent.add(72);
        isTranslucent.add(75);
        isTranslucent.add(76);
        isTranslucent.add(77);
        isTranslucent.add(78);
        isTranslucent.add(79);
        isTranslucent.add(81);
        isTranslucent.add(83);
        isTranslucent.add(85);
        isTranslucent.add(90);
        isTranslucent.add(92);
        isTranslucent.add(93);
        isTranslucent.add(94);
        isTranslucent.add(96);
        isTranslucent.add(101);
        isTranslucent.add(102);
        isTranslucent.add(104);
        isTranslucent.add(105);
        isTranslucent.add(106);
        isTranslucent.add(107);
        isTranslucent.add(108);
        isTranslucent.add(109);
        isTranslucent.add(111);
        isTranslucent.add(113);
        isTranslucent.add(114);
        isTranslucent.add(115);
        isTranslucent.add(116);
        isTranslucent.add(117);
        isTranslucent.add(118);
        isTranslucent.add(126);
        isTranslucent.add(127);
        isTranslucent.add(128);
        isTranslucent.add(130);
        isTranslucent.add(131);
        isTranslucent.add(132);
        isTranslucent.add(134);
        isTranslucent.add(135);
        isTranslucent.add(136);
        isTranslucent.add(139);
        isTranslucent.add(140);
        isTranslucent.add(141);
        isTranslucent.add(142);
        isTranslucent.add(143);
        isTranslucent.add(144);
        isTranslucent.add(145);
        isTranslucent.add(146);
        isTranslucent.add(147);
        isTranslucent.add(148);
        isTranslucent.add(149);
        isTranslucent.add(150);
        isTranslucent.add(151);
        isTranslucent.add(154);
        isTranslucent.add(156);
        isTranslucent.add(157);
        isTranslucent.add(166);
        isTranslucent.add(167);
        isTranslucent.add(171);
        isTranslucent.add(160);
        isTranslucent.add(175);
        isTranslucent.add(176);
        isTranslucent.add(177);
        isTranslucent.add(178);
        isTranslucent.add(180);
        isTranslucent.add(182);
        isTranslucent.add(183);
        isTranslucent.add(184);
        isTranslucent.add(185);
        isTranslucent.add(186);
        isTranslucent.add(187);
        isTranslucent.add(188);
        isTranslucent.add(189);
        isTranslucent.add(190);
        isTranslucent.add(191);
        isTranslucent.add(192);
        isTranslucent.add(193);
        isTranslucent.add(194);
        isTranslucent.add(195);
        isTranslucent.add(196);
        isTranslucent.add(197);
        dataBlockBagItems = new HashMap<Integer, BaseItem>();
        nonDataBlockBagItems = new HashMap<Integer, BaseItem>();
        doNotDestroy = new BaseItemStack(0, 0);
        nonDataBlockBagItems.put(1, new BaseItem(4));
        nonDataBlockBagItems.put(2, new BaseItem(3));
        BlockType.addIdentities(3, 3);
        BlockType.addIdentity(4);
        BlockType.addIdentities(5, 6);
        BlockType.addIdentities(6, 6);
        nonDataBlockBagItems.put(7, doNotDestroy);
        BlockType.addIdentity(12);
        BlockType.addIdentity(13);
        BlockType.addIdentity(14);
        BlockType.addIdentity(15);
        nonDataBlockBagItems.put(16, new BaseItem(263));
        BlockType.addIdentities(17, 4);
        BlockType.addIdentities(18, 4);
        BlockType.addIdentity(19);
        BlockType.addIdentity(20);
        BlockType.addIdentity(21);
        BlockType.addIdentity(22);
        BlockType.addIdentity(23);
        BlockType.addIdentity(24);
        BlockType.addIdentity(25);
        BlockType.addIdentities(26, 8);
        BlockType.addIdentity(27);
        BlockType.addIdentity(28);
        BlockType.addIdentity(29);
        nonDataBlockBagItems.put(30, new BaseItem(287));
        BlockType.addIdentity(33);
        BlockType.addIdentities(35, 16);
        BlockType.addIdentity(37);
        BlockType.addIdentity(38);
        BlockType.addIdentity(39);
        BlockType.addIdentity(40);
        BlockType.addIdentity(41);
        BlockType.addIdentity(42);
        BlockType.addIdentities(43, 7);
        BlockType.addIdentities(44, 7);
        BlockType.addIdentity(45);
        BlockType.addIdentity(46);
        BlockType.addIdentity(47);
        BlockType.addIdentity(48);
        BlockType.addIdentity(49);
        BlockType.addIdentity(50);
        BlockType.addIdentity(53);
        BlockType.addIdentity(54);
        nonDataBlockBagItems.put(55, new BaseItem(331));
        nonDataBlockBagItems.put(56, new BaseItem(264));
        BlockType.addIdentity(57);
        BlockType.addIdentity(58);
        nonDataBlockBagItems.put(59, new BaseItem(295));
        nonDataBlockBagItems.put(60, new BaseItem(3));
        BlockType.addIdentity(61);
        nonDataBlockBagItems.put(62, new BaseItem(61));
        nonDataBlockBagItems.put(63, new BaseItem(323));
        BlockType.addIdentities(64, 8);
        BlockType.addIdentity(65);
        BlockType.addIdentity(66);
        BlockType.addIdentity(67);
        nonDataBlockBagItems.put(68, new BaseItem(323));
        BlockType.addIdentity(69);
        BlockType.addIdentity(70);
        BlockType.addIdentities(71, 8);
        BlockType.addIdentity(72);
        BlockType.addIdentity(73);
        nonDataBlockBagItems.put(74, new BaseItem(73));
        nonDataBlockBagItems.put(75, new BaseItem(76));
        BlockType.addIdentity(76);
        BlockType.addIdentity(77);
        BlockType.addIdentity(78);
        BlockType.addIdentity(79);
        BlockType.addIdentity(80);
        BlockType.addIdentity(81);
        BlockType.addIdentity(82);
        nonDataBlockBagItems.put(83, new BaseItem(338));
        BlockType.addIdentity(84);
        BlockType.addIdentity(85);
        BlockType.addIdentity(86);
        BlockType.addIdentity(87);
        BlockType.addIdentity(88);
        BlockType.addIdentity(89);
        BlockType.addIdentity(91);
        nonDataBlockBagItems.put(92, new BaseItem(354));
        nonDataBlockBagItems.put(93, new BaseItem(356));
        nonDataBlockBagItems.put(94, new BaseItem(356));
        BlockType.addIdentities(160, 16);
        BlockType.addIdentity(96);
        nonDataBlockBagItems.put(97, doNotDestroy);
        BlockType.addIdentity(98);
        BlockType.addIdentity(99);
        BlockType.addIdentity(100);
        BlockType.addIdentity(101);
        BlockType.addIdentity(102);
        BlockType.addIdentity(103);
        nonDataBlockBagItems.put(104, new BaseItem(361));
        nonDataBlockBagItems.put(105, new BaseItem(362));
        nonDataBlockBagItems.put(106, doNotDestroy);
        BlockType.addIdentity(107);
        BlockType.addIdentity(108);
        BlockType.addIdentity(109);
        nonDataBlockBagItems.put(110, new BaseItem(3));
        BlockType.addIdentity(111);
        BlockType.addIdentity(112);
        BlockType.addIdentity(113);
        BlockType.addIdentity(114);
        nonDataBlockBagItems.put(115, new BaseItem(372));
        BlockType.addIdentity(116);
        nonDataBlockBagItems.put(117, new BaseItem(379));
        nonDataBlockBagItems.put(118, new BaseItem(380));
        nonDataBlockBagItems.put(119, doNotDestroy);
        nonDataBlockBagItems.put(120, doNotDestroy);
        BlockType.addIdentity(121);
        BlockType.addIdentity(123);
        nonDataBlockBagItems.put(124, new BaseItem(123));
        BlockType.addIdentities(125, 7);
        BlockType.addIdentities(126, 7);
        nonDataBlockBagItems.put(127, new BaseItem(351, 3));
        BlockType.addIdentity(128);
        nonDataBlockBagItems.put(129, new BaseItem(388));
        BlockType.addIdentity(130);
        BlockType.addIdentity(131);
        nonDataBlockBagItems.put(132, new BaseItem(287));
        BlockType.addIdentity(133);
        BlockType.addIdentity(134);
        BlockType.addIdentity(135);
        BlockType.addIdentity(136);
        BlockType.addIdentity(137);
        BlockType.addIdentities(139, 1);
        nonDataBlockBagItems.put(140, new BaseItemStack(390));
        nonDataBlockBagItems.put(141, new BaseItemStack(391));
        nonDataBlockBagItems.put(142, new BaseItemStack(392));
        BlockType.addIdentity(143);
        nonDataBlockBagItems.put(144, doNotDestroy);
        BlockType.addIdentities(145, 2);
        BlockType.addIdentity(146);
        BlockType.addIdentity(147);
        BlockType.addIdentity(148);
        nonDataBlockBagItems.put(149, new BaseItemStack(404));
        nonDataBlockBagItems.put(150, new BaseItemStack(404));
        BlockType.addIdentity(151);
        BlockType.addIdentity(152);
        nonDataBlockBagItems.put(153, new BaseItemStack(406));
        BlockType.addIdentity(154);
        BlockType.addIdentities(155, 1);
        for (int i = 2; i <= 4; ++i) {
            dataBlockBagItems.put(BlockType.typeDataKey(155, i), new BaseItem(155, 2));
        }
        BlockType.addIdentity(156);
        BlockType.addIdentity(157);
        BlockType.addIdentity(158);
        BlockType.addIdentities(159, 16);
        BlockType.addIdentity(170);
        BlockType.addIdentities(171, 16);
        BlockType.addIdentity(172);
        BlockType.addIdentity(173);
        BlockType.addIdentities(162, 1);
        BlockType.addIdentities(161, 1);
        BlockType.addIdentity(163);
        BlockType.addIdentity(164);
        BlockType.addIdentity(174);
        BlockType.addIdentities(160, 16);
        BlockType.addIdentities(175, 6);
        random = new Random();
        dataAttachments = new HashMap<Integer, PlayerDirection>();
        nonDataAttachments = new HashMap<Integer, PlayerDirection>();
        nonDataAttachments.put(6, PlayerDirection.DOWN);
        nonDataAttachments.put(31, PlayerDirection.DOWN);
        nonDataAttachments.put(32, PlayerDirection.DOWN);
        for (int offset2 = 0; offset2 < 16; offset2 += 8) {
            dataAttachments.put(BlockType.typeDataKey(34, offset2 + 0), PlayerDirection.UP);
            dataAttachments.put(BlockType.typeDataKey(34, offset2 + 1), PlayerDirection.DOWN);
            BlockType.addCardinals(34, offset2 + 2, offset2 + 5, offset2 + 3, offset2 + 4);
        }
        nonDataAttachments.put(37, PlayerDirection.DOWN);
        nonDataAttachments.put(38, PlayerDirection.DOWN);
        nonDataAttachments.put(39, PlayerDirection.DOWN);
        nonDataAttachments.put(40, PlayerDirection.DOWN);
        for (int blockId : new int[]{50, 76, 75}) {
            dataAttachments.put(BlockType.typeDataKey(blockId, 0), PlayerDirection.DOWN);
            dataAttachments.put(BlockType.typeDataKey(blockId, 5), PlayerDirection.DOWN);
            BlockType.addCardinals(blockId, 4, 1, 3, 2);
        }
        nonDataAttachments.put(55, PlayerDirection.DOWN);
        nonDataAttachments.put(59, PlayerDirection.DOWN);
        nonDataAttachments.put(63, PlayerDirection.DOWN);
        nonDataAttachments.put(64, PlayerDirection.DOWN);
        BlockType.addCardinals(65, 2, 5, 3, 4);
        BlockType.addCardinals(68, 2, 5, 3, 4);
        for (offset = 0; offset < 16; offset += 8) {
            BlockType.addCardinals(69, offset + 4, offset + 1, offset + 3, offset + 2);
            dataAttachments.put(BlockType.typeDataKey(69, offset + 5), PlayerDirection.DOWN);
            dataAttachments.put(BlockType.typeDataKey(69, offset + 6), PlayerDirection.DOWN);
            dataAttachments.put(BlockType.typeDataKey(69, offset + 7), PlayerDirection.UP);
            dataAttachments.put(BlockType.typeDataKey(69, offset + 0), PlayerDirection.UP);
        }
        nonDataAttachments.put(70, PlayerDirection.DOWN);
        nonDataAttachments.put(71, PlayerDirection.DOWN);
        nonDataAttachments.put(72, PlayerDirection.DOWN);
        for (offset = 0; offset < 16; offset += 8) {
            BlockType.addCardinals(77, offset + 4, offset + 1, offset + 3, offset + 2);
            BlockType.addCardinals(143, offset + 4, offset + 1, offset + 3, offset + 2);
        }
        dataAttachments.put(BlockType.typeDataKey(77, 0), PlayerDirection.UP);
        dataAttachments.put(BlockType.typeDataKey(77, 5), PlayerDirection.DOWN);
        dataAttachments.put(BlockType.typeDataKey(143, 0), PlayerDirection.UP);
        dataAttachments.put(BlockType.typeDataKey(143, 5), PlayerDirection.DOWN);
        nonDataAttachments.put(81, PlayerDirection.DOWN);
        nonDataAttachments.put(83, PlayerDirection.DOWN);
        nonDataAttachments.put(92, PlayerDirection.DOWN);
        nonDataAttachments.put(93, PlayerDirection.DOWN);
        nonDataAttachments.put(94, PlayerDirection.DOWN);
        for (offset = 0; offset < 16; offset += 4) {
            BlockType.addCardinals(96, offset + 0, offset + 3, offset + 1, offset + 2);
            BlockType.addCardinals(167, offset + 0, offset + 3, offset + 1, offset + 2);
        }
        nonDataAttachments.put(104, PlayerDirection.DOWN);
        nonDataAttachments.put(105, PlayerDirection.DOWN);
        dataAttachments.put(BlockType.typeDataKey(106, 0), PlayerDirection.UP);
        BlockType.addCardinals(106, 1, 2, 4, 8);
        nonDataAttachments.put(115, PlayerDirection.DOWN);
        for (offset = 0; offset < 16; offset += 4) {
            BlockType.addCardinals(127, offset + 0, offset + 1, offset + 2, offset + 3);
        }
        for (offset = 0; offset < 16; offset += 4) {
            BlockType.addCardinals(131, offset + 2, offset + 3, offset + 0, offset + 1);
        }
        nonDataAttachments.put(132, PlayerDirection.DOWN);
        nonDataAttachments.put(140, PlayerDirection.DOWN);
        nonDataAttachments.put(141, PlayerDirection.DOWN);
        nonDataAttachments.put(142, PlayerDirection.DOWN);
        nonDataAttachments.put(145, PlayerDirection.DOWN);
        nonDataAttachments.put(147, PlayerDirection.DOWN);
        nonDataAttachments.put(148, PlayerDirection.DOWN);
        nonDataAttachments.put(149, PlayerDirection.DOWN);
        nonDataAttachments.put(150, PlayerDirection.DOWN);
        nonDataAttachments.put(171, PlayerDirection.DOWN);
        nonDataAttachments.put(175, PlayerDirection.DOWN);
        nonDataAttachments.put(176, PlayerDirection.DOWN);
        BlockType.addCardinals(177, 4, 2, 5, 3);
        nonDataAttachments.put(193, PlayerDirection.DOWN);
        nonDataAttachments.put(194, PlayerDirection.DOWN);
        nonDataAttachments.put(195, PlayerDirection.DOWN);
        nonDataAttachments.put(196, PlayerDirection.DOWN);
        nonDataAttachments.put(197, PlayerDirection.DOWN);
        for (offset = 0; offset < 16; offset += 8) {
            BlockType.addCardinals(27, offset + 3, offset + 4, offset + 2, offset + 5);
            BlockType.addCardinals(28, offset + 3, offset + 4, offset + 2, offset + 5);
            BlockType.addCardinals(66, offset + 3, offset + 4, offset + 2, offset + 5);
            BlockType.addCardinals(157, offset + 3, offset + 4, offset + 2, offset + 5);
        }
    }
}

