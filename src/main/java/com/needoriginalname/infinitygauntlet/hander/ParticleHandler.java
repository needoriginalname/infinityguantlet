package com.needoriginalname.infinitygauntlet.hander;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;

/**
 * Created by Owner on 10/22/2016.
 */
public class ParticleHandler {
    private static Minecraft mc = Minecraft.getMinecraft();
    static World w = mc.theWorld;
    static TextureManager renderEngine = mc.renderEngine;

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
                /*if (particle.equals("patreon"){

                }*/

            }

            mc.effectRenderer.addEffect(particle);
            return particle;
        }
        return null;
    }

}
