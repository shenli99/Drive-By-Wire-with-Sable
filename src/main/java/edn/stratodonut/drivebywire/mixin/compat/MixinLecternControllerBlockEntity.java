package edn.stratodonut.drivebywire.mixin.compat;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import edn.stratodonut.drivebywire.mixinducks.LecternControllerHubDuck;
import edn.stratodonut.drivebywire.util.HubItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternControllerBlockEntity.class)
public abstract class MixinLecternControllerBlockEntity implements LecternControllerHubDuck {
    @Unique
    private static final String DRIVEBYWIRE$HUB_KEY = "DriveByWireHub";

    @Unique
    private BlockPos drivebywire$hubPos;

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
        drivebywire$hubPos = compound.contains(DRIVEBYWIRE$HUB_KEY) ? BlockPos.of(compound.getLong(DRIVEBYWIRE$HUB_KEY)) : null;
    }

    @Inject(method = "setController", at = @At("TAIL"), remap = false)
    private void drivebywire$captureHubFromInsertedController(final ItemStack newController, final CallbackInfo ci) {
        drivebywire$hubPos = newController == null ? null : HubItem.getHubPos(newController).orElse(null);
    }

    @Inject(method = "createLinkedController", at = @At("RETURN"), cancellable = true, remap = false)
    private void drivebywire$restoreHubOnGeneratedController(final CallbackInfoReturnable<ItemStack> cir) {
        if (drivebywire$hubPos == null) {
            return;
        }

        final ItemStack controller = cir.getReturnValue();
        HubItem.putHub(controller, drivebywire$hubPos);
        cir.setReturnValue(controller);
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
