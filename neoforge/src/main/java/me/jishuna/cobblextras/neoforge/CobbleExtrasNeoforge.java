package me.jishuna.cobblextras.neoforge;

import com.cobblemon.mod.common.api.ai.config.task.TaskConfig;
import com.cobblemon.mod.common.api.npc.configuration.NPCInteractConfiguration;
import me.jishuna.cobblextras.common.Cobblextras;
import me.jishuna.cobblextras.common.ai.BattlePlayerTaskConfig;
import me.jishuna.cobblextras.common.block.Blocks;
import me.jishuna.cobblextras.common.block.entity.BlockEntities;
import me.jishuna.cobblextras.common.interact.TrainerInteractionConfiguration;
import me.jishuna.cobblextras.common.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Cobblextras.MOD_ID)
@EventBusSubscriber
public class CobbleExtrasNeoforge {
    public CobbleExtrasNeoforge() {
        TaskConfig.Companion.getTypes().put(Cobblextras.key("battle_player"), BattlePlayerTaskConfig.class);
        NPCInteractConfiguration.Companion.register("trainer", Component.literal("Trainer"), TrainerInteractionConfiguration.class);
    }

    @SubscribeEvent
    private static void onRegister(RegisterEvent event) {
        Blocks.touch();
        BlockEntities.touch();
        Items.touch();
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            Blocks.INVISIBLE_PARTICLE_BLOCKS.forEach(event::accept);
        }
    }
}
