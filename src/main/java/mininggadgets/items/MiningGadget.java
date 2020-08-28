package mininggadgets.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mininggadgets.MiningGadgets;
import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.blocks.RenderBlock;
import mininggadgets.client.OurKeys;
import mininggadgets.client.particles.playerparticle.PlayerParticleData;
import mininggadgets.client.screens.ModScreens;
import mininggadgets.config.MGConfig;
import mininggadgets.init.InitUtils;
import mininggadgets.init.MGContent;
import mininggadgets.items.gadget.MiningProperties;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import mininggadgets.sounds.LaserLoopSound;
import mininggadgets.sounds.OurSounds;
import mininggadgets.util.MagicHelpers;
import mininggadgets.util.VectorHelper;
import mininggadgets.items.gadget.MiningCollect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;

public class MiningGadget extends Item implements EnergyHolder, ItemDurabilityExtensions {
    public final int maxCharge;
    public final EnergyTier tier;
    private Random rand = new Random();
    private LaserLoopSound laserLoopSound;

    public MiningGadget() {
        super(new Item.Settings().maxCount(1).group(MiningGadgets.ITEMGROUP));
        this.maxCharge = MGConfig.MININGGADGET_MAXPOWER;
        this.tier = EnergyTier.HIGH;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> itemList) {
        super.appendStacks(group, itemList);
        if (!isIn(group)) {
            return;
        }
        InitUtils.initPoweredItems(this, itemList);
    }

    @Override
    public double getMaxStoredPower() {
        return maxCharge;
    }

    @Override
    public EnergyTier getTier() {
        return tier;
    }

    @Override
    public int getDurabilityColor(ItemStack stack) {
        return PowerSystem.getDisplayPower().colour;
    }

    @Override
    public boolean showDurability(ItemStack stack) {
        if (stack.getItem() instanceof MiningGadget) {
            return Energy.of(stack).getEnergy() > 0;
        }

        return false;
    }

    @Override
    public double getDurability(ItemStack stack) {
        return 1 - ItemUtils.getPowerForDurabilityBar(stack);
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        List<Upgrade> upgrades = UpgradeTools.getUpgrades(stack);
        MinecraftClient mc = MinecraftClient.getInstance();
        
        if (!InputUtil.isKeyPressed(mc.getWindow().getHandle(), mc.options.keySneak.getDefaultKey().getCode())) {
            tooltip.add(new TranslatableText("mininggadgets.tooltip.item.show_upgrades",
                    new TranslatableText(mc.options.keySneak.getTranslationKey()).getString().toLowerCase())
                    .setStyle(Style.EMPTY.withColor(TextColor.parse("GRAY"))));
        } else {
            tooltip.add(new TranslatableText("mininggadgets.tooltip.item.break_cost", getEnergyCost(stack)).setStyle(Style.EMPTY.withColor(TextColor.parse("RED"))));
            if (!(upgrades.isEmpty())) {
                tooltip.add(new TranslatableText("mininggadgets.tooltip.item.upgrades").setStyle(Style.EMPTY.withColor(TextColor.parse("AQUA"))));
                for (Upgrade upgrade : upgrades) {
                    tooltip.add(new LiteralText(" - " +
                            I18n.translate(upgrade.getLocal())
                    ).setStyle(Style.EMPTY.withColor(TextColor.parse("GRAY"))));
                }
            }
        }

        if (Energy.valid(stack)) {
            int energy = (int) Energy.of(stack).getEnergy();

            tooltip.add(
                new TranslatableText("mininggadgets.gadget.energy", 
                    MagicHelpers.tidyValue(energy),
                    MagicHelpers.tidyValue(energy)
                ).setStyle(Style.EMPTY.withColor(TextColor.parse("GREEN")))
            );
        }
    }

    public static void changeRange(ItemStack tool) {
        if (MiningProperties.getRange(tool) == 1) {
            MiningProperties.setRange(tool, 3);
        } else {
            MiningProperties.setRange(tool, 1);
        }
    }

    public static boolean canMine(ItemStack tool) {
        EnergyHandler energy = Energy.of(tool);
        int cost = getEnergyCost(tool);

        if (MiningProperties.getRange(tool) == 3)
            cost = cost * 9;

        return energy.getEnergy() > cost;
    }

    public static boolean canMineBlock(ItemStack tool, World world, PlayerEntity player, BlockPos pos, BlockState state) {
        if (!player.canModifyBlocks() || !world.canPlayerModifyAt(player, pos))
            return false;

        return canMine(tool);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Environment(EnvType.CLIENT)
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (player.isSneaking()) {
            return this.onItemShiftRightClick(world, player, hand, itemStack);
        }

        if (world.isClient) {
            float volume = MiningProperties.getVolume(itemStack);
            if (volume != 0.0f) {
                player.playSound(OurSounds.LASER_START.getSound(), volume * 0.5f, 1f);
            }

            return new TypedActionResult<>(ActionResult.PASS, itemStack);
        }

        if (!canMine(itemStack)) {
            return new TypedActionResult<>(ActionResult.FAIL, itemStack);
        }

        player.setCurrentHand(hand);
        return new TypedActionResult<>(ActionResult.PASS, itemStack);
    }

    @Environment(EnvType.CLIENT)
    private TypedActionResult<ItemStack> onItemShiftRightClick(World world, PlayerEntity player, Hand hand, ItemStack itemstack) {
        if (!world.isClient)
            MiningProperties.setCanMine(itemstack, true);

        if (world.isClient) {
            if (OurKeys.shiftClickGuiBinding.getDefaultKey() == InputUtil.UNKNOWN_KEY) {
                ModScreens.openGadgetSettingsScreen(itemstack);
            }
        }

        return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
    }

    public List<BlockPos> findSources(World world, List<BlockPos> coords) {
        List<BlockPos> sources = new ArrayList<>();
        for (BlockPos coord : coords) {
            for (Direction side : Direction.values()) {
                BlockPos sidePos = coord.offset(side);
                FluidState state = world.getFluidState(sidePos);
                if ((state.getFluid().equals(Fluids.LAVA) || state.getFluid().equals(Fluids.WATER)))
                    if (!sources.contains(sidePos))
                        sources.add(sidePos);
            }
        }

        return sources;
    }

    @Environment(EnvType.CLIENT)
    private void spawnFreezeParticle(PlayerEntity player, BlockPos sourcePos, World world, ItemStack stack) {
        float randomPartSize = 0.05f + (0.125f - 0.05f) * rand.nextFloat();
        double randomTX = rand.nextDouble();
        double randomTY = rand.nextDouble();
        double randomTZ = rand.nextDouble();
        double alpha = -0.5f + (1.0f - 0.5f) * rand.nextDouble(); //rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        Vec3d playerPos = player.getPos().add(0, player.getEyeHeight(player.getPose()), 0);
        Vec3d look = player.getRotationVector(); // or getLook(partialTicks)
        int range = MiningProperties.getBeamRange(stack);
        BlockHitResult lookAt = VectorHelper.getLookingAt(player, RaycastContext.FluidHandling.NONE, range);
        Vec3d lookingAt = lookAt.getPos();
        //The next 3 variables are directions on the screen relative to the players look direction. So right = to the right of the player, regardless of facing direction.
        Vec3d right = new Vec3d(-look.z, 0, look.x).normalize();
        Vec3d forward = look;
        Vec3d backward = look.multiply(-1, 1, -1);
        Vec3d down = right.crossProduct(forward);

        //These are used to calculate where the particles are going. We want them going into the laser, so we move the destination right, down, and forward a bit.
        right = right.multiply(0.65f);
        forward = forward.multiply(0.85f);
        down = down.multiply(-0.35);
        backward = backward.multiply(0.05);

        //Take the player's eye position, and shift it to where the end of the laser is (Roughly)
        Vec3d laserPos = playerPos.add(right);
        laserPos = laserPos.add(forward);
        laserPos = laserPos.add(down);
        lookingAt = lookingAt.add(backward);
        PlayerParticleData data = PlayerParticleData.playerparticle("ice", sourcePos.getX() + randomTX, sourcePos.getY() + randomTY, sourcePos.getZ() + randomTZ, randomPartSize, 1f, 1f, 1f, 120, true);
        //Change the below laserPos to lookingAt to have it emit from the laser gun itself
        world.addParticle(data, laserPos.x, laserPos.y, laserPos.z, 0.025, 0.025f, 0.025);
    }

    @Environment(EnvType.CLIENT)
    public void playLoopSound(LivingEntity player, ItemStack stack) {
        float volume = MiningProperties.getVolume(stack);
        PlayerEntity myplayer = MinecraftClient.getInstance().player;
        if (myplayer.equals(player)) {
            if (volume != 0.0f) {
                if (laserLoopSound == null) {
                    laserLoopSound = new LaserLoopSound((PlayerEntity) player, volume);
                    MinecraftClient.getInstance().getSoundManager().play(laserLoopSound);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void usageTick(World world, LivingEntity player, ItemStack stack, int count) {
        if (world.isClient) {
            this.playLoopSound(player, stack);
        }

        if (!MiningProperties.getCanMine(stack))
            return;

        int range = MiningProperties.getBeamRange(stack);
        BlockHitResult lookingAt = VectorHelper.getLookingAt((PlayerEntity) player, RaycastContext.FluidHandling.NONE, range);
        if (lookingAt == null || (world.getBlockState(VectorHelper.getLookingAt((PlayerEntity) player, stack, range).getBlockPos()) == Blocks.AIR.getDefaultState()))
            return;

        List<BlockPos> coords = MiningCollect.collect((PlayerEntity) player, lookingAt, world, MiningProperties.getRange(stack));

        if (UpgradeTools.containsActiveUpgrade(stack, Upgrade.FREEZING)) {
            for (BlockPos sourcePos : findSources(player.world, coords)) {
                if (player instanceof PlayerEntity) {
                    int delay = MiningProperties.getFreezeDelay(stack);
                    if (delay == 0 || count % delay == 0) {
                        spawnFreezeParticle((PlayerEntity) player, sourcePos, player.world, stack);
                    }
                }
            }
        }

        if (!world.isClient) {
            int efficiency = 0;
            if (UpgradeTools.containsActiveUpgrade(stack, Upgrade.EFFICIENCY_1))
                efficiency = UpgradeTools.getUpgradeFromGadget(stack, Upgrade.EFFICIENCY_1).get().getTier();

            float hardness = getHardness(coords, (PlayerEntity) player, efficiency);
            hardness = hardness * MiningProperties.getRange(stack) * 1;
            hardness = (float) Math.floor(hardness);
            if (hardness == 0) hardness = 1;
            for (BlockPos coord : coords) {
                BlockState state = world.getBlockState(coord);
                if (!(state.getBlock() instanceof RenderBlock)) {
                    if (!canMineBlock(stack, world, (PlayerEntity) player, coord, state)) {
                        return;
                    }
                    List<Upgrade> gadgetUpgrades = UpgradeTools.getUpgrades(stack);
                    world.setBlockState(coord, MGContent.RENDER_BLOCK.getDefaultState());
                    RenderBlockBlockEntity be = (RenderBlockBlockEntity) world.getBlockEntity(coord);
                    be.setRenderBlock(state);
                    be.setBreakType(MiningProperties.getBreakType(stack));
                    be.setGadgetUpgrades(gadgetUpgrades);
                    be.setGadgetFilters(MiningProperties.getFiltersAsList(stack));
                    be.setGadgetIsWhitelist(MiningProperties.getWhiteList(stack));
                    be.setPriorDurability((int) hardness + 1);
                    be.setOriginalDurability((int) hardness + 1);
                    be.setDurability((int) hardness, stack);
                    be.setPlayer((PlayerEntity) player);
                    be.setBlockAllowed();
                } else {
                    RenderBlockBlockEntity be = (RenderBlockBlockEntity) world.getBlockEntity(coord);
                    int durability = be.getDurability();
                    durability = durability - 1;
                    if (durability <= 0) {
//                        Energy.of(stack).use(getEnergyCost(stack) * -1);
                        if (MiningProperties.getPrecisionMode(stack)) {
                            MiningProperties.setCanMine(stack, false);
                            player.clearActiveItem();
                        }

                    }
                    be.setDurability(durability, stack);
                }
            }
            if (!(UpgradeTools.containsActiveUpgrade(stack, Upgrade.LIGHT_PLACER)))
                return;

            Direction side = lookingAt.getSide();
            boolean vertical = side.getAxis().isVertical();
            Direction up = vertical ? player.getHorizontalFacing() : Direction.UP;
            Direction right = vertical ? up.rotateYClockwise() : side.rotateYCounterclockwise();

            BlockPos pos;
            if (MiningProperties.getRange(stack) == 1)
                pos = lookingAt.getBlockPos().offset(side, 4);
            else
                pos = lookingAt.getBlockPos().offset(side).offset(right);

            if (world.getLightLevel(pos) <= 7 && world.getBlockState(pos).getMaterial() == Material.AIR) {
//                world.setBlockState(pos, MGContent.MINERS_LIGHT.getDefaultState());
//                Energy.of(stack).use(MGConfig.UPGRADECOST_LIGHT * -1);
            }
        }
    }

    public static int getEnergyCost(ItemStack stack) {
        int cost = MGConfig.MININGGADGET_BASECOST;
        List<Upgrade> upgrades = UpgradeTools.getActiveUpgrades(stack);
        if (upgrades.isEmpty())
            return cost;

        return cost + upgrades.stream().mapToInt(Upgrade::getCostPerBlock).sum();
    }

    private static float getHardness(List<BlockPos> coords, PlayerEntity player, int efficiency) {
        float hardness = 0;
        float toolSpeed = 8;
        if (efficiency > 0) {
            toolSpeed = toolSpeed + ((efficiency * efficiency + 1));
        }
        StatusEffectInstance hasteEffect = player.getStatusEffect(StatusEffects.HASTE);
        if (hasteEffect != null) {
            int hasteLevel = hasteEffect.getAmplifier() + 1;
            toolSpeed = toolSpeed + (toolSpeed * ((hasteLevel * 20f) / 100));
        }
        World world = player.getEntityWorld();
        for (BlockPos coord : coords) {
            BlockState state = world.getBlockState(coord);
            float temphardness = state.getHardness(world, coord);
            hardness += (temphardness * 30) / toolSpeed;
        }

        return ((hardness / coords.size()));
    }

    public static void applyUpgrade(ItemStack tool, UpgradeCard upgradeCard) {
        if (UpgradeTools.containsUpgrade(tool, upgradeCard.getUpgrade()))
            return;

        UpgradeTools.setUpgrade(tool, upgradeCard);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int remainingUseTicks) {
        if (world.isClient) {
            if (laserLoopSound != null) {
                float volume = MiningProperties.getVolume(stack);
                if (volume != 0.0f && !laserLoopSound.isDone()) {
                    entityLiving.playSound(OurSounds.LASER_END.getSound(), volume * 0.5f, 1f);
                }
                laserLoopSound = null;
            }
        }

        if (entityLiving instanceof PlayerEntity)
            entityLiving.clearActiveItem();

        if (!world.isClient)
            MiningProperties.setCanMine(stack, true);
    }

    public static ItemStack getGadget(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandStack();
        if (!(heldItem.getItem() instanceof MiningGadget)) {
            heldItem = player.getOffHandStack();
            if (!(heldItem.getItem() instanceof MiningGadget)) {
                return ItemStack.EMPTY;
            }
        }

        return heldItem;
    }

    public static boolean isHolding(PlayerEntity entity) {
        return getGadget(entity).getItem() instanceof MiningGadget;
    }
}