package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerButtonPacket;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.util.HubItem;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(TweakedLinkedControllerButtonPacket.class)
public abstract class MixinTweakedControllerButtonPacket {
    @Shadow private short buttonStates;

    @Inject(method = "handleLectern", at = @At("RETURN"), remap = false)
    private void drivebywire$handleLectern(
        final ServerPlayer player,
        final TweakedLecternControllerBlockEntity lectern,
        final CallbackInfo ci
    ) {
        final ControllerRedstoneOutput output = new ControllerRedstoneOutput();
        output.DecodeButtons(buttonStates);
        TweakedControllerWireServerHandler.receiveButton(player.level(), lectern.getBlockPos(), List.of(output.buttons));
    }

    @Inject(method = "handleItem", at = @At("RETURN"), remap = false)
    private void drivebywire$handleItem(final ServerPlayer player, final ItemStack heldItem, final CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> {
            final ControllerRedstoneOutput output = new ControllerRedstoneOutput();
            output.DecodeButtons(buttonStates);
            TweakedControllerWireServerHandler.receiveButton(player.level(), pos, List.of(output.buttons));
        });
    }
}
