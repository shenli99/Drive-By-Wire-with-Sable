package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerStopLecternPacket;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.util.HubItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(TweakedLinkedControllerStopLecternPacket.class)
public class MixinTweakedControllerStopLecternPacket {
    @Inject(method = "handleLectern", at = @At("RETURN"), remap = false)
    private void drivebywire$handleLectern(
        final ServerPlayer player,
        final TweakedLecternControllerBlockEntity lectern,
        final CallbackInfo ci
    ) {
        TweakedControllerWireServerHandler.reset(player.level(), lectern.getBlockPos());
    }

    @Inject(method = "handleItem", at = @At("RETURN"), remap = false)
    private void drivebywire$handleItem(final ServerPlayer player, final ItemStack heldItem, final CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> TweakedControllerWireServerHandler.reset(player.level(), pos));
    }
}
