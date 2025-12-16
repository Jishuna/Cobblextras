package me.jishuna.cobblextras.common.block.entity

import com.cobblemon.mod.common.util.toVec3d
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class WarpBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(BlockEntities.WARP_BLOCK, pos, state) {
    private var target: BlockPos? = null

    fun getTeleportPos(entity: Entity, blockPos: BlockPos): Vec3? {
        if (target == null) {
            return null;
        }

        val offset = entity.position().subtract(blockPos.toVec3d())
        return target!!.toVec3d().add(offset);
    }

    override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.loadAdditional(tag, provider)

        NbtUtils.readBlockPos(tag, "target").filter {
            Level.isInSpawnableBounds(it)
        }.ifPresent { this.target = it }
    }

    override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
        super.saveAdditional(tag, provider)

        if (target != null) {
            tag.put("target", NbtUtils.writeBlockPos(target!!))
        }
    }
}