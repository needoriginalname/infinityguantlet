package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Al on 5/16/2015.
 */
public class ItemIG extends Item{
    ItemIG(){super();}


    @Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", Reference.MODID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    public String getTextureName(int meta){
        return String.format("%s%s", Reference.MODID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return String.format("item.%s%s", Reference.MODID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }


    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }


}
