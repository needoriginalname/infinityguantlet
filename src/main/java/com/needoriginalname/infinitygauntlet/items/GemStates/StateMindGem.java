package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.util.NBTHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Al on 5/16/2015.
 */
public class StateMindGem extends AbstractGemState {


    @Override
    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isMindGemGauntletEnabled;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        EntityLivingBase entity = this.GetTargetEntityLiving(worldIn, playerIn, ConfigurationHandler.seekRangeForEntities);
        if (entity != null && entity instanceof EntityPlayer){
            EntityPlayer otherPlayer = (EntityPlayer) entity;
            playerIn.addExperience(otherPlayer.experienceTotal);
            otherPlayer.removeExperienceLevel(otherPlayer.experienceLevel + 1);

            otherPlayer.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 60 * 3));
            otherPlayer.addPotionEffect(new PotionEffect(Potion.blindness.getId(), 20 * 60));
            otherPlayer.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 20 * 60 * 3));

        }
    }

    @Override
    public boolean hasEffect(ItemStack item) {
        return NBTHelper.getBoolean(item, "hasTargetAttacker");
    }

    private boolean setAttacker(ItemStack itemStackin, World worldIn, EntityPlayer player){
        EntityLivingBase target = this.GetTargetEntityLiving(worldIn, player, entitySeekRange);

        if (target != null && target instanceof EntityHorse || (target instanceof EntityTameable && ((EntityTameable) target).getOwner() == null)){

            if (target instanceof EntityWolf){
                this.tameWolf(player, (EntityWolf)target);
            } else if (target instanceof EntityOcelot) {
                this.tameOcelot(player, (EntityOcelot) target);
            } else if (target instanceof EntityHorse && !((EntityHorse) target).isUndead()){
                this.tameHorse(player, (EntityHorse)target);
            }
        }


        if (target != null && (target instanceof EntityMob || target instanceof EntityGolem)){
            worldIn.playSoundEffect(player.posX, player.posY, player.posZ, "random.successful_hit", 1, 1);

            int id = target.getEntityId();
            NBTHelper.setBoolean(itemStackin, "hasTargetAttacker", true);
            NBTHelper.setInteger(itemStackin, "attackerID", id);
            return true;
        }

        return false;
    }

    private void tameHorse(EntityPlayer player, EntityHorse target) {
        target.setTamedBy(player);
        target.worldObj.setEntityState(target, (byte) 7);
    }

    protected void playTameEffect(boolean p_70908_1_, EntityTameable e)
    {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;

        if (!p_70908_1_)
        {
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
        }

        for (int i = 0; i < 7; ++i)
        {
            double d0 = e.worldObj.rand.nextGaussian() * 0.02D;
            double d1 = e.worldObj.rand.nextGaussian() * 0.02D;
            double d2 = e.worldObj.rand.nextGaussian() * 0.02D;
            e.worldObj.spawnParticle(enumparticletypes, e.posX + (double)(e.worldObj.rand.nextFloat() * e.width * 2.0F) - (double)e.width, e.posY + 0.5D + (double)(e.worldObj.rand.nextFloat() * e.height), e.posZ + (double)(e.worldObj.rand.nextFloat() * e.width * 2.0F) - (double)e.width, d0, d1, d2, new int[0]);
        }
    }


    private void tameWolf(EntityPlayer player, EntityWolf target) {

        target.setTamed(true);
        target.getNavigator().clearPathEntity();
        target.setAttackTarget(null);
        target.getAISit().setSitting(true);
        target.setHealth(target.getMaxHealth());

        target.setOwnerId(player.getUniqueID().toString());
        this.playTameEffect(true, target);
        target.worldObj.setEntityState(target, (byte)7);

    }

    private void tameOcelot(EntityPlayer player, EntityOcelot target){
        target.setTamed(true);
        target.setTameSkin(1 + target.worldObj.rand.nextInt(3));
        target.setOwnerId(player.getUniqueID().toString());
        this.playTameEffect(true, target);
        target.getAISit().setSitting(true);
        target.worldObj.setEntityState(target, (byte)7);
    }


    private boolean attackTarget(ItemStack itemStackIn, World worldIn, EntityPlayer player){
        boolean b = false;

        if (!NBTHelper.getBoolean(itemStackIn, "hasTargetAttacker")){return false;}
        int id = NBTHelper.getInt(itemStackIn, "attackerID");
        Entity e = worldIn.getEntityByID(id);
        if (e != null && (e instanceof EntityMob || e instanceof EntityGolem)){
            EntityLivingBase target = this.GetTargetEntityLiving(worldIn, player, entitySeekRange);
            if (target != null && target.getEntityId() != e.getEntityId()) {
                worldIn.playSoundEffect(player.posX, player.posY, player.posZ, "random.successful_hit", 1, 1);
                //((EntityLiving) e).setAttackTarget(target);
                ((EntityLiving) e).setRevengeTarget(target);
                b = true;
            }
        }
        NBTHelper.setBoolean(itemStackIn, "hasTargetAttacker", false);
        return b;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (ConfigurationHandler.isMindGemEnabled) {
            if (!playerIn.isSneaking()) {
                if (!attackTarget(itemStackIn, worldIn, playerIn)) {
                    setAttacker(itemStackIn, worldIn, playerIn);
                }
            } else {
                if (groupAttackOnTarget(itemStackIn, worldIn, playerIn)) {
                    worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "random.successful_hit", 1, 1);
                    NBTHelper.setBoolean(itemStackIn, "hasTargetAttacker", false);
                }

            }

        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    private boolean groupAttackOnTarget(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        EntityLivingBase target = this.GetTargetEntityLiving(worldIn, playerIn, entitySeekRange);
        if (target != null){
            List<Entity> list = target.worldObj.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().expand(64,16,64));
            for (Entity e : list){
                if (e != null && e instanceof EntityLiving){
                    ((EntityLiving) e).setAttackTarget(target);
                }
            }

            return list.size() > 1;

        }
        return false;
    }


    @Override
    public int getActualTimeLife(ItemStack stack) {
        return ConfigurationHandler.mindGauntletChargeTime;
    }
}
