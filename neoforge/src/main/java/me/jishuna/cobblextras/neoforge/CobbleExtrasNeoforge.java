package me.jishuna.cobblextras.neoforge;

import com.cobblemon.mod.common.api.ai.config.task.TaskConfig;
import com.cobblemon.mod.common.api.npc.configuration.NPCInteractConfiguration;
import me.jishuna.cobblextras.common.Memories;
import me.jishuna.cobblextras.common.ai.BattlePlayerTaskConfig;
import me.jishuna.cobblextras.common.interact.TrainerInteractionConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("cobblextras")
@EventBusSubscriber
public class CobbleExtrasNeoforge {
    public CobbleExtrasNeoforge() {
        TaskConfig.Companion.getTypes().put(ResourceLocation.fromNamespaceAndPath("cobblextras", "battle_player"), BattlePlayerTaskConfig.class);
        NPCInteractConfiguration.Companion.register("trainer", Component.literal("Trainer"), TrainerInteractionConfiguration.class);
    }

    @SubscribeEvent
    private static void onRegister(RegisterEvent event) {
        event.register(Registries.MEMORY_MODULE_TYPE, registry ->
                registry.register(
                        ResourceLocation.fromNamespaceAndPath("cobblextras", "battled_players"),
                        Memories.BATTLED_TRAINERS
                ));
    }
}
