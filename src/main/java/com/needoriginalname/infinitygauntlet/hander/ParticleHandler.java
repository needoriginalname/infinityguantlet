package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.particles.PatreonParticuleFx;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by Owner on 10/22/2016.
 */
public class ParticleHandler {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Map<EntityPlayer, PatreonParticuleFx> trackedParticules = new HashMap<EntityPlayer, PatreonParticuleFx>();
    //public static Set<EntityPlayer> trackedPlayers = new HashSet<EntityPlayer>();




    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void stitcherEventPre(TextureStitchEvent.Pre event){
        ResourceLocation rl = new ResourceLocation(Reference.MODID+ ":particles/tinygem");
        event.map.registerSprite(rl);
    }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public void onEntityJoinWorld(EntityJoinWorldEvent event){
       if (event.entity == Minecraft.getMinecraft().thePlayer){
            resetTrackedParticules();
       }
   }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        if (event.side.isServer()) return;
        EntityPlayer currentplayer = Minecraft.getMinecraft().thePlayer;
        if (currentplayer.worldObj == event.player.worldObj
                && event.player.getDistanceToEntity(currentplayer) < 64){
            attachParticleOnPlayer(event.player);
        } else {
            detachParticleOnPlayer(event.player);
        }


    }


    public static void resetTrackedParticules(){
        trackedParticules.clear();
    }


    private static void attachParticleOnPlayer(EntityPlayer player) {
        //checks to see if it can attach particule to player

        if (!trackedParticules.containsKey(player) && RewardListHandler.getRewardUsernames().contains(player.getDisplayNameString().toLowerCase())){
            PatreonParticuleFx particule = new PatreonParticuleFx(player.worldObj, player);
            trackedParticules.put(player,  particule);
            Minecraft.getMinecraft().effectRenderer.addEffect(particule);

            LogHelper.info("Attaching particule to " + player.getDisplayNameString());
            //return true;
        }
    }


    public static boolean detachParticleOnPlayer(EntityPlayer player){

            if (trackedParticules.containsKey(player)){
                PatreonParticuleFx fx = trackedParticules.remove(player);
                fx.setDead();


                LogHelper.info("Detaching particule from " + player.getDisplayNameString());
                return true;
            }




        return false;
    }



    public static EntityFX spawnParticule(String name, double x, double y, double z, double speedX, double speedY, double speedZ){
        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null){
            World w = Minecraft.getMinecraft().theWorld;
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
