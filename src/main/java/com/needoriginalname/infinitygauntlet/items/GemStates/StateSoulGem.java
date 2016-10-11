package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.dimension.SoulGemCaptureTeleporter;
import com.needoriginalname.infinitygauntlet.dimension.SoulGemReleaseTeleporter;
import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.network.MessageCustomSoundPacket;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.*;

/**
 * Created by Al on 5/18/2015.
 */
public class StateSoulGem extends AbstractGemState{

    private static final String CAPTUREDPLAYERS = "capturedPlayers";
    private static int MAXLENGTH = ConfigurationHandler.maxNumberOfEntitiesInSoulGem;
    private static final String CAPTUREDMOBS = "capturedMobs";

    @Override
    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isSoulGemGauntletEnabled;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        EntityLivingBase e = this.GetTargetEntityLiving(worldIn, playerIn, this.entitySeekRange);
        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
        BlockPos pos = mop != null ? mop.getBlockPos() : null;
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        if (worldIn instanceof WorldServer && playerIn instanceof EntityPlayerMP) {

            if (playerIn.isSneaking())

            {

                stack = releasePlayer(pos, (WorldServer)worldIn, stack, playerIn);

            } else {

                if (  e instanceof EntityPlayerMP) {
                    stack = capturePlayer((EntityPlayerMP)playerIn, (EntityPlayerMP) e, (WorldServer) worldIn, stack);
                }
            }

        }





    }

    private ItemStack releasePlayer(BlockPos pos, WorldServer worldIn, ItemStack stack, EntityPlayer player ) {
        NBTTagCompound tag = stack.getTagCompound();
        if (pos == null || tag.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND).tagCount() <= 0) return stack;
        NBTTagCompound playerData = readPlayerDataFromNBT(tag, worldIn);
        String playerName = playerData.getString("playername");

        EntityPlayer capturedPlayer = null;
        for (Object o: worldIn.getMinecraftServer().getConfigurationManager().playerEntityList){
            if (o instanceof EntityPlayer){
                if (((EntityPlayer) o).getName().equals(playerName)){
                    capturedPlayer = (EntityPlayer) o;
                    break;
                }
            }
        }

        if (capturedPlayer != null && capturedPlayer instanceof EntityPlayerMP) {
            worldIn.getMinecraftServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) capturedPlayer, player.dimension, new SoulGemReleaseTeleporter(worldIn, pos.getX(), pos.getY(), pos.getZ()));
            stack.setTagCompound(removePlayerDataFromNBT(stack.getTagCompound()));

            EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
            if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == worldIn) {

                String soundfile = "mob.endermen.portal";
                PacketHandler.dispatcher.sendTo(new MessageCustomSoundPacket(soundfile, (int) player.posX, (int) player.posY, (int) player.posZ), (EntityPlayerMP) player);
                PacketHandler.dispatcher.sendToAllAround(new MessageCustomSoundPacket(soundfile, pos.getX(), pos.getY(), pos.getZ()), new NetworkRegistry.TargetPoint(((EntityPlayerMP) player).dimension, pos.getX(), pos.getY(), pos.getZ(), 16));
                CreateParticlePackets(pos, entityplayermp, new int[0]);

            }

        } else if (capturedPlayer == null){
            try {
                ChangeOfflinePlayerLocation(playerData, worldIn, player, pos);
                player.addChatMessage(new ChatComponentTranslation("message.releasedofflineplayer", new Object[0]));
            } catch (Exception e){
                LogHelper.fatal("Error, unable to modify player save file of UUID" + playerData.getString("UUID"));
            }
        }


        tag = this.removePlayerDataFromNBT(tag);
        LogHelper.debug("Releasing Player: " + playerName + ".");
        stack.setTagCompound(tag);
        return  stack;


    }

    private void ChangeOfflinePlayerLocation(NBTTagCompound tag, WorldServer worldIn, EntityPlayer player, BlockPos pos) throws IOException {
        String UUID = tag.getString("UUID");

        File playersDirectory = new File(worldIn.getSaveHandler().getWorldDirectory(), "playerdata");
        File playerFile = new File(playersDirectory, UUID + ".dat");
        if (playerFile.exists() && playerFile.isFile())
        {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(playerFile));
            nbttagcompound = this.AdjustPositionInNBT(nbttagcompound, pos);
            nbttagcompound.setInteger("Dimension", player.dimension);

            File file1 = new File(playersDirectory, UUID + ".dat.tmp");
            File file2 = new File(playersDirectory, UUID + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file1.renameTo(file2);


        }
    }


    private NBTTagCompound removePlayerDataFromNBT(NBTTagCompound tagCompound) {
        NBTTagList capturedPlayers = tagCompound.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND);
        capturedPlayers.removeTag(capturedPlayers.tagCount() - 1);
        tagCompound.setTag(CAPTUREDPLAYERS, capturedPlayers);
        return tagCompound;
    }

    private ItemStack capturePlayer(EntityPlayerMP player, EntityPlayerMP target, WorldServer world, ItemStack stack) {


        DropAllItems(target);


        NBTTagCompound tag = stack.getTagCompound();
        if (tag.getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND).tagCount()  + (tag.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND).tagCount())>= MAXLENGTH){
            return stack;
        }

        stack.setTagCompound(this.writePlayerDataIntoNBT(stack.getTagCompound(), world, target));
        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);

        if (player.playerNetServerHandler.getNetworkManager().isChannelOpen() && player.worldObj == world) {
            String soundfile = "mob.endermen.portal";
            PacketHandler.dispatcher.sendTo(new MessageCustomSoundPacket(soundfile, (int) player.posX, (int) player.posY, (int) player.posZ), (EntityPlayerMP) player);
            PacketHandler.dispatcher.sendToAllAround(new MessageCustomSoundPacket(soundfile, pos.getX(), pos.getY(), pos.getZ()), new NetworkRegistry.TargetPoint(((EntityPlayerMP) player).dimension, pos.getX(), pos.getY(), pos.getZ(), 16));
            CreateParticlePackets(pos, player, new int[0]);
        }


       target.mcServer.getConfigurationManager().transferPlayerToDimension(target, ConfigurationHandler.soulGemDimensionID, new SoulGemCaptureTeleporter(world, (int)target.posX, (int)target.posY, (int)target.posZ));

        return stack;
    }

    private void DropAllItems(EntityPlayerMP target) {
        target.captureDrops = true;
        target.capturedDrops.clear();

        target.inventory.dropAllItems();

        target.captureDrops = false;

        for (net.minecraft.entity.item.EntityItem item : target.capturedDrops)
        {
            target.worldObj.spawnEntityInWorld(item);
        }

    }

    private NBTTagCompound readPlayerDataFromNBT(NBTTagCompound tagCompound, WorldServer world){
        NBTTagList players = new NBTTagList();
        players = tagCompound.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND);
        NBTTagCompound playerData = new NBTTagCompound();
        playerData = players.getCompoundTagAt(players.tagCount()-1);
        return playerData;
    }

    private NBTTagCompound writePlayerDataIntoNBT(NBTTagCompound tagCompound, WorldServer world, EntityPlayer target) {
        NBTTagList players = new NBTTagList();
        players = tagCompound.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND);
        NBTTagCompound playerData = new NBTTagCompound();
        String playerName = target.getName();
        playerData.setString("playername", playerName);
        playerData.setString("UUID", target.getUniqueID().toString());
        players.appendTag(playerData);
        tagCompound.setTag(CAPTUREDPLAYERS, players);
        return tagCompound;
    }

    @Override
    public boolean hasEffect(ItemStack item) {
        return (item.hasTagCompound() && (item.getTagCompound().getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND).tagCount() > 0 || item.getTagCompound().getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND).tagCount() > 0));
    }



    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (ConfigurationHandler.isSoulGemEnabled && (itemStackIn != null && !worldIn.isRemote && playerIn != null)) {
            EntityLivingBase e = this.GetTargetEntityLiving(worldIn, playerIn, this.entitySeekRange);
            MovingObjectPosition mop = getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
            BlockPos pos = mop != null ? mop.getBlockPos() : null;
            if (!itemStackIn.hasTagCompound()) itemStackIn.setTagCompound(new NBTTagCompound());


            if (playerIn.isSneaking()) {
                itemStackIn = ReleaseMob(itemStackIn, worldIn, pos, playerIn);
            } else {
                itemStackIn = CaptureMob(itemStackIn, worldIn, playerIn, (EntityLiving)e);
            }

        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    private ItemStack ReleaseMob(ItemStack itemStackIn, World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (canReleaseMob(worldIn, pos)){
            NBTTagCompound mobNBT = this.getMobFromNBT(itemStackIn.getTagCompound());
            mobNBT = this.AdjustPositionInNBT(mobNBT, pos);
            Entity entity = EntityList.createEntityFromNBT(mobNBT, worldIn);

            if (worldIn.spawnEntityInWorld(entity)){
                itemStackIn.setTagCompound(removeLastMobFromNBT(itemStackIn.getTagCompound()));

                if (playerIn instanceof EntityPlayerMP && !worldIn.isRemote) {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP) playerIn;

                    if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == worldIn) {

                        String soundfile = "mob.endermen.portal";
                        PacketHandler.dispatcher.sendTo(new MessageCustomSoundPacket(soundfile, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ), (EntityPlayerMP) playerIn);
                        PacketHandler.dispatcher.sendToAllAround(new MessageCustomSoundPacket(soundfile, pos.getX(), pos.getY(), pos.getZ()), new NetworkRegistry.TargetPoint(((EntityPlayerMP) playerIn).dimension, pos.getX(), pos.getY(), pos.getZ(), 16));
                        CreateParticlePackets(pos, entityplayermp, new int[0]);

                    }
                }


            }
        }

        return itemStackIn;
    }



    private NBTTagCompound removeLastMobFromNBT(NBTTagCompound tagCompound) {
        NBTTagList mobs = tagCompound.getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND);
        mobs.removeTag(mobs.tagCount() - 1);
        tagCompound.setTag(CAPTUREDMOBS, mobs);
        return tagCompound;

    }

    private NBTTagCompound AdjustPositionInNBT(NBTTagCompound mobNBT, BlockPos pos) {
        NBTTagList list = new NBTTagList();
        NBTTagDouble x = new NBTTagDouble(pos.getX() + 0.5f);
        NBTTagDouble y = new NBTTagDouble(pos.getY() + 1.0f);
        NBTTagDouble z = new NBTTagDouble(pos.getZ() + 0.5f);
        list.appendTag(x); list.appendTag(y); list.appendTag(z);
        mobNBT.setTag("Pos", list);
        return mobNBT;

    }

    private NBTTagCompound getMobFromNBT(NBTTagCompound tagCompound) {
        NBTTagList mobs = tagCompound.getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND);
        NBTTagCompound mob = mobs.getCompoundTagAt(mobs.tagCount() - 1);
        return mob;

    }

    private boolean canReleaseMob(World worldIn, BlockPos pos) {
        if (pos != null && worldIn.isBlockLoaded(pos)){
            return true;
        }

        return false;
    }

    private ItemStack  CaptureMob(ItemStack itemStack, World world, EntityPlayer player, EntityLivingBase entity) {
        if (entity != null && itemStack != null){
            if (canCaptureMob(itemStack.getTagCompound(), entity, player)) {
                BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
                itemStack.setTagCompound(this.writeMobIntoNBT(itemStack.getTagCompound(), world, entity));
                world.removeEntity(entity);


                if (player instanceof EntityPlayerMP && !world.isRemote) {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP) player;

                    if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == world) {

                        String soundfile = "mob.endermen.portal";
                        PacketHandler.dispatcher.sendTo(new MessageCustomSoundPacket(soundfile, (int) player.posX, (int) player.posY, (int) player.posZ), (EntityPlayerMP) player);
                        PacketHandler.dispatcher.sendToAllAround(new MessageCustomSoundPacket(soundfile, pos.getX(), pos.getY(), pos.getZ()), new NetworkRegistry.TargetPoint(((EntityPlayerMP) player).dimension, pos.getX(), pos.getY(), pos.getZ(), 16));
                        CreateParticlePackets(pos, entityplayermp, new int[0]);

                    }
                }




                        }
        }
        return itemStack;

    }

    private NBTTagCompound writeMobIntoNBT(NBTTagCompound tagCompound, World world, EntityLivingBase entity) {
        NBTTagList mobs = new NBTTagList();
        mobs = tagCompound.getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND);
        NBTTagCompound mob = new NBTTagCompound();

        entity.writeToNBTOptional(mob);
        String s = EntityList.getEntityString(entity);

        mob.setString("id", s);


        mobs.appendTag(mob);
        tagCompound.setTag(CAPTUREDMOBS, mobs);
        return tagCompound;
    }

    private boolean canCaptureMob(NBTTagCompound tag, EntityLivingBase entity, EntityPlayer player) {

        if (tag.getTagList(CAPTUREDMOBS, Constants.NBT.TAG_COMPOUND).tagCount()  + (tag.getTagList(CAPTUREDPLAYERS, Constants.NBT.TAG_COMPOUND).tagCount())>= MAXLENGTH){
            return false;
        }

        if (entity instanceof  EntityPlayer || entity instanceof IEntityMultiPart || entity.riddenByEntity != null || entity.isDead || entity.isRiding()){
            return false;
        }

        if (entity instanceof EntityTameable){
            if  (((EntityTameable) entity).getOwner() == null || ((EntityTameable) entity).getOwner() == player ){
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

}
