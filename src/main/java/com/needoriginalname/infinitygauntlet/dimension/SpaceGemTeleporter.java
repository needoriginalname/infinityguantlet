package com.needoriginalname.infinitygauntlet.dimension;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;

/**
 * Created by Al on 5/26/2015.
 *
 * Credit for this code goes to Mithion as this was largely ripped from AMTeleported, then converted to use BlockPos and BlocksStates by me.
 */
public class SpaceGemTeleporter extends Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private boolean clearPath;
    private BlockPos newPos;

    public SpaceGemTeleporter(WorldServer worldServer){
        super(worldServer);
        this.worldServerInstance = worldServer;
        this.random = worldServer.rand;
        this.clearPath = true;
        this.newPos = null;
    }

    public SpaceGemTeleporter(WorldServer worldServer, BlockPos newPos, boolean clearPath){
        super(worldServer);
        this.worldServerInstance = worldServer;
        this.random = worldServer.rand;
        this.clearPath = clearPath;
        this.newPos = newPos;
    }



    @Override
    public boolean makePortal(Entity entity) {
        if (clearPath){
            BlockPos playerSpawnAt = newPos;
            if (playerSpawnAt.getY() < 5){
                playerSpawnAt = new BlockPos(playerSpawnAt.getX(), 5, playerSpawnAt.getZ());
            }


            BlockPos topRightBlock = playerSpawnAt.add(1,1,1);

            for (int x = 0; x > -2; --x){
                for(int y = 0; y >= -2; --y){
                    for (int z = 0; z > -2; --z){
                        BlockPos currentBlock = topRightBlock.add(x,y,z);
                        if (y != -2){
                            worldServerInstance.setBlockToAir(currentBlock);
                            worldServerInstance.notifyNeighborsRespectDebug(currentBlock, Blocks.air);
                        } else {
                            if (worldServerInstance.isAirBlock(currentBlock)
                                    || worldServerInstance.getBlockState(currentBlock).getBlock() instanceof BlockLiquid) {
                                worldServerInstance.setBlockState(currentBlock, Blocks.cobblestone.getDefaultState());
                                worldServerInstance.notifyNeighborsRespectDebug(currentBlock, Blocks.cobblestone);
                            }
                        }
                    }
                }
            }

        }

        return true;
    }

    @Override
    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
        if (newPos == null){
            newPos = entityIn.getPosition();
        }


        newPos = new BlockPos(MathHelper.clamp_int(newPos.getX(), -29999850, 29999850),
                MathHelper.clamp_int(newPos.getY(), 0, worldServerInstance.getHeight()),
                MathHelper.clamp_int(newPos.getZ(), -29999850, 29999850));


        if (!clearPath){
            entityIn.setLocationAndAngles(newPos.getX()+.5d, newPos.getY()+.5d, newPos.getZ()+.5d, entityIn.rotationYaw, entityIn.rotationPitch);
            return true;
        } else {
            BlockPos startingPlayerPos = newPos;
            BlockPos currentSearchPos = startingPlayerPos;

            // can only go as low as 5 height level
            if (currentSearchPos.getY() < 5){
                currentSearchPos = new BlockPos(currentSearchPos.getX(), 5, currentSearchPos.getZ());
            }

            boolean found = false;
            //check above player for an air block
            while (worldServerInstance.isAirBlock(currentSearchPos) && currentSearchPos.getY() < worldServerInstance.getActualHeight()-1){

                if(worldServerInstance.isAirBlock(currentSearchPos)
                        && worldServerInstance.isAirBlock(currentSearchPos.up())
                        && worldServerInstance.isSideSolid(currentSearchPos.down(), EnumFacing.UP)){
                    found = true;
                } else {
                    currentSearchPos = currentSearchPos.up();
                }
            }
            //check under player, for air block
            if (!found){
                for(int i = 5; !found && i < startingPlayerPos.getY(); i++){
                    if(worldServerInstance.isAirBlock(currentSearchPos)
                            && worldServerInstance.isAirBlock(currentSearchPos.up())
                            && worldServerInstance.isSideSolid(currentSearchPos.down(), EnumFacing.UP)){
                        found = true;
                    } else {
                        currentSearchPos = new BlockPos(currentSearchPos.getX(), i, currentSearchPos.getZ());
                    }
                }
            }

            if (found){
                entityIn.setLocationAndAngles(currentSearchPos.getX(), currentSearchPos.getY(), currentSearchPos.getZ(), entityIn.rotationYaw, entityIn.rotationPitch);
            }

            return found;




        }

    }

    @Override
    public void removeStalePortalLocations(long worldTime) {

    }




}
