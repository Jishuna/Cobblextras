package me.jishuna.cobblextras.common.block

import com.mojang.serialization.MapCodec
import me.jishuna.cobblextras.common.block.entity.WarpBlockEntity
import me.jishuna.cobblextras.common.item.Items
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class WarpBlock(properties: Properties) : BaseEntityBlock(properties), SimpleWaterloggedBlock {
    private val CODEC: MapCodec<WarpBlock> = simpleCodec(::WarpBlock)

    override fun codec(): MapCodec<out BaseEntityBlock> {
        return CODEC
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return WarpBlockEntity(blockPos, blockState)
    }

    override fun entityInside(blockState: BlockState, level: Level, blockPos: BlockPos, entity: Entity) {
        if (level.isClientSide) return
        val blockEntity = level.getBlockEntity(blockPos)!!
        if (blockEntity is WarpBlockEntity) {
            val vec3 = blockEntity.getTeleportPos(entity, blockPos)
            if (vec3 != null) {
                entity.teleportTo(vec3.x, vec3.y, vec3.z)
            }
        }
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext,
    ): VoxelShape {
        return if (collisionContext.isHoldingItem(Items.WARP_BLOCK)) Shapes.block() else Shapes.empty()
    }

    override fun propagatesSkylightDown(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
    ): Boolean {
        return blockState.fluidState.isEmpty
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.INVISIBLE
    }

    override fun getShadeBrightness(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): Float {
        return 1.0f
    }

    override fun updateShape(
        blockState: BlockState,
        direction: Direction,
        blockState2: BlockState,
        levelAccessor: LevelAccessor,
        blockPos: BlockPos,
        blockPos2: BlockPos,
    ): BlockState {
        if (blockState.getValue(BlockStateProperties.WATERLOGGED) as Boolean) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor))
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
    }

    override fun getFluidState(blockState: BlockState): FluidState {
        return if (blockState.getValue(BlockStateProperties.WATERLOGGED) as Boolean) Fluids.WATER.getSource(false) else super.getFluidState(
            blockState
        )
    }

    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(
            BlockStateProperties.WATERLOGGED,
            blockPlaceContext.level.getFluidState(blockPlaceContext.clickedPos).type === Fluids.WATER
        ) as BlockState
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        builder.add(*arrayOf<Property<*>>(BlockStateProperties.WATERLOGGED))
    }

    override fun pickupBlock(
        player: Player?,
        levelAccessor: LevelAccessor,
        blockPos: BlockPos,
        blockState: BlockState,
    ): ItemStack {
        return if (player != null && player.isCreative) super.pickupBlock(
            player,
            levelAccessor,
            blockPos,
            blockState
        ) else ItemStack.EMPTY
    }

    override fun canPlaceLiquid(
        player: Player?,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        blockState: BlockState,
        fluid: Fluid,
    ): Boolean {
        return if (player != null && player.isCreative) super.canPlaceLiquid(
            player,
            blockGetter,
            blockPos,
            blockState,
            fluid
        ) else false
    }
}