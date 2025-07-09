package com.kittycatcasey.echoofdeath.data.datagen;

import com.kittycatcasey.echoofdeath.Mod;
import com.kittycatcasey.echoofdeath.data.ShapedRetainingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookups) {
        super(output, lookups);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        new ShapedRetainingRecipeBuilder(RecipeCategory.TRANSPORTATION, new ItemStack(Mod.ECHO_OF_DEATH_ITEM.get()))
                .pattern( " O " )
                .pattern( "OCO" )
                .pattern( " O " )
                .overrideRetain( 'C', true )
                .define( 'O', Items.ENDER_PEARL )
                .define( 'C', Items.RECOVERY_COMPASS )
                .unlockedBy( "has_recovery_requirements", has(Items.RECOVERY_COMPASS))
                .save( output, ResourceLocation.fromNamespaceAndPath( Mod.MODID, "echo_of_death" ) );
    }
}
