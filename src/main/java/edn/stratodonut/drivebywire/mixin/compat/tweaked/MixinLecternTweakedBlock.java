package edn.stratodonut.drivebywire.mixin.compat.tweaked;

import com.getitemfromblock.create_tweaked_controllers.block.TweakedLecternControllerBlock;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

@Pseudo
@Mixin(TweakedLecternControllerBlock.class)
public abstract class MixinLecternTweakedBlock implements MultiChannelWireSource {
    @Unique
    private static final List<String> DRIVEBYWIRE$CHANNELS = Stream.concat(
        Arrays.stream(TweakedControllerWireServerHandler.AXIS_TO_CHANNEL),
        Arrays.stream(TweakedControllerWireServerHandler.BUTTON_TO_CHANNEL)
    ).toList();

    @Override
    public List<String> wire$getChannels() {
        return DRIVEBYWIRE$CHANNELS;
    }

    @Override
    public String wire$nextChannel(final String current, final boolean forward) {
        final int currentIndex = DRIVEBYWIRE$CHANNELS.indexOf(current);
        if (currentIndex == -1) {
            return DRIVEBYWIRE$CHANNELS.getFirst();
        }
        return DRIVEBYWIRE$CHANNELS.get(Math.floorMod(currentIndex + (forward ? 1 : -1), DRIVEBYWIRE$CHANNELS.size()));
    }
}
