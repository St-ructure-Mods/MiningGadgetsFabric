package mininggadgets.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;

import java.util.Collections;
import java.util.List;


public abstract class ScrollPanel extends Screen {
    private final MinecraftClient client;
    protected final int width;
    protected final int height;
    protected final int top;
    protected final int bottom;
    protected final int right;
    protected final int left;
    private boolean scrolling;
    protected float scrollDistance;
    protected boolean captureMouse = true;
    protected final int border;

    private final int barWidth;
    private final int barLeft;
    private final int bgColorFrom;
    private final int bgColorTo;
    private final int barBgColor;
    private final int barColor;
    private final int barBorderColor;

    public ScrollPanel(MinecraftClient client, int width, int height, int top, int left) {
        this(client, width, height, top, left, 4);
    }

    public ScrollPanel(MinecraftClient client, int width, int height, int top, int left, int border) {
        this(client, width, height, top, left, border, 6);
    }

    public ScrollPanel(MinecraftClient client, int width, int height, int top, int left, int border, int barWidth) {
        this(client, width, height, top, left, border, barWidth, 0xC0101010, 0xD0101010);
    }

    public ScrollPanel(MinecraftClient client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo)
    {
        this(client, width, height, top, left, border, barWidth, bgColorFrom, bgColorTo, 0xFF000000, 0xFF808080, 0xFFC0C0C0);
    }

    public ScrollPanel(MinecraftClient client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo, int barBgColor, int barColor, int barBorderColor)
    {
        super(new TranslatableText("test"));
        this.client = client;
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
        this.bottom = height + this.top;
        this.right = width + this.left;
        this.barLeft = this.left + this.width - barWidth;
        this.border = border;
        this.barWidth = barWidth;
        this.bgColorFrom = bgColorFrom;
        this.bgColorTo = bgColorTo;
        this.barBgColor = barBgColor;
        this.barColor = barColor;
        this.barBorderColor = barBorderColor;
    }

    protected abstract int getContentHeight();

    protected void drawBackground() {};

    protected void drawBackground(MatrixStack matrix, Tessellator tess, float partialTicks)
    {
        BufferBuilder worldr = tess.getBuffer();

        if (this.client.world != null)
        {
            this.drawGradientRect(matrix, this.left, this.top, this.right, this.bottom, bgColorFrom, bgColorTo);
        }
        else // Draw dark dirt background
        {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, 0);
            final float texScale = 32.0F;
            worldr.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            worldr.vertex(this.left,  this.bottom, 0.0D).texture(this.left  / texScale, (this.bottom + (int)this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).next();
            worldr.vertex(this.right, this.bottom, 0.0D).texture(this.right / texScale, (this.bottom + (int)this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).next();
            worldr.vertex(this.right, this.top,    0.0D).texture(this.right / texScale, (this.top    + (int)this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).next();
            worldr.vertex(this.left,  this.top,    0.0D).texture(this.left  / texScale, (this.top    + (int)this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).next();
            tess.draw();
        }
    }

    protected abstract void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX, int mouseY);

    protected boolean clickPanel(double mouseX, double mouseY, int button) { return false; }

    private int getMaxScroll()
    {
        return this.getContentHeight() - (this.height - this.border);
    }

    private void applyScrollLimits()
    {
        int max = getMaxScroll();

        if (max < 0)
        {
            max /= 2;
        }

        if (this.scrollDistance < 0.0F)
        {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > max)
        {
            this.scrollDistance = max;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
    {
        if (scroll != 0)
        {
            this.scrollDistance += -scroll * getScrollAmount();
            applyScrollLimits();
            return true;
        }
        return false;
    }

    protected int getScrollAmount()
    {
        return 20;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY)
    {
        return mouseX >= this.left && mouseX <= this.left + this.width &&
                mouseY >= this.top && mouseY <= this.bottom;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {

        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        this.scrolling = button == 0 && mouseX >= barLeft && mouseX < barLeft + barWidth;
        if (this.scrolling)
        {
            return true;
        }
        int mouseListY = ((int)mouseY) - this.top - this.getContentHeight() + (int)this.scrollDistance - border;
        if (mouseX >= left && mouseX <= right && mouseListY < 0)
        {
            return this.clickPanel(mouseX - left, mouseY - this.top + (int)this.scrollDistance - border, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (super.mouseReleased(mouseX, mouseY, button))
            return true;
        boolean ret = this.scrolling;
        this.scrolling = false;
        return ret;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
    {
        if (this.scrolling)
        {
            int maxScroll = height - getBarHeight();
            double moved = deltaY / maxScroll;
            this.scrollDistance += getMaxScroll() * moved;
            applyScrollLimits();
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        double scale = client.getWindow().getScaleFactor();
        RenderSystem.enableScissor((int)(left  * scale), (int)(client.getWindow().getHeight() - (bottom * scale)),
                (int)(width * scale), (int)(height * scale));

        this.drawBackground(matrix, tess, partialTicks);

        int baseY = this.top + border - (int)this.scrollDistance;
        this.drawPanel(matrix, right, baseY, tess, mouseX, mouseY);

        RenderSystem.disableDepthTest();

        int extraHeight = (this.getContentHeight() + border) - height;
        if (extraHeight > 0)
        {
            int barHeight = getBarHeight();

            int barTop = (int)this.scrollDistance * (height - barHeight) / extraHeight + this.top;
            if (barTop < this.top)
            {
                barTop = this.top;
            }

            int barBgAlpha = this.barBgColor >> 24 & 0xff;
            int barBgRed   = this.barBgColor >> 16 & 0xff;
            int barBgGreen = this.barBgColor >>  8 & 0xff;
            int barBgBlue  = this.barBgColor       & 0xff;

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            worldr.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            worldr.vertex(barLeft,            this.bottom, 0.0D).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).next();
            worldr.vertex(barLeft + barWidth, this.bottom, 0.0D).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).next();
            worldr.vertex(barLeft + barWidth, this.top,    0.0D).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).next();
            worldr.vertex(barLeft,            this.top,    0.0D).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).next();
            tess.draw();

            int barAlpha = this.barColor >> 24 & 0xff;
            int barRed   = this.barColor >> 16 & 0xff;
            int barGreen = this.barColor >>  8 & 0xff;
            int barBlue  = this.barColor       & 0xff;

            worldr.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            worldr.vertex(barLeft,            barTop + barHeight, 0.0D).color(barRed, barGreen, barBlue, barAlpha).next();
            worldr.vertex(barLeft + barWidth, barTop + barHeight, 0.0D).color(barRed, barGreen, barBlue, barAlpha).next();
            worldr.vertex(barLeft + barWidth, barTop,             0.0D).color(barRed, barGreen, barBlue, barAlpha).next();
            worldr.vertex(barLeft,            barTop,             0.0D).color(barRed, barGreen, barBlue, barAlpha).next();
            tess.draw();

            int barBorderAlpha = this.barBorderColor >> 24 & 0xff;
            int barBorderRed   = this.barBorderColor >> 16 & 0xff;
            int barBorderGreen = this.barBorderColor >>  8 & 0xff;
            int barBorderBlue  = this.barBorderColor       & 0xff;

            worldr.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            worldr.vertex(barLeft,                barTop + barHeight - 1, 0.0D).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).next();
            worldr.vertex(barLeft + barWidth - 1, barTop + barHeight - 1, 0.0D).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).next();
            worldr.vertex(barLeft + barWidth - 1, barTop,                 0.0D).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).next();
            worldr.vertex(barLeft,                barTop,                 0.0D).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).next();
            tess.draw();
        }

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.disableScissor();
    }

    private int getBarHeight()
    {
        int barHeight = (height * height) / this.getContentHeight();

        if (barHeight < 32) barHeight = 32;

        if (barHeight > height - border*2)
            barHeight = height - border*2;

        return barHeight;
    }

    protected void drawGradientRect(MatrixStack mStack, int left, int top, int right, int bottom, int color1, int color2)
    {
        drawGradientRect(mStack.peek().getModel(), 0, left, top, right, bottom, color1, color2);
    }

    public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float startAlpha = (float)(startColor >> 24 & 255) / 255.0F;
        float startRed   = (float)(startColor >> 16 & 255) / 255.0F;
        float startGreen = (float)(startColor >>  8 & 255) / 255.0F;
        float startBlue  = (float)(startColor       & 255) / 255.0F;
        float endAlpha   = (float)(endColor   >> 24 & 255) / 255.0F;
        float endRed     = (float)(endColor   >> 16 & 255) / 255.0F;
        float endGreen   = (float)(endColor   >>  8 & 255) / 255.0F;
        float endBlue    = (float)(endColor         & 255) / 255.0F;

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(mat, right,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        buffer.vertex(mat,  left,    top, zLevel).color(startRed, startGreen, startBlue, startAlpha).next();
        buffer.vertex(mat,  left, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).next();
        buffer.vertex(mat, right, bottom, zLevel).color(  endRed,   endGreen,   endBlue,   endAlpha).next();
        tessellator.draw();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    @Override
    public List<? extends Element> children()
    {
        return Collections.emptyList();
    }
}