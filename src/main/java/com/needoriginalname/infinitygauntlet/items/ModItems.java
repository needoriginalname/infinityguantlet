package com.needoriginalname.infinitygauntlet.items;

import com.needoriginalname.infinitygauntlet.hander.crafting.GauntletRecipe;
import com.needoriginalname.infinitygauntlet.reference.IDs;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.util.FuelHandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Al on 5/16/2015.
 */
public class ModItems {
    public static final Item itemGem = new ItemGem();
    public static final Item itemInfinityGauntlet = new ItemGauntlet();
    public static final Item itemGemReplica = new ItemGemReplica();
    public static final Item itemInfinityGauntletReplica = new ItemGauntletReplica();


    public static void preInit(FMLPreInitializationEvent event){
        GameRegistry.registerItem(itemGem, Names.InfinityGem);
        GameRegistry.registerItem(itemInfinityGauntlet, Names.InfinityGauntlet);
        GameRegistry.registerItem(itemGemReplica, Names.InfinityGemReplica);
        GameRegistry.registerItem(itemInfinityGauntletReplica, Names.InfinityGauntletReplica);


    }

    public static void init (FMLInitializationEvent event) {
        registerDungeonLoot(event);

        if (event.getSide() == Side.CLIENT) registerRenderForAllItems(event);

        GameRegistry.registerFuelHandler(new FuelHandler());

        GameRegistry.addRecipe(new GauntletRecipe());

        for (IDs.Gems gem : IDs.Gems.values()){
            GameRegistry.addShapedRecipe(new ItemStack(ModItems.itemGemReplica, 1, gem.getID()),
                    " D ",
                    "DED",
                    " D ",
                    'D', new ItemStack(Items.dye, 0, gem.getDyeColor().getDyeDamage()),
                    'E', Items.emerald
                    );
        }

        GameRegistry.addRecipe(new ItemStack(ModItems.itemInfinityGauntletReplica),
                "ABC",
                "DZE",
                "GFG",
                'G', Items.gold_ingot,
                'A', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.RealityGem.getID()),
                'B', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.MindGem.getID()),
                'C', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.SpaceGem.getID()),
                'D', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.PowerGem.getID()),
                'E', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.TimeGem.getID()),
                'F', new ItemStack(ModItems.itemGemReplica, 1, IDs.Gems.SoulGem.getID()),
                'Z', Items.diamond
                );

    }

    private static void registerRenderForAllItems(FMLInitializationEvent event) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        registerRender(renderItem, itemInfinityGauntlet, 0);
        registerRender(renderItem, itemInfinityGauntletReplica, 0);
        ModelBakery.registerItemVariants(itemGem,
                new ResourceLocation("infinitygauntlet:itemInfinityGemPower"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemSoul"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemReality"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemMind"),
                new ResourceLocation( "infinitygauntlet:itemInfinityGemSpace"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemTime") );
        ModelBakery.registerItemVariants(itemGemReplica,
                new ResourceLocation("infinitygauntlet:itemInfinityGemPower"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemSoul"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemReality"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemMind"),
                new ResourceLocation( "infinitygauntlet:itemInfinityGemSpace"),
                new ResourceLocation("infinitygauntlet:itemInfinityGemTime") );

        for (int i = 0; i < IDs.Gems.values().length; i++){
            registerRender(renderItem, itemGem, i);
            registerRender(renderItem, itemGemReplica, i);
        }


    }

    private static void registerRender(RenderItem renderitem, Item item, int metadata){


        String fileLocation;
        if (item instanceof  ItemIG){
            fileLocation = ((ItemIG) item).getTextureName(metadata);
        } else if (item instanceof ItemToolIG){
            fileLocation = ((ItemToolIG) item).getTextureName(metadata);
        } else {
            LogHelper.error("Error, unable to find the filelocation in registerRender");
            fileLocation = "";
        }

        renderitem.getItemModelMesher().register(item, metadata, new ModelResourceLocation(fileLocation, "inventory"));
    }


    public static void registerDungeonLoot(FMLInitializationEvent event) {
        ChestGenHooks dungeonChest = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
        ChestGenHooks netherFortressChest = ChestGenHooks.getInfo(ChestGenHooks.NETHER_FORTRESS);
        ChestGenHooks pyramidJungleChest = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
        ChestGenHooks pyramidDesertChest = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
        ChestGenHooks strongholdCorridor = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR);
        ChestGenHooks strongholdCrossing = ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING);


        for (IDs.Gems gem : IDs.Gems.values()){
            ItemStack item = new ItemStack(itemGem, 1, gem.getID());
            int rarity = gem.getRarity();

            dungeonChest.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
            netherFortressChest.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
            pyramidJungleChest.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
            pyramidDesertChest.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
            strongholdCorridor.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
            strongholdCrossing.addItem(new WeightedRandomChestContent(item, 1, 1, rarity));
        }


    }
}
