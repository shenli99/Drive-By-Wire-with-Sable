package edn.stratodonut.drivebywire.blocks;

import com.mojang.serialization.MapCodec;
import edn.stratodonut.drivebywire.WireBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public class WireNetworkBackupBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<WireNetworkBackupBlock> CODEC = simpleCodec(WireNetworkBackupBlock::new);

    public WireNetworkBackupBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        final Direction facing;
        if (context.getClickedFace().getAxis().isVertical()) {
            facing = context.getHorizontalDirection().getOpposite();
        } else {
            facing = context.getClickedFace();
        }

        return this.defaultBlockState().setValue(FACING, facing);
    }

    @Override
    protected BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        return new WireNetworkBackupBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
        final Level level,
        final BlockState state,
        final BlockEntityType<T> blockEntityType
    ) {
        if (level.isClientSide() || blockEntityType != WireBlockEntities.BACKUP_BLOCK.get()) {
            return null;
        }

        return (tickLevel, tickPos, tickState, blockEntity) -> WireNetworkBackupBlockEntity.serverTick(
            tickLevel,
            tickPos,
            tickState,
            (WireNetworkBackupBlockEntity) blockEntity
        );
    }
}
