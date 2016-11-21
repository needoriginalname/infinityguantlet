package com.needoriginalname.infinitygauntlet.proxy;

import com.needoriginalname.infinitygauntlet.hander.KeyBindingHandler;
import com.needoriginalname.infinitygauntlet.hander.ParticleHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Al on 5/12/2015.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerKeyBinding() {
        KeyBindingHandler.init();
        ClientRegistry.registerKeyBinding(KeyBindingHandler.changeGemState);
        //ClientRegistry.registerKeyBinding(KeyBindingHandler.testParticule);

        super.registerKeyBinding();
    }

    @Override
    public void CreateAndRegisterHandlers() {
        MinecraftForge.EVENT_BUS.register(new ParticleHandler());
        super.CreateAndRegisterHandlers();
    }
}