package com.needoriginalname.infinitygauntlet.dimension;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created by Al on 5/26/2015.
 *
 * Credit for this code goes to Mithion as this was largely ripped from AMTeleported, then converted to use BlockPos and BlocksStates by me.
 */
public class SpaceGemTeleporter extends Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    public SpaceGemTeleporter(WorldServer worldServer){
        super(worldServer);
        this.worldServerInstance = worldServer;
        this.random = worldServer.rand;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
            }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long worldTime) {
    }

    public void teleport(EntityLivingBase e){
        if (e instanceof EntityPlayerMP && e.worldObj.provider.getDimensionId() != worldServerInstance.provider.getDimensionId()){

            BlockPos pos = clearTeleportPath(worldServerInstance, e);

            //stop falling and motion
            e.motionX = e.motionY = e.motionZ = 0.0D;
            e.fallDistance = 0;
            e.setPosition(pos.getX(), pos.getY(), pos.getZ());

            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) e, worldServerInstance.provider.getDimensionId(), this);


        }
    }

    private BlockPos clearTeleportPath(World world, EntityLivingBase entity) {
        BlockPos vec = new BlockPos(entity);
        double newX = (vec.getX() /world.provider.getMovementFactor());
        double newY = vec.getY() / world.provider.getMovementFactor();
        double newZ = vec.getZ() / world.provider.getMovementFactor();
        if (entity.dimension != -1){
            boolean canFindHigherGround = false;
            newY = (float)entity.posY;
            if (newY < 5 || newY >= world.getActualHeight() - 10)
                newY = 5;

            while (true){
                if (world.getBlockState(new BlockPos(newX, newY, newZ)).getBlock() == Blocks.air || newY >= world.getActualHeight()){
                    canFindHigherGround = true;
                    break;
                }
                newY++;
            }

            if (canFindHigherGround){
                while (world.getBlockState(new BlockPos(newX, newY-1, newZ)).getBlock() == Blocks.air && newY > 0){
                    newY--;
                }
            }else{
                if (newY < 5)
                    newY = 5;
                if (newY > world.getActualHeight() - 10)
                    newY = world.getActualHeight() - 10;

                for (int q = (int)Math.floor(newY) - 2; q < newY + 1; ++q){
                    for (int i = (int)Math.floor(newX) - 1; i < newX + 1; ++i){
                        for (int k = (int)Math.floor(newZ) - 1; k < newZ + 1; ++k){
                            if (q == (int)Math.floor(newY - 2)){
                                world.setBlockState(new BlockPos(i, q, k), Blocks.air.getDefaultState());
                            }
                        }
                    }
                }
            }
        }else{
            boolean canFindHigherGround = false;
            while (true){
                if (world.getBlockState(new BlockPos(newX, newY-1, newZ)).getBlock() == Blocks.air || newY >= 256){
                    canFindHigherGround = true;
                    break;
                }
                newY++;
            }

            if (!canFindHigherGround){
                for (int q = (int)Math.floor(newY) - 2; q < newY + 1; ++q){
                    for (int i = (int)Math.floor(newX) - 1; i < newX + 1; ++i){
                        for (int k = (int)Math.floor(newZ) - 1; k < newZ + 1; ++k){
                            if (q == (int)Math.floor(newY - 2)){
                                world.setBlockState(new BlockPos(i, q, k), Blocks.netherrack.getDefaultState());
                            }else{
                                world.setBlockState(new BlockPos(i, q, k), Blocks.air.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
        return vec;
    }


}
