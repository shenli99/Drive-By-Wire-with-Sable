package edn.stratodonut.drivebywire;

import edn.stratodonut.drivebywire.blocks.WireNetworkBackupBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
        Registries.BLOCK_ENTITY_TYPE,
        DriveByWireMod.MOD_ID
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireNetworkBackupBlockEntity>> BACKUP_BLOCK = BLOCK_ENTITY_TYPES.register(
        "backup_block",
        () -> BlockEntityType.Builder.of(WireNetworkBackupBlockEntity::new, WireBlocks.BACKUP_BLOCK.get()).build(null)
    );

    private WireBlockEntities() {
    }

    public static void register(final IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
