package edn.stratodonut.drivebywire.mixin.compat.aeronautics;

import edn.stratodonut.drivebywire.compat.WireRedstoneCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity", remap = false)
public abstract class MixinWheelMountBlockEntity {
    @Redirect(
        method = "sable$physicsTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getSignal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I"
        ),
        remap = false
    )
    private int drivebywire$useWireForBrakeSignal(final Level level, final BlockPos pos, final Direction direction) {
        return WireRedstoneCompat.getSignalIncludingReverseWire(level, pos, direction);
    }

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getSignal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I"
        ),
        remap = false
    )
    private int drivebywire$useWireForClientBrakeSignal(final Level level, final BlockPos pos, final Direction direction) {
        return WireRedstoneCompat.getSignalIncludingReverseWire(level, pos, direction);
    }

    @Redirect(
        method = "getSteeringSignal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getSignal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
            ordinal = 0
        ),
        remap = false
    )
    private int drivebywire$useWireForLeftSteeringSignal(final Level level, final BlockPos pos, final Direction direction) {
        return WireRedstoneCompat.getSignalIncludingReverseWire(level, pos, direction);
    }

    @Redirect(
        method = "getSteeringSignal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getSignal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I",
            ordinal = 1
        ),
        remap = false
    )
    private int drivebywire$useWireForRightSteeringSignal(final Level level, final BlockPos pos, final Direction direction) {
        return WireRedstoneCompat.getSignalIncludingReverseWire(level, pos, direction);
    }
}
