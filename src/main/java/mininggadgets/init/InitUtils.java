package mininggadgets.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import team.reborn.energy.Energy;

public class InitUtils {
    public static void initPoweredItems(Item item, DefaultedList<ItemStack> itemList) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);

		Energy.of(charged).set(Energy.of(charged).getMaxStored());

		itemList.add(uncharged);
		itemList.add(charged);
	}
}