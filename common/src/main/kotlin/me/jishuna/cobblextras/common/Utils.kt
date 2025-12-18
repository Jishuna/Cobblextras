package me.jishuna.cobblextras.common

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.dialogue.Dialogue
import com.cobblemon.mod.common.api.dialogue.DialogueManager
import com.cobblemon.mod.common.api.dialogue.DialoguePage
import com.cobblemon.mod.common.api.dialogue.input.DialogueInput
import com.cobblemon.mod.common.api.dialogue.input.DialogueNoInput
import com.cobblemon.mod.common.api.text.text
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.battles.BattleRegistry
import com.cobblemon.mod.common.battles.BattleSide
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.entity.npc.NPCBattleActor
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.cobblemon.mod.common.util.isInBattle
import com.cobblemon.mod.common.util.isPartyBusy
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.update
import fr.harmex.cobbledollars.common.utils.extensions.cobbleDollars
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerPlayer
import java.math.BigDecimal

object Utils {
    fun canBattle(player: ServerPlayer, npc: NPCEntity, format: BattleFormat): Boolean {
        if (player.party().filter { it.isFullHealth() }.size < format.battleType.slotsPerActor) return false
        if (player.isPartyBusy() || player.isInBattle()) return false;
        if (npc.party == null || npc.party!!.filter { it.isFullHealth() }.size < format.battleType.slotsPerActor) return false

        return true
    }

    fun showDialogue(
        player: ServerPlayer,
        npc: NPCEntity,
        message: MutableComponent,
        input: DialogueInput = DialogueNoInput(),
    ) {
        val page = DialoguePage.of(
            speaker = "npc",
            lines = listOf(message),
            input = input
        )

        val dialogue = Dialogue.of(
            pages = listOf(page),
            escapeAction = { dialogue -> dialogue.close() },
            speakers = mapOf("npc" to npc.asSpeaker()),
        )

        DialogueManager.startDialogue(player, npc, dialogue)
    }

    fun pvt(
        player: ServerPlayer,
        npcEntity: NPCEntity,
        battleFormat: BattleFormat = BattleFormat.GEN_9_SINGLES,
    ) {
        if (npcEntity.party == null) return

        val playerActor =
            PlayerBattleActor(player.uuid, player.party().toBattleTeam(clone = false, healPokemon = false))
        val npcActor = NPCBattleActor(
            npcEntity,
            npcEntity.party!!.toBattleTeam(clone = true, healPokemon = true),
            npcEntity.skill ?: npcEntity.npc.skill
        )

        playerActor.battleTheme = npcEntity.getBattleTheme()

        BattleRegistry.startBattle(
            battleFormat = battleFormat,
            side1 = BattleSide(playerActor),
            side2 = BattleSide(npcActor)
        ).ifSuccessful { battle ->
            npcEntity.entityData.update(NPCEntity.BATTLE_IDS) { it + battle.battleId }
            battle.onEndHandlers.add { handleBattleEnd(it, player, npcEntity) }
        }
    }


    private fun handleBattleEnd(battle: PokemonBattle, player: ServerPlayer, npc: NPCEntity) {
        val npcLost = battle.losers.any { it.uuid == npc.uuid }
        val message = npc.config.map[if (npcLost) "defeat_message" else "victory_message"]?.asString()?.text() ?: "".text()
        showDialogue(player, npc, message)

        if (npcLost) {
            TrainerManager.battledPlayers.getOrPut(npc.uuid) { mutableSetOf() }.add(player.uuid)
            val money = npc.config.map["money_reward"]?.asDouble() ?: -1.0
            if (money > 0) {
                val balance = player.cobbleDollars.add(BigDecimal.valueOf(money).toBigInteger())
                player.cobbleDollars = balance
            }
        }
    }
}