package com.needoriginalname.infinitygauntlet;


import com.needoriginalname.infinitygauntlet.blocks.ModBlocks;
import com.needoriginalname.infinitygauntlet.command.ToggleParticleCommand;
import com.needoriginalname.infinitygauntlet.dimension.WorldProviderSoulGem;
import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.hander.EventListener;
import com.needoriginalname.infinitygauntlet.hander.KeyBindingHandler;
import com.needoriginalname.infinitygauntlet.items.ModItems;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.proxy.CommonProxy;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by Al on 5/16/2015.
 */


@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.MODNAME)
public class InfinityQuantletMod {

    @Mod.Instance(Reference.MODID)
    public static InfinityQuantletMod instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());

        PacketHandler.registerPackets();
        proxy.registerKeyBinding();

        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
        proxy.CreateAndRegisterHandlers();
        ModItems.preInit(event);
        ModBlocks.preInit(event);
        //EnumHelper.addEnum(EnumParticleTypes.class, "patreon", 50, false);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModItems.init(event);
        ModBlocks.init(event);


        MinecraftForge.EVENT_BUS.register(new EventListener());

        DimensionManager.registerProviderType(ConfigurationHandler.soulGemDimensionID, WorldProviderSoulGem.class, false);
        DimensionManager.registerDimension(ConfigurationHandler.soulGemDimensionID, ConfigurationHandler.soulGemDimensionID);




        //MinecraftForge.EVENT_BUS.register(new SunHandler());

    }

    public void postInit(FMLPostInitializationEvent event){

    }

    @Mod.EventHandler
    public void start(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ToggleParticleCommand());
    }

}
