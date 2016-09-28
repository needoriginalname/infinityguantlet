package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.network.MessageKeyPressed;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.reference.Key;
import com.needoriginalname.infinitygauntlet.reference.Names;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by Al on 5/18/2015.
 */
public class KeyBindingHandler {
    public static KeyBinding changeGemState;


    public static void init(){
        changeGemState = new KeyBinding(Names.Keys.ChangeGemState, Keyboard.KEY_C, Names.Keys.Category);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (changeGemState.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            PacketHandler.dispatcher.sendToServer(new MessageKeyPressed(Key.Keys.ChangeGauntletMode));
        }
    }

}
