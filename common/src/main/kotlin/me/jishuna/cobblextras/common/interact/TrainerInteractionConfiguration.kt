package me.jishuna.cobblextras.common.interact

import com.cobblemon.mod.common.api.dialogue.FunctionDialogueAction
import com.cobblemon.mod.common.api.dialogue.input.DialogueNoInput
import com.cobblemon.mod.common.api.npc.configuration.NPCInteractConfiguration
import com.cobblemon.mod.common.api.text.text
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.entity.npc.NPCEntity
import me.jishuna.cobblextras.common.TrainerManager
import me.jishuna.cobblextras.common.Utils
import me.jishuna.cobblextras.common.Utils.pvt
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.server.level.ServerPlayer

class TrainerInteractionConfiguration(
    override val type: String = "trainer",
) : NPCInteractConfiguration {

    override fun interact(npc: NPCEntity, player: ServerPlayer): Boolean {
        val format = BattleFormat.fromFormatIdentifier(npc.config.map["battle_format"]?.asString() ?: "")
        val canBattle = Utils.canBattle(player, npc, format)
        val defeated = TrainerManager.playerData[player.uuid]?.contains(npc.uuid) ?: false

        val cannotBattleMessage = npc.config.map["cannot_battle_message"]?.asString()?.text() ?: "".text()
        val alreadyDefeatedMessage = npc.config.map["already_defeated_message"]?.asString()?.text() ?: "".text()

        if (!canBattle || defeated) {
            Utils.showDialogue(
                player,
                npc,
                if (!canBattle) cannotBattleMessage else alreadyDefeatedMessage
            )
            return true
        }

        val interactMessage = npc.config.map["interact_message"]?.asString()?.text() ?: "".text()

        Utils.showDialogue(
            player,
            npc,
            interactMessage,
            DialogueNoInput(FunctionDialogueAction { _, _ -> pvt(player, npc, format) })
        )

        return true
    }

    override fun decode(buffer: RegistryFriendlyByteBuf) {
    }

    override fun encode(buffer: RegistryFriendlyByteBuf) {
    }

    override fun readFromNBT(compoundTag: CompoundTag) {
    }

    override fun writeToNBT(compoundTag: CompoundTag) {
    }

    override fun isDifferentTo(other: NPCInteractConfiguration): Boolean {
        return false
    }
}
