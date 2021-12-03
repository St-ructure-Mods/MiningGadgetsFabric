package mininggadgets.client.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ModScreens {
    public static void openGadgetSettingsScreen(ItemStack itemStack) {
        MinecraftClient.getInstance().setScreen(new MiningSettingScreen(itemStack));
    }
}
