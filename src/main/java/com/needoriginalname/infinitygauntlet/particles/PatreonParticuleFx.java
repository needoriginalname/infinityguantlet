package com.needoriginalname.infinitygauntlet.particles;


import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Owner on 10/21/2016.
 */
public class PatreonParticuleFx extends EntityFX {

    private final Vec3 baseV;
    private final double startingX;
    private final double startingY;
    private final double startingZ;
    private final int startingPoint;

    public Entity getAttachedEntity() {
        return attachedEntity;
    }

    Entity attachedEntity;



    public PatreonParticuleFx(World worldin, Entity e){
        super(worldin, e.posX, e.posY, e.posZ);

        this.startingX = e.posX;
        this.startingY = e.posY;
        this.startingZ = e.posZ;
        attachedEntity = e;
        baseV = new Vec3(1,0,0);
        this.particleBlue = 1.0F;
        this.particleRed = 0.F;
        this.particleGreen = 0.5F;
        this.particleScale = 1.4F;
        this.particleMaxAge = 80;
        this.noClip = true;
        this.particleMaxAge = Integer.MAX_VALUE;

        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;




        Vec3 v = this.getParticuleVec(e);
        this.posX = v.xCoord;
        this.posY = v.yCoord;
        this.posZ = v.zCoord;


        this.startingPoint = new Random().nextInt(1200);
        this.setParticleTextureIndex(48);

    }

    public PatreonParticuleFx(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        baseV = new Vec3(1,0,0);

        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.particleTextureIndexX = 0;
        this.particleTextureIndexY = 0;
        this.particleMaxAge = getNumberOfTicksPerCycle();
        this.startingX = xCoordIn;
        this.startingY = yCoordIn;
        this.startingZ = zCoordIn;

        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.startingPoint = new Random().nextInt(1200);


        this.setParticleTextureIndex(48);
    }



    boolean isAttachedEntityAlive() {
        if (attachedEntity != null){
            return attachedEntity.isEntityAlive();
        } else {
            return true;
        }
    }


    @Override
    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float x, float y, float z, float yaw, float pitch) {
        //not sure if the last 2 are actually yaw and pitch
        Vec3 newV = getParticuleVec(entityIn, partialTicks);
        this.posX = newV.xCoord;
        this.posY = newV.yCoord;
        this.posZ = newV.zCoord;

        Vec3 colorV = getColorVector(entityIn, partialTicks);
        this.particleRed = (float) colorV.xCoord / 255;
        this.particleGreen = (float) colorV.yCoord / 255;
        this.particleBlue = (float) colorV.zCoord / 255;

        super.renderParticle(worldRendererIn, entityIn, partialTicks, x,y,z,yaw,pitch);
    }

    private Vec3 getColorVector(Entity entityIn, float partialTicks) {
        int step = (entityIn.ticksExisted + startingPoint) % 1200;
        Vec3 v;
        //power gem color
        if (step <= 150){
            v = new Vec3(255, 0, 0);
        }
        //power to reality
        else if (step <= 200){
            float s = (step - 150)/50f;
            v = new Vec3(255, 255 * s, 0);
        }
        //reality color
        else if (step <= 350){
            v = new Vec3(255, 255, 0);
        }
        //reality to mind
        else if (step <= 400){
            float s = (step - 350)/50f;
            v = new Vec3(255*(1-s), 255*(1-s), 255*s);
        }
        //mind color
        else if (step <= 550){
            v = new Vec3(0,0,255);
        }
        //mind to space
        else if (step <= 600){
            float s = (step - 550)/50f;
            v = new Vec3(127*s,0, 255);
        }
        //space gem
        else if (step <= 750){
            v = new Vec3(127,0,255);
        }
        //space to time
        else if (step <= 800){
            float s = (step - 750)/50f;
            v = new Vec3(127 + (255-127)*s, 128*s, 255*(1-s));
        }
        //time color
        else if (step <= 950){
            v = new Vec3(255, 128, 0);
        }
        //time to soul color
        else if (step <= 1000){
            float s = (step-950)/50f;
            v = new Vec3(255*(1-s), 128+(127*s), 0);
        }
        // soul color
        else if (step <= 1150){
            v = new Vec3(0,255,0);
        } else if (step <= 1200){
            float s = (step-1150)/50f;
            v = new Vec3(255*s, 255*(1-s),0);
        } else {
            LogHelper.error("Invalid color determined for particule");
            v = new Vec3(0,0,0);
        }

        return v;





    }

    private Vec3 getParticuleVec(Entity entityIn) {
        return getParticuleVec(entityIn, 0);
    }


    private Vec3 getParticuleVec(Entity entityIn, float partialTicks) {
        int ticksPerCycle = getNumberOfTicksPerCycle();
        float percentAround = (entityIn.ticksExisted % ticksPerCycle); // + (partialTicks * (entityIn.ticksExisted % ticksPerCycle)   / ticksPerCycle);
        //percentAround += partialTicks;
        percentAround /= ticksPerCycle;
        Vec3 newV = baseV.rotateYaw((float) (percentAround * ( 2* Math.PI)));
        newV = this.getParticulePosition(newV);
        return newV;
    }

    private Vec3 getParticulePosition(Vec3 newV) {
        double x, y, z;
        if (attachedEntity != null){
            x = attachedEntity.posX + newV.xCoord;
            y = attachedEntity.posY + newV.yCoord + 1;
            z = attachedEntity.posZ + newV.zCoord;
        } else {
            x = startingX + newV.xCoord;
            y = startingY + newV.yCoord + 1;
            z = startingZ + newV.zCoord;
        }

        return new Vec3(x,y,z);

    }

    private int getNumberOfTicksPerCycle() {
        return 200;
    }


}