package mininggadgets.client.particles.playerparticle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mininggadgets.client.particles.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

@Environment(EnvType.CLIENT)
public class PlayerParticleData implements ParticleEffect {
    public final float size;
    public final float r, g, b;
    public final float maxAgeMul;
    public final boolean depthTest;
    public final double targetX;
    public final double targetY;
    public final double targetZ;
    public final String partType;

    public static PlayerParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, 1);
    }

    public static PlayerParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, maxAgeMul, true);
    }

    public static PlayerParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, boolean depth) {
        return playerparticle(type, targetX, targetY, targetZ, size, r, g, b, 1, depth);
    }

    public static PlayerParticleData playerparticle(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        return new PlayerParticleData(type, targetX, targetY, targetZ, size, r, g, b, maxAgeMul, depthTest);
    }

    private PlayerParticleData(String type, double targetX, double targetY, double targetZ, float size, float r, float g, float b, float maxAgeMul, boolean depthTest) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.maxAgeMul = maxAgeMul;
        this.depthTest = depthTest;
        this.partType = type;
    }

    @Override
    public ParticleType<PlayerParticleData> getType() {
        return ModParticles.PLAYERPARTICLE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(partType);
        buf.writeDouble(targetX);
        buf.writeDouble(targetY);
        buf.writeDouble(targetZ);
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(maxAgeMul);
        buf.writeBoolean(depthTest);
    }

    @Override
    public String asString() {
        return null;
    }

    public static final Factory<PlayerParticleData> FACTORY = new Factory<PlayerParticleData>() {

        @Override
        public PlayerParticleData read(ParticleType<PlayerParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String partType = reader.readString();
            reader.expect(' ');
            double targetX = reader.readDouble();
            reader.expect(' ');
            double targetY = reader.readDouble();
            reader.expect(' ');
            double targetZ = reader.readDouble();
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
            return new PlayerParticleData(partType, targetX, targetY, targetZ, size, r, g, b, mam, depth);
        }

        @Override
        public PlayerParticleData read(ParticleType<PlayerParticleData> type, PacketByteBuf buf) {
            return new PlayerParticleData(buf.readString(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
        }
    };
}
