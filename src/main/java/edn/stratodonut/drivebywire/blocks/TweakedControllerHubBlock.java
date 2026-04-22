package edn.stratodonut.drivebywire.blocks;

import edn.stratodonut.drivebywire.WireSounds;
import edn.stratodonut.drivebywire.compat.TweakedControllerWireServerHandler;
import edn.stratodonut.drivebywire.mixinducks.TweakedControllerDuck;
import edn.stratodonut.drivebywire.util.HubItem;
import edn.stratodonut.drivebywire.wire.MultiChannelWireSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TweakedControllerHubBlock extends Block implements MultiChannelWireSource {
    private static final List<String> CHANNELS = Stream.concat(
        Arrays.stream(TweakedControllerWireServerHandler.AXIS_TO_CHANNEL),
        Arrays.stream(TweakedControllerWireServerHandler.BUTTON_TO_CHANNEL)
    ).toList();

    public TweakedControllerHubBlock(final Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(
        final ItemStack itemStack,
        final BlockState state,
        final Level level,
        final BlockPos blockPos,
        final Player player,
        final InteractionHand interactionHand,
        final BlockHitResult hitResult
    ) {
        if (!(itemStack.getItem() instanceof TweakedControllerDuck)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            HubItem.putHub(itemStack, blockPos);
            level.playSound(null, blockPos, WireSounds.PLUG_IN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            player.displayClientMessage(Component.literal("Controller connected!"), true);
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(
        final BlockState state,
        final BlockGetter level,
        final BlockPos pos,
        final CollisionContext context
    ) {
        return ControllerHubBlock.BOTTOM_AABB;
    }

    @Override
    public List<String> wire$getChannels() {
        return CHANNELS;
    }

    @Override
    public String wire$nextChannel(final String current, final boolean forward) {
        final int currentIndex = CHANNELS.indexOf(current);
        if (currentIndex == -1) {
            return CHANNELS.getFirst();
        }
        return CHANNELS.get(Math.floorMod(currentIndex + (forward ? 1 : -1), CHANNELS.size()));
    }
}
