package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.items.GemStates.AbstractGemState;
import com.needoriginalname.infinitygauntlet.network.MessageCustomSoundPacket;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import com.needoriginalname.infinitygauntlet.reference.Key;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.util.IKeyBound;
import com.needoriginalname.infinitygauntlet.util.MiscUtil;
import com.needoriginalname.infinitygauntlet.util.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import sun.security.provider.Sun;

import java.util.List;

/**
 * Created by Al on 5/18/2015.
 */
public class ItemGauntlet extends ItemIG implements IKeyBound {
    ItemGauntlet(){
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setUnlocalizedName(Names.InfinityGauntlet);
        this.hasSubtypes = false;

    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    /**
     * Check whether this Item can harvest the given Block
     *
     * @param blockIn
     */
    @Override
    public boolean canHarvestBlock(Block blockIn) {
        return true;
    }



    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        return ConfigurationHandler.infinityGauntletMineSpeed;
    }

    private int getGemId(ItemStack item){
        int id = NBTHelper.getInt(item, "currentMode");
        return id;
    }



    private IDs.Gems getCurrentGemMode(ItemStack item){
        int id = this.getGemId(item);
        IDs.Gems gem = null;
        if (MiscUtil.InRange(id, 0, IDs.Gems.values().length)){
            gem = IDs.Gems.values()[id];
        }
        return gem;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        AbstractGemState state = this.getCurrentGemMode(stack).getGemState();
        if (state == null){
            return super.hasEffect(stack);
        } else {
            return state.hasEffect(stack);
        }


    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        if (playerIn == null) return;
        if (stack == null) return;


        IDs.Gems gem = this.getCurrentGemMode(stack);
        String currentStateName = gem == null ? "none" : gem.getUnlocalizedName();
        tooltip.add("Current State: " + currentStateName);
    }


    /**
     * Called each tick while using an item.
     *
     * @param stack  The Item being used
     * @param playerIn The Player using the item
     * @param count  The amount of time in tick the item has been used for continuously
     */
    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer playerIn, int count) {
        if (playerIn instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) playerIn;
            AbstractGemState state = this.getCurrentGemMode(stack).getGemState();
            if (state == null) return;

            int actualTimeLeft = state.getActualTimeLife(stack) - (this.getMaxItemUseDuration(stack) - count);
            System.out.println(actualTimeLeft);

            if ((actualTimeLeft <= 0)){
                if (player.ticksExisted % 30 == 0) {
                    PacketHandler.dispatcher.sendTo(new MessageCustomSoundPacket("random.successful_hit", (int) player.posX, (int) player.posY, (int) player.posZ), player);
                }
            }

        }

        super.onUsingTick(stack, playerIn, count);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     *
     * @param stack
     * @param worldIn
     * @param entityIn
     * @param itemSlot
     * @param isSelected
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }




    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     *
     * @param stack
     * @param worldIn
     * @param playerIn
     * @param timeLeft The amount of ticks left before the using would have been complete
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        if (worldIn.isRemote) return;
        AbstractGemState state = this.getCurrentGemMode(stack).getGemState();
        if (state == null) return;

        int actualTimeLeft = state.getActualTimeLife(stack) - (this.getMaxItemUseDuration(stack) - timeLeft);

        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }

        if (actualTimeLeft <=0 && state.isGauntletTypeEnabled()) {
                state.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);

        } else {
                stack = state.onItemRightClick(stack, worldIn, playerIn);

        }
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));

        return itemStackIn;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 720000;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }

        AbstractGemState state = this.getCurrentGemMode(stack).getGemState();
        if (state == null){
            return super.itemInteractionForEntity(stack, playerIn, target);
        } else {
            return state.itemInteractionForEntity(stack, playerIn, target);
        }
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        if (stack!= null && stack.getItem() == this)
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
    public void doKeyAction(EntityPlayer player, ItemStack stack, Key.Keys key) {
        if (player != null && stack != null && stack.getItem() == this){
            if (key == Key.Keys.ChangeGauntletMode){
                int n = NBTHelper.getInt(stack, "currentMode");
                n = IDs.Gems.values().length - 1 <= n ? 0 : n + 1;
                NBTHelper.setInteger(stack, "currentMode", n);
                String modeName = this.getCurrentGemMode(stack).getUnlocalizedName();
                player.addChatMessage(new ChatComponentTranslation("message.currentmode." + modeName));
            }


        }
    }
}
