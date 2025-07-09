package com.kittycatcasey.echoofdeath;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;
import java.util.Set;

public class EchoOfDeathItem extends Item {
    public EchoOfDeathItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return Config.echoUseTime;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity){
        if ( !(livingEntity instanceof Player) )
            return stack;

        Player player = (Player) livingEntity;

        var deathPosOpt = player.getLastDeathLocation();
        if ( !deathPosOpt.isEmpty())
        {
            var deathPos = deathPosOpt.get();
            var deathCoords = deathPos.pos();

            level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
            if (!level.isClientSide())
            {
                var targetDimId = deathPos.dimension();
                var targetDim = level.getServer().getLevel(targetDimId);
                if (targetDim != null) {
                    stack.consume(1, player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(GameEvent.TELEPORT, player.position(), GameEvent.Context.of(player));
                    //((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, player.position().x, player.position().y, player.position().z, 32, 0, 1, 0, 0.3f );
                    player.teleportTo(targetDim, deathCoords.getX(), deathCoords.getY(), deathCoords.getZ(), Set.of(), player.getYRot(), player.getXRot());
                }
                else {
                    player.displayClientMessage(Component.translatable(Mod.MODID + ".action_bar.death_dimension_invalid" ), true);
                }
            }
        }
        else
        {
            if (!level.isClientSide())
            {
                player.displayClientMessage(Component.translatable(Mod.MODID + ".action_bar.no_death_position" ), true);
            }
        }

        return stack;
    }
}
