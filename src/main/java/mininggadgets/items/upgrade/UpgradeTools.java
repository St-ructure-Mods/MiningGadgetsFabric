package mininggadgets.items.upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mininggadgets.items.UpgradeCard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class UpgradeTools {
    private static final String KEY_UPGRADES = "upgrades";
    private static final String KEY_UPGRADE = "upgrade";
    private static final String KEY_ENABLED = "enabled";

    private static void setUpgradeNBT(CompoundTag nbt, UpgradeCard upgrade) {
        ListTag list = nbt.getList(KEY_UPGRADES, 1);

        CompoundTag compound = new CompoundTag();
        compound.putString(KEY_UPGRADE, upgrade.getUpgrade().getName());
        compound.putBoolean(KEY_ENABLED, upgrade.getUpgrade().isEnabled());

        list.add(compound);
        nbt.put(KEY_UPGRADES, list);
    }

    public static CompoundTag setUpgradesNBT(List<Upgrade> laserUpgrades) {
        CompoundTag listCompound = new CompoundTag();
        ListTag list = new ListTag();

        laserUpgrades.forEach(upgrade -> {
            CompoundTag compound = new CompoundTag();
            compound.putString(KEY_UPGRADE, upgrade.getName());
            compound.putBoolean(KEY_ENABLED, upgrade.isEnabled());
            list.add(compound);
        });

        listCompound.put(KEY_UPGRADES, list);
        return listCompound;
    }

    public static void setUpgrade(ItemStack tool, UpgradeCard upgrade) {
        CompoundTag tagCompound = tool.getOrCreateTag();
        setUpgradeNBT(tagCompound, upgrade);
    }

    public static List<Upgrade> getUpgradesFromTag(CompoundTag tagCompound) {
        ListTag upgrades = tagCompound.getList(KEY_UPGRADES, 1);

        List<Upgrade> functionalUpgrades = new ArrayList<>();
        if (upgrades.isEmpty())
            return functionalUpgrades;

        for (int i = 0; i < upgrades.size(); i++) {
            CompoundTag tag = upgrades.getCompound(i);

            // Skip unknowns
            Upgrade type = getUpgradeByName(tag.getString(KEY_UPGRADE));
            if( type == null )
                continue;

            type.setEnabled(!tag.contains(KEY_ENABLED) || tag.getBoolean(KEY_ENABLED));
            functionalUpgrades.add(type);
        }

        return functionalUpgrades;
    }

    public static Upgrade getUpgradeByName(String name) {
        // If the name doesn't exist then move on
        try {
            Upgrade type = Upgrade.valueOf(name.toUpperCase());
            return type;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static List<Upgrade> getUpgrades(ItemStack tool) {
        CompoundTag tagCompound = tool.getOrCreateTag();
        return getUpgradesFromTag(tagCompound);
    }

    public static List<Upgrade> getActiveUpgrades(ItemStack tool) {
        CompoundTag tagCompound = tool.getOrCreateTag();
        return getActiveUpgradesFromTag(tagCompound);
    }

     public static List<Upgrade> getActiveUpgradesFromTag(CompoundTag tagCompound) {
        ListTag upgrades = tagCompound.getList(KEY_UPGRADES, 1);

        List<Upgrade> functionalUpgrades = new ArrayList<>();
        if (upgrades.isEmpty())
            return functionalUpgrades;

        for (int i = 0; i < upgrades.size(); i++) {
            CompoundTag tag = upgrades.getCompound(i);

            Upgrade type = getUpgradeByName(tag.getString(KEY_UPGRADE));
            if (type == null)
                continue;

            type.setEnabled(!tag.contains(KEY_ENABLED) || tag.getBoolean(KEY_ENABLED));
            if (type.isEnabled())
                functionalUpgrades.add(type);
        }

        return functionalUpgrades;
    }

    public static Optional<Upgrade> getUpgradeFromList(List<Upgrade> upgrades, Upgrade type) {
        if( upgrades == null || upgrades.isEmpty() )
            return Optional.empty();

        return upgrades.stream()
                .filter(upgrade -> upgrade.getBaseName().equals(type.getBaseName()))
                .findFirst();
    }

    public static Optional<Upgrade> getUpgradeFromGadget(ItemStack tool, Upgrade type) {
        List<Upgrade> upgrades = getUpgrades(tool);
        return getUpgradeFromList(upgrades, type);
    }

    public static boolean containsUpgrade(ItemStack tool, Upgrade type) {
        return getUpgradeFromGadget(tool, type).isPresent();
    }

    public static boolean containsActiveUpgrade(ItemStack tool, Upgrade type) {
        Optional<Upgrade> upgrade = getUpgradeFromGadget(tool, type);
        return upgrade.isPresent() && upgrade.get().isEnabled();
    }

    public static boolean containsActiveUpgradeFromList(List<Upgrade> upgrades, Upgrade type) {
        Optional<Upgrade> upgrade = getUpgradeFromList(upgrades, type);
        return upgrade.isPresent() && upgrade.get().isEnabled();
    }

    public static boolean containsUpgradeFromList(List<Upgrade> upgrades, Upgrade type) {
        return getUpgradeFromList(upgrades, type).isPresent();
    }

    public static Text getName(Upgrade upgrade) {
        return new LiteralText("TEST");
    }
}