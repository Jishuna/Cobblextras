package me.jishuna.cobblextras.common.item

import me.jishuna.cobblextras.common.block.Blocks
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity

object Items {

    @JvmField
    val SPAWN_MARKER_1: Item = Items.registerBlock(BlockItem(Blocks.SPAWN_MARKER_1, Item.Properties().rarity(Rarity.EPIC)))

    @JvmField
    val SPAWN_MARKER_2: Item = Items.registerBlock(BlockItem(Blocks.SPAWN_MARKER_2, Item.Properties().rarity(Rarity.EPIC)))

    @JvmField
    val SPAWN_MARKER_3: Item = Items.registerBlock(BlockItem(Blocks.SPAWN_MARKER_3, Item.Properties().rarity(Rarity.EPIC)))

    @JvmField
    val WARP_BLOCK: Item = Items.registerBlock(BlockItem(Blocks.WARP_BLOCK, Item.Properties().rarity(Rarity.EPIC)))

    @JvmField
    val MOVEMENT_PAD: Item = Items.registerBlock(BlockItem(Blocks.MOVEMENT_PAD, Item.Properties()))

    @JvmStatic
    fun touch() {
    }
}