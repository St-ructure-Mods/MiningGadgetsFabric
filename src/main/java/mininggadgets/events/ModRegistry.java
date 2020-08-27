package mininggadgets.events;

import mininggadgets.MiningGadgets;
import mininggadgets.init.MGContent;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import reborncore.RebornRegistry;

public class ModRegistry {
    
    public static void setup() {
        registerItems();
    }


    private static void registerItems() {
        RebornRegistry.registerItem(MGContent.MINING_GADGET = setup(new MiningGadget(), "mining_gadet"));
    }

    private static <I extends Item> I setup(I item, String name) {
        RebornRegistry.registerIdent(item, new Identifier(MiningGadgets.MOD_ID, name));
        return item;
    }
}