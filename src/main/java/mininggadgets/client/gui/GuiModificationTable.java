package mininggadgets.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import mininggadgets.MiningGadgets;
import mininggadgets.blockentities.ModificationTableBlockEntity;
import mininggadgets.items.MiningGadget;
import mininggadgets.items.UpgradeCard;
import mininggadgets.items.upgrade.Upgrade;
import mininggadgets.items.upgrade.UpgradeTools;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;
import reborncore.common.util.ItemHandlerUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiModificationTable extends GuiBase<BuiltScreenHandler> {
    private static final Identifier background = new Identifier(MiningGadgets.MOD_ID, "textures/gui/modification_table.png");

    ModificationTableBlockEntity blockEntity;
    ScrollingUpgrades scrollingUpgrades;

    public GuiModificationTable(int syncId, final PlayerEntity player, final ModificationTableBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncId, player));
        this.blockEntity = blockEntity;
    }

    @Override
    public void init() {
        super.init();

        this.scrollingUpgrades = new ScrollingUpgrades(MinecraftClient.getInstance(), this.backgroundWidth - 14, 72, getGuiTop() + 17, getGuiLeft() + 7, this);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.scrollingUpgrades.render(matrixStack, mouseX, mouseY, partialTicks);;
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(matrixStack, lastFrameDuration, mouseX, mouseY);

        RenderSystem.setShaderTexture(0, background);
        int relX = (this.width - this.backgroundWidth) / 2;
        int relY = (this.height - this.backgroundHeight) / 2;
        drawTexture(matrixStack, relX - 23, relY, 0, 0, this.backgroundWidth + 23, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawForeground(matrixStack, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        ItemStack gadget = this.blockEntity.inventory.getStack(0);
        if (!gadget.isEmpty() && gadget.getItem() instanceof MiningGadget) {
            if (scrollingUpgrades.isMouseOver(mouseX, mouseY)) {
//                if (UpgradeTools.containsUpgrade(gadget, ((UpgradeCard) heldStack.getItem()).getUpgrade())) {
//                    return false;
//                }

//                PacketHandler.sendToServer(new PacketInsertUpgrade(this.tePos, heldStack));
//                this.menu.setCarried(ItemStack.EMPTY);
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private static class ScrollingUpgrades extends ScrollPanel {
        GuiModificationTable parent;
        Upgrade upgrade = null;

        ScrollingUpgrades(MinecraftClient client, int width, int height, int top, int left, GuiModificationTable parent) {
            super(client, width, height, top, left);
            this.parent = parent;
        }

        @Override
        protected int getContentHeight() {
            return (int) Math.ceil(this.parent.blockEntity.getUpgradesCache().size() / 7f) * 20;
        }

        @Override
        protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY) {
            Upgrade currentUpgrade = null;
            int x = (entryRight - this.width) + 3;
            int y = relativeY;

            int index = 0;
            for (Upgrade upgrade : this.parent.blockEntity.getUpgradesCache()) {
                MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(new ItemStack(upgrade.getCard()), x, y);

                if( isMouseOver(mouseX, mouseY) && (mouseX > x && mouseX < x + 15 && mouseY > y && mouseY < y + 15)  )
                    currentUpgrade = upgrade;

                x += 22;
                index ++;
                if( index % 7 == 0 ) {
                    y += 20;
                    x = (entryRight - this.width) + 3;
                }
            }

            if(currentUpgrade == null || !currentUpgrade.equals(this.upgrade))
                this.upgrade = currentUpgrade;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if( !isMouseOver(mouseX, mouseY) || this.upgrade == null )
                return false;

//            PacketHandler.sendToServer(new PacketExtractUpgrade(this.parent.tePos, this.upgrade.getName(), this.upgrade.getName().length()));
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            super.render(stack, mouseX, mouseY, partialTicks);

            List<Text> textList = null;

            if( this.upgrade != null  )
                textList = new ArrayList<>(this.upgrade.getStack().getTooltip(this.parent.getMinecraft().player, TooltipContext.Default.NORMAL));
//                this.parent.renderTooltip(stack, textList, mouseX, mouseY);
        }
    }
}
