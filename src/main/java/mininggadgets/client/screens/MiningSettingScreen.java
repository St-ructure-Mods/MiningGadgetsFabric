package mininggadgets.client.screens;

import mininggadgets.MiningGadgets;
import mininggadgets.items.MiningGadget;
import mininggadgets.items.gadget.MiningProperties;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import mininggadgets.client.screens.widget.ToggleButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiningSettingScreen extends Screen {
    private ItemStack gadget;

    private int beamRange;
    private int freezeDelay;
    private float volume;
    private int currentSize = 1;
    private boolean isWhitelist = true;
    private boolean isPrecision = true;
    private SliderWidget rangeSlider;
    private SliderWidget volumeSlider;
    private SliderWidget freezeDelaySlider;
    private List<Upgrade> toggleableList = new ArrayList<>();
    private HashMap<Upgrade, ToggleButton> upgradeButtons = new HashMap<>();
    private boolean containsFreeze = false;

    public MiningSettingScreen(ItemStack gadget) {
        super(new LiteralText("title"));

        this.gadget = gadget;
        this.beamRange = MiningProperties.getBeamRange(gadget);
        this.volume = MiningProperties.getVolume(gadget);
        this.freezeDelay = MiningProperties.getFreezeDelay(gadget);
    }

    @Override
    protected void init() {
        List<ClickableWidget> leftWidgets = new ArrayList<>();

        int baseX = width / 2, baseY = height / 2;

        toggleableList.clear();
        toggleableList = UpgradeTools.getUpgrades(this.gadget).stream().filter(Upgrade::isToggleable).collect(Collectors.toList());
        containsFreeze = UpgradeTools.containsUpgradeFromList(toggleableList, Upgrade.FREEZING);
        boolean containsVoid = UpgradeTools.containsUpgradeFromList(toggleableList, Upgrade.VOID_JUNK);

        isWhitelist = MiningProperties.getWhiteList(gadget);
        isPrecision = MiningProperties.getPrecisionMode(gadget);

        int top = baseY - (containsFreeze ? 80 : 60);

        int index = 0, x = baseX + 10, y = top + (containsVoid ? 45 : 20);
        for (Upgrade upgrade : toggleableList) {
            ToggleButton btn = new ToggleButton(x + (index * 30), y, UpgradeTools.getName(upgrade), new Identifier(MiningGadgets.MOD_ID, "textures/item/upgrade_" + upgrade.getName() + ".png"), send -> this.toggleUpgrade(upgrade, send));
        }
    }

    private boolean toggleUpgrade(Upgrade upgrade, boolean update) {
        // When the button is clicked we toggle
        if( update ) {
            this.updateButtons(upgrade);
//            PacketHandler.sendToServer(new PacketUpdateUpgrade(upgrade.getName()));
        }

        // When we're just init the gui, we check if it's on or off.
        return upgrade.isEnabled();
    }

    private void updateButtons(Upgrade upgrade) {
        for(Map.Entry<Upgrade, ToggleButton> btn : this.upgradeButtons.entrySet()) {
            Upgrade btnUpgrade = btn.getKey();

            if( (btnUpgrade.lazyIs(Upgrade.FORTUNE_1) && btn.getValue().isEnabled() && upgrade.lazyIs(Upgrade.SILK) )
                    || ((btnUpgrade.lazyIs(Upgrade.SILK)) && btn.getValue().isEnabled() && upgrade.lazyIs(Upgrade.FORTUNE_1)) ) {
                this.upgradeButtons.get(btn.getKey()).setEnabled(false);
            }
        }
    }
}
