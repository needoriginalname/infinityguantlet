package com.needoriginalname.infinitygauntlet.util;

import com.needoriginalname.infinitygauntlet.reference.Key;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Al on 5/19/2015.
 */
public interface IKeyBound {
    void doKeyAction(EntityPlayer player, ItemStack stack, Key.Keys key);
}
