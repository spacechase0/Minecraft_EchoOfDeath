package com.kittycatcasey.echoofdeath;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.ListCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShapedRetainingRecipe extends ShapedRecipe {

    private final List<Boolean> retainOverrides;

    public ShapedRetainingRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, List<Boolean> retainOverrides, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
        this.retainOverrides = retainOverrides;
    }

    public ShapedRetainingRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, List<Boolean> retainOverrides, ItemStack result) {
        this(group, category, pattern, retainOverrides, result, true);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Mod.SHAPED_RETAINING.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        var results = super.getRemainingItems(input);

        for ( int i = 0; i < input.size(); ++i )
        {
            boolean override = retainOverrides.get(i);
            if (override)
            {
                results.set(i, input.getItem(i).copy());
            }
        }

        return results;
    }

    public ShapedRecipePattern getPattern() {
        return pattern;
    }

    public List<Boolean> getForcedRetains()
    {
        return retainOverrides;
    }

    public static class Serializer implements RecipeSerializer<ShapedRetainingRecipe>{

        public static final MapCodec<ShapedRetainingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRetainingRecipe::getGroup),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRetainingRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter((ShapedRetainingRecipe r) -> r.pattern),
                Codec.list(Codec.BOOL).fieldOf("forceRetain").forGetter(ShapedRetainingRecipe::getForcedRetains),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter((ShapedRetainingRecipe r) -> r.result),
                Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRetainingRecipe::showNotification)
        ).apply(inst, ShapedRetainingRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRetainingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ShapedRetainingRecipe::getGroup,
                CraftingBookCategory.STREAM_CODEC, ShapedRetainingRecipe::category,
                ShapedRecipePattern.STREAM_CODEC, ShapedRetainingRecipe::getPattern,
                ByteBufCodecs.BOOL.apply(ByteBufCodecs.list()), ShapedRetainingRecipe::getForcedRetains,
                ItemStack.STREAM_CODEC, (ShapedRetainingRecipe r) -> r.result,
                ByteBufCodecs.BOOL, ShapedRetainingRecipe::showNotification,
                ShapedRetainingRecipe::new
        );
        @Override
        public MapCodec<ShapedRetainingRecipe> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedRetainingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
