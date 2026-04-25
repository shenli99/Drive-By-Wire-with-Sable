package edn.stratodonut.drivebywire.network;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WireSourceSignalPacket(BlockPos sourcePos, String channel, int value) implements CustomPacketPayload {
    public static final Type<WireSourceSignalPacket> TYPE = new Type<>(DriveByWireMod.asResource("wire_source_signal"));
    public static final StreamCodec<ByteBuf, WireSourceSignalPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, WireSourceSignalPacket::sourcePos,
        ByteBufCodecs.STRING_UTF8, WireSourceSignalPacket::channel,
        ByteBufCodecs.INT, WireSourceSignalPacket::value,
        WireSourceSignalPacket::new
    );

    @Override
    public Type<WireSourceSignalPacket> type() {
        return TYPE;
    }

    public static void sendTo(final ServerPlayer player, final BlockPos sourcePos, final String channel, final int value) {
        PacketDistributor.sendToPlayer(player, new WireSourceSignalPacket(sourcePos, channel, value));
    }

    public static void handle(final WireSourceSignalPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> WireNetworkManager.trySetSignalAt(
            context.player().level(),
            payload.sourcePos(),
            payload.channel(),
            payload.value()
        ));
    }
}
