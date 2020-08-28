package mininggadgets.client.particles.laserparticle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mininggadgets.client.particles.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

import java.util.Locale;

public class LaserParticleData implements ParticleEffect {
    public final float size;
    public final float r, g, b;
    public final float maxAgeMul;
    public final boolean depthTest;
    public final BlockState state;

    public static LaserParticleData laserparticle(BlockState state, float size, float r, float g, float b) {
        return laserparticle(state, size, r, g, b, 1);
    }

    public static LaserParticleData laserparticle(BlockState state, float size, float r, float g, float b, float maxAgeMul) {
        return laserparticle(state, size, r, g, b, maxAgeMul, true);
    }

    public static LaserParticleData laserparticle(BlockState state, float size, float r, float g, float b, boolean depth) {
        return laserparticle(state, size, r, g, b, 1, depth);
    }

    public static LaserParticleData laserparticle(BlockState state, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        return new LaserParticleData(state, size, r, g, b, maxAgeMul, depthTest);
    }

    private LaserParticleData(BlockState state, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.maxAgeMul = maxAgeMul;
        this.depthTest = depthTest;
        this.state = state;
    }

    @Override
    public ParticleType<LaserParticleData> getType() {
        return ModParticles.LASERPARTICLE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(Block.STATE_IDS.getRawId(state));
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(maxAgeMul);
        buf.writeBoolean(depthTest);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %s",
                this.getType(), this.size, this.r, this.g, this.b, this.maxAgeMul, this.depthTest);
    }


    public static final Factory<LaserParticleData> FACTORY = new Factory<LaserParticleData>() {

        @Override
        public LaserParticleData read(ParticleType<LaserParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            BlockState state = (new BlockArgumentParser(reader, false)).parse(false).getBlockState();
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float mam = reader.readFloat();
            boolean depth = true;
            if (reader.canRead()) {
                reader.expect(' ');
                depth = reader.readBoolean();
            }
            return new LaserParticleData(state, size, r, g, b, mam, depth);
        }

        @Override
        public LaserParticleData read(ParticleType<LaserParticleData> type, PacketByteBuf buf) {
            return new LaserParticleData(Block.STATE_IDS.get(buf.readVarInt()), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }
    };
}
