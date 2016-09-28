package com.needoriginalname.infinitygauntlet.util;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.items.ItemGauntlet;
import com.needoriginalname.infinitygauntlet.items.ItemGem;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

/**
 * Created by Al on 5/22/2015.
 */
public class FuelHandler implements IFuelHandler {

    @Override
    public int getBurnTime(ItemStack fuel) {
        int burnTime = 0;

        if (fuel != null){
            if (fuel.getItem() instanceof ItemGauntlet){
                burnTime = ConfigurationHandler.infinityGauntletBurnTime;
            } else if (fuel.getItem() instanceof ItemGem && fuel.getMetadata() == IDs.Gems.PowerGem.getID()){
                burnTime = ConfigurationHandler.powerGemBurnTime;
            }
        }

        return burnTime;



    }
}
