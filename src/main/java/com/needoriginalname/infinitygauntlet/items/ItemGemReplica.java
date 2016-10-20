package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.reference.IDs;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.registry.LanguageRegistry;

import java.util.List;

/**
 * Created by Owner on 10/20/2016.
 */
public class ItemGemReplica extends ItemIG {
    public ItemGemReplica(){
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        //this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setUnlocalizedName(Names.InfinityGemReplica);

    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *
     * @param stack
     * @param playerIn
     * @param tooltip
     * @param advanced
     */
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(LanguageRegistry.instance().getStringLocalization("message.replica"));


        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
            int meta = MathHelper.clamp_int(itemStack.getMetadata(),0, IDs.Gems.values().length-1);
            return super.getUnlocalizedName(itemStack) + '.' + IDs.Gems.values()[meta].getUnlocalizedName() ;
    }

    public String getTextureName(int meta){
        //tell to use the ItemGem's texture files
        return ((ItemIG) ModItems.itemGem).getTextureName(meta);
    }


}
