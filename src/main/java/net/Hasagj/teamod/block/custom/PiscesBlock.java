package net.hasagj.teamod.block.custom;

import com.mojang.serialization.MapCodec;
import net.hasagj.teamod.block.ModBlocks;
import net.hasagj.teamod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PiscesBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<PiscesBlock> CODEC = simpleCodec(PiscesBlock::new);
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty HAS_ENCH = BooleanProperty.create("has_ench");
    private ItemEnchantments enchList = ItemEnchantments.EMPTY;

    public MapCodec<PiscesBlock> codec() {
        return CODEC;
    }
    public PiscesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(ACTIVE, false).setValue(HAS_ENCH, false)));
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean b) {
        return new ItemStack(ModItems.PISCES.get());

    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tick, BlockPos pos,
                                  Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState(); // уничтожаем блок
        }
        return super.updateShape(state, level, tick, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        List<Item> tea_list = List.of(ModItems.CUP_GREEN_TEA.get(), ModItems.CUP_BLACK_TEA.get(), ModItems.CUP_HIBISCUS_TEA.get(), ModItems.CUP_DAISY_TEA.get(), ModItems.CUP_PALE_TEA.get(), ModItems.CUP_PITCHER_TEA.get(), ModItems.CUP_CACTUS_TEA.get(), ModItems.CUP_CHORUS_TEA.get(), ModItems.CUP_ANCIENT_TEA.get(), ModItems.CUP_WANDERERS_TEA.get());
        if (level instanceof ServerLevel serverLevel) {
            if (tea_list.contains(stack.getItem()) && !state.getValue(ACTIVE)) {
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
                stack.consume(1, player);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ModItems.CUP.get()));
                } else if (!player.getInventory().add(new ItemStack(ModItems.CUP.get()))) {
                    player.drop(new ItemStack(ModItems.CUP.get()), false);
                }
                serverLevel.sendParticles(ParticleTypes.SPLASH,
                        pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                        30,
                        0.25F, 0.25F, 0.25F,
                        0);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

                return InteractionResult.SUCCESS;
            } else if (state.getValue(ACTIVE) && stack.isEnchanted() && !state.getValue(HAS_ENCH)) {
                level.setBlockAndUpdate(pos, state.setValue(HAS_ENCH, true));
                enchList = stack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT));
                stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
                this.emitParticles((ServerPlayer) player, serverLevel, pos, 50, false);

                return InteractionResult.SUCCESS;
            } else if (state.getValue(ACTIVE) && stack.has(DataComponents.ENCHANTABLE) && state.getValue(HAS_ENCH)) {
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false).setValue(HAS_ENCH, false));
                state = level.getBlockState(pos);
                if (level.random.nextInt(100) <= 20) {
                    stack.hurtWithoutBreaking(stack.getMaxDamage() - 1, player);
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                            5,
                            0.2F, 0.2F, 0.2F,
                            0.3F);
                    ItemParticleOption particleOption = new ItemParticleOption(ParticleTypes.ITEM, stack);
                    serverLevel.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS);
                    serverLevel.sendParticles(particleOption,
                            player.getX() + 0.5F, player.getY() + 0.5F, player.getZ() + 0.5F,
                            10,
                            0.2F, 0.2F, 0.2F,
                            0.0F);
                } else {
                    for (Holder<Enchantment> ench : enchList.keySet()) {
                        if (stack.supportsEnchantment(ench)) {
                            stack.enchant(ench, enchList.getLevel(ench));
                        }
                    }
                    this.emitParticles((ServerPlayer) player, serverLevel, pos, 50, true);
                    serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private void emitParticles(ServerPlayer player, ServerLevel level, BlockPos pos, int count, boolean reverseDirection) {
        RandomSource randomsource = level.random;

        for(double d0 = (double)0.0F; d0 < (double)count; ++d0) {
            int i = d0 % 2 != 0 ? 0xE727F5 : 0x8E2096;
            AABB aabb = player.getBoundingBox();
            Vec3 vec3 = aabb.getMinPosition().add(randomsource.nextDouble() * aabb.getXsize(), randomsource.nextDouble() * aabb.getYsize(), randomsource.nextDouble() * aabb.getZsize());
            Vec3 vec31 = Vec3.atLowerCornerOf(pos).add(randomsource.nextDouble(), randomsource.nextDouble(), randomsource.nextDouble());
            if (reverseDirection) {
                Vec3 vec32 = vec3;
                vec3 = vec31;
                vec31 = vec32;
            }

            TrailParticleOption trailparticleoption = new TrailParticleOption(vec31, i, randomsource.nextInt(40) + 10);
            level.sendParticles(trailparticleoption, true, true, vec3.x, vec3.y, vec3.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, ACTIVE, HAS_ENCH});
    }




}
