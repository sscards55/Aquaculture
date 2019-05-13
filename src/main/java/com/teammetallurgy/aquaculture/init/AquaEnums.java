package com.teammetallurgy.aquaculture.init;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;

public class AquaEnums {
    public static IItemTier NEPTUNIUM_TOOL = new IItemTier() {
        @Override
        public int getMaxUses() {
            return 2500;
        }
        @Override
        public float getEfficiency() {
            return 9.0F;
        }
        @Override
        public float getAttackDamage() {
            return 6.0F;
        }
        @Override
        public int getHarvestLevel() {
            return 3;
        }
        @Override
        public int getEnchantability() {
            return 15;
        }
        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AquaItems.NEPTUNIUM_INGOT);
        }
    };
    public static IArmorMaterial NEPTINIUM_ARMOR = new IArmorMaterial() {
        @Override
        public int getDurability(@Nonnull EntityEquipmentSlot slot) {
            int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
            return MAX_DAMAGE_ARRAY[slot.getIndex()] * 75;
        }
        @Override
        public int getDamageReductionAmount(@Nonnull EntityEquipmentSlot slot) {
            int[] damageReduction = new int[]{3, 6, 8, 3};
            return damageReduction[slot.getIndex()];
        }
        @Override
        public int getEnchantability() {
            return 15;
        }
        @Override
        @Nonnull
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
        }
        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AquaItems.NEPTUNIUM_INGOT);
        }
        @Override
        @Nonnull
        public String getName() {
            return "neptunium";
        }
        @Override
        public float getToughness() {
            return 2.0F;
        }
    };
}