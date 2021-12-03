package mininggadgets.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import reborncore.common.powerSystem.RcEnergyItem;

public class InitUtils {
    public static void initPoweredItems(Item item, DefaultedList<ItemStack> itemList) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);
		RcEnergyItem energyItem = (RcEnergyItem) item;

		energyItem.setStoredEnergy(charged, energyItem.getEnergyCapacity());

		itemList.add(uncharged);
		itemList.add(charged);
	}
}