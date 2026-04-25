package edn.stratodonut.drivebywire.client;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.blocks.TweakedControllerHubBlock;
import edn.stratodonut.drivebywire.mixin.client.MixinTweakedLinkedControllerClientHandlerAccessor;
import edn.stratodonut.drivebywire.network.BindLecternControllerHubPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DriveByWireMod.MOD_ID, value = Dist.CLIENT)
public final class ClientTweakedLecternControllerHubHandler {
    private ClientTweakedLecternControllerHubHandler() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getSide().isServer() || !ModList.get().isLoaded("create_tweaked_controllers")) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        final Player player = event.getEntity();
        if (player == null || player.isSpectator() || !event.getItemStack().isEmpty()) {
            return;
        }

        if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof TweakedControllerHubBlock)) {
            return;
        }

        final BlockPos lecternPos = MixinTweakedLinkedControllerClientHandlerAccessor.drivebywire$getLecternPos();
        if (lecternPos == null) {
            return;
        }

        PacketDistributor.sendToServer(new BindLecternControllerHubPacket(lecternPos, event.getPos()));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
