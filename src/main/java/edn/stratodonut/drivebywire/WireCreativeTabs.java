package edn.stratodonut.drivebywire;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        DriveByWireMod.MOD_ID
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_CREATIVE_TAB = CREATIVE_MODE_TABS.register(
        "base",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.drivebywire"))
            .icon(() -> WireItems.WIRE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(WireItems.WIRE.get());
                output.accept(WireItems.WIRE_CUTTER.get());
                output.accept(WireItems.BACKUP_BLOCK.get());
                output.accept(WireItems.CONTROLLER_HUB_BLOCK.get());
                if (ModList.get().isLoaded("create_tweaked_controllers")) {
                    output.accept(WireItems.TWEAKED_CONTROLLER_HUB_BLOCK.get());
                }
            })
            .build()
    );

    private WireCreativeTabs() {
    }

    public static void register(final IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
