package com.needoriginalname.infinitygauntlet.blocks;

import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Al on 5/30/2015.
 */
public class BlockIG extends Block {
    protected BlockIG(Material materialIn) {
        super(materialIn);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MODID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }


    public String getTextureName(int meta){
        return String.format("%s%s", Reference.MODID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }




    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

}
