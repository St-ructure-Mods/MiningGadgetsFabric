package mininggadgets.client.renderer;

import mininggadgets.blockentities.RenderBlockBlockEntity;
import mininggadgets.blocks.RenderBlock;
import mininggadgets.items.gadget.MiningProperties;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Random;

public class RenderBlockBER extends BlockEntityRenderer<RenderBlockBlockEntity> {

    public RenderBlockBER(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    private void renderModelBrightnessColorQuads(MatrixStack.Entry matrixEntry, VertexConsumer builder, float red, float green, float blue, float alpha, List<BakedQuad> listQuads, int combinedLightsIn, int combinedOverlayIn) {
        for (BakedQuad bakedquad  : listQuads) {
            float f;
            float f1;
            float f2;

            if (bakedquad .hasColor()) {
                f = red * 1f;
                f1 = green * 1f;
                f2 = blue * 1f;
            } else {
                f = 1f;
                f1 = 1f;
                f2 = 1f;
            }

            builder.color(f, f1, f2, alpha).quad(matrixEntry, bakedquad, f, f1, f2, combinedLightsIn, combinedOverlayIn);
        }
    }

    @Override
    public void render(RenderBlockBlockEntity entity, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int durability = entity.getDurability();
        int originalDurability = entity.getOriginalDurability();
        int prevDurability = entity.getPriorDurability();
        float nowScale = (float) (prevDurability) / (float) originalDurability;
        float prevScale = (float) (prevDurability) / (float) originalDurability;
        float scale = (MathHelper.lerp(partialTicks, prevScale, nowScale));

        if (scale >= 1.0f)
            scale = 1f;
        if (scale <= 0)
            scale = 0;

        BlockState renderState = entity.getRenderBlock();

        if (renderState == null)
            return;

        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        MinecraftClient.getInstance().getTextureManager().bindTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        MiningProperties.BreakTypes breakType = entity.getBreakType();
        BakedModel bakedModel = blockRenderManager.getModel(renderState);
        BlockColors blockColors = MinecraftClient.getInstance().getBlockColors();
        int color = blockColors.getColor(renderState, entity.getWorld(), entity.getPos(), 0);
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;

        matrixStackIn.push();

        if (breakType == MiningProperties.BreakTypes.SHRINK) {
            matrixStackIn.translate((1 - scale) / 2, (1 - scale) / 2, (1 - scale) / 2);
            matrixStackIn.scale(scale, scale, scale);

            for (Direction direction : Direction.values()) {
                renderModelBrightnessColorQuads(matrixStackIn.peek(), vertexConsumers.getBuffer(RenderLayer.getCutout()), f, f1, f2, 1f, bakedModel.getQuads(renderState, direction, new Random(MathHelper.hashCode(entity.getPos()))), light, overlay);
            }
        } else if (breakType == MiningProperties.BreakTypes.FADE) {
            scale = MathHelper.lerp(scale, 0.1f, 1.0f);
            for (Direction direction : Direction.values()) {
                if (!(entity.getWorld().getBlockState(entity.getPos().offset(direction)).getBlock() instanceof RenderBlock)) {
                    renderModelBrightnessColorQuads(matrixStackIn.peek(), vertexConsumers.getBuffer(MyRenderType.RenderBlock), f, f1, f2, scale, bakedModel.getQuads(renderState, direction, new Random(MathHelper.hashCode(entity.getPos()))), light, overlay);
                }
            }
        }

        matrixStackIn.pop();
    }
}
