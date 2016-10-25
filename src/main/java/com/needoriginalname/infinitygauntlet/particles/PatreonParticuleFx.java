package com.needoriginalname.infinitygauntlet.particles;


import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Created by Owner on 10/21/2016.
 */
public class PatreonParticuleFx extends EntityFX {

    private final Vec3 baseV;
    private final double startingX;
    private final double startingY;
    private final double startingZ;

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

        super.renderParticle(worldRendererIn, entityIn, partialTicks, x,y,z,yaw,pitch);
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