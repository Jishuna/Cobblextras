package me.jishuna.cobblextras.common.mixin;

import me.jishuna.cobblextras.common.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    private ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Shadow
    private final Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "getMarkerParticleTarget", at = @At("HEAD"), cancellable = true)
    private void onGetMarkerParticleTarget(CallbackInfoReturnable<Block> ci) {
        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            ItemStack itemStack = this.minecraft.player.getMainHandItem();
            Item item = itemStack.getItem();
            if (item instanceof BlockItem blockItem && Blocks.SPAWN_MARKERS.contains(blockItem.getBlock())) {
                ci.setReturnValue(blockItem.getBlock());
            }
        }
    }
}
