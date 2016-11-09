package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Al on 5/16/2015.
 */
public class StateTimeGem extends AbstractGemState{

    @Override
    public int getActualTimeLife(ItemStack stack) {
        return ConfigurationHandler.timeGauntletChargeTime;
    }

    @Override
    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isTimeGemGauntletEnabled;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        EntityLivingBase entity = this.GetTargetEntityLiving(worldIn, playerIn, ConfigurationHandler.seekRangeForBlocks);
        if (entity != null && worldIn.getSpawnPoint() != null){
            BlockPos pos = worldIn.getSpawnPoint();

            do {
                pos = pos.up();
            } while (!worldIn.isAirBlock(pos));
            entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        }

    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean b = false;
        if (ConfigurationHandler.isTimeGemEnabled) {
           b = this.applyBonemeal(stack, worldIn, pos, playerIn);


            if (b && worldIn.isRemote) {
                this.spawnBonemealParticles(worldIn, pos, 0);
            }

        }
        return b;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (ConfigurationHandler.isTimeGemEnabled) {
            if (playerIn.isSneaking()) {
                playerIn.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 30 * 300, 10));
                playerIn.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 30 * 300, 10));
            } else {

                EntityLivingBase e = GetTargetEntityLiving(worldIn, playerIn, this.entitySeekRange);

                if (ConfigurationHandler.allowGauntletToChangeEntityAgeable && e != null && e instanceof EntityAgeable) {
                    if (!e.isChild()) {
                        ((EntityAgeable) e).setGrowingAge(-24000);

                    } else {
                        ((EntityAgeable) e).setGrowingAge(24000);
                    }
                } else if (ConfigurationHandler.allowGauntletToChangeEntityAgeable && e instanceof EntityHorse && !((EntityHorse) e).isUndead()) {
                    if (!e.isChild()) {
                        ((EntityHorse) e).setGrowingAge(-24000);

                    } else {
                        ((EntityHorse) e).setGrowingAge(24000);
                    }
                }else if (e != null && e instanceof  EntityLiving) {
                    e.addPotionEffect(new PotionEffect(Potion.digSlowdown.getId(), 30*300, 10));
                    e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 30*30, 10));
                } else {
                    MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
                    boolean b = false;
                    if (mop != null)
                        b = this.applyBonemeal(itemStackIn, worldIn, mop.getBlockPos(), playerIn);


                    if (b && worldIn.isRemote) {
                        this.spawnBonemealParticles(worldIn, mop.getBlockPos(), 0);
                    }
                }


            }

        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player) {
        IBlockState iblockstate = worldIn.getBlockState(target);

        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack);
        if (hook != 0) return hook > 0;

        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }


                }

                return true;
            }
        }
        return false;
    }

    private static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount)
    {
        Random itemRand = new Random();
        if (amount == 0)
        {
            amount = 15;
        }

        Block block = worldIn.getBlockState(pos).getBlock();

        if (block.isAir(worldIn, pos))
        {
            block.setBlockBoundsBasedOnState(worldIn, pos);

            for (int j = 0; j < amount; ++j)
            {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double)((float)pos.getX() + itemRand.nextFloat()), (double)pos.getY() + (double)itemRand.nextFloat() * block.getBlockBoundsMaxY(), (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
            }
        }
        else
        {
            for (int i1 = 0; i1 < amount; ++i1)
            {
                double d0 = itemRand.nextGaussian() * 0.02D;
                double d1 = itemRand.nextGaussian() * 0.02D;
                double d2 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double)((float)pos.getX() + itemRand.nextFloat()), (double)pos.getY() + (double)itemRand.nextFloat() * 1.0f, (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
            }
        }
    }
}
