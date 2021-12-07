package mininggadgets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mininggadgets.MiningGadgets;
import mininggadgets.blockentities.ModificationTableBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;

public class GuiModificationTable extends GuiBase<BuiltScreenHandler> {
    private static final Identifier background = new Identifier(MiningGadgets.MOD_ID, "textures/gui/modification_table.png");

    ModificationTableBlockEntity blockEntity;

    public GuiModificationTable(int syncId, final PlayerEntity player, final ModificationTableBlockEntity blockEntity) {
        super(player, blockEntity, blockEntity.createScreenHandler(syncId, player));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(matrixStack, lastFrameDuration, mouseX, mouseY);

        RenderSystem.setShaderTexture(0, background);
        drawTexture(matrixStack, getGuiLeft(), getGuiTop(), 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawForeground(matrixStack, mouseX, mouseY);
    }
}
