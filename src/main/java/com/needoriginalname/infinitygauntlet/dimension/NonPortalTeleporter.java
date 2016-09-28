package com.needoriginalname.infinitygauntlet.dimension;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Al on 5/26/2015.
 */
public class NonPortalTeleporter {

    //Much of this code game from Ars magica

    private static WorldServer worldServerInstance;
    private static MinecraftServer mcServer;
    private static Random random;


    private static BlockPos adjustPos (World world, World worldServerInst, BlockPos pos) {


        BlockPos spawnPoint = pos != null ? pos : world.getSpawnPoint();
        if (spawnPoint == null){
            spawnPoint = new BlockPos(0,2,0);
        }
        if (spawnPoint.getY() <= 1){
            spawnPoint = new BlockPos(spawnPoint.getX(), 2, spawnPoint.getZ());
        }

        int startingPoint = spawnPoint.getY();
        while(!worldServerInst.isSideSolid(spawnPoint.down(), EnumFacing.UP, false) || !worldServerInst.isAirBlock(spawnPoint) || !worldServerInst.isAirBlock(spawnPoint.up())){

            spawnPoint = spawnPoint.up();
            if (spawnPoint.getY() >= worldServerInst.getActualHeight()){
                spawnPoint =  spawnPoint.down(spawnPoint.getY());
            }
            if (spawnPoint.getY() == startingPoint) {
                break;
            }
        }

        double x = spawnPoint.getX() + 0.5d;
        double y = spawnPoint.getY() + 0.5d;
        double z = spawnPoint.getZ() + 0.5d;
        return new BlockPos(x,y,z);
    }

    public static void teleport(World worldFrom, Entity entity, World worldTo, BlockPos pos, float yaw) {
        if (worldFrom.isRemote) return;

        pos = adjustPos(worldTo, worldTo, pos);
        if (mcServer == null) mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (entity.riddenByEntity != null) entity.riddenByEntity = null;
        teleport(worldTo, entity, pos, yaw);
    }

    private static void setEntityVelocity(Entity entity, float yaw) {
        int direction = (int) (-yaw / 90f);
        double force = Math.max(Math.abs(entity.motionX), Math.abs(entity.motionZ));
        double mZ = 0;
        double mX = 0;

        switch (direction) {
            case 0:
                mZ = force;
                break;
            case 1:
                mX = force;
                break;
            case 2:
                mZ = -force;
                break;
            case 3:
                mX = -force;
                break;
        }
        entity.motionX = mX;
        entity.motionZ = mZ;
    }

    private static Entity teleport(World world, Entity entity, BlockPos pos, float yaw) {
        boolean differentWorld = entity.worldObj != world;
        Entity mount = entity.ridingEntity;
        if (entity.ridingEntity != null) {
            entity.mountEntity(null);
            mount = teleport(world, mount, pos, yaw);
        }

        setEntityVelocity(entity, yaw);
        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.closeScreen();
            if (differentWorld) {
                player.dimension = world.provider.getDimensionId();
                ((WorldServer) entity.worldObj).getPlayerManager().removePlayer(player);
            }
        }
        if (differentWorld) {
            if (entity instanceof EntityPlayer) {
                World w = entity.worldObj;
                EntityPlayer player = (EntityPlayer) entity;
                player.closeScreen();
                w.playerEntities.remove(player);
                w.updateAllPlayersSleepingFlag();
                int i = entity.chunkCoordX;
                int j = entity.chunkCoordZ;
                if ((entity.addedToChunk) && (w.getChunkProvider().chunkExists(i, j))) {
                    w.getChunkFromChunkCoords(i, j).removeEntity(entity);
                    w.getChunkFromChunkCoords(i, j).setModified(true);
                }
                w.loadedEntityList.remove(entity);
                w.onEntityRemoved(entity);
            }
            entity.isDead = false;
        }
        entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, yaw, entity.rotationPitch);
        ((WorldServer) world).theChunkProviderServer.loadChunk(((int) pos.getX()) >> 4, pos.getZ() >> 4);
        if (differentWorld) {
            if (!(entity instanceof EntityPlayer)) { // NOT PLAYER
                NBTTagCompound entityNBT = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(entityNBT);
                entity.isDead = true;
                entity = EntityList.createEntityFromNBT(entityNBT, world);
                if (entity == null) return null;
                entity.dimension = world.provider.getDimensionId();
            }
            world.spawnEntityInWorld(entity);
            entity.setWorld(world);
        }
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, entity.rotationPitch);
        world.updateEntityWithOptionalForce(entity, false);
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, entity.rotationPitch);
        if (entity instanceof EntityPlayerMP) { // PLAYER
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (differentWorld) {
                player.mcServer.getConfigurationManager().preparePlayer(player, (WorldServer) world);
            }
            player.playerNetServerHandler.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        }
        world.updateEntityWithOptionalForce(entity, false);
        if (entity instanceof EntityPlayerMP && differentWorld) { // PLAYER  CHANGED WORLD
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.theItemInWorldManager.setWorld((WorldServer) world);
            player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer) world);
            player.mcServer.getConfigurationManager().syncPlayerInventory(player);
            Iterator potions = player.getActivePotionEffects().iterator();
            while (potions.hasNext()) {
                PotionEffect effect = (PotionEffect) potions.next();
                player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), effect));
            }
            player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
        }
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, entity.rotationPitch);
        //##################################################################################


        // If we had a mount before, hop back on.
        if (mount != null) {
            if (entity instanceof EntityPlayerMP) {
                world.updateEntityWithOptionalForce(entity, true);
            }
            entity.mountEntity(mount);
        }
        return entity;
    }
}
    /*
    @Override
    public boolean placeInExistingPortal(Entity entityIn, float p_180620_2_) {
        return false;
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {

        BlockPos spawnPoint =worldServerInstance.getSpawnPoint();
        if (spawnPoint == null){
            spawnPoint = new BlockPos(0,2,0);
        }
        if (spawnPoint.getY() <= 1){
            spawnPoint = new BlockPos(spawnPoint.getX(), 2, spawnPoint.getZ());
        }

        int startingPoint = spawnPoint.getY();
        while(!worldServerInstance.isSideSolid(spawnPoint.down(), EnumFacing.UP, false) || !worldServerInstance.isAirBlock(spawnPoint) || !worldServerInstance.isAirBlock(spawnPoint.up())){

            spawnPoint = spawnPoint.up();
            if (spawnPoint.getY() >= worldServerInstance.getActualHeight()){
                spawnPoint =  spawnPoint.down(spawnPoint.getY());
            }
            if (spawnPoint.getY() == startingPoint) {
                break;
            }
            worldServerInstance.setBlockToAir(spawnPoint);
            worldServerInstance.setBlockToAir(spawnPoint.up());
            if (! worldServerInstance.isSideSolid(spawnPoint.down(),EnumFacing.UP) || !worldServerInstance.isSideSolid(spawnPoint.down(),EnumFacing.DOWN)){
                worldServerInstance.setBlockState(spawnPoint.down(), Block.getBlockFromName("minecraft:stone").getDefaultState());
            }
        }

        double x = spawnPoint.getX() + 0.5d;
        double y = spawnPoint.getY() + 0.5d;
        double z = spawnPoint.getZ() + 0.5d;
        //worldServerInstance.theChunkProviderServer.chunkLoader.loadChunk(entityIn.worldObj,x,z)
        entityIn.setPositionAndUpdate(x,y,z);

    }
    */

