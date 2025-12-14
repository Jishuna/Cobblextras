package me.jishuna.cobblextras.common

import net.minecraft.world.entity.ai.memory.MemoryModuleType
import java.util.*

object Memories {
    @JvmField
    val BATTLED_TRAINERS = MemoryModuleType<Set<UUID>>(Optional.empty())
}