package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.items.GemStates.AbstractGemState;
import com.needoriginalname.infinitygauntlet.items.GemStates.GemFactory;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Al on 5/16/2015.
 */
public class ItemGem extends ItemIG {
    ItemGem(){
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        //this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setUnlocalizedName(Names.InfinityGem);

    }


    @Override
    public boolean hasContainerItem(ItemStack stack) {
        if (stack!= null && stack.getItem() == this && stack.getMetadata() == IDs.Gems.PowerGem.getID())
            return true;
        else {
            return super.hasContainerItem(stack);
        }
    }


    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if (this.hasContainerItem(itemStack)){
            ItemStack stack = itemStack.copy();
            stack.stackSize = 1;
            return stack;
        } else {

            return super.getContainerItem(itemStack);
        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }



    @Override
    public boolean hasEffect(ItemStack stack) {


        AbstractGemState state = this.getGemState(stack);

        if (state == null){
            return super.hasEffect(stack);
        } else {
            return state.hasEffect(stack);
        }

    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int meta = MathHelper.clamp_int(itemStack.getMetadata(),0, IDs.Gems.values().length-1);

        return super.getUnlocalizedName(itemStack) + '.' + IDs.Gems.values()[meta].getUnlocalizedName() ;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }


        return this.getGemState(stack).onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (!itemStackIn.hasTagCompound()){
            itemStackIn.setTagCompound(new NBTTagCompound());
        }

        if (/*!worldIn.isRemote &&*/ itemStackIn != null && worldIn != null && playerIn != null) {

            itemStackIn = this.getGemState(itemStackIn).onItemRightClick(itemStackIn, worldIn, playerIn);


        }
        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }

    public AbstractGemState getGemState(ItemStack itemStack) {
        return this.getGemState(itemStack.getMetadata());
    }

    private AbstractGemState getGemState(int meta){
        return GemFactory.getGemState(meta);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {

        if (!stack.hasTagCompound()){
            stack.setTagCompound( new NBTTagCompound());
        }

        return this.getGemState(stack).itemInteractionForEntity(stack, playerIn, target);
    }

    @Override
    public String getTextureName(int meta){
        String base =super.getTextureName(meta);
        String trail = AbstractGemState.getEnumGem(meta).getUnlocalizedName();
        trail = trail.substring(0,1).toUpperCase() + trail.substring(1);
        return base+trail;}
}
