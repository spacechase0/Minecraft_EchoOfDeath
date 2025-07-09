package com.kittycatcasey.echoofdeath;

import com.kittycatcasey.echoofdeath.data.datagen.ItemModelProvider;
import com.kittycatcasey.echoofdeath.data.datagen.LanguageProvider;
import com.kittycatcasey.echoofdeath.data.datagen.RecipeProvider;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@net.neoforged.fml.common.Mod(Mod.MODID)
public class Mod {
    public static final String MODID = "kittycatcasey_echoofdeath";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<Item> ECHO_OF_DEATH_ITEM = ITEMS.registerItem(
            "echo_of_death",
            EchoOfDeathItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .component(DataComponents.LORE, new ItemLore(Collections.singletonList(Component.translatable("item." + Mod.MODID + ".echo_of_death.tooltip"))))
    );

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapedRetainingRecipe>> SHAPED_RETAINING = RECIPE_SERIALIZERS.register("shaped_retaining", ShapedRetainingRecipe.Serializer::new);

    public Mod(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.register( this );

        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }

    @SubscribeEvent
    private void datagen(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookups = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new RecipeProvider(output, lookups));
        gen.addProvider(event.includeClient(), new LanguageProvider(output, helper));
        gen.addProvider(event.includeClient(), new ItemModelProvider(output, helper));
    }

    @SubscribeEvent
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
            event.accept(ECHO_OF_DEATH_ITEM);
    }
}
