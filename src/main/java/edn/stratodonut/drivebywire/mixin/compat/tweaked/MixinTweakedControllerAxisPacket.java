package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlockEntity;
import com.getitemfromblock.create_tweaked_controllers.controller.ControllerRedstoneOutput;
import com.getitemfromblock.create_tweaked_controllers.packet.TweakedLinkedControllerAxisPacket;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.mixinducks.LecternControllerHubDuck;
import edn.stratodonut.drivebywire.util.HubItem;
import java.util.ArrayList;
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
@Mixin(TweakedLinkedControllerAxisPacket.class)
public abstract class MixinTweakedControllerAxisPacket {
    @Shadow private int axis;

    @Inject(method = "handleLectern", at = @At("RETURN"), remap = false)
    private void drivebywire$handleLectern(
        final ServerPlayer player,
        final TweakedLecternControllerBlockEntity lectern,
        final CallbackInfo ci
    ) {
        final List<Byte> axisValues = decodeAxis(axis);
        TweakedControllerWireServerHandler.receiveAxis(player.level(), lectern.getBlockPos(), axisValues);
        if (lectern instanceof final LecternControllerHubDuck lecternHub && lecternHub.drivebywire$getHubPos() != null) {
            TweakedControllerWireServerHandler.receiveAxis(player.level(), lecternHub.drivebywire$getHubPos(), axisValues);
        }
    }

    @Inject(method = "handleItem", at = @At("RETURN"), remap = false)
    private void drivebywire$handleItem(final ServerPlayer player, final ItemStack heldItem, final CallbackInfo ci) {
        HubItem.ifHubPresent(heldItem, pos -> TweakedControllerWireServerHandler.receiveAxis(player.level(), pos, decodeAxis(axis)));
    }

    private static List<Byte> decodeAxis(final int axis) {
        final ControllerRedstoneOutput output = new ControllerRedstoneOutput();
        final List<Byte> axisValues = new ArrayList<>(TweakedControllerWireServerHandler.AXIS_TO_CHANNEL.length);
        output.DecodeAxis(axis);

        for (byte index = 0; index < TweakedControllerWireServerHandler.AXIS_TO_CHANNEL.length; index++) {
            byte value = 0;
            if (index < 8) {
                final boolean highBit = (output.axis[index / 2] & 16) != 0;
                if ((index % 2 == 1) == highBit) {
                    value = (byte) (output.axis[index / 2] & 15);
                }
            } else {
                value = output.axis[index - 4];
            }

            axisValues.add(value);
        }

        return axisValues;
    }
}
