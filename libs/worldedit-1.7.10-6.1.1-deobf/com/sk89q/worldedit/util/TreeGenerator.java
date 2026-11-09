/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.util;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public class TreeGenerator {
    private static final Random RANDOM = new Random();
    private TreeType type;

    @Deprecated
    public TreeGenerator(TreeType type) {
        this.type = type;
    }

    public boolean generate(EditSession editSession, Vector position) throws MaxChangedBlocksException {
        return this.type.generate(editSession, position);
    }

    private static void makePineTree(EditSession editSession, Vector basePosition) throws MaxChangedBlocksException {
        int i;
        int trunkHeight = (int)Math.floor(Math.random() * 2.0) + 3;
        int height = (int)Math.floor(Math.random() * 5.0) + 8;
        BaseBlock logBlock = new BaseBlock(17);
        BaseBlock leavesBlock = new BaseBlock(18);
        for (i = 0; i < trunkHeight; ++i) {
            if (editSession.setBlockIfAir(basePosition.add(0, i, 0), logBlock)) continue;
            return;
        }
        basePosition = basePosition.add(0, trunkHeight, 0);
        for (i = 0; i < height; ++i) {
            int j;
            editSession.setBlockIfAir(basePosition.add(0, i, 0), logBlock);
            double chance = i == 0 || i == height - 1 ? 0.6 : 1.0;
            editSession.setChanceBlockIfAir(basePosition.add(-1, i, 0), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(1, i, 0), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(0, i, -1), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(0, i, 1), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(1, i, 1), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(-1, i, 1), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(1, i, -1), leavesBlock, chance);
            editSession.setChanceBlockIfAir(basePosition.add(-1, i, -1), leavesBlock, chance);
            if (i == 0 || i == height - 1) continue;
            for (j = -2; j <= 2; ++j) {
                editSession.setChanceBlockIfAir(basePosition.add(-2, i, j), leavesBlock, 0.6);
            }
            for (j = -2; j <= 2; ++j) {
                editSession.setChanceBlockIfAir(basePosition.add(2, i, j), leavesBlock, 0.6);
            }
            for (j = -2; j <= 2; ++j) {
                editSession.setChanceBlockIfAir(basePosition.add(j, i, -2), leavesBlock, 0.6);
            }
            for (j = -2; j <= 2; ++j) {
                editSession.setChanceBlockIfAir(basePosition.add(j, i, 2), leavesBlock, 0.6);
            }
        }
        editSession.setBlockIfAir(basePosition.add(0, height, 0), leavesBlock);
    }

    @Nullable
    public static TreeType lookup(String type) {
        return TreeType.lookup(type);
    }

    public static enum TreeType {
        TREE("Oak tree", "oak", "tree", "regular"),
        BIG_TREE("Large oak tree", "largeoak", "bigoak", "big", "bigtree"),
        REDWOOD("Spruce tree", "spruce", "redwood", "sequoia", "sequoioideae"),
        TALL_REDWOOD("Tall spruce tree", "tallspruce", "bigspruce", "tallredwood", "tallsequoia", "tallsequoioideae"),
        MEGA_REDWOOD("Large spruce tree", "largespruce", "megaredwood"),
        RANDOM_REDWOOD("Random spruce tree", new String[]{"randspruce", "randredwood", "randomredwood", "anyredwood"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeType[] choices = new TreeType[]{REDWOOD, TALL_REDWOOD, MEGA_REDWOOD};
                return choices[RANDOM.nextInt(choices.length)].generate(editSession, pos);
            }
        }
        ,
        BIRCH("Birch tree", "birch", "white", "whitebark"),
        TALL_BIRCH("Tall birch tree", "tallbirch"),
        RANDOM_BIRCH("Random birch tree", new String[]{"randbirch", "randombirch"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeType[] choices = new TreeType[]{BIRCH, TALL_BIRCH};
                return choices[RANDOM.nextInt(choices.length)].generate(editSession, pos);
            }
        }
        ,
        JUNGLE("Jungle tree", "jungle"),
        SMALL_JUNGLE("Small jungle tree", "shortjungle", "smalljungle"),
        SHORT_JUNGLE("Short jungle tree", new String[0]){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                return SMALL_JUNGLE.generate(editSession, pos);
            }
        }
        ,
        RANDOM_JUNGLE("Random jungle tree", new String[]{"randjungle", "randomjungle"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeType[] choices = new TreeType[]{JUNGLE, SMALL_JUNGLE};
                return choices[RANDOM.nextInt(choices.length)].generate(editSession, pos);
            }
        }
        ,
        JUNGLE_BUSH("Jungle bush", "junglebush", "jungleshrub"),
        RED_MUSHROOM("Red mushroom", "redmushroom", "redgiantmushroom"),
        BROWN_MUSHROOM("Brown mushroom", "brownmushroom", "browngiantmushroom"),
        RANDOM_MUSHROOM("Random mushroom", new String[]{"randmushroom", "randommushroom"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeType[] choices = new TreeType[]{RED_MUSHROOM, BROWN_MUSHROOM};
                return choices[RANDOM.nextInt(choices.length)].generate(editSession, pos);
            }
        }
        ,
        SWAMP("Swamp tree", "swamp", "swamptree"),
        ACACIA("Acacia tree", "acacia"),
        DARK_OAK("Dark oak tree", "darkoak"),
        PINE("Pine tree", new String[]{"pine"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeGenerator.makePineTree(editSession, pos);
                return true;
            }
        }
        ,
        RANDOM("Random tree", new String[]{"rand", "random"}){

            @Override
            public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
                TreeType[] choices = TreeType.values();
                return choices[RANDOM.nextInt(choices.length)].generate(editSession, pos);
            }
        };

        private static final Map<String, TreeType> lookup;
        private static final Set<String> primaryAliases;
        private final String name;
        private final String[] lookupKeys;

        private TreeType(String name, String ... lookupKeys) {
            this.name = name;
            this.lookupKeys = lookupKeys;
        }

        public static Set<String> getAliases() {
            return Collections.unmodifiableSet(lookup.keySet());
        }

        public static Set<String> getPrimaryAliases() {
            return Collections.unmodifiableSet(primaryAliases);
        }

        public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            return editSession.getWorld().generateTree(this, editSession, pos);
        }

        public String getName() {
            return this.name;
        }

        @Nullable
        public static TreeType lookup(String name) {
            return lookup.get(name.toLowerCase());
        }

        static {
            lookup = new HashMap<String, TreeType>();
            primaryAliases = Sets.newHashSet();
            for (TreeType type : EnumSet.allOf(TreeType.class)) {
                for (String key : type.lookupKeys) {
                    lookup.put(key, type);
                }
                if (type.lookupKeys.length <= 0) continue;
                primaryAliases.add(type.lookupKeys[0]);
            }
        }
    }
}

