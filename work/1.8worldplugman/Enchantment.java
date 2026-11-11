/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.v1_8_R3;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnchantmentArrowDamage;
import net.minecraft.server.v1_8_R3.EnchantmentArrowKnockback;
import net.minecraft.server.v1_8_R3.EnchantmentDepthStrider;
import net.minecraft.server.v1_8_R3.EnchantmentDigging;
import net.minecraft.server.v1_8_R3.EnchantmentDurability;
import net.minecraft.server.v1_8_R3.EnchantmentFire;
import net.minecraft.server.v1_8_R3.EnchantmentFlameArrows;
import net.minecraft.server.v1_8_R3.EnchantmentInfiniteArrows;
import net.minecraft.server.v1_8_R3.EnchantmentKnockback;
import net.minecraft.server.v1_8_R3.EnchantmentLootBonus;
import net.minecraft.server.v1_8_R3.EnchantmentLure;
import net.minecraft.server.v1_8_R3.EnchantmentOxygen;
import net.minecraft.server.v1_8_R3.EnchantmentProtection;
import net.minecraft.server.v1_8_R3.EnchantmentSilkTouch;
import net.minecraft.server.v1_8_R3.EnchantmentSlotType;
import net.minecraft.server.v1_8_R3.EnchantmentThorns;
import net.minecraft.server.v1_8_R3.EnchantmentWaterWorker;
import net.minecraft.server.v1_8_R3.EnchantmentWeaponDamage;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EnumMonsterType;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.LocaleI18n;
import net.minecraft.server.v1_8_R3.MinecraftKey;
import org.bukkit.craftbukkit.v1_8_R3.enchantments.CraftEnchantment;

public abstract class Enchantment {
    private static final Enchantment[] byId = new Enchantment[256];
    public static final Enchantment[] b;
    private static final Map<MinecraftKey, Enchantment> E;
    public static final Enchantment PROTECTION_ENVIRONMENTAL;
    public static final Enchantment PROTECTION_FIRE;
    public static final Enchantment PROTECTION_FALL;
    public static final Enchantment PROTECTION_EXPLOSIONS;
    public static final Enchantment PROTECTION_PROJECTILE;
    public static final Enchantment OXYGEN;
    public static final Enchantment WATER_WORKER;
    public static final Enchantment THORNS;
    public static final Enchantment DEPTH_STRIDER;
    public static final Enchantment DAMAGE_ALL;
    public static final Enchantment DAMAGE_UNDEAD;
    public static final Enchantment DAMAGE_ARTHROPODS;
    public static final Enchantment KNOCKBACK;
    public static final Enchantment FIRE_ASPECT;
    public static final Enchantment LOOT_BONUS_MOBS;
    public static final Enchantment DIG_SPEED;
    public static final Enchantment SILK_TOUCH;
    public static final Enchantment DURABILITY;
    public static final Enchantment LOOT_BONUS_BLOCKS;
    public static final Enchantment ARROW_DAMAGE;
    public static final Enchantment ARROW_KNOCKBACK;
    public static final Enchantment ARROW_FIRE;
    public static final Enchantment ARROW_INFINITE;
    public static final Enchantment LUCK;
    public static final Enchantment LURE;
    public final int id;
    private final int weight;
    public EnchantmentSlotType slot;
    protected String name;

    static {
        E = Maps.newHashMap();
        PROTECTION_ENVIRONMENTAL = new EnchantmentProtection(0, new MinecraftKey("protection"), 10, 0);
        PROTECTION_FIRE = new EnchantmentProtection(1, new MinecraftKey("fire_protection"), 5, 1);
        PROTECTION_FALL = new EnchantmentProtection(2, new MinecraftKey("feather_falling"), 5, 2);
        PROTECTION_EXPLOSIONS = new EnchantmentProtection(3, new MinecraftKey("blast_protection"), 2, 3);
        PROTECTION_PROJECTILE = new EnchantmentProtection(4, new MinecraftKey("projectile_protection"), 5, 4);
        OXYGEN = new EnchantmentOxygen(5, new MinecraftKey("respiration"), 2);
        WATER_WORKER = new EnchantmentWaterWorker(6, new MinecraftKey("aqua_affinity"), 2);
        THORNS = new EnchantmentThorns(7, new MinecraftKey("thorns"), 1);
        DEPTH_STRIDER = new EnchantmentDepthStrider(8, new MinecraftKey("depth_strider"), 2);
        DAMAGE_ALL = new EnchantmentWeaponDamage(16, new MinecraftKey("sharpness"), 10, 0);
        DAMAGE_UNDEAD = new EnchantmentWeaponDamage(17, new MinecraftKey("smite"), 5, 1);
        DAMAGE_ARTHROPODS = new EnchantmentWeaponDamage(18, new MinecraftKey("bane_of_arthropods"), 5, 2);
        KNOCKBACK = new EnchantmentKnockback(19, new MinecraftKey("knockback"), 5);
        FIRE_ASPECT = new EnchantmentFire(20, new MinecraftKey("fire_aspect"), 2);
        LOOT_BONUS_MOBS = new EnchantmentLootBonus(21, new MinecraftKey("looting"), 2, EnchantmentSlotType.WEAPON);
        DIG_SPEED = new EnchantmentDigging(32, new MinecraftKey("efficiency"), 10);
        SILK_TOUCH = new EnchantmentSilkTouch(33, new MinecraftKey("silk_touch"), 1);
        DURABILITY = new EnchantmentDurability(34, new MinecraftKey("unbreaking"), 5);
        LOOT_BONUS_BLOCKS = new EnchantmentLootBonus(35, new MinecraftKey("fortune"), 2, EnchantmentSlotType.DIGGER);
        ARROW_DAMAGE = new EnchantmentArrowDamage(48, new MinecraftKey("power"), 10);
        ARROW_KNOCKBACK = new EnchantmentArrowKnockback(49, new MinecraftKey("punch"), 2);
        ARROW_FIRE = new EnchantmentFlameArrows(50, new MinecraftKey("flame"), 2);
        ARROW_INFINITE = new EnchantmentInfiniteArrows(51, new MinecraftKey("infinity"), 1);
        LUCK = new EnchantmentLootBonus(61, new MinecraftKey("luck_of_the_sea"), 2, EnchantmentSlotType.FISHING_ROD);
        LURE = new EnchantmentLure(62, new MinecraftKey("lure"), 2, EnchantmentSlotType.FISHING_ROD);
        ArrayList<Enchantment> arraylist = Lists.newArrayList();
        Enchantment[] aenchantment = byId;
        int i = aenchantment.length;
        int j = 0;
        while (j < i) {
            Enchantment enchantment = aenchantment[j];
            if (enchantment != null) {
                arraylist.add(enchantment);
            }
            ++j;
        }
        b = arraylist.toArray(new Enchantment[arraylist.size()]);
    }

    public static Enchantment getById(int i) {
        return i >= 0 && i < byId.length ? byId[i] : null;
    }

    protected Enchantment(int i, MinecraftKey minecraftkey, int j, EnchantmentSlotType enchantmentslottype) {
        this.id = i;
        this.weight = j;
        this.slot = enchantmentslottype;
        if (byId[i] != null) {
            throw new IllegalArgumentException("Duplicate enchantment id!");
        }
        Enchantment.byId[i] = this;
        E.put(minecraftkey, this);
        org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment(this));
    }

    public static Enchantment getByName(String s) {
        return E.get(new MinecraftKey(s));
    }

    public static Set<MinecraftKey> getEffects() {
        return E.keySet();
    }

    public int getRandomWeight() {
        return this.weight;
    }

    public int getStartLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int a(int i) {
        return 1 + i * 10;
    }

    public int b(int i) {
        return this.a(i) + 5;
    }

    public int a(int i, DamageSource damagesource) {
        return 0;
    }

    public float a(int i, EnumMonsterType enummonstertype) {
        return 0.0f;
    }

    public boolean a(Enchantment enchantment) {
        return this != enchantment;
    }

    public Enchantment c(String s) {
        this.name = s;
        return this;
    }

    public String a() {
        return "enchantment." + this.name;
    }

    public String d(int i) {
        String s = LocaleI18n.get(this.a());
        return String.valueOf(s) + " " + LocaleI18n.get("enchantment.level." + i);
    }

    public boolean canEnchant(ItemStack itemstack) {
        return this.slot.canEnchant(itemstack.getItem());
    }

    public void a(EntityLiving entityliving, Entity entity, int i) {
    }

    public void b(EntityLiving entityliving, Entity entity, int i) {
    }
}

