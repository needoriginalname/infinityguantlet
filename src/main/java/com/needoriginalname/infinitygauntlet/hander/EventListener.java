package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.items.GemStates.StateMindGem;
import com.needoriginalname.infinitygauntlet.items.ItemGauntlet;
import com.needoriginalname.infinitygauntlet.items.ItemGem;
import com.needoriginalname.infinitygauntlet.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * Created by Al on 5/17/2015.
 */
public class EventListener {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        ParticleHandler.attachParticuleToPlayer(event.player);
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
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

    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event){
        Entity e = event.entity;
        if (ConfigurationHandler.chanceForPatreonRewardSpawning != 0 && e != null && (e instanceof EntityZombie || e instanceof EntitySkeleton) && ((EntityLiving) e).getCurrentArmor(3) == null){
            EntityLiving living = (EntityLiving) e;
            Double d = new Random().nextDouble();
            if (ConfigurationHandler.chanceForPatreonRewardSpawning >= d){
                //add custom head and nametag to entity, and replica gauntlet if hand is free
                ItemStack playerSkull = new ItemStack(Items.skull, 1, 3);
                NBTTagCompound tag = playerSkull.hasTagCompound() ? playerSkull.getTagCompound() : new NBTTagCompound();
                String username = RewardListHandler.getRandomRewardUsername();
                tag.setString("SkullOwner", username);
                living.setCustomNameTag(username);
                playerSkull.setTagCompound(tag);
                living.setCurrentItemOrArmor(4, playerSkull);
                if (living.getEquipmentInSlot(0) == null){
                    living.setCurrentItemOrArmor(0, new ItemStack(ModItems.itemInfinityGauntletReplica));
                }
            }
        }
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