package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.google.common.base.Predicate;
import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.network.MessageVectorParticle;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import com.needoriginalname.infinitygauntlet.util.MiscUtil;
import com.needoriginalname.infinitygauntlet.util.nodes.SmiteNode;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, final EntityPlayer playerIn, int timeLeft) {
        if (playerIn != null && stack != null){


            Predicate<EntityLivingBase> IS_ENTITY_IN_RANGE = new Predicate<EntityLivingBase>() {
                @Override
                public boolean apply(@Nullable EntityLivingBase input) {
                    float distence = playerIn.getDistanceToEntity(input);
                    if (input != playerIn && distence <= ConfigurationHandler.seekRangeForEntities){
                        EntityLivingBase entity = input;
                        boolean okToSmite = false;
                        // Check if tamed horse
                        boolean isTamedHorse = entity instanceof EntityHorse && ((EntityHorse) entity).isTame();
                        // check if entities owner
                        boolean isMyTamedEntity = (entity instanceof EntityTameable && ((EntityTameable) entity).getOwner()== playerIn);
                        //check for team mate's tamable.
                        boolean isTeamMatesEntity = (entity instanceof EntityTameable &&
                                (playerIn.getTeam() != null && ((EntityTameable) entity).getOwner() != null && playerIn.getTeam().isSameTeam(((EntityTameable) entity).getOwner().getTeam())) );

                        //check for team mate
                        boolean isTeammate = (playerIn.getTeam()!= null && entity.getTeam() != null && playerIn.getTeam().isSameTeam(entity.getTeam()));

                        okToSmite = !(isTamedHorse || isMyTamedEntity || isTeamMatesEntity || isTeammate);
                        return okToSmite;
                    } else {
                        return false;
                    }

                }
            };

            List<EntityLivingBase> list = worldIn.getEntities(EntityLivingBase.class, IS_ENTITY_IN_RANGE);
            proxy.addDeferredAction(new SmiteNode(worldIn, playerIn.getPosition(), worldIn.getTotalWorldTime() + 1, new Random().nextInt(), playerIn, list ));



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

                    MiscUtil.DamageEntity(e, playerIn);

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


}
