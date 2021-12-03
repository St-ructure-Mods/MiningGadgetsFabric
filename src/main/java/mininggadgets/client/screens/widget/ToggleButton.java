package mininggadgets.client.screens.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ToggleButton extends ClickableWidget {
    private Predicate<Boolean> onPress;
    private boolean enabled;
    private Identifier texture;

    public ToggleButton(int xIn, int yIn, Text msg, Identifier texture, Predicate<Boolean> onPress) {
        super(xIn, yIn, 21, 26, msg);

        this.onPress = onPress;
        this.texture = texture;

        this.enabled = this.onPress.test(false);
    }

    @Override
    public void renderButton(MatrixStack stack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Color activeColor = this.enabled ? Color.GREEN : Color.RED;

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SrcFactor.ONE.value, GlStateManager.DstFactor.ZERO.value);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA.value, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA.value);

        RenderSystem.disableTexture();
        RenderSystem.clearColor(activeColor.getRed() / 255f, activeColor.getGreen() / 255f, activeColor.getBlue() / 255f, this.enabled ? .4f : .6f);
        drawTexture(stack, this.x, this.y, 0, 0, this.width, this.height);
        RenderSystem.enableTexture();

        RenderSystem.clearColor(1f, 1f, 1f, 1f);
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
        drawTexture(stack, this.x + 2, this.y + 5, 0, 0, 16, 16, 16, 16);
    }

    public List<Text> getToolTip() {
        return Arrays.asList(this.getMessage(), new LiteralText("Enabled: " + this.enabled).setStyle(Style.EMPTY.withColor(this.enabled ? Formatting.GREEN : Formatting.RED)));
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        this.enabled = !this.enabled;
        this.onPress.test(true);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }
}
