package me.jishuna.cobblextras.common.block

import com.cobblemon.mod.common.util.toVec3d
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class MovementPadBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {
    private val CODEC: MapCodec<HorizontalDirectionalBlock> = simpleCodec(::MovementPadBlock)
    private val shape: VoxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0)

    override fun codec(): MapCodec<out HorizontalDirectionalBlock> {
        return CODEC
    }

    override fun entityInside(blockState: BlockState, level: Level, blockPos: BlockPos, entity: Entity) {
        if (level.isClientSide) return

        val direction = blockState.getValue(FACING)
        val movement = direction.step().mul(0.25F).toVec3d()
        if (level.noBlockCollision(entity, entity.boundingBox.move(movement))) {
            entity.teleportRelative(movement.x, movement.y, movement.z)
        }
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext,
    ): VoxelShape {
        return shape
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(*arrayOf<Property<*>>(FACING))
    }

    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, blockPlaceContext.horizontalDirection) as BlockState
    }
}