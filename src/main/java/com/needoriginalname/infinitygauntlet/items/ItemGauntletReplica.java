package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.reference.Names;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

import java.util.List;
import java.util.Set;

/**
 * Created by Owner on 10/20/2016.
 */
public class ItemGauntletReplica extends ItemIG {

    ItemGauntletReplica(){
        this.setMaxStackSize(1);
        //this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setUnlocalizedName(Names.InfinityGauntletReplica);
        this.hasSubtypes = false;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public String getTextureName(int meta) {
        return ((ItemIG) ModItems.itemInfinityGauntlet).getTextureName(meta);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(LanguageRegistry.instance().getStringLocalization("message.replica"));
        super.addInformation(stack, playerIn, tooltip, advanced);
    }
}
