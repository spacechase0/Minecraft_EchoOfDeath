package com.kittycatcasey.echoofdeath.data.datagen;

import com.kittycatcasey.echoofdeath.Mod;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class LanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public LanguageProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Mod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(Mod.MODID + ".action_bar.no_death_position", "You have not died.");
        add(Mod.MODID + ".action_bar.death_dimension_invalid", "The dimension you died in no longer exists.");

        addItem(Mod.ECHO_OF_DEATH_ITEM, "Echo of Death");
        add( "item." + Mod.MODID + ".echo_of_death.tooltip", "Teleports you to your last death position on use." );
    }
}
