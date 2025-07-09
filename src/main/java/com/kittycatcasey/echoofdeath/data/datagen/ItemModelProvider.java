package com.kittycatcasey.echoofdeath.data.datagen;

import com.kittycatcasey.echoofdeath.Mod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Mod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(Mod.ECHO_OF_DEATH_ITEM.get());
    }
}
