package me.jishuna.cobblextras.common

import java.util.*

object TrainerManager {
    val battledPlayers = mutableMapOf<UUID, MutableSet<UUID>>()
}