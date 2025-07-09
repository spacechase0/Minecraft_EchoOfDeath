package com.kittycatcasey.echoofdeath;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Mod.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue ECHO_USE_TIME = BUILDER.comment("The time it takes to use the Echo of Death item, in ticks.").defineInRange("echoUseTime", 30, 0, 10 * 20);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int echoUseTime;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        echoUseTime = ECHO_USE_TIME.get();
    }
}
