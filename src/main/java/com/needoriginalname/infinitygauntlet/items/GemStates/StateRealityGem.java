package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.util.BlockSearchHandler;
import com.needoriginalname.infinitygauntlet.util.GraphNode;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.FakePlayer;

import java.util.PriorityQueue;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Al on 5/16/2015.
 */
public class StateRealityGem extends AbstractGemState{




    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {


        IBlockState blockstate = null;
        for (int i = 0; i < 9; ++i){
            if (playerIn.inventory.getStackInSlot(i) == stack){
                ItemStack stackInSlot = playerIn.inventory.getStackInSlot(++i);
                if (stackInSlot != null && stackInSlot.getItem() instanceof ItemBlock){
                    blockstate = ((ItemBlock) stackInSlot.getItem()).getBlock().getDefaultState();
                    break;
                } else {
                    blockstate = null;
                }
            }
        }
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

        if (mop != null && mop.getBlockPos() != null){
            PriorityQueue<GraphNode> queue = new BlockSearchHandler().getBlocks(worldIn, mop.getBlockPos(), blockstate, 50, 10000);
            proxy.addDeferredBlockReplacement(worldIn, queue);
        }
/*
        EntityLivingBase entity = this.GetTargetEntityLiving(worldIn, playerIn, ConfigurationHandler.seekRangeForEntities);
        if (entity instanceof EntityPlayer){
            EntityPlayer otherPlayer = (EntityPlayer) entity;
            if (otherPlayer.capabilities.isCreativeMode){
                otherPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
            }
        }

        super.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);
        */
    }

    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isRealityGauntletGemEnabled;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (ConfigurationHandler.isRealityGemEnabled) {
            if (playerIn.isSneaking()) {

                worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "random.levelup", 1, 1);
                if (playerIn.capabilities.isCreativeMode) {
                    playerIn.setGameType(WorldSettings.GameType.SURVIVAL);
                } else {
                    playerIn.setGameType(WorldSettings.GameType.CREATIVE);
                }
            }
        }

        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }
}
