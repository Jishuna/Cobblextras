package me.jishuna.cobblextras.common.block

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.MapColor
import java.util.function.Function

object Blocks {

    @JvmField
    val INVISIBLE_PARTICLE_BLOCKS = mutableSetOf<Block>()

    @JvmField
    val SPAWN_MARKER_1: Block = spawnMarker("cobblextras:spawn_marker_1")

    @JvmField
    val SPAWN_MARKER_2: Block = spawnMarker("cobblextras:spawn_marker_2")

    @JvmField
    val SPAWN_MARKER_3: Block = spawnMarker("cobblextras:spawn_marker_3")

    @JvmField
    val WARP_BLOCK: Block = Blocks.register(
        "cobblextras:warp_block", WarpBlock(
            BlockBehaviour.Properties.of()
                .replaceable()
                .strength(-1.0f, 3600000.8f)
                .mapColor(MapColor.NONE)
                .noLootTable()
                .noOcclusion()
                .noCollission()
        )
    )

    @JvmStatic
    fun touch() {
        INVISIBLE_PARTICLE_BLOCKS.add(WARP_BLOCK)
    }

    private fun spawnMarker(id: String): Block {
        val block = Blocks.register(
            id, SpawnMarkerBlock(
                ResourceLocation.parse(id),
                BlockBehaviour.Properties.of()
                    .replaceable()
                    .strength(-1.0f, 3600000.8f)
                    .mapColor(waterloggedMapColor(MapColor.NONE))
                    .noLootTable()
                    .noOcclusion()
            )
        )

        INVISIBLE_PARTICLE_BLOCKS.add(block)
        return block;
    }

    private fun waterloggedMapColor(mapColor: MapColor): Function<BlockState, MapColor> {
        return Function { blockState: BlockState -> if (blockState.getValue(BlockStateProperties.WATERLOGGED) as Boolean) MapColor.WATER else mapColor }
    }
}