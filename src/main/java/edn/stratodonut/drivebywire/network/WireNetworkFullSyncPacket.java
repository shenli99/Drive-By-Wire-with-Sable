package edn.stratodonut.drivebywire.network;

import edn.stratodonut.drivebywire.DriveByWireMod;
import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record WireNetworkFullSyncPacket(CompoundTag network) implements CustomPacketPayload {
    public static final Type<WireNetworkFullSyncPacket> TYPE = new Type<>(DriveByWireMod.asResource("wire_network_full_sync"));
    public static final StreamCodec<ByteBuf, WireNetworkFullSyncPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.COMPOUND_TAG, WireNetworkFullSyncPacket::network,
        WireNetworkFullSyncPacket::new
    );

    @Override
    public Type<WireNetworkFullSyncPacket> type() {
        return TYPE;
    }

    public static void sendTo(final ServerPlayer player) {
        final CompoundTag tag = WireNetworkManager.get(player.serverLevel()).save(new CompoundTag());
        PacketDistributor.sendToPlayer(player, new WireNetworkFullSyncPacket(tag));
    }

    public static void handle(final WireNetworkFullSyncPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> WireNetworkManager.get(context.player().level()).load(payload.network()));
    }
}
