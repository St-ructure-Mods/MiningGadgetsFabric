package mininggadgets.items;

import java.util.List;
import java.util.Random;

import mininggadgets.MiningGadgets;
import mininggadgets.config.MGConfig;
import mininggadgets.init.InitUtils;
import mininggadgets.items.gadget.MiningProperties;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import mininggadgets.util.MagicHelpers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
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
        return true;
    }

    @Override
    public double getDurability(ItemStack stack) {
        return 1 - ItemUtils.getPowerForDurabilityBar(stack);
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return 0;
    }

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

    public static int getEnergyCost(ItemStack stack) {
        int cost = MGConfig.MININGGADGET_BASECOST;
        List<Upgrade> upgrades = UpgradeTools.getActiveUpgrades(stack);
        if (upgrades.isEmpty())
            return cost;

        return cost + upgrades.stream().mapToInt(Upgrade::getCostPerBlock).sum();
    }
}