package edn.stratodonut.drivebywire.compat;

import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class ControllerSignalStore {
    private ControllerSignalStore() {
    }

    public static void setSignal(final Level level, final BlockPos pos, final String channel, final int value) {
        WireNetworkManager.trySetSignalAt(level, pos, channel, value);
    }

    public static void clear(final Level level, final BlockPos pos) {
        WireNetworkManager.get(level).clearSourceSignals(level, pos);
    }

    public static Map<String, Integer> getSignals(final Level level, final BlockPos pos) {
        return WireNetworkManager.get(level).getSourceSignals(pos);
    }
}
