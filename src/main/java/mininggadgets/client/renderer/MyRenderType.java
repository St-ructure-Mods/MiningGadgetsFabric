package mininggadgets.client.renderer;

import mininggadgets.MiningGadgets;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class MyRenderType extends RenderLayer {
    private final static Identifier laserBeam = new Identifier(MiningGadgets.MOD_ID + ":textures/misc/laser.png");
    private final static Identifier laserBeam2 = new Identifier(MiningGadgets.MOD_ID + ":textures/misc/lase2r.png");
    private final static Identifier laserBeamGlow = new Identifier(MiningGadgets.MOD_ID + ":textures/misc/laser_glow.png");

    public MyRenderType(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }

    private static final LineWidth THICK_LINES = new LineWidth(OptionalDouble.of(3.0D));

    public static final RenderLayer LASER_MAIN_BEAM = of("MiningLaserMainBeam",
            VertexFormats.POSITION_COLOR_TEXTURE, GL11.GL_QUADS, 256,
            RenderLayer.MultiPhaseParameters.builder().texture(new Texture(laserBeam2, false, false))
                    .layering(Layering.NO_LAYERING)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(ALWAYS_DEPTH_TEST)
                    .cull(DISABLE_CULLING)
                    .lightmap(DISABLE_LIGHTMAP)
                    .writeMaskState(COLOR_MASK)
                    .build(false));

    public static final RenderLayer RenderBlock = of("MiningLaserRenderBlock",
            VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, GL11.GL_QUADS, 256,
            RenderLayer.MultiPhaseParameters.builder()
                    .shadeModel(SHADE_MODEL)
                    .lightmap(ENABLE_LIGHTMAP)
                    .texture(MIPMAP_BLOCK_ATLAS_TEXTURE)
                    .layering(Layering.NO_LAYERING)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .depthTest(LEQUAL_DEPTH_TEST)
                    .cull(ENABLE_CULLING)
                    .writeMaskState(COLOR_MASK)
                    .build(false));
}
