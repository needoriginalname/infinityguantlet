package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.util.FakePlayer;

/**
 * Created by Al on 5/16/2015.
 */
public class StateRealityGem extends AbstractGemState{




    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {

        EntityLivingBase entity = this.GetTargetEntityLiving(worldIn, playerIn, ConfigurationHandler.seekRangeForEntities);
        if (entity instanceof EntityPlayer){
            EntityPlayer otherPlayer = (EntityPlayer) entity;
            if (otherPlayer.capabilities.isCreativeMode){
                otherPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
            }
        }

        super.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);
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
