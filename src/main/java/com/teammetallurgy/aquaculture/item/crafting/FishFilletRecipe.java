package com.teammetallurgy.aquaculture.item.crafting;

import com.google.common.collect.Lists;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fish.FishData;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class FishFilletRecipe extends SpecialRecipe {
    public static final DeferredRegister<IRecipeSerializer<?>> IRECIPE_SERIALIZERS_DEFERRED = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Aquaculture.MOD_ID);
    private static final RegistryObject<IRecipeSerializer<?>> FISH_FILLET_SERIALIZER = registerRecipeSerializer("crafting_special_fish_fillet", new SpecialRecipeSerializer<>(FishFilletRecipe::new));

    private FishFilletRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return new ResourceLocation(Aquaculture.MOD_ID, "fish_fillet");
    }

    @Override
    public boolean matches(@Nonnull CraftingInventory craftingInventory, @Nonnull World world) {
        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.newArrayList();

        for (int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
            ItemStack slotStack = craftingInventory.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (AquacultureAPI.FISH_DATA.hasFilletAmount(slotStack.getItem())) {
                    if (!stack.isEmpty()) {
                        return false;
                    }
                    stack = slotStack;
                } else {
                    if (!(slotStack.getItem().isIn(AquacultureAPI.Tags.FILLET_KNIFE) && (slotStack.isDamageable() || isKnifeNeptunium(slotStack.getItem())) && slotStack.getItem() instanceof TieredItem)) {
                        return false;
                    }
                    list.add(slotStack);
                }
            }
        }
        return !stack.isEmpty() && !list.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull CraftingInventory craftingInventory) {
        ItemStack fish = ItemStack.EMPTY;
        Item knife = null;

        for (int i = 0; i < craftingInventory.getSizeInventory(); ++i) {
            ItemStack stackSlot = craftingInventory.getStackInSlot(i);
            if (!stackSlot.isEmpty()) {
                Item item = stackSlot.getItem();
                if (AquacultureAPI.FISH_DATA.hasFilletAmount(item.getItem())) {
                    if (!fish.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    fish = stackSlot.copy();
                } else {
                    if (!(item.isIn(AquacultureAPI.Tags.FILLET_KNIFE))) {
                        return ItemStack.EMPTY;
                    }
                    knife = item;
                }
            }
        }
        if (!fish.isEmpty() && knife != null) {
            int filletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(fish.getItem());
            if (AquaConfig.BASIC_OPTIONS.randomWeight.get() && fish.getTag() != null && fish.getTag().contains("fishWeight")) {
                filletAmount = FishData.getFilletAmountFromWeight(fish.getTag().getDouble("fishWeight"));
            }
            if (isKnifeNeptunium(knife)) {
                filletAmount += filletAmount * (25.0F / 100.0F);
            }
            return new ItemStack(AquaItems.FISH_FILLET, filletAmount);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory craftingInventory) {
        NonNullList<ItemStack> list = NonNullList.withSize(craftingInventory.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = craftingInventory.getStackInSlot(i);
            if (stack.getItem().isIn(AquacultureAPI.Tags.FILLET_KNIFE)) {
                ItemStack knife = stack.copy();
                if (!isKnifeNeptunium(knife.getItem())) {
                    if (knife.attemptDamageItem(1, new Random(), null)) {
                        knife.shrink(1);
                    }
                }
                list.set(i, knife);
            }
        }
        return list;
    }

    public static boolean isKnifeNeptunium(@Nonnull Item knife) {
        return knife instanceof TieredItem && ((TieredItem) knife).getTier() == AquacultureAPI.MATS.NEPTUNIUM;
    }

    @Override
    @Nonnull
    public IRecipeSerializer<?> getSerializer() {
        return FISH_FILLET_SERIALIZER.get();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    public static RegistryObject<IRecipeSerializer<?>> registerRecipeSerializer(String name, IRecipeSerializer<?> serializer) {
        return IRECIPE_SERIALIZERS_DEFERRED.register(name, () -> serializer);
    }
}