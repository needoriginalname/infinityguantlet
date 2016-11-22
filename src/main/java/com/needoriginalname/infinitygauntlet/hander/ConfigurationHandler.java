package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.reference.Reference;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Al on 5/18/2015.
 */
public class ConfigurationHandler {
    public static Configuration configuration;
    public static boolean isPowerGemEnabled;
    public static boolean isMindGemEnabled;
    public static boolean isSoulGemEnabled;
    public static boolean isSpaceGemEnabled;
    public static boolean isTimeGemEnabled;
    public static boolean isRealityGemEnabled;
    public static boolean isPowerGemGauntletEnabled;
    public static boolean isMindGemGauntletEnabled;
    public static boolean isSoulGemGauntletEnabled;
    public static boolean isSpaceGemGauntletEnabled;
    public static boolean isTimeGemGauntletEnabled;
    public static boolean isRealityGauntletGemEnabled;
    public static int seekRangeForEntities;
    public static int seekRangeForBlocks;
    public static int powerGemRarity;
    public static int soulGemRarity;
    public static int realityGemRarity;
    public static int mindGemRarity;
    public static int timeGemRarity;
    public static int spaceGemRarity;
    public static int powerGemDamageAmount;
    public static int powerGemExplosionPower;
    public static int maxNumberOfEntitiesInSoulGem;
    public static int powerGemBurnTime;
    public static int infinityGauntletBurnTime;
    public static int infinityGauntletMineSpeed;
    public static int soulGemDimensionID;
    public static boolean allowGauntletToChangeEntityAgeable;
    public static int soulGemBiomeID;
    public static short maxDepthOfBlockReplacement;
    public static int maxActionsPerTick;
    public static int[] supportedDimensionIDs;
    public static int seekNewTargetRange;
    public static double chanceForPatreonRewardSpawning;
    public static boolean forcePatreonListToReload;
    public static int mindGauntletChargeTime;
    public static int powerGauntletChargeTime;
    public static int realityGauntletChargeTime;
    public static int soulGauntletChargeTime;
    public static int spaceGauntletChargeTime;
    public static int timeGauntletChargeTime;
    public static List<String> replaceBlockList;
    public static boolean isReplaceBlockListWhiteList;
    public static List<String> replaceBlockListWith;
    public static boolean isReplaceBlockListWithWhiteList;
    public static byte stopBlockUpdateForLiquids;
    public static boolean allowNonFullBlocksToBeReplacedWith;


    public static void init(File configFile){
        if (configuration == null){
            configuration = new Configuration(configFile);
            loadConfiguration();
            MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
        }
    }

    private static void loadConfiguration() {
        isPowerGemEnabled = configuration.getBoolean("isPowerGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Power Gem's abilities");
        isMindGemEnabled = configuration.getBoolean("isMindGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Mind Gem's abilities");
        isSoulGemEnabled = configuration.getBoolean("isSoulGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Soul Gem's abilities");
        isSpaceGemEnabled = configuration.getBoolean("isSpaceGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Space Gem's abilities");
        isTimeGemEnabled = configuration.getBoolean("isTimeGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Time Gem's abilities");
        isRealityGemEnabled = configuration.getBoolean("isRealityGemEnabled", Configuration.CATEGORY_GENERAL, true, "Enable the Reality Gem's abilities");

        powerGemRarity = configuration.getInt("powerGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10, "Chance for Power Gem to spawn in Dungions, 1 is same chance for Golden Apple, 10 is same chance for bread");
        soulGemRarity = configuration.getInt("soulGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10, "Chance for Soul Gem to spawn in Dungions, 1 is same chance for Golden Apple, 10 is same chance for bread");
        realityGemRarity = configuration.getInt("realityGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10,"Chance for Reality Gem to spawn in Dungions, 1 is same chance for Goldne Apple, 10 is same chance for bread");
        mindGemRarity = configuration.getInt("mindGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10, "Chance for Mind Gem to spawn in Dungions, 1 is same chance for Golden Apple, 10 is same chance for bread");
        timeGemRarity = configuration.getInt("timeGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10, "Chance for Time Gem to spawn in Dungions, 1 is same chance for Golden Apple, 10 is same chance for bread");
        spaceGemRarity = configuration.getInt("spaceGemRarity", Configuration.CATEGORY_GENERAL, 2, 0, 10, "Chance for Space Gem to spawn in Dungions, 1 is same chance for Golden Apple, 10 is same chance for bread");

        isPowerGemGauntletEnabled = configuration.getBoolean("isPowerGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Power Gem's Gauntlet Only ability");
        isMindGemGauntletEnabled = configuration.getBoolean("isMindGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Mind Gem's Gauntlet Only ability");
        isSoulGemGauntletEnabled = configuration.getBoolean("isSoulGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Soul Gem's Gauntlet Only ability");
        isTimeGemGauntletEnabled = configuration.getBoolean("isTimeGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Time Gem's Gauntlet Only ability");
        isRealityGauntletGemEnabled = configuration.getBoolean("isRealityGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Reality Gem's Gauntlet Only ability");
        isSpaceGemGauntletEnabled = configuration.getBoolean("isSpaceGemGauntletEnabled", Configuration.CATEGORY_GENERAL, true, "Enables the Space Gem's Gauntlet Only ability");

        seekNewTargetRange = configuration.getInt("seekRangeForNewTarget", Configuration.CATEGORY_GENERAL, 32, 1, 64, "Distance mobs targeting players will look for different target");
        seekRangeForEntities = configuration.getInt("seekRangeForEntities", Configuration.CATEGORY_GENERAL, 64, 0, 128, "The distance the Gauntlet or Gem will try to find an Entity in the player crosshair");
        seekRangeForBlocks = configuration.getInt("seekRangeForBlocks", Configuration.CATEGORY_GENERAL, 64, 0, 128, "The distance the Gauntlet or Gem will try to find Blocks in the player crosshair");
        powerGemDamageAmount = configuration.getInt("powerGemDamageAmount", Configuration.CATEGORY_GENERAL, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, "Amount of Damage that the PowerGem will send to an enemy");
        powerGemExplosionPower = configuration.getInt("powerGemExplosionPower", Configuration.CATEGORY_GENERAL, 4, 1, 50, "amount of explosion pwoer a power gem can give off, 4.0 is the same as a vanilla TNT block");
        maxNumberOfEntitiesInSoulGem = configuration.getInt("maxNumberofEntitesInSoulGem", Configuration.CATEGORY_GENERAL, 25, 0, 100, "max number of entities the Soul Gem can hold");
        powerGemBurnTime = configuration.getInt("powerGemBurnTime", Configuration.CATEGORY_GENERAL, 1600, 0, 72000, "The Burn time the Power Gem will have in a furnace, does not use up item.");
        infinityGauntletBurnTime = configuration.getInt("infinityGauntletBurnTime", Configuration.CATEGORY_GENERAL, 1600, 0, 72000, "The Burn time the Infinity Gauntlet will have in a furnace, does not use up item");
        infinityGauntletMineSpeed = configuration.getInt("infinityGauntletMineSpeed", Configuration.CATEGORY_GENERAL, 100, 0, Integer.MAX_VALUE, "Amount of damage the Infinity Gauntlet will give when used to mine a block");
        allowGauntletToChangeEntityAgeable = configuration.getBoolean("allowGauntletToChangeEntityAgeable", Configuration.CATEGORY_GENERAL, true, "Allow Gauntlet to change Age of Entity Ageable");
        chanceForPatreonRewardSpawning = configuration.getFloat("chanceForPatreonRewardSpawning", Configuration.CATEGORY_GENERAL, 0.05f, 0, 1, "The percent chance for Zombies to spawn with Contrib / Patreon Rewards");


        maxDepthOfBlockReplacement = (short) configuration.getInt("maxDepthOfBlockReplacement", Configuration.CATEGORY_GENERAL, 32, 1, 50, "Max depth of Block replacement algorithm for the Reality Gem");
        maxActionsPerTick = configuration.getInt("maxActionsPerTick", Configuration.CATEGORY_GENERAL, 25, 1, 150, "Limits the number of actions per tick per player action");

        soulGemDimensionID = configuration.getInt("soulGemDimensionID", Configuration.CATEGORY_GENERAL, -10, Short.MIN_VALUE, Short.MAX_VALUE, "dimension ID for Soul Gem Dimension");
        soulGemBiomeID = configuration.getInt("soulGemBiomeID", Configuration.CATEGORY_GENERAL, 50, 50, Short.MAX_VALUE, "Biome ID for Soul Gem Biome");

        mindGauntletChargeTime = configuration.getInt("mindGauntletChargeTime", Configuration.CATEGORY_GENERAL, 20, 20, 72000, "Number of ticks to charge up the gauntlet's mind gem ability.");
        spaceGauntletChargeTime = configuration.getInt("spaceGauntletChargeTime", Configuration.CATEGORY_GENERAL, 20, 20, 72000, "Number of ticks to charge up the gauntlet's space gem ability.");
        powerGauntletChargeTime = configuration.getInt("powerGauntletChargeTime", Configuration.CATEGORY_GENERAL, 200, 20, 72000, "Number of ticks to charge up the gauntlet's power gem ability.");
        realityGauntletChargeTime = configuration.getInt("realityGauntletChargeTime", Configuration.CATEGORY_GENERAL, 40, 20, 72000, "Number of ticks to charge up the gauntlet's reality gem ability.");
        timeGauntletChargeTime = configuration.getInt("timeGauntletChargeTime", Configuration.CATEGORY_GENERAL, 40, 20, 72000, "Number of ticks to charge up the gauntlet's time gem ability.");
        soulGauntletChargeTime = configuration.getInt("soulGauntletChargeTime", Configuration.CATEGORY_GENERAL, 40, 20, 72000, "Number of ticks to charge up the gauntlet's soul gem ability.");
        stopBlockUpdateForLiquids = (byte) configuration.getInt("stopBlockUpdateForLiquids", Configuration.CATEGORY_GENERAL, 3, 0, 3, "0 to not stop block updates when clearing out liquids with reality gem, 1 when not at maxDepth, 2 when at maxDepth, 3 for both.");
        String[] defaultDim = new String[3];
        defaultDim[0] = "0"; defaultDim[1] = "-1"; defaultDim[2] = "1";

        String[] supportedDimensionIDsString = configuration.getStringList("supportedDimensionIDs", Configuration.CATEGORY_GENERAL, defaultDim, "Dimintion IDs that the Space Gem can warp the player to, if The End is allowed to warp, it must be the last one");

        supportedDimensionIDs = new int[supportedDimensionIDsString.length];

        for (int i = 0; i < supportedDimensionIDs.length ; i++){
            supportedDimensionIDs[i] = Integer.parseInt(supportedDimensionIDsString[i]);
        }
        forcePatreonListToReload = configuration.getBoolean("forcePatreonListReload", Configuration.CATEGORY_GENERAL, false, "When Toggled to True, force Patreon List to reload. Toggled to False, does nothing.");

        if (forcePatreonListToReload){
            ParticleHandler.resetTrackedParticules();
            if(RewardListHandler.loadList()){
                LogHelper.info("Patreon list reloaded");
            } else {
                LogHelper.error("Failed to reload Patreon list");
            }
        }

        replaceBlockList = new ArrayList<String>();
        replaceBlockListWith = new ArrayList<String>();
        isReplaceBlockListWhiteList = configuration.getBoolean("isReplaceBlockListWhiteList", Configuration.CATEGORY_GENERAL, false, "replaceBlockList is a whitelist if true, blacklist if false");
        isReplaceBlockListWithWhiteList = configuration.getBoolean("isReplaceBlockListWithWhiteList", Configuration.CATEGORY_GENERAL, false, "replaceBlockListWith is a whitelist if true, blacklist if false");
        allowNonFullBlocksToBeReplacedWith = configuration.getBoolean("allowNonFullBlocksToBeReplacedWith", Configuration.CATEGORY_GENERAL, false, "allows the reality gem to replace blocks with a non full block, EX: chests, anvils, crops, etc");
        for(String s: configuration.getStringList("replaceBlockList", Configuration.CATEGORY_GENERAL, new String[]{}, "a white or black list of unlocalized names for  infinity gauntlet to replace.")){
            replaceBlockList.add(s);
        }




        for (String s: configuration.getStringList("replaceBlockListWith", Configuration.CATEGORY_GENERAL,
                new String[]{},
                "a white or black list of unlocalized names for infinity gauntlet to replace block with.")){
            replaceBlockListWith.add(s);
        }



        if (configuration.hasChanged()){
            configuration.save();
        }

    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {

        if (event.modID.equalsIgnoreCase(Reference.MODID))
        {
            loadConfiguration();
        }
    }
}
