package mininggadgets.config;

import reborncore.common.config.Config;

public class MGConfig {

    @Config(config = "items", category = "general", key = "miningGadgetMaxPower", comment = "Mining Gadget Max Charge")
    public static int MININGGADGET_MAXPOWER = 1_000_000;

    @Config(config = "items", category = "general", key = "miningGadgetBaseCost", comment = "Mining Gadget Base Cost")
    public static int MININGGADGET_BASECOST = 20_000;
}