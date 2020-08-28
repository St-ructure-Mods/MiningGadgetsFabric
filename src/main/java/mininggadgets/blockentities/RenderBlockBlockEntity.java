package mininggadgets.blockentities;

import mininggadgets.client.particles.laserparticle.LaserParticleData;
import mininggadgets.config.MGConfig;
import mininggadgets.init.MGContent;
import mininggadgets.items.gadget.MiningProperties;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import mininggadgets.util.SpecialBlockActions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class RenderBlockBlockEntity extends BlockEntity implements Tickable {
    private BlockState renderBlock;

    private int priorDurability = 9999;
    private int clientPrevDurability;
    private int clientDurability;
    private int durability;
    private UUID playerUUID;
    private int originalDurability;
    private final Random rand = new Random();
    private int ticksSinceMine = 0;
    private List<Upgrade> gadgetUpgrades;
    private List<ItemStack> gadgetFilters;
    private boolean gadgetIsWhitelist;
    private boolean packetReceived = false;
    private int totalAge;
    private MiningProperties.BreakTypes breakType;
    private boolean blockAllowed;

    public static final HashMap<Block, Pair<Integer, Integer>> experienceDrops = new HashMap<Block, Pair<Integer, Integer>>() {{
        put(Blocks.COAL_ORE, Pair.of(0, 2));
        put(Blocks.DIAMOND_ORE, Pair.of(3, 7));
        put(Blocks.EMERALD_ORE, Pair.of(3, 7));
        put(Blocks.LAPIS_ORE, Pair.of(2, 5));
        put(Blocks.NETHER_QUARTZ_ORE, Pair.of(2, 5));
    }};

    public RenderBlockBlockEntity() {
        super(MGContent.RENDERBLOCK_ENTITY);
    }

    public static boolean blockAllowed(List<ItemStack> drops, List<ItemStack> filters, boolean isWhiteList) {
        boolean blockAllowed = false;
        for (ItemStack dropStack : drops) {
            if (filters.size() == 0)
                return true;

            boolean contains = false;
            for (ItemStack filter : filters) {
                if (dropStack.isItemEqual(filter)) {
                    contains = true;
                    break;
                }
            }

            blockAllowed = (isWhiteList && contains) || (!isWhiteList && !contains);

            if (blockAllowed)
                break;
        }

        return blockAllowed;
    }

    public BlockState getRenderBlock() {
        return renderBlock;
    }

    public void setRenderBlock(BlockState renderBlock) {
        this.renderBlock = renderBlock;
    }

    public MiningProperties.BreakTypes getBreakType() {
        return breakType;
    }

    public void setBreakType(MiningProperties.BreakTypes breakType) {
        this.breakType = breakType;
    }

    public void justSetDurability(int durability) {
        priorDurability = this.durability;
        this.durability = durability;
    }

    public void setDurability(int dur, ItemStack stack) {
        ticksSinceMine = 0;
        if (durability != 0)
            priorDurability = durability;
        durability = dur;
        if (dur <= 0) {
            removeBlock();
            if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.FREEZING)) {
                freeze(stack);
            }
        }
        if (!(world.isClient)) {
            markDirty();
//            ServerTickHandler.addToList(pos, durability, world);
        }
    }

    private void freeze(ItemStack stack) {
        for (Direction side : Direction.values()) {
            BlockPos sidePos = pos.offset(side);
            FluidState state = world.getFluidState(sidePos);
            int freezeCost = MGConfig.UPGRADECOST_FREEZE * -1;
            if (state.getFluid().matchesType(Fluids.LAVA) && state.getFluid().isStill(state)) {
                world.setBlockState(sidePos, Blocks.OBSIDIAN.getDefaultState());
//                Energy.of(stack).use(freezeCost);
            } else if (state.getFluid().matchesType(Fluids.WATER) && state.getFluid().isStill(state)) {
                world.setBlockState(sidePos, Blocks.PACKED_ICE.getDefaultState());
//                Energy.of(stack).use(freezeCost);
            } else if ((state.getFluid().matchesType(Fluids.WATER) || state.getFluid().matchesType(Fluids.LAVA)) && !state.getFluid().isStill(state)) {
                world.setBlockState(sidePos, Blocks.COBBLESTONE.getDefaultState());
//                Energy.of(stack).use(freezeCost);
            }
        }
    }

    public void spawnParticle() {
        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.MAGNET) && originalDurability > 0) {
            int PartCount = 20 / originalDurability;
            if (PartCount <= 1) PartCount = 1;
            for (int i = 0; i <= PartCount; i++) {
                double randomPartSize = 0.125 + rand.nextDouble() * 0.5;
                double randomX = rand.nextDouble();
                double randomY = rand.nextDouble();
                double randomZ = rand.nextDouble();

                LaserParticleData data = LaserParticleData.laserparticle(renderBlock, (float) randomPartSize, 1f, 1f, 1f, 200);
                getWorld().addParticle(data, this.getPos().getX() + randomX, this.getPos().getY() + randomY, this.getPos().getZ() + randomZ, 0, 0.0f, 0);
            }
        }
    }

    public int getDurability() {
        return durability;
    }

    public int getOriginalDurability() {
        return originalDurability;
    }

    public void setOriginalDurability(int originalDurability) {
        this.originalDurability = originalDurability;
    }

    public PlayerEntity getPlayer() {
        if (getWorld() == null)
            return null;

        return this.getWorld().getPlayerByUuid(playerUUID);
    }

    public void setPlayer(PlayerEntity player) {
        this.playerUUID = player.getUuid();
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public int getTicksSinceMine() {
        return ticksSinceMine;
    }

    public void setTicksSinceMine(int ticksSinceMine) {
        this.ticksSinceMine = ticksSinceMine;
    }

    public int getPriorDurability() {
        return priorDurability;
    }

    public void setPriorDurability(int priorDurability) {
        this.priorDurability = priorDurability;
    }

    public int getClientDurability() {
        return clientDurability;
    }

    public void setClientDurability(int clientDurability) {
        if (this.durability == 0)
            this.clientPrevDurability = clientDurability;
        else
            this.clientPrevDurability = this.durability;

        this.clientDurability = clientDurability;
        packetReceived = true;
    }

    public List<Upgrade> getGadgetUpgrades() {
        return gadgetUpgrades;
    }

    public void setGadgetUpgrades(List<Upgrade> gadgetUpgrades) {
        this.gadgetUpgrades = gadgetUpgrades;
    }

    public List<ItemStack> getGadgetFilters() {
        return gadgetFilters;
    }

    public void setGadgetFilters(List<ItemStack> gadgetFilters) {
        this.gadgetFilters = gadgetFilters;
    }

    public boolean isGadgetIsWhitelist() {
        return gadgetIsWhitelist;
    }

    public void setGadgetIsWhitelist(boolean gadgetIsWhitelist) {
        this.gadgetIsWhitelist = gadgetIsWhitelist;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(pos, 0, toInitialChunkDataTag());
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return toTag(new CompoundTag());
    }

    public void markDirtyClient() {
        markDirty();
        if (getWorld() != null) {
            BlockState state = getWorld().getBlockState(getPos());
            getWorld().updateListeners(getPos(), state, state,3);
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        renderBlock = NbtHelper.toBlockState(tag.getCompound("renderBlock"));
        originalDurability = tag.getInt("originalDurability");
        priorDurability = tag.getInt("priorDurability");
        durability = tag.getInt("durability");
        ticksSinceMine = tag.getInt("ticksSinceMine");
        playerUUID = tag.getUuid("playerUUID");
        gadgetUpgrades = UpgradeTools.getUpgradesFromTag(tag);
        breakType = MiningProperties.BreakTypes.values()[tag.getByte("breakType")];
        gadgetFilters = MiningProperties.deserializeItemStackList(tag.getCompound("gadgetFilters"));
        gadgetIsWhitelist = tag.getBoolean("gadgetIsWhitelist");
        blockAllowed = tag.getBoolean("blockAllowed");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        if (renderBlock != null)
            tag.put("renderBlock", NbtHelper.fromBlockState(renderBlock));
        tag.putInt("originalDurability", originalDurability);
        tag.putInt("priorDurability", priorDurability);
        tag.putInt("durability", durability);
        tag.putInt("ticksSinceMine", ticksSinceMine);
        if (playerUUID != null)
            tag.putUuid("playerUUID", playerUUID);
        tag.put("upgrades", UpgradeTools.setUpgradesNBT(gadgetUpgrades).getList("upgrades", 1));
        tag.putByte("breakType", (byte) breakType.ordinal());
        tag.put("gadgetFilters", MiningProperties.serializeItemStackList(getGadgetFilters()));
        tag.putBoolean("gadgetIsWhitelist", isGadgetIsWhitelist());
        tag.putBoolean("blockAllowed", blockAllowed);
        return super.toTag(tag);
    }

    private void removeBlock() {
        System.out.println(world);
        System.out.println(world.isClient);
        System.out.println(playerUUID);

        if (world == null || world.isClient || playerUUID == null)
            return;

        PlayerEntity player = world.getPlayerByUuid(playerUUID);
        if (player == null)
            return;

        int silk = 0;
        int fortune = 0;

        ItemStack tempTool = new ItemStack(MGContent.MINING_GADGET);

        // If silk is in the upgrades, apply it without a tier.
        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.SILK)) {
            tempTool.addEnchantment(Enchantments.SILK_TOUCH, 1);
            silk = 1;
        }

        // FORTUNE_1 is eval'd against the basename so this'll support all fortune upgrades
        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.FORTUNE_1)) {
            Optional<Upgrade> upgrade = UpgradeTools.getUpgradeFromList(gadgetUpgrades, Upgrade.FORTUNE_1);
            if (upgrade.isPresent()) {
                fortune = upgrade.get().getTier();
                tempTool.addEnchantment(Enchantments.FORTUNE, fortune);
            }
        }

        List<ItemStack> drops = Block.getDroppedStacks(renderBlock, (ServerWorld) world, this.pos, null, player, tempTool);

        if (blockAllowed) {
            int exp = getExp(renderBlock.getBlock(), tempTool);

            boolean magnetMode = (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.MAGNET));
            for (ItemStack drop : drops) {
                if (drop != null) {
                    if (magnetMode) {
                        Block.dropStack(world, player.getBlockPos(), drop);
                    } else {
                        Block.dropStack(world, pos, drop);
                    }
                }
            }
            if (magnetMode) {
                if (exp > 0)
                    player.addExperience(exp);
            } else {
                if (exp > 0)
                    dropExperience((ServerWorld) world, pos, exp);
            }

            renderBlock.onStacksDropped((ServerWorld) world, pos, tempTool);
        }

        BlockState underState = world.getBlockState(this.pos.down());

        world.removeBlockEntity(this.pos);
        world.setBlockState(this.pos, Blocks.AIR.getDefaultState());

        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.PAVER)) {
            if (this.pos.getY() <= player.getY() && underState == Blocks.AIR.getDefaultState()) {
                world.setBlockState(this.pos.down(), Blocks.COBBLESTONE.getDefaultState());
            }
        }

        // Add to the break stats
        player.incrementStat(Stats.MINED.getOrCreateStat(renderBlock.getBlock()));

        // Handle special cases
        if (SpecialBlockActions.getRegister().containsKey(renderBlock.getBlock()))
            SpecialBlockActions.getRegister().get(renderBlock.getBlock()).accept(world, pos, renderBlock);
    }

    private void mgResetBlock() {
        if (world == null)
            return;

        if (!world.isClient) {
            if (renderBlock != null)
                world.setBlockState(this.pos, renderBlock);
            else
                world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void tick() {
        totalAge++;

        if (ticksSinceMine == 0) {
            spawnParticle();
        }

        if (world.isClient) {
            if (playerUUID != null) {
                if (getPlayer() != null && !getPlayer().isUsingItem()) ticksSinceMine++;
                else ticksSinceMine = 0;
            }

            if (packetReceived) {
                this.priorDurability = this.durability;
                this.durability = this.clientDurability;

                packetReceived = false;
            } else {
                if (durability != 0)
                    this.priorDurability = this.durability;
            }
        }

        if (!world.isClient) {
            if (ticksSinceMine == 1) {
                priorDurability = durability;
//                ServerTickHandler.addToList(pos, durability, world);
            }
            if (ticksSinceMine >= 10) {
                priorDurability = durability;
                durability++;
//                ServerTickHandler.addToList(pos, durability, world);
            }
            if (durability >= originalDurability) {
                mgResetBlock();
            }
            ticksSinceMine++;
        }
    }

    public void setBlockAllowed() {
        if (!UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.VOID_JUNK)) {
            this.blockAllowed = true;
            return;
        }

        PlayerEntity player = world.getPlayerByUuid(playerUUID);
        if (player == null) return;
        int silk = 0;
        int fortune = 0;

        ItemStack tempTool = new ItemStack(MGContent.MINING_GADGET);

        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.SILK)) {
            tempTool.addEnchantment(Enchantments.SILK_TOUCH, 1);
            silk = 1;
        }

        if (UpgradeTools.containsActiveUpgradeFromList(gadgetUpgrades, Upgrade.FORTUNE_1)) {
            Optional<Upgrade> upgrade = UpgradeTools.getUpgradeFromList(gadgetUpgrades, Upgrade.FORTUNE_1);
            if (upgrade.isPresent()) {
                fortune = upgrade.get().getTier();
                tempTool.addEnchantment(Enchantments.FORTUNE, fortune);
            }
        }

        List<ItemStack> drops = Block.getDroppedStacks(renderBlock, (ServerWorld) world, this.pos, null, player, tempTool);

        this.blockAllowed = blockAllowed(drops, getGadgetFilters(), isGadgetIsWhitelist());
    }

    public boolean getBlockAllowed() {
        return blockAllowed;
    }

    private int getExp(Block block, ItemStack tempTool) {
        int exp = 0;

        if (experienceDrops.containsKey(renderBlock.getBlock())) {
            Pair<Integer, Integer> pair = experienceDrops.get(renderBlock.getBlock());
            exp = MathHelper.nextInt(rand, pair.getLeft(), pair.getRight());
        } else if (renderBlock.getBlock() instanceof RedstoneOreBlock) {
            if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tempTool) == 0) {
                exp = 1 + world.random.nextInt(5);
            }
        }

        return exp;
    }

    private void dropExperience(ServerWorld world, BlockPos pos, int size) {
        if (world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            while(size > 0) {
                int i = ExperienceOrbEntity.roundToOrbSize(size);
                size -= i;
                world.spawnEntity(new ExperienceOrbEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i));
            }
        }
    }
}