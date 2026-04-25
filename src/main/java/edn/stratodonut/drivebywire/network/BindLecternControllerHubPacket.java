package edn.stratodonut.drivebywire.network;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.mixinducks.LecternControllerHubDuck;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BindLecternControllerHubPacket(BlockPos lecternPos, BlockPos hubPos) implements CustomPacketPayload {
    public static final Type<BindLecternControllerHubPacket> TYPE = new Type<>(DriveByWireMod.asResource("bind_lectern_controller_hub"));
    public static final StreamCodec<ByteBuf, BindLecternControllerHubPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, BindLecternControllerHubPacket::lecternPos,
        BlockPos.STREAM_CODEC, BindLecternControllerHubPacket::hubPos,
        BindLecternControllerHubPacket::new
    );

    @Override
    public Type<BindLecternControllerHubPacket> type() {
        return TYPE;
    }

    public static void handle(final BindLecternControllerHubPacket payload, final IPayloadContext context) {
        if (!(context.player() instanceof final ServerPlayer player)) {
            return;
        }

        final BlockEntity blockEntity = player.level().getBlockEntity(payload.lecternPos());
        if (!(blockEntity instanceof final LecternControllerHubDuck lecternHub) || !isUsedByPlayer(blockEntity, player)) {
            return;
        }

        lecternHub.drivebywire$setHubPos(payload.hubPos());
        blockEntity.setChanged();
        if (blockEntity instanceof final SmartBlockEntity smartBlockEntity) {
            smartBlockEntity.sendData();
        }
        player.level().playSound(null, payload.hubPos(), WireSounds.PLUG_IN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        player.displayClientMessage(Component.literal("Controller connected!"), true);
    }

    private static boolean isUsedByPlayer(final BlockEntity blockEntity, final Player player) {
        try {
            final Method isUsedBy = blockEntity.getClass().getMethod("isUsedBy", Player.class);
            final Object result = isUsedBy.invoke(blockEntity, player);
            return result instanceof final Boolean used && used;
        } catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            DriveByWireMod.LOGGER.debug("Failed to query lectern user state for {}", blockEntity.getType(), exception);
            return false;
        }
    }
}
