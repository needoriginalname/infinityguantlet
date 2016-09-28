package com.needoriginalname.infinitygauntlet.blocks;

import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

/**
 * Created by Al on 5/24/2015.
 */

//3;minecraft:bedrock,2*infinitygauntlet:soulgemtrap;1;
public class BlockSoulGemTrap extends BlockIG {
    BlockSoulGemTrap(){
        super(Material.barrier);
        this.setBlockUnbreakable();
        this.setHardness(-1);
        //this.setLightLevel(15);
        this.setUnlocalizedName(Names.SoulGemTrap);
        this.setResistance(50);
    }

}
