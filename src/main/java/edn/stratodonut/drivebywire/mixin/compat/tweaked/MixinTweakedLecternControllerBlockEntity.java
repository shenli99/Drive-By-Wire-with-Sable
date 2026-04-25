package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import edn.stratodonut.drivebywire.mixinducks.LecternControllerHubDuck;
import edn.stratodonut.drivebywire.util.HubItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity", remap = false)
public abstract class MixinTweakedLecternControllerBlockEntity implements LecternControllerHubDuck {
    @Unique
    private static final String DRIVEBYWIRE$HUB_KEY = "DriveByWireHub";

    @Unique
    private BlockPos drivebywire$hubPos;

    @Shadow(remap = false)
    public abstract ItemStack getController();

    @Inject(method = "write", at = @At("TAIL"), remap = false)
    private void drivebywire$writeHub(
        final CompoundTag compound,
        final HolderLookup.Provider registries,
        final boolean clientPacket,
        final CallbackInfo ci
    ) {
        if (drivebywire$hubPos != null) {
            compound.putLong(DRIVEBYWIRE$HUB_KEY, drivebywire$hubPos.asLong());
        }
    }

    @Inject(method = "writeSafe", at = @At("TAIL"), remap = false)
    private void drivebywire$writeSafeHub(
        final CompoundTag compound,
        final HolderLookup.Provider registries,
        final CallbackInfo ci
    ) {
        if (drivebywire$hubPos != null) {
            compound.putLong(DRIVEBYWIRE$HUB_KEY, drivebywire$hubPos.asLong());
        }
    }

    @Inject(method = "read", at = @At("TAIL"), remap = false)
    private void drivebywire$readHub(
        final CompoundTag compound,
        final HolderLookup.Provider registries,
        final boolean clientPacket,
        final CallbackInfo ci
    ) {
        if (compound.contains(DRIVEBYWIRE$HUB_KEY)) {
            drivebywire$hubPos = BlockPos.of(compound.getLong(DRIVEBYWIRE$HUB_KEY));
        } else {
            final ItemStack controller = getController();
            drivebywire$hubPos = controller == null ? null : HubItem.getHubPos(controller).orElse(null);
        }
    }

    @Inject(method = "setController", at = @At("TAIL"), remap = false)
    private void drivebywire$captureHubFromController(final ItemStack newController, final CallbackInfo ci) {
        drivebywire$hubPos = newController == null ? null : HubItem.getHubPos(newController).orElse(null);
    }

    @Inject(method = "getController", at = @At("RETURN"), cancellable = true, remap = false)
    private void drivebywire$restoreHubOnController(final CallbackInfoReturnable<ItemStack> cir) {
        final ItemStack controller = cir.getReturnValue();
        if (drivebywire$hubPos != null && controller != null && !controller.isEmpty()) {
            HubItem.putHub(controller, drivebywire$hubPos);
            cir.setReturnValue(controller);
        }
    }

    @Override
    public BlockPos drivebywire$getHubPos() {
        return drivebywire$hubPos;
    }

    @Override
    public void drivebywire$setHubPos(final BlockPos hubPos) {
        drivebywire$hubPos = hubPos == null ? null : hubPos.immutable();
    }
}
