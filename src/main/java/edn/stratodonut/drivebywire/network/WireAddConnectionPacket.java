package edn.stratodonut.drivebywire.network;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WireAddConnectionPacket(BlockPos source, BlockPos sink, Direction direction, String channel) implements CustomPacketPayload {
    public static final Type<WireAddConnectionPacket> TYPE = new Type<>(DriveByWireMod.asResource("wire_add_connection"));
    public static final StreamCodec<ByteBuf, WireAddConnectionPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, WireAddConnectionPacket::source,
        BlockPos.STREAM_CODEC, WireAddConnectionPacket::sink,
        ByteBufCodecs.VAR_INT, packet -> packet.direction().get3DDataValue(),
        ByteBufCodecs.STRING_UTF8, WireAddConnectionPacket::channel,
        (source, sink, direction, channel) -> new WireAddConnectionPacket(source, sink, Direction.from3DDataValue(direction), channel)
    );

    @Override
    public Type<WireAddConnectionPacket> type() {
        return TYPE;
    }

    public static void handle(final WireAddConnectionPacket payload, final IPayloadContext context) {
        if (!(context.player() instanceof final ServerPlayer player)) {
            return;
        }

        final WireNetworkManager.ConnectionResult result = WireNetworkManager.createConnection(
            player.level(),
            payload.source(),
            payload.sink(),
            payload.direction(),
            payload.channel()
        );
        if (result.isSuccess()) {
            player.level().playSound(null, payload.sink(), WireSounds.PLUG_IN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            WireNetworkFullSyncPacket.sendTo(player);
            return;
        }

        player.displayClientMessage(Component.literal(result.getDescription()).withStyle(ChatFormatting.RED), true);
    }
}
