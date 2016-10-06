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
            EntityPlayerMP player = (EntityPlayerMP) e;


            boolean comingToTheEnd = worldServerInstance.provider.getDimensionId() == 1;

            //a hotfix that prevents the player when traveling to the End on a never loaded chunk.
            if (comingToTheEnd) {
                player.travelToDimension(1);
                return;
            }

            BlockPos pos = clearTeleportPath(worldServerInstance, player);
            boolean comingFromEnd = player.dimension == 1;


            //stop falling and motion
            player.motionX = player.motionY = player.motionZ = 0.0D;
            player.fallDistance = 0;

            MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) e, worldServerInstance.provider.getDimensionId(), this);

            //taken from Random Things PlayerUtil, used to provide teleportion logic that is specfic when teleporting from the end.
            if (comingFromEnd){
                e.setLocationAndAngles(pos.getX()+.5d, pos.getY()+.5d, pos.getZ()+.5d, player.rotationYaw, player.rotationPitch);
                player.worldObj.spawnEntityInWorld(e);

                player.worldObj.updateEntityWithOptionalForce(e, false);
            } else {
                player.worldObj.provider.createChunkGenerator().provideChunk(pos);
                e.setLocationAndAngles(pos.getX()+.5d, pos.getY()+.5d, pos.getZ()+.5d, player.rotationYaw, player.rotationPitch);
            }
            player.playerNetServerHandler.setPlayerLocation(pos.getX()+.5d, pos.getY()+.5d, pos.getZ()+.5d, player.rotationYaw, player.rotationPitch);
            player.removeExperienceLevel(0);
            player.setPlayerHealthUpdated();
            
        }

    }

    private BlockPos clearTeleportPath(World world, EntityLivingBase entity) {
        BlockPos vec = new BlockPos(entity);
        double newX = (vec.getX() /world.provider.getMovementFactor());
        double newY;
        double newZ = vec.getZ() / world.provider.getMovementFactor();
            newY = (float)entity.posY;
            if (newY < 5 || newY >= world.getActualHeight() - 10)
                newY = 5;

        //find an air block to teleport toe
            while (true){
                if (newY > 5 && (world.getBlockState(new BlockPos(newX, newY, newZ)).getBlock() == Blocks.air || newY >= world.getActualHeight())){
                    break;
                }
                newY++;
            }

            while (world.getBlockState(new BlockPos(newX, newY-1, newZ)).getBlock() == Blocks.air && newY > 5){
                    newY--;
            }
            for (int q = (int)Math.floor(newY) - 2; q <= newY + 1; ++q){
                for (int i = (int)Math.floor(newX) - 1; i <= newX + 1; ++i){
                    for (int k = (int)Math.floor(newZ) - 1; k <= newZ + 1; ++k){
                        //if block is empty fill it
                        if (q == (int)Math.floor(newY - 2)){
                            boolean r = world.setBlockState(new BlockPos(i, q, k), Blocks.cobblestone.getDefaultState());
                            System.out.println(r);
                            //assert r;
                        }else{
                            boolean r2 = world.setBlockState(new BlockPos(i, q, k), Blocks.air.getDefaultState());
                            System.out.println(r2);

                            //assert r;
                        }
                    }
                }
            }


        return new BlockPos(newX, newY, newZ);
    }


}
