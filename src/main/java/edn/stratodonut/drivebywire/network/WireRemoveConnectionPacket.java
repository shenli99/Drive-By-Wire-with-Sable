package edn.stratodonut.drivebywire.network;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WireRemoveConnectionPacket(BlockPos source, BlockPos sink, Direction direction, String channel) implements CustomPacketPayload {
    public static final Type<WireRemoveConnectionPacket> TYPE = new Type<>(DriveByWireMod.asResource("wire_remove_connection"));
    public static final StreamCodec<ByteBuf, WireRemoveConnectionPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, WireRemoveConnectionPacket::source,
        BlockPos.STREAM_CODEC, WireRemoveConnectionPacket::sink,
        ByteBufCodecs.VAR_INT, packet -> packet.direction().get3DDataValue(),
        ByteBufCodecs.STRING_UTF8, WireRemoveConnectionPacket::channel,
        (source, sink, direction, channel) -> new WireRemoveConnectionPacket(source, sink, Direction.from3DDataValue(direction), channel)
    );

    @Override
    public Type<WireRemoveConnectionPacket> type() {
        return TYPE;
    }

    public static void handle(final WireRemoveConnectionPacket payload, final IPayloadContext context) {
        if (!(context.player() instanceof final ServerPlayer player)) {
            return;
        }

        if (WireNetworkManager.removeConnection(player.level(), payload.source(), payload.sink(), payload.direction(), payload.channel())) {
            player.level().playSound(null, payload.sink(), WireSounds.PLUG_OUT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        WireNetworkFullSyncPacket.sendTo(player);
    }
}
