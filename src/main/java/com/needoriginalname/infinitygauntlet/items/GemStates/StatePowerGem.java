package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.network.MessageVectorParticle;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

/**
 * Created by Al on 5/16/2015.
 */
public class StatePowerGem extends AbstractGemState {

    @Override
    public int getActualTimeLife(ItemStack stack) {
        return 20*20;
    }

    @Override
    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isPowerGemGauntletEnabled;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        if (playerIn != null && stack != null){


            List entities = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().expand(ConfigurationHandler.seekRangeForEntities,ConfigurationHandler.seekRangeForEntities,ConfigurationHandler.seekRangeForEntities));
            for (Object o: entities){
                if (o instanceof EntityLivingBase){
                    EntityLivingBase entity = (EntityLivingBase) o;
                    boolean okToKill = false;
                    // Check if tamed horse
                    boolean isTamedHorse = entity instanceof EntityHorse && ((EntityHorse) entity).isTame();
                    // check if entities owner
                    boolean isMyTamedEntity = (entity instanceof EntityTameable && ((EntityTameable) entity).getOwner()== playerIn);
                    //check for team mate's tamable.
                    boolean isTeamMatesEntity = (entity instanceof EntityTameable &&
                            (playerIn.getTeam() != null && ((EntityTameable) entity).getOwner() != null && playerIn.getTeam().isSameTeam(((EntityTameable) entity).getOwner().getTeam())) );

                    //check for team mate
                    boolean isTeammate = (playerIn.getTeam()!= null && entity.getTeam() != null && playerIn.getTeam().isSameTeam(entity.getTeam()));

                    okToKill = !(isTamedHorse || isMyTamedEntity || isTeamMatesEntity || isTeammate);

                    if (okToKill) {
                        //worldIn.spawnEntityInWorld(new EntityLightningBolt(worldIn, entity.posX, entity.posY, entity.posZ));
                        this.DamageEntity(entity, playerIn);
                        worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, entity.posX, entity.posY, entity.posZ));

                        if (playerIn instanceof EntityPlayerMP){
                          //  ((EntityPlayerMP) playerIn).playerNetServerHandler.sendPacket(new
                        }

                    }
                }
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (ConfigurationHandler.isPowerGemEnabled) {
            if (playerIn.isSneaking()) {
                playerIn.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 30 * 300, 10));
                playerIn.addPotionEffect(new PotionEffect(Potion.resistance.id, 30 * 300, 10));
            } else {
                EntityLivingBase e = GetTargetEntityLiving(worldIn, playerIn, this.entitySeekRange);
                if (e != null) {

                    if (!worldIn.isRemote) {
                        PacketHandler.dispatcher.sendToAllAround(
                                new MessageVectorParticle(EnumParticleTypes.FLAME, playerIn.posX, playerIn.posY + 1.5, playerIn.posZ, e.posX, e.posY, e.posZ, 0.25, 0.25),
                                new NetworkRegistry.TargetPoint(playerIn.dimension, playerIn.posX, playerIn.posY, playerIn.posZ, 256));
                    }
                    worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "mob.ghast.fireball", 1.0f, 1.0f);

                    DamageEntity(e, playerIn);

                } else {
                    MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
                    if (mop != null) {
                        worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "mob.ghast.fireball", 1.0f, 1.0f);
                        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                            BlockPos pos = mop.getBlockPos();
                            worldIn.createExplosion(playerIn, pos.getX(), pos.getY(), pos.getZ(), ConfigurationHandler.powerGemExplosionPower, true);


                            if (!worldIn.isRemote)
                                PacketHandler.dispatcher.sendToAllAround(
                                        new MessageVectorParticle(EnumParticleTypes.FLAME, playerIn.posX , playerIn.posY + 1.5, playerIn.posZ, pos.getX() + 0.5, pos.getY()+0.5, pos.getZ() + 0.5, 0.25, 0.25),
                                        new NetworkRegistry.TargetPoint(playerIn.dimension, playerIn.posX, playerIn.posY, playerIn.posZ, 256));


                        }
                    }
                }
            }
        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    private void DamageEntity(EntityLivingBase e, EntityPlayer playerIn) {
        if (e instanceof EntityPlayer) {
            e.attackEntityFrom(new EntityDamageSourceIndirect("powergem", e, playerIn).setMagicDamage().setDamageBypassesArmor().setDamageAllowedInCreativeMode(), ConfigurationHandler.powerGemDamageAmount);

        } else if (e instanceof  IEntityMultiPart) {
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
