package edn.stratodonut.drivebywire;

import edn.stratodonut.drivebywire.items.WireCutterItem;
import edn.stratodonut.drivebywire.items.WireItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DriveByWireMod.MOD_ID);

    public static final DeferredItem<WireItem> WIRE = ITEMS.register("wire", () -> new WireItem(new Item.Properties()));
    public static final DeferredItem<WireCutterItem> WIRE_CUTTER = ITEMS.register(
        "wire_cutter",
        () -> new WireCutterItem(new Item.Properties().stacksTo(1))
    );
    public static final DeferredItem<BlockItem> BACKUP_BLOCK = ITEMS.registerSimpleBlockItem("backup_block", WireBlocks.BACKUP_BLOCK);
    public static final DeferredItem<BlockItem> CONTROLLER_HUB_BLOCK = ITEMS.registerSimpleBlockItem("controller_hub", WireBlocks.CONTROLLER_HUB);
    public static final DeferredItem<BlockItem> TWEAKED_CONTROLLER_HUB_BLOCK = ITEMS.registerSimpleBlockItem(
        "tweaked_controller_hub",
        WireBlocks.TWEAKED_CONTROLLER_HUB
    );

    private WireItems() {
    }

    public static void register(final IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
