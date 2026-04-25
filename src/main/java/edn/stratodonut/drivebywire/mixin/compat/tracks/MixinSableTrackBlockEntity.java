package edn.stratodonut.drivebywire.mixin.compat.tracks;

import edn.stratodonut.drivebywire.compat.WireRedstoneCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.qwxon.tracks.content.blocks.sable_track.SableTrackBlockEntity", remap = false)
public abstract class MixinSableTrackBlockEntity {
    @Redirect(
        method = "sable$physicsTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getSignal(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)I"
        ),
        remap = false
    )
    private int drivebywire$useWireForTrackPhysicsSignal(final Level level, final BlockPos pos, final Direction direction) {
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
    private int drivebywire$useWireForTrackVisualSignal(final Level level, final BlockPos pos, final Direction direction) {
        return WireRedstoneCompat.getSignalIncludingReverseWire(level, pos, direction);
    }
}
