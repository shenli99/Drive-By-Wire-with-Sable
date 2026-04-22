package edn.stratodonut.drivebywire.network;

import edn.stratodonut.drivebywire.DriveByWireMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WireNetworkRequestSyncPacket() implements CustomPacketPayload {
    public static final WireNetworkRequestSyncPacket INSTANCE = new WireNetworkRequestSyncPacket();
    public static final Type<WireNetworkRequestSyncPacket> TYPE = new Type<>(DriveByWireMod.asResource("wire_network_request_sync"));
    public static final StreamCodec<ByteBuf, WireNetworkRequestSyncPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<WireNetworkRequestSyncPacket> type() {
        return TYPE;
    }

    public static void handle(final WireNetworkRequestSyncPacket payload, final IPayloadContext context) {
        if (!(context.player() instanceof final ServerPlayer player)) {
            return;
        }

        WireNetworkFullSyncPacket.sendTo(player);
    }
}
