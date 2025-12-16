package me.jishuna.cobblextras.common.block

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BarrierBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SpawnMarkerBlock(
    itemId: ResourceLocation,
    properties: Properties,
) : BarrierBlock(properties) {

    val item: Item by lazy {
        BuiltInRegistries.ITEM.get(itemId)
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext,
    ): VoxelShape {
        return if (collisionContext.isHoldingItem(item)) Shapes.block() else Shapes.empty()
    }
}