package com.needoriginalname.infinitygauntlet.hander.crafting;

import com.needoriginalname.infinitygauntlet.items.ModItems;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

import static com.needoriginalname.infinitygauntlet.items.ModItems.itemGem;

/**
 * Created by Owner on 10/20/2016.
 */
public class GauntletRecipe extends ShapedRecipes{

    public GauntletRecipe() {

        super(3, 3,  new ItemStack[]{
                new ItemStack(itemGem, 1, IDs.Gems.RealityGem.getID()),
                new ItemStack(itemGem, 1, IDs.Gems.MindGem.getID()),
                new ItemStack(itemGem, 1, IDs.Gems.SpaceGem.getID()),
                new ItemStack(itemGem, 1, IDs.Gems.PowerGem.getID()),
                new ItemStack(Items.diamond),
                new ItemStack(itemGem, 1, IDs.Gems.TimeGem.getID()),
                new ItemStack(Items.gold_ingot),
                new ItemStack(itemGem, 1, IDs.Gems.SoulGem.getID()),
                new ItemStack(Items.gold_ingot)}
        ,new ItemStack(ModItems.itemInfinityGauntlet));
    }


    //ensures that the power gem is not return, since it is technically a container item of itself for fuel purposes.
    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[9];
    }
}
