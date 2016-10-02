package com.needoriginalname.infinitygauntlet.proxy;

import com.needoriginalname.infinitygauntlet.hander.KeyBindingHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Created by Al on 5/12/2015.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerKeyBinding() {
        KeyBindingHandler.init();
        ClientRegistry.registerKeyBinding(KeyBindingHandler.changeGemState);

        super.registerKeyBinding();
    }
}