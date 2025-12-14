package me.jishuna.cobblextras.common.mixin;

import com.cobblemon.mod.common.entity.npc.NPCEntity;
import com.mojang.serialization.DataResult;
import me.jishuna.cobblextras.common.TrainerManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.UUID;

@Mixin(NPCEntity.class)
public abstract class NPCEntityMixin extends AgeableMob {

    private NPCEntityMixin(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "saveWithoutId",
            at = @At("TAIL")
    )
    private void onSaveWithoutId(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag returned = cir.getReturnValue();

        Set<UUID> battled = TrainerManager.INSTANCE.getBattledPlayers().get(getUUID());
        if (battled != null) {
            DataResult<Tag> result = UUIDUtil.CODEC_SET.encodeStart(NbtOps.INSTANCE, battled);

            result.result().ifPresent(encoded -> returned.put("battled_players", encoded));
        }
    }

    @Inject(
            method = "load",
            at = @At("TAIL")
    )
    private void onLoad(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("battled_players")) {
            DataResult<Set<UUID>> readResult = UUIDUtil.CODEC_SET.parse(NbtOps.INSTANCE, nbt.get("battled_players"));

            readResult.result().ifPresent(uuidSet -> TrainerManager.INSTANCE.getBattledPlayers().put(getUUID(), uuidSet));
        }
    }
}
