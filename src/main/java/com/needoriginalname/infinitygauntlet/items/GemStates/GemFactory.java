package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.reference.IDs;
import net.minecraft.util.MathHelper;

/**
 * Created by Al on 5/16/2015.
 */
public class GemFactory {
    public static AbstractGemState getGemState(int id){

        id = MathHelper.clamp_int(id, 0, IDs.Gems.values().length-1);
        return IDs.Gems.values()[id].getGemState();
    }

}
