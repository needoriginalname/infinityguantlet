package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Al on 5/16/2015.
 */
public class AbstractGemState {
    protected final int entitySeekRange = ConfigurationHandler.seekRangeForEntities;

    public static IDs.Gems getEnumGem(ItemStack stack){
        int meta = stack.getMetadata();
        return getEnumGem(meta);
    }

    protected void CreateParticlePackets(BlockPos pos, EntityPlayerMP entityplayermp, int[] particlePra) {
        for (int i = 0; i < 5; ++i) {
            Random rand = new Random();
            entityplayermp.playerNetServerHandler.sendPacket(new S2APacketParticles(EnumParticleTypes.PORTAL, false, pos.getX(), (float) pos.getY() + rand.nextFloat() * 2.0f, (float) pos.getZ(), (float) rand.nextGaussian(), 0.0f, (float) rand.nextGaussian(), 1.0f, 15, particlePra));
        }
    }

    public static IDs.Gems getEnumGem(int meta){
        meta = MathHelper.clamp_int(meta,0, IDs.Gems.values().length);
        return IDs.Gems.values()[meta];
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn){
        return itemStackIn;
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
        return false;
    }

    public boolean hasEffect(ItemStack item){
        return false;
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float f = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch);
        float f1 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw);
        double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX);
        double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) + (double)playerIn.getEyeHeight();
        double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ);
        Vec3 vec3 = new Vec3(d0, d1, d2);

        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = ConfigurationHandler.seekRangeForBlocks;
        Vec3 vec31 = vec3.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
    }

    protected EntityLivingBase GetTargetEntityLiving(World world, EntityPlayer player, int scanRadius)
    {

        double targetDistance = Math.pow(scanRadius,2);

        EntityLivingBase target = null;
        //int tryCount = 0;
        List lst = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(player.posX - scanRadius, player.posY - scanRadius, player.posZ - scanRadius, player.posX + scanRadius, player.posY + scanRadius, player.posZ + scanRadius));
        for (int i = 0; i < lst.size(); i ++)
        {
            Entity ent = (Entity) lst.get(i);
            if (player.isRiding() && player.ridingEntity != null){
                if (player.ridingEntity.getEntityId() == ent.getEntityId()) continue;
            }

            if ( ent!=null && ent.getEntityBoundingBox() != null && ent instanceof EntityLivingBase )
            {
                //tryCount++;
                float distance = player.getDistanceToEntity(ent) + 0.1f;
                float angle = player.rotationYawHead;
                float pitch = player.rotationPitch;

                Vec3 look = player.getLookVec();
                Vec3 targetVec = new Vec3(player.posX + look.xCoord * distance, player.getEyeHeight() + player.posY + look.yCoord * distance, player.posZ + look.zCoord * distance);

                //world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, targetVec.xCoord, targetVec.yCoord, targetVec.zCoord, 0, 0.5, 0);

				//System.out.println("Bounds: min: x=" + ent.getEntityBoundingBox().minX + " y=" + ent.getEntityBoundingBox().minY + " z=" + ent.getEntityBoundingBox().minZ);
				//System.out.println("Bounds: max: x=" + ent.getEntityBoundingBox().maxX + " y=" + ent.getEntityBoundingBox().maxY + " z=" + ent.getEntityBoundingBox().maxZ);
				//System.out.println("Target: x=" + targetVec.xCoord + " y=" + targetVec.yCoord + " z=" + targetVec.zCoord);
                double expandedX = 1.0 * Math.abs(ent.getEntityBoundingBox().maxX - ent.getEntityBoundingBox().minX);
                double expandedY = 1.0 * Math.abs(ent.getEntityBoundingBox().maxY - ent.getEntityBoundingBox().minY);
                double expandedZ = 1.0 * Math.abs(ent.getEntityBoundingBox().maxZ - ent.getEntityBoundingBox().minZ);

                AxisAlignedBB expandedBB = ent.getEntityBoundingBox().expand(expandedX, expandedY, expandedZ);


                if (expandedBB.isVecInside(targetVec))
                {

                    if (distance < targetDistance && distance > 0)
                    {
                        targetDistance = distance;
                        target = (EntityLivingBase) ent;
                    }
                }


            }
        }
        return target;
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        return false;

    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 720000;
    }

    public int getActualTimeLife(ItemStack stack){
        return 30;
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {

    }


    public boolean isGauntletTypeEnabled(){
        return false;
    }
}
