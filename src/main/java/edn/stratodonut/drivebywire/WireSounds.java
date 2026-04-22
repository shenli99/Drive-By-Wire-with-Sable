package edn.stratodonut.drivebywire;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class WireSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, DriveByWireMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PLUG_IN = SOUND_EVENTS.register(
        "plug_in",
        () -> SoundEvent.createVariableRangeEvent(DriveByWireMod.asResource("plug_in"))
    );
    public static final DeferredHolder<SoundEvent, SoundEvent> PLUG_OUT = SOUND_EVENTS.register(
        "plug_out",
        () -> SoundEvent.createVariableRangeEvent(DriveByWireMod.asResource("plug_out"))
    );

    private WireSounds() {
    }

    public static void register(final IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }
}
