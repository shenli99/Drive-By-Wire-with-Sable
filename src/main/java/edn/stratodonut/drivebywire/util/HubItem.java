package edn.stratodonut.drivebywire.util;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class HubItem {
    private static final String HUB_KEY = "DriveByWireHub";
    private static final String LEGACY_HUB_KEY = "Hub";

    private HubItem() {
    }

    public static void putHub(final ItemStack itemStack, final BlockPos pos) {
        final CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putLong(HUB_KEY, pos.asLong());
        tag.putLong(LEGACY_HUB_KEY, pos.asLong());
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static void ifHubPresent(final ItemStack itemStack, final Consumer<BlockPos> consumer) {
        final CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(HUB_KEY, Tag.TAG_LONG)) {
            consumer.accept(BlockPos.of(tag.getLong(HUB_KEY)));
            return;
        }

        if (tag.contains(LEGACY_HUB_KEY, Tag.TAG_LONG)) {
            consumer.accept(BlockPos.of(tag.getLong(LEGACY_HUB_KEY)));
        }
    }
}
