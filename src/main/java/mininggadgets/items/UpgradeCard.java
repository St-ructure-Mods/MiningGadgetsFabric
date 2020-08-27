package mininggadgets.items;

import java.util.List;

import mininggadgets.MiningGadgets;
import mininggadgets.items.upgrade.Upgrade;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class UpgradeCard extends Item {
    private Upgrade upgrade;

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (stack.getItem() instanceof UpgradeCard) {
            Upgrade upgrade = ((UpgradeCard) stack.getItem()).upgrade;
            int cost = upgrade.getCostPerBlock();
            if (cost > 0)
                tooltip.add(
                    new TranslatableText(
                        "mininggadgets.tooltip.item.upgrade_cost", cost
                    )
                    .setStyle(
                        Style.EMPTY
                        .withColor(TextColor.parse("AQUA"))
                    )
                );

            cost = 0;
            

            if (cost > 0)
                tooltip.add(
                    new TranslatableText(
                        "mininggadgets.tooltip.item.use_cost", cost
                    )
                    .setStyle(
                        Style.EMPTY
                        .withColor(TextColor.parse("AQUA"))
                    )
                );

            tooltip.add(
                new TranslatableText(this.upgrade.getToolTip())
                .setStyle(
                    Style.EMPTY
                    .withColor(TextColor.parse("GRAY"))
                )
            );
        }
    }

    public UpgradeCard(Upgrade upgrade, int maxStack) {
        super(new Item.Settings().group(MiningGadgets.ITEMGROUP).maxCount(maxStack));
        this.upgrade = upgrade;
    }

    public Upgrade getUpgrade() {
        return upgrade;
    }
}