package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.items.GemStates.StateMindGem;
import com.needoriginalname.infinitygauntlet.items.ItemGauntlet;
import com.needoriginalname.infinitygauntlet.items.ItemGem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by Al on 5/17/2015.
 */
public class EventListener {


    @SubscribeEvent
    public void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
        if (event.entity.worldObj.isRemote) return;
        if (!(event.entity instanceof EntityPlayer)) {
            EntityLivingBase e = event.target;
            if (e == null) return;
            if (e instanceof EntityPlayer) {
                if (!this.canTarget((EntityPlayer) e)){
                 this.changeEntityTarget(event);
                }

            }
        }
    }

    private boolean canTarget(EntityPlayer player){
        ItemStack [] stacks = player.inventory.mainInventory;

        for (ItemStack stack : stacks) {
            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof ItemGauntlet || (item instanceof ItemGem && ( ((ItemGem) item).getGemState(stack) instanceof StateMindGem))) {

                    return false;
                }

            }
        }
        return true;

    }

    private  void changeEntityTarget(LivingSetAttackTargetEvent event) {
        EntityPlayer result = null;

        int range = ConfigurationHandler.seekNewTargetRange;

        List list = event.entity.worldObj.getEntitiesWithinAABBExcludingEntity(event.target, event.entity.getEntityBoundingBox().expand(range,4,range));

        for (Object o : list){
            if (o instanceof EntityLivingBase && !(o instanceof EntityAmbientCreature)){
                if (o instanceof EntityPlayer && this.canTarget((EntityPlayer) o)) {


                    //((EntityLiving) event.entity).setAttackTarget((EntityLivingBase) o);

                    ((EntityLiving) event.entity).setRevengeTarget((EntityLivingBase) o);
                    return;
                } else if (!(o instanceof  EntityPlayer)){
                    ((EntityLiving) event.entity).setRevengeTarget((EntityLivingBase) o);
                }
            }
        }

        ((EntityLiving)event.entity).setAttackTarget(null);
    }
}