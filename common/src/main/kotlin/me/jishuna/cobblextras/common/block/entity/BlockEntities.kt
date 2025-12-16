package me.jishuna.cobblextras.common.block.entity

import me.jishuna.cobblextras.common.Cobblextras
import me.jishuna.cobblextras.common.block.Blocks
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

object BlockEntities {

    val WARP_BLOCK = register(Cobblextras.key("warp_block"), BlockEntityType.Builder.of(::WarpBlockEntity, Blocks.WARP_BLOCK))

    private fun <T : BlockEntity?> register(id: ResourceLocation, builder: BlockEntityType.Builder<T>): BlockEntityType<*> {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, builder.build(null)) as BlockEntityType<*>
    }

    @JvmStatic
    fun touch() {
    }
}