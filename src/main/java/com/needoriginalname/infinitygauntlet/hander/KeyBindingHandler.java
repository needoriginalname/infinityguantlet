package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.network.MessageKeyPressed;
import com.needoriginalname.infinitygauntlet.network.PacketHandler;
import com.needoriginalname.infinitygauntlet.particles.PatreonParticuleFx;
import com.needoriginalname.infinitygauntlet.reference.Key;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
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
    //public static KeyBinding testParticule;


    public static void init(){
        changeGemState = new KeyBinding(Names.Keys.ChangeGemState, Keyboard.KEY_C, Names.Keys.Category);
        //testParticule = new KeyBinding(Names.Keys.TestParticule, Keyboard.KEY_PAUSE, Names.Keys.Category);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (changeGemState.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            PacketHandler.dispatcher.sendToServer(new MessageKeyPressed(Key.Keys.ChangeGauntletMode));
        } /* else if (testParticule.isPressed()){
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().effectRenderer.addEffect(new PatreonParticuleFx(Minecraft.getMinecraft().theWorld, player));
            LogHelper.info("Spawned at " + player.posX + " " + player.posY + " " + player.posZ);
        } */
    }

}
