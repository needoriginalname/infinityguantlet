package com.needoriginalname.infinitygauntlet.util;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSourceIndirect;

/**
 * Created by Al on 5/13/2015.
 */
public class MiscUtil {
    public static boolean InRange(int value, int min, int max){
        return (value >= min && value <= max);
    }
    public static boolean InRange(float value, float min, float max){
        return (value >= min && value <= max);
    }
    public static boolean InRange(double value, double min, double max){
        return (value >= min && value <= max);
    }
    public static void DamageEntity(EntityLivingBase e, EntityPlayer playerIn) {
        if (e instanceof EntityPlayer) {
            e.attackEntityFrom(new EntityDamageSourceIndirect("powergem", e, playerIn).setMagicDamage().setDamageBypassesArmor().setDamageAllowedInCreativeMode(), ConfigurationHandler.powerGemDamageAmount);

        } else if (e instanceof IEntityMultiPart) {
            Entity part = e.getParts()[0];
            if (part != null) {
                part.attackEntityFrom(new EntityDamageSourceIndirect("powergem", e, playerIn).setMagicDamage().setDamageBypassesArmor().setDamageAllowedInCreativeMode(), ConfigurationHandler.powerGemDamageAmount);
            } else {
                LogHelper.error("Error, unable to find part from IEntityMultiPart on Entity: " + e.toString() + "with ID" + e.getEntityId());
            }
        } else {
            e.attackEntityFrom(new EntityDamageSourceIndirect("powergem", e, playerIn).setMagicDamage().setDamageBypassesArmor().setDamageAllowedInCreativeMode(), ConfigurationHandler.powerGemDamageAmount);
        }
    }
}
