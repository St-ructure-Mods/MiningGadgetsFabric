package mininggadgets.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class OurKeys {
    public static final KeyBinding shiftClickGuiBinding = new KeyBinding(
            "mininggadgets.text.open_gui",
            InputUtil.UNKNOWN_KEY.getCode(),
            "itemGroup.mininggadgets.item_group"
    );

    public static void register() {
        KeyBindingHelper.registerKeyBinding(shiftClickGuiBinding);
    }
}
