package edn.stratodonut.drivebywire.client;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.blocks.ControllerHubBlock;
import edn.stratodonut.drivebywire.mixin.client.MixinLinkedControllerClientHandlerAccessor;
import edn.stratodonut.drivebywire.network.BindLecternControllerHubPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DriveByWireMod.MOD_ID, value = Dist.CLIENT)
public final class ClientLecternControllerHubHandler {
    private ClientLecternControllerHubHandler() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide().isServer()) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        final Player player = event.getEntity();
        if (player == null || player.isSpectator() || !event.getItemStack().isEmpty()) {
            return;
        }

        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof ControllerHubBlock)) {
            return;
        }

        final BlockPos lecternPos = MixinLinkedControllerClientHandlerAccessor.drivebywire$getLecternPos();
        if (lecternPos == null) {
            return;
        }

        PacketDistributor.sendToServer(new BindLecternControllerHubPacket(lecternPos, event.getPos()));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
