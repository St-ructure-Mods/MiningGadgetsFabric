package mininggadgets.items.upgrade;

import java.util.function.Supplier;

import mininggadgets.MiningGadgets;
import mininggadgets.config.MGConfig;
import mininggadgets.items.UpgradeCard;
import net.minecraft.item.ItemStack;

public enum Upgrade {
    //Blank
    EMPTY("empty", () -> 0, false),

    SILK("silk", () -> MGConfig.UPGRADECOST_SILKTOUCH, true),
    VOID_JUNK("void_junk", () -> MGConfig.UPGRADECOST_VOID),
    MAGNET("magnet", () -> MGConfig.UPGRADECOST_MAGNET),
    FREEZING("freezing", () -> 0),
    THREE_BY_THREE("three_by_three", () -> 0, false),
    LIGHT_PLACER("light_placer", () -> 0),
    PAVER("paver", () -> 10),

    // Tiered
    FORTUNE_1("fortune_1", 1, () -> MGConfig.UPGRADECOST_FORTUNE1, true),
    FORTUNE_2("fortune_2", 2, () -> MGConfig.UPGRADECOST_FORTUNE2, true),
    FORTUNE_3("fortune_3", 3, () -> MGConfig.UPGRADECOST_FORTUNE3, true),

    BATTERY_1("battery_1", 1, () -> 0),
    BATTERY_2("battery_2", 2, () -> 0),
    BATTERY_3("battery_3", 3, () -> 0),

    RANGE_1("range_1", 1, () -> 0),
    RANGE_2("range_2", 2, () -> 0),
    RANGE_3("range_3", 3, () -> 0),

    EFFICIENCY_1("efficiency_1", 1, () -> MGConfig.UPGRADECOST_EFFICIENCY1, true),
    EFFICIENCY_2("efficiency_2", 2, () -> MGConfig.UPGRADECOST_EFFICIENCY2, true),
    EFFICIENCY_3("efficiency_3", 3, () -> MGConfig.UPGRADECOST_EFFICIENCY3, true),
    EFFICIENCY_4("efficiency_4", 4, () -> MGConfig.UPGRADECOST_EFFICIENCY4, true),
    EFFICIENCY_5("efficiency_5", 5, () -> MGConfig.UPGRADECOST_EFFICIENCY5, true);

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