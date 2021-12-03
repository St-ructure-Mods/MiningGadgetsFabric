package mininggadgets.items.gadget;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class MiningProperties {
    private MiningProperties() {}

    private static final String KEY_BEAM_RANGE = "beamRange";
    private static final String KEY_MAX_BEAM_RANGE = "maxBeamRange";
    private static final String KEY_WHITELIST = "isWhitelist";
    private static final String KEY_RANGE = "range";
    private static final String KEY_SPEED = "speed";
    private static final String BREAK_TYPE = "breakType";
    private static final String CAN_MINE = "canMine";
    private static final String PRECISION_MODE = "precisionMode";
    private static final String VOLUME = "volume";
    private static final String FREEZE_PARTICLE_DELAY = "freeze_particle_delay";

    public static final String KEY_FILTERS = "filters";
    public static final String COLOR_RED = "colorRed";
    public static final String COLOR_GREEN = "colorGreen";
    public static final String COLOR_BLUE = "colorBlue";
    public static final String COLOR_RED_INNER = "colorRedInner";
    public static final String COLOR_GREEN_INNER = "colorGreenInner";
    public static final String COLOR_BLUE_INNER = "colorBlueInner";

    public static final int MIN_RANGE = 5;

    public enum BreakTypes {
        SHRINK,
        FADE
    }

    public static short getColor(ItemStack gadget, String color) {
        NbtCompound compound = gadget.getOrCreateNbt();
        if (color.equals(COLOR_RED) || color.contains("Inner")) {
            return !compound.contains(color) ? setColor(gadget, (short) 255, color) : compound.getShort(color);
        } else {
            return !compound.contains(color) ? setColor(gadget, (short) 0, color) : compound.getShort(color);
        }
    }

    public static short setColor(ItemStack gadget, short colorValue, String color) {
        gadget.getOrCreateNbt().putShort(color, colorValue);
        return colorValue;
    }

    public static BreakTypes setBreakType(ItemStack gadget, BreakTypes breakType) {
        gadget.getOrCreateNbt().putByte(BREAK_TYPE, (byte) breakType.ordinal());
        return breakType;
    }

    public static void nextBreakType(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        if (compound.contains(BREAK_TYPE)) {
            int type = getBreakType(gadget).ordinal() == BreakTypes.values().length - 1 ? 0 : getBreakType(gadget).ordinal() + 1;
            setBreakType(gadget, BreakTypes.values()[type]);
        } else {
            setBreakType(gadget, BreakTypes.FADE);
        }
    }

    public static BreakTypes getBreakType(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(BREAK_TYPE) ? setBreakType(gadget, BreakTypes.SHRINK) : BreakTypes.values()[compound.getByte(BREAK_TYPE)];
    }

    public static int setSpeed(ItemStack gadget, int speed) {
        gadget.getOrCreateNbt().putInt(KEY_SPEED, speed);
        return speed;
    }

    public static int getSpeed(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(KEY_SPEED) ? setSpeed(gadget, 1) : compound.getInt(KEY_SPEED);
    }

    public static int setRange(ItemStack gadget, int range) {
        gadget.getOrCreateNbt().putInt(KEY_RANGE, range);
        return range;
    }

    public static int getRange(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(KEY_RANGE) ? setRange(gadget, 1) : compound.getInt(KEY_RANGE);
    }

    public static int setBeamRange(ItemStack gadget, int range) {
        gadget.getOrCreateNbt().putInt(KEY_BEAM_RANGE, range);
        return range;
    }

    public static int setBeamMaxRange(ItemStack gadget, int range) {
        gadget.getOrCreateNbt().putInt(KEY_MAX_BEAM_RANGE, range);
        return range;
    }

    public static int getBeamRange(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(KEY_BEAM_RANGE) ? setBeamRange(gadget, MIN_RANGE) : compound.getInt(KEY_BEAM_RANGE);
    }

    public static int getBeamMaxRange(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(KEY_MAX_BEAM_RANGE) ? setBeamMaxRange(gadget, MIN_RANGE) : compound.getInt(KEY_MAX_BEAM_RANGE);
    }

    public static boolean setWhitelist(ItemStack gadget, boolean isWhitelist) {
        gadget.getOrCreateNbt().putBoolean(KEY_WHITELIST, isWhitelist);
        return isWhitelist;
    }

    public static boolean getWhiteList(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(KEY_WHITELIST) ? setWhitelist(gadget, true) : compound.getBoolean(KEY_WHITELIST);
    }

    public static boolean setCanMine(ItemStack gadget, boolean canMine) {
        gadget.getOrCreateNbt().putBoolean(CAN_MINE, canMine);
        return canMine;
    }

    public static boolean getCanMine(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(CAN_MINE) ? setCanMine(gadget, true) : compound.getBoolean(CAN_MINE);
    }

    public static boolean setPrecisionMode(ItemStack gadget, boolean precisionMode) {
        gadget.getOrCreateNbt().putBoolean(PRECISION_MODE, precisionMode);
        return precisionMode;
    }

    public static boolean getPrecisionMode(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(PRECISION_MODE) ? setPrecisionMode(gadget, false) : compound.getBoolean(PRECISION_MODE);
    }

    public static float setVolume(ItemStack gadget, float volume) {
        gadget.getOrCreateNbt().putFloat(VOLUME, Math.max(0.0f, Math.min(1.0f, volume)));
        return volume;
    }

    public static float getVolume(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(VOLUME) ? setVolume(gadget, 1.0f) : compound.getFloat(VOLUME);
    }

    public static int setFreezeDelay(ItemStack gadget, int volume) {
        gadget.getOrCreateNbt().putInt(FREEZE_PARTICLE_DELAY, Math.max(0, Math.min(10, volume)));
        return volume;
    }

    public static int getFreezeDelay(ItemStack gadget) {
        NbtCompound compound = gadget.getOrCreateNbt();
        return !compound.contains(FREEZE_PARTICLE_DELAY) ? setFreezeDelay(gadget, 0) : compound.getInt(FREEZE_PARTICLE_DELAY);
    }

    /**
     * So this is a bit fun, because we only need the items in our list we're ditching half the data
     * that the `Items` actually contains.
     *
     * @implNote Please do not use {@link #deserializeItemStackList(NbtCompound)} or {@link #serializeItemStackList(List)}
     *           if you wish to maintain the original tag data on the gadget. These have specific uses.
     *
     *           See {@link com.direwolf20.mininggadgets.common.network.packets.PacketOpenFilterContainer.Handler} for an
     *           understanding on why you shouldn't change the tad data on the gadget directly.
     */
    public static List<ItemStack> getFiltersAsList(ItemStack gadget) {
        return deserializeItemStackList(gadget.getOrCreateSubNbt(MiningProperties.KEY_FILTERS));
    }

    // mostly stolen from ItemStackHandler
    public static List<ItemStack> deserializeItemStackList(NbtCompound nbt) {
        List<ItemStack> stacks = new ArrayList<>();
        NbtList tagList = nbt.getList("Items", 1);

        for (int i = 0; i < tagList.size(); i++) {
            NbtCompound itemTags = tagList.getCompound(i);
            stacks.add(ItemStack.fromNbt(itemTags));
        }

        return stacks;
    }

    public static NbtCompound serializeItemStackList(List<ItemStack> stacks) {
        NbtList nbtTagList = new NbtList();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (stacks.get(i).isEmpty())
                continue;

            NbtCompound itemTag = new NbtCompound();
            stacks.get(i).writeNbt(itemTag);
            nbtTagList.add(itemTag);
        }

        NbtCompound nbt = new NbtCompound();
        nbt.put("Items", nbtTagList);
        return nbt;
    }
}