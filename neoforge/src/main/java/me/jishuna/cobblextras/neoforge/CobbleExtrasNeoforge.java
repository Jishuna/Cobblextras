package me.jishuna.cobblextras.neoforge;

import com.cobblemon.mod.common.api.ai.config.task.TaskConfig;
import com.cobblemon.mod.common.api.npc.configuration.NPCInteractConfiguration;
import me.jishuna.cobblextras.common.Cobblextras;
import me.jishuna.cobblextras.common.ai.BattlePlayerTaskConfig;
import me.jishuna.cobblextras.common.interact.TrainerInteractionConfiguration;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.common.Mod;

@Mod(Cobblextras.MOD_ID)
public class CobbleExtrasNeoforge {
    public CobbleExtrasNeoforge() {
        TaskConfig.Companion.getTypes().put(Cobblextras.key("battle_player"), BattlePlayerTaskConfig.class);
        NPCInteractConfiguration.Companion.register("trainer", Component.literal("Trainer"), TrainerInteractionConfiguration.class);
    }
}
