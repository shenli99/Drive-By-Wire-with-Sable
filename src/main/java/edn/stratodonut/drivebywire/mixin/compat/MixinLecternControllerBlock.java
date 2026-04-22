package edn.stratodonut.drivebywire.mixin.compat;

import com.simibubi.create.content.redstone.link.controller.LecternControllerBlock;
import edn.stratodonut.drivebywire.compat.LinkedControllerWireServerHandler;
import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;
import java.util.Arrays;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LecternControllerBlock.class)
public abstract class MixinLecternControllerBlock implements MultiChannelWireSource {
    @Unique
    private static final List<String> DRIVEBYWIRE$CHANNELS = Arrays.stream(LinkedControllerWireServerHandler.KEY_TO_CHANNEL).toList();

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
