package edn.stratodonut.drivebywire.mixin.compat;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlockEntity;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerInputPacket;
import edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler;
import edn.stratodonut.drivebywire.util.HubItem;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LinkedControllerInputPacket.class)
public abstract class MixinLinkedControllerInputPacket {
    @Shadow @Final private List<Integer> activatedButtons;
    @Shadow @Final private boolean press;

    @Inject(method = "handleLectern", at = @At("RETURN"), remap = false)
    private void drivebywire$handleLectern(final ServerPlayer player, final LecternControllerBlockEntity lectern, final CallbackInfo ci) {
        LinkedControllerWireServerHandler.receivePressed(player.level(), lectern.getBlockPos(), activatedButtons, press);
    }

    @Inject(method = "handleItem", at = @At("RETURN"), remap = false)
    private void drivebywire$handleItem(final ServerPlayer player, final ItemStack heldItem, final CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> LinkedControllerWireServerHandler.receivePressed(player.level(), pos, activatedButtons, press));
    }
}
