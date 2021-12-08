package mininggadgets.init;

import mininggadgets.MiningGadgets;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import reborncore.RebornRegistry;
import reborncore.common.powerSystem.RcEnergyItem;

public class InitUtils {
	public static <I extends Item> I setup(I item, String name) {
		RebornRegistry.registerIdent(item, new Identifier(MiningGadgets.MOD_ID, name));
		return item;
	}

	public static <B extends Block> B setup(B block, String name) {
		RebornRegistry.registerIdent(block, new Identifier(MiningGadgets.MOD_ID, name));
		return block;
	}

	public static SoundEvent setup(String name) {
		Identifier identifier = new Identifier(MiningGadgets.MOD_ID, name);
		return Registry.register(Registry.SOUND_EVENT, identifier, new SoundEvent(identifier));
	}

    public static void initPoweredItems(Item item, DefaultedList<ItemStack> itemList) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);
		RcEnergyItem energyItem = (RcEnergyItem) item;

		energyItem.setStoredEnergy(charged, energyItem.getEnergyCapacity());

		itemList.add(uncharged);
		itemList.add(charged);
	}
}