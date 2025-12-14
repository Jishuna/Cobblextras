package me.jishuna.cobblextras.common.ai

import com.cobblemon.mod.common.api.ai.BehaviourConfigurationContext
import com.cobblemon.mod.common.api.ai.WrapperLivingEntityTask
import com.cobblemon.mod.common.api.ai.asVariables
import com.cobblemon.mod.common.api.ai.config.task.SingleTaskConfig
import com.cobblemon.mod.common.api.npc.configuration.MoLangConfigVariable
import com.cobblemon.mod.common.entity.npc.NPCEntity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.behavior.BehaviorControl
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.SensorType
import kotlin.math.cos

class BattlePlayerTaskConfig : SingleTaskConfig {
    val range = numberVariable("range", "range", 5).asExpressible()
    val fov = numberVariable("fov", "fov", 30).asExpressible()

    override fun getVariables(
        entity: LivingEntity,
        behaviourConfigurationContext: BehaviourConfigurationContext,
    ): List<MoLangConfigVariable> {
        return listOf(range, fov).asVariables()
    }

    override fun createTask(
        entity: LivingEntity,
        behaviourConfigurationContext: BehaviourConfigurationContext,
    ): BehaviorControl<in LivingEntity>? {
        if (entity is NPCEntity) {
            behaviourConfigurationContext.addMemories(MemoryModuleType.NEAREST_PLAYERS)
            behaviourConfigurationContext.addSensors(SensorType.NEAREST_PLAYERS)

            var range = this.range.resolveFloat(behaviourConfigurationContext.runtime)
            range *= range

            var fov = this.fov.resolveFloat(behaviourConfigurationContext.runtime)
            fov = cos(Math.toRadians(fov / 2.0)).toFloat()

            return WrapperLivingEntityTask(
                BattlePlayerTask(range, fov),
                NPCEntity::class.java
            )
        }
        return null
    }
}