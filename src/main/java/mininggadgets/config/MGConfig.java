package mininggadgets.config;

import reborncore.common.config.Config;

public class MGConfig {

    @Config(config = "items", category = "general", key = "miningGadgetMaxPower", comment = "Mining Gadget Max Charge")
    public static int MININGGADGET_MAXPOWER = 1_000_000;

    @Config(config = "items", category = "general", key = "miningGadgetBaseCost", comment = "Mining Gadget Base Cost")
    public static int MININGGADGET_BASECOST = 20_000;

    @Config(config = "items", category = "general", key = "miningGadgetVoidCost", comment = "Mining Gadget Void Cost")
    public static int UPGRADECOST_VOID = 10;

    @Config(config = "items", category = "general", key = "miningGadgetSilkTouchCost", comment = "Mining Gadget Silk Touch Cost")
    public static int UPGRADECOST_SILKTOUCH = 100;

    @Config(config = "items", category = "general", key = "miningGadgetMagnetCost", comment = "Mining Gadget Magnet Cost")
    public static int UPGRADECOST_MAGNET = 25;

    @Config(config = "items", category = "general", key = "miningGadgetF1Cost", comment = "Mining Gadget Fortune 1 Cost")
    public static int UPGRADECOST_FORTUNE1 = 30;

    @Config(config = "items", category = "general", key = "miningGadgetF2Cost", comment = "Mining Gadget Fortune 2 Cost")
    public static int UPGRADECOST_FORTUNE2 = 60;

    @Config(config = "items", category = "general", key = "miningGadgetF3Cost", comment = "Mining Gadget Fortune 3 Cost")
    public static int UPGRADECOST_FORTUNE3 = 100;

    @Config(config = "items", category = "general", key = "miningGadgetE1Cost", comment = "Mining Gadget Efficiency 1 Cost")
    public static int UPGRADECOST_EFFICIENCY1 = 10;

    @Config(config = "items", category = "general", key = "miningGadgetE2Cost", comment = "Mining Gadget Efficiency 2 Cost")
    public static int UPGRADECOST_EFFICIENCY2 = 20;

    @Config(config = "items", category = "general", key = "miningGadgetE3Cost", comment = "Mining Gadget Efficiency 3 Cost")
    public static int UPGRADECOST_EFFICIENCY3 = 30;

    @Config(config = "items", category = "general", key = "miningGadgetE4Cost", comment = "Mining Gadget Efficiency 4 Cost")
    public static int UPGRADECOST_EFFICIENCY4 = 40;

    @Config(config = "items", category = "general", key = "miningGadgetE5Cost", comment = "Mining Gadget Efficiency 5 Cost")
    public static int UPGRADECOST_EFFICIENCY5 = 50;
}