package edn.stratodonut.drivebywire.wire;

import java.util.List;

public interface MultiChannelWireSource {
    List<String> wire$getChannels();

    String wire$nextChannel(String current, boolean forward);
}
