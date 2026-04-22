package edn.stratodonut.drivebywire.compat;

import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.createmod.catnip.data.WorldAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class TweakedControllerWireServerHandler {
    public static final String[] BUTTON_TO_CHANNEL = new String[] {
        "buttonA",
        "buttonB",
        "buttonX",
        "buttonY",
        "shoulderLeft",
        "shoulderRight",
        "buttonBack",
        "buttonStart",
        "buttonGuide",
        "leftJoyStickClick",
        "rightJoyStickClick",
        "dPadUp",
        "dPadRight",
        "dPadDown",
        "dPadLeft"
    };
    public static final String[] AXIS_TO_CHANNEL = new String[] {
        "axisLeftX+",
        "axisLeftX-",
        "axisLeftY+",
        "axisLeftY-",
        "axisRightX+",
        "axisRightX-",
        "axisRightY+",
        "axisRightY-",
        "axisTriggerLeft",
        "axisTriggerRight"
    };
    private static final int TIMEOUT = 30;
    private static final WorldAttached<Map<Pair<BlockPos, Integer>, Integer>> BUTTON_TIMEOUTS = new WorldAttached<>(level -> new HashMap<>());
    private static final WorldAttached<Map<Pair<BlockPos, Integer>, Integer>> AXIS_TIMEOUTS = new WorldAttached<>(level -> new HashMap<>());

    private TweakedControllerWireServerHandler() {
    }

    public static void tick(final Level level) {
        tickTimeouts(level, BUTTON_TIMEOUTS.get(level), BUTTON_TO_CHANNEL);
        tickTimeouts(level, AXIS_TIMEOUTS.get(level), AXIS_TO_CHANNEL);
    }

    public static void receiveAxis(final Level level, final BlockPos pos, final List<Byte> axisStates) {
        final Map<Pair<BlockPos, Integer>, Integer> timeoutMap = AXIS_TIMEOUTS.get(level);
        for (int index = 0; index < axisStates.size() && index < AXIS_TO_CHANNEL.length; index++) {
            final int value = Byte.toUnsignedInt(axisStates.get(index));
            final Pair<BlockPos, Integer> key = Pair.of(pos.immutable(), index);
            ControllerSignalStore.setSignal(level, pos, AXIS_TO_CHANNEL[index], value);
            if (value > 0) {
                timeoutMap.put(key, TIMEOUT);
            } else {
                timeoutMap.remove(key);
            }
        }
    }

    public static void receiveButton(final Level level, final BlockPos pos, final List<Boolean> buttonStates) {
        final Map<Pair<BlockPos, Integer>, Integer> timeoutMap = BUTTON_TIMEOUTS.get(level);
        for (int index = 0; index < buttonStates.size() && index < BUTTON_TO_CHANNEL.length; index++) {
            final boolean pressed = Boolean.TRUE.equals(buttonStates.get(index));
            final Pair<BlockPos, Integer> key = Pair.of(pos.immutable(), index);
            ControllerSignalStore.setSignal(level, pos, BUTTON_TO_CHANNEL[index], pressed ? 15 : 0);
            if (pressed) {
                timeoutMap.put(key, TIMEOUT);
            } else {
                timeoutMap.remove(key);
            }
        }
    }

    public static void reset(final Level level, final BlockPos pos) {
        final Map<Pair<BlockPos, Integer>, Integer> axisTimeoutMap = AXIS_TIMEOUTS.get(level);
        final Map<Pair<BlockPos, Integer>, Integer> buttonTimeoutMap = BUTTON_TIMEOUTS.get(level);
        for (int index = 0; index < AXIS_TO_CHANNEL.length; index++) {
            ControllerSignalStore.setSignal(level, pos, AXIS_TO_CHANNEL[index], 0);
            axisTimeoutMap.remove(Pair.of(pos, index));
        }
        for (int index = 0; index < BUTTON_TO_CHANNEL.length; index++) {
            ControllerSignalStore.setSignal(level, pos, BUTTON_TO_CHANNEL[index], 0);
            buttonTimeoutMap.remove(Pair.of(pos, index));
        }
    }

    private static void tickTimeouts(
        final Level level,
        final Map<Pair<BlockPos, Integer>, Integer> timeoutMap,
        final String[] channels
    ) {
        final Iterator<Map.Entry<Pair<BlockPos, Integer>, Integer>> iterator = timeoutMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Pair<BlockPos, Integer>, Integer> entry = iterator.next();
            final int ttl = entry.getValue() - 1;
            if (ttl <= 0) {
                final Pair<BlockPos, Integer> key = entry.getKey();
                ControllerSignalStore.setSignal(level, key.getFirst(), channels[key.getSecond()], 0);
                iterator.remove();
                continue;
            }

            entry.setValue(ttl);
        }
    }
}
