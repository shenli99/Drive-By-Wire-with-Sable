package edn.stratodonut.drivebywire.wire;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class WireNetworkSavedData extends SavedData {
    private static final String DATA_NAME = "drivebywire_network";

    private final WireNetworkManager manager;

    private WireNetworkSavedData() {
        this.manager = new WireNetworkManager(this::setDirty);
    }

    public static SavedData.Factory<WireNetworkSavedData> factory() {
        return new SavedData.Factory<>(WireNetworkSavedData::new, WireNetworkSavedData::load);
    }

    public static WireNetworkManager get(final ServerLevel level) {
        final WireNetworkSavedData data = level.getDataStorage().computeIfAbsent(factory(), DATA_NAME);
        data.manager.attachLevel(level);
        return data.manager;
    }

    private static WireNetworkSavedData load(final CompoundTag tag, final HolderLookup.Provider registries) {
        final WireNetworkSavedData data = new WireNetworkSavedData();
        data.manager.load(tag);
        return data;
    }

    @Override
    public CompoundTag save(final CompoundTag tag, final HolderLookup.Provider registries) {
        return manager.save(tag);
    }
}
