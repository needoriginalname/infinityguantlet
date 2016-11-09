package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.particles.PatreonParticuleFx;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Owner on 10/22/2016.
 */
public class ParticleHandler {
    private static Minecraft mc = Minecraft.getMinecraft();
    static World w = mc.theWorld;
    static TextureManager renderEngine = mc.renderEngine;

    public static Map<EntityPlayer, PatreonParticuleFx> trackedParticules = new HashMap<EntityPlayer, PatreonParticuleFx>();
    public static Set<EntityPlayer> trackedPlayers = new HashSet<EntityPlayer>();

    public static boolean handleParticlesOnPlayer(EntityPlayer player){
        if (!trackedPlayers.contains(player) && w.getEntityByID(player.getEntityId()) != null && player.getDistanceToEntity(mc.thePlayer) < 128){
            attachParticleOnPlayer(player);
        } else {
            if(trackedPlayers.contains(player) && (player.getDistanceToEntity(mc.thePlayer) >= 128 || mc.theWorld.getEntityByID(player.getEntityId()) == null  )){
                detachParticleOnPlayer(player);
            }


            //checks to see if particule is dead
            if(trackedPlayers.contains(player)){
                if (trackedParticules.containsKey(player)){
                    //kill particle if sent to new dim
                    if (trackedParticules.get(player).dimension != mc.thePlayer.dimension){
                        trackedParticules.get(player).setDead();
                    }

                    //stop tracking particule if dead
                    if (trackedParticules.get(player).isDead){
                        detachParticleOnPlayer(player);
                    }
                }
            }
        }

        return false;
    }

    private static void attachParticleOnPlayer(EntityPlayer player) {
        trackedPlayers.add(player);

        if (RewardListHandler.getRewardUsernames().contains(player.getDisplayNameString())){
            PatreonParticuleFx particule = new PatreonParticuleFx(player.worldObj, player);
            trackedParticules.put(player,  particule);
            Minecraft.getMinecraft().effectRenderer.addEffect(particule);

            LogHelper.info("Attaching particule to " + player.getDisplayNameString());
            //return true;
        }
    }


    public static boolean detachParticleOnPlayer(EntityPlayer player){
        if (trackedPlayers.contains(player)){
            trackedPlayers.remove(player);
            if (trackedParticules.containsKey(player)){
                PatreonParticuleFx fx = trackedParticules.remove(player);
                fx.setDead();


                LogHelper.info("Detaching particule from " + player.getDisplayNameString());
                return true;
            }



        }
        return false;
    }



    public static EntityFX spawnParticule(String name, double x, double y, double z, double speedX, double speedY, double speedZ){
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null){
            int particleSetting = mc.gameSettings.particleSetting;
            if (particleSetting == 1 && w.rand.nextInt(3) == 0){
                particleSetting = 2;
            }

            double relativeX = mc.getRenderViewEntity().posX - x;
            double relativeY = mc.getRenderViewEntity().posY - y;
            double relativeZ = mc.getRenderViewEntity().posZ - z;
            EntityFX particle = null;
            double var22 = 16.0D;

            //check to see if particule is more then 16 blocks away
            if (relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ > var22 * var22){
                return null;
            } else if (particleSetting > 1){
                return null;
            } else {
                particle = new PatreonParticuleFx(w, x, y, z, speedX, speedY, speedZ);
            }

            return particle;
        }
        return null;
    }

}
