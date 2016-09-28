package com.needoriginalname.infinitygauntlet.blocks;

import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Al on 5/26/2015.
 */
public class ModBlocks {
    public static Block blockSoulGemTrap;

    public static void preInit(FMLPreInitializationEvent event){
        blockSoulGemTrap = (Block) new BlockSoulGemTrap();

        GameRegistry.registerBlock(blockSoulGemTrap, Names.SoulGemTrap );
    }

    public static void init(FMLInitializationEvent event) {


        if (event.getSide() == Side.CLIENT)RegisterRenderForAllBlocks(event);
    }

    private static void RegisterRenderForAllBlocks(FMLInitializationEvent event) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        registerRender(renderItem, blockSoulGemTrap, 0);


    }

    private static void registerRender(RenderItem renderItem, Block block, int metadata) {
        String fileLocation = new String();
        if (block instanceof  BlockIG){
            fileLocation = ((BlockIG) block).getTextureName(metadata);
        } else {
            LogHelper.error(new String("Error, unable to find the filelocation in registerRender"));
        }

        renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), metadata, new ModelResourceLocation(fileLocation, "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(block), metadata, new ModelResourceLocation(fileLocation, "normal"));



    }
}
