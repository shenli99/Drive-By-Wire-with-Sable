package edn.stratodonut.drivebywire.compat;

import edn.stratodonut.drivebywire.wire.WireNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public final class WireRedstoneCompat {
    private WireRedstoneCompat() {
    }

    public static int getSignalIncludingReverseWire(
        final Level level,
        final BlockPos queriedPos,
        final Direction queriedDirection
    ) {
        final int baseSignal = level.getSignal(queriedPos, queriedDirection);
        final BlockPos sinkPos = queriedPos.relative(queriedDirection);
        final Direction sinkFace = queriedDirection.getOpposite();
        return Math.max(baseSignal, WireNetworkManager.get(level).getSignalAt(sinkPos, sinkFace));
    }
}
