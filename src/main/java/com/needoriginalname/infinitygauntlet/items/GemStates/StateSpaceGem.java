package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.InfinityQuantletMod;
import com.needoriginalname.infinitygauntlet.dimension.NonPortalTeleporter;
import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.util.NBTHelper;
import com.needoriginalname.infinitygauntlet.util.nodes.TransferPlayerNode;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created by Al on 5/16/2015.
 */


public class StateSpaceGem extends AbstractGemState{


    @Override
    public int getActualTimeLife(ItemStack stack) {
        return ConfigurationHandler.spaceGauntletChargeTime;
    }

    @Override
    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isSpaceGemGauntletEnabled;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {

        warpPlayer(stack, worldIn, playerIn);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (ConfigurationHandler.isSpaceGemEnabled) {
            if (!playerIn.isSneaking()) {
                teleportPlayer(worldIn, playerIn);
            } else {

                openEnderChest(playerIn);
                //warpPlayer(itemStackIn, worldIn, playerIn);
            }

        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    private void openEnderChest(EntityPlayer playerIn) {

        InventoryEnderChest inventoryenderchest = playerIn.getInventoryEnderChest();
        playerIn.displayGUIChest(inventoryenderchest);

    }

    private void warpPlayer(ItemStack itemstack, World worldIn, EntityPlayer playerIn) {
        int id = playerIn.dimension;

        if (playerIn.isRiding()) {
            playerIn.ridingEntity.riddenByEntity = null;
            playerIn.riddenByEntity =null;
        }

        if (playerIn instanceof EntityPlayerMP && worldIn instanceof WorldServer) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;
            WorldServer worldServer = (WorldServer) worldIn;

            int i = 0;
            int[] DimIDs = ConfigurationHandler.supportedDimensionIDs;

            for (i = 0; i < DimIDs.length; i++){
                if (DimIDs[i] == id)break;
            }

            int resultId =  (i >= DimIDs.length - 1) ? DimIDs[0] : DimIDs[i+1];

            entityPlayerMP.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("mob.endermen.portal", playerIn.posX, playerIn.posY, playerIn.posZ, 1.0f, 1.0f));

            this.CreateParticlePackets(new BlockPos(entityPlayerMP),entityPlayerMP, new int[0]);



            InfinityQuantletMod.proxy.addDeferredAction(new TransferPlayerNode(entityPlayerMP, entityPlayerMP.worldObj, resultId, entityPlayerMP.getPosition(), true));
            //InfinityQuantletMod.proxy.addDeferredDimTransfer(entityPlayerMP, resultId);
                /*
                WorldServer nextDimServer = entityPlayerMP.mcServer.worldServerForDimension(resultId);
                SpaceGemTeleporter teleporter = new SpaceGemTeleporter(nextDimServer);
                teleporter.teleport(entityPlayerMP); */


            entityPlayerMP.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("mob.endermen.portal", playerIn.posX , playerIn.posY, playerIn.posZ, 1.0f, 1.0f));
            this.CreateParticlePackets(new BlockPos(entityPlayerMP), entityPlayerMP, new int[0]);



        }



    }

    private void teleportPlayer(World worldIn, EntityPlayer playerIn) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);

        if (mop != null) {
            int posX = mop.getBlockPos().getX();
            int posY = mop.sideHit == EnumFacing.UP ? mop.getBlockPos().up().up().getY() : mop.getBlockPos().getY();
            int posZ = mop.getBlockPos().getZ();
            IBlockState state = worldIn.getBlockState(mop.getBlockPos());

            Block b = state.getBlock();
            int meta = state.getBlock().getMetaFromState(state);

            if (playerIn instanceof EntityPlayerMP && !worldIn.isRemote) {
                EntityPlayerMP entityplayermp = (EntityPlayerMP) playerIn;

                if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == worldIn && !entityplayermp.isPlayerSleeping()) {
                    net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(entityplayermp, posX, posY, posZ, 5.0F);

                    //acts like a enderpearl event, can be stopped by stuff that prevents ender pearl teleports
                    if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {

                        //entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("mob.endermen.portal", entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, 1.0f, 1.0f));
                        entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("mob.endermen.portal", playerIn.posX , playerIn.posY, playerIn.posZ, 1.0f, 1.0f));
                        entityplayermp.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("mob.endermen.portal", event.targetX , event.targetY, event.targetZ, 1.0f, 1.0f));

                        this.CreateParticlePackets(new BlockPos(entityplayermp),entityplayermp, new int[0]);
                        this.CreateParticlePackets(new BlockPos(event.targetX, event.targetY, event.targetZ), entityplayermp, new int[0]);
                        if (playerIn.isRiding()) {
                            playerIn.mountEntity(null);
                        }

                        playerIn.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);

                    }
                }
            }

        }
    }
}
