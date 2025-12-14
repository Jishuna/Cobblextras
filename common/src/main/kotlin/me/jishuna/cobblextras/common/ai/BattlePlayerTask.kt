package me.jishuna.cobblextras.common.ai

import com.cobblemon.mod.common.battles.BattleBuilder.pvn
import com.cobblemon.mod.common.battles.BattleRegistry.getBattle
import com.cobblemon.mod.common.entity.npc.NPCEntity
import com.google.common.collect.ImmutableMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.behavior.Behavior
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.memory.MemoryStatus
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.cos


class BattlePlayerTask(
    val range: Float = 25.0F,
    val fov: Float = cos(Math.toRadians(15.0)).toFloat(),
) : Behavior<NPCEntity>(ImmutableMap.of(MemoryModuleType.NEAREST_PLAYERS, MemoryStatus.VALUE_PRESENT), 0, 0) {

    override fun canStillUse(level: ServerLevel, owner: NPCEntity, time: Long): Boolean {
        return owner.brain.hasMemoryValue(MemoryModuleType.NEAREST_PLAYERS)
    }

    override fun checkExtraStartConditions(level: ServerLevel, owner: NPCEntity): Boolean {
        return owner.brain.hasMemoryValue(MemoryModuleType.NEAREST_PLAYERS)
    }

    override fun tick(level: ServerLevel, entity: NPCEntity, time: Long) {
        if (level.isClientSide) return

        for (player in entity.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(listOf())) {
            if (areInBattle(entity, player)) continue
            if (!isWithinRange(entity, player)) continue

            pvn(
                player = player as ServerPlayer,
                npcEntity = entity
            )
        }
    }

    private fun areInBattle(npc: NPCEntity, player: Player): Boolean {
        for (battleId in npc.battleIds) {
            val battle = getBattle(battleId)
            if (battle?.getActor(player.uuid) != null) {
                return true
            }
        }
        return false
    }

    private fun isWithinRange(npc: NPCEntity, player: Player): Boolean {
        if (player.distanceToSqr(npc) > range) return false
        val lookVec: Vec3 = npc.lookAngle.normalize()
        val toTarget: Vec3 = player.eyePosition.subtract(npc.eyePosition).normalize()
        val dot = lookVec.dot(toTarget)

        return dot >= fov
    }
}