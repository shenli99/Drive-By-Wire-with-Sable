package edn.stratodonut.drivebywire.wire.graph;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class WireNetworkNode {
    private final Map<InputKey, Integer> inputs = new HashMap<>();
    private final long position;
    private final int direction;

    public WireNetworkNode(final BlockPos pos, final Direction direction) {
        this(pos.asLong(), direction.get3DDataValue());
    }

    public WireNetworkNode(final long position, final int direction) {
        this.position = position;
        this.direction = direction;
    }

    public boolean setInput(final InputKey key, final int signal) {
        if (signal <= 0) {
            return inputs.remove(key) != null;
        }

        final Integer previous = inputs.put(key, signal);
        return previous == null || previous != signal;
    }

    public boolean isEmpty() {
        return inputs.isEmpty();
    }

    public int getSignal() {
        return inputs.values().stream().max(Comparator.naturalOrder()).orElse(0);
    }

    public long getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public record InputKey(long sourcePos, String channel) {
    }

    public record WireNetworkSink(long position, int direction) {
        public static WireNetworkSink of(final BlockPos pos, final Direction direction) {
            return new WireNetworkSink(pos.asLong(), direction.get3DDataValue());
        }
    }
}
