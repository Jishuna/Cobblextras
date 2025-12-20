package me.jishuna.cobblextras.common

import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.LevelResource
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object TrainerManager {
    private var playerDataFolder: Path? = null
    val playerData = mutableMapOf<UUID, MutableSet<UUID>>()

    @JvmStatic
    fun onPlayerLogin(player: ServerPlayer) {
        if (playerDataFolder == null) {
            checkPlayerDataFolder(player.server)
        }

        val playerFile = playerDataFolder!!.resolve(player.uuid.toString() + ".dat")
        val data = NbtIo.read(playerFile) ?: return

        if (data.contains("defeated_trainers")) {
            val readResult = UUIDUtil.CODEC_SET.parse(NbtOps.INSTANCE, data.get("defeated_trainers"))

            readResult.result().ifPresent { uuidSet: Set<UUID> ->
                playerData[player.uuid] = uuidSet.toMutableSet()
            }
        }
    }

    @JvmStatic
    fun onPlayerLogout(player: ServerPlayer) {
        if (playerDataFolder == null) {
            checkPlayerDataFolder(player.server)
        }

        if (!playerData.containsKey(player.uuid)) return

        val playerFile = playerDataFolder!!.resolve(player.uuid.toString() + ".dat")
        val tag = CompoundTag()

        val result = UUIDUtil.CODEC_SET.encodeStart(NbtOps.INSTANCE, playerData[player.uuid])
        result.result().ifPresent { encoded: Tag -> tag.put("defeated_trainers", encoded) }

        NbtIo.write(tag, playerFile)
    }

    private fun checkPlayerDataFolder(server: MinecraftServer) {
        playerDataFolder = server.getWorldPath(LevelResource.ROOT).resolve("cobblextras")
        if (!Files.exists(playerDataFolder!!)) {
            Files.createDirectory(playerDataFolder!!)
        }
    }
}