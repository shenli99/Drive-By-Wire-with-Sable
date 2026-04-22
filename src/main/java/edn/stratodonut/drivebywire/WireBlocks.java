package edn.stratodonut.drivebywire;

import edn.stratodonut.drivebywire.blocks.ControllerHubBlock;
import edn.stratodonut.drivebywire.blocks.TweakedControllerHubBlock;
import edn.stratodonut.drivebywire.blocks.WireNetworkBackupBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DriveByWireMod.MOD_ID);

    public static final DeferredBlock<WireNetworkBackupBlock> BACKUP_BLOCK = BLOCKS.register(
        "backup_block",
        () -> new WireNetworkBackupBlock(commonProperties())
    );
    public static final DeferredBlock<ControllerHubBlock> CONTROLLER_HUB = BLOCKS.register(
        "controller_hub",
        () -> new ControllerHubBlock(commonProperties())
    );
    public static final DeferredBlock<TweakedControllerHubBlock> TWEAKED_CONTROLLER_HUB = BLOCKS.register(
        "tweaked_controller_hub",
        () -> new TweakedControllerHubBlock(commonProperties())
    );

    private WireBlocks() {
    }

    public static void register(final IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    private static BlockBehaviour.Properties commonProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_ORANGE)
            .sound(SoundType.COPPER)
            .strength(3.0F, 6.0F)
            .requiresCorrectToolForDrops();
    }
}
