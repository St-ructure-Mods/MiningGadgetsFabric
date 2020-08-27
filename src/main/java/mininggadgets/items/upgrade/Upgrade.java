package mininggadgets.items.upgrade;

import java.util.function.Supplier;

import mininggadgets.MiningGadgets;
import mininggadgets.items.UpgradeCard;
import net.minecraft.item.ItemStack;

public enum Upgrade {
    EMPTY("empty", () -> 0, false);

    private final String name;
    private final String baseName;
    private final UpgradeCard card;
    private final int tier;
    private final Supplier<Integer> costPerBlock;
    private boolean active = true;
    private final boolean isToggleable;
    private final String toolTip;
    private final ItemStack upgradeStack;

    Upgrade(String name, int tier, Supplier<Integer> costPerBlock, boolean isToggleable) {
        this.name = name;
        this.tier = tier;
        this.costPerBlock = costPerBlock;
        this.card = new UpgradeCard(this, name.equals("empty") ? 64 : 1);
        this.upgradeStack = new ItemStack(this.card);
        this.baseName = tier == -1 ? name : name.substring(0, name.lastIndexOf("_"));
        this.isToggleable = isToggleable;
        this.toolTip = "tooltip.mininggadgets." + this.baseName;
    }

    Upgrade(String name, int tier, Supplier<Integer> costPerBlock) {
        this(name, tier, costPerBlock, false);
    }

    Upgrade(String name, Supplier<Integer> costPerBlock) {
        this(name, -1, costPerBlock, true);
    }

    Upgrade(String name, Supplier<Integer> costPerBlock, boolean isToggleable) {
        this(name, -1, costPerBlock, isToggleable);
    }

    public String getName() {
        return name;
    }

    public UpgradeCard getCard() {
        return card;
    }

    public ItemStack getStack() {
        return upgradeStack;
    }

    public int getCostPerBlock() {
        return costPerBlock.get();
    }

    public String getBaseName() {
        return baseName;
    }

    public String getLocal() {
        return String.format("item.mininggadgets.upgrade_%s", this.getName());
    }

    public String getLocalReplacement() {
        return MiningGadgets.MOD_ID + ".upgrade.replacement";
    }

    public boolean hasTier() {
        return tier != -1;
    }

    public boolean isEnabled() {
        return active;
    }

    public void setEnabled(boolean active) {
        this.active = active;
    }

    public boolean isToggleable() {
        return isToggleable;
    }

    public String getToolTip() {
        return toolTip;
    }

    public boolean lazyIs(Upgrade upgrade) {
        return this.getBaseName().equals(upgrade.getBaseName());
    }
}