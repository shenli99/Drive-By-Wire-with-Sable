package edn.stratodonut.drivebywire.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class WirePackets {
    private WirePackets() {
    }

    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar("1")
            .playToClient(WireNetworkFullSyncPacket.TYPE, WireNetworkFullSyncPacket.STREAM_CODEC, WireNetworkFullSyncPacket::handle)
            .playToServer(WireAddConnectionPacket.TYPE, WireAddConnectionPacket.STREAM_CODEC, WireAddConnectionPacket::handle)
            .playToServer(WireRemoveConnectionPacket.TYPE, WireRemoveConnectionPacket.STREAM_CODEC, WireRemoveConnectionPacket::handle)
            .playToServer(WireNetworkRequestSyncPacket.TYPE, WireNetworkRequestSyncPacket.STREAM_CODEC, WireNetworkRequestSyncPacket::handle);
    }
}
