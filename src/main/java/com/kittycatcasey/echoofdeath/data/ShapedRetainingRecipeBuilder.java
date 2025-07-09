package com.kittycatcasey.echoofdeath.data;

import com.google.common.collect.Lists;
import com.kittycatcasey.echoofdeath.ShapedRetainingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShapedRetainingRecipeBuilder extends ShapedRecipeBuilder {
    private Map<Character, Boolean> retainOverrides;

    public ShapedRetainingRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        super(category, result, count);
    }

    public ShapedRetainingRecipeBuilder(RecipeCategory p_249996_, ItemStack result) {
        super(p_249996_, result);
    }

    public ShapedRetainingRecipeBuilder pattern(String pattern) {
        super.pattern(pattern);

        retainOverrides = new HashMap<Character, Boolean>();

        return this;
    }

    // TODO: use the symbol system like base ShapedRecipeBuilder
    public ShapedRetainingRecipeBuilder overrideRetain(Character symbol, boolean override)
    {
        retainOverrides.put(symbol, override);
        return this;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        List<Boolean> retains = NonNullList.withSize(rows.size() * rows.get(0).length(), false);
        for ( int ir = 0; ir < rows.size(); ++ir )
        {
            String row = rows.get(ir);
            for ( int ic = 0; ic < row.length(); ++ic )
            {
                if ( retainOverrides.containsKey( row.charAt( ic ) ) )
                    retains.set(ir * row.length() + ic, retainOverrides.get( row.charAt( ic ) ) );
            }
        }

        // Copied from the parent class and modified to add our stuff
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(id);
        Advancement.Builder advancement$builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        ShapedRetainingRecipe shapedrecipe = new ShapedRetainingRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), shapedrecipepattern, retains, this.resultStack, this.showNotification);
        recipeOutput.accept(id, shapedrecipe, advancement$builder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }
}
