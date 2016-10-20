package com.needoriginalname.infinitygauntlet.reference;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.items.GemStates.*;
import net.minecraft.item.EnumDyeColor;

/**
 * Created by Al on 5/16/2015.
 */
public class IDs {
    public static class Packets{
        public static int ChangeGauntletMode = 1;
    }


    public enum Gems {
        PowerGem(0, new StatePowerGem(), Names.PowerGem, ConfigurationHandler.powerGemRarity, EnumDyeColor.RED),
        SoulGem(1, new StateSoulGem(), Names.SoulGem, ConfigurationHandler.soulGemRarity, EnumDyeColor.GREEN),
        RealityGem(2, new StateRealityGem(), Names.RealityGem, ConfigurationHandler.realityGemRarity, EnumDyeColor.YELLOW),
        MindGem(3, new StateMindGem(), Names.MindGem, ConfigurationHandler.mindGemRarity, EnumDyeColor.BLUE),
        SpaceGem(4, new StateSpaceGem(), Names.SpaceGem, ConfigurationHandler.spaceGemRarity, EnumDyeColor.PURPLE),
        TimeGem(5, new StateTimeGem(), Names.TimeGem, ConfigurationHandler.timeGemRarity, EnumDyeColor.ORANGE);

        private final EnumDyeColor dyeColor;
        int id;
        int rarity;
        AbstractGemState gemState;
        String unlocalizedName;

        private Gems(int id, AbstractGemState gemState, String unlocalizedName, int rarity, EnumDyeColor dyeColor){
            this.id = id;
            this.gemState = gemState;
            this.unlocalizedName = unlocalizedName;
            this.rarity = rarity;
            this.dyeColor = dyeColor;

        }

        public int getRarity(){
            return this.rarity;
        }

       public int getID() {
           return this.id;
       }

        public AbstractGemState getGemState(){
            return this.gemState;
        }

        public String getUnlocalizedName(){
            return this.unlocalizedName;
        }

        public EnumDyeColor getDyeColor() { return this.dyeColor;}
    }

}
