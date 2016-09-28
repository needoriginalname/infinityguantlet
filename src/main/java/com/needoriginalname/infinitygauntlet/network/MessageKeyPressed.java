package com.needoriginalname.infinitygauntlet.network;

import com.needoriginalname.infinitygauntlet.util.IKeyBound;
import com.needoriginalname.infinitygauntlet.reference.Key;
import com.needoriginalname.infinitygauntlet.util.MiscUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Al on 5/19/2015.
 */
public class MessageKeyPressed implements IMessage, IMessageHandler<MessageKeyPressed, IMessage> {
    private byte keyPressed;

    public byte getKeyPressed(){
        return keyPressed;
    }

    public MessageKeyPressed(){

    }

    public MessageKeyPressed(Key.Keys key){
        if (MiscUtil.InRange(key.ordinal(), 0, Key.Keys.values().length)){
            this.keyPressed = (byte) key.ordinal();
        } else {
            this.keyPressed = (byte) Key.Keys.Unknown.ordinal();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyPressed = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(keyPressed);
    }

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        this.keyPressed = message.getKeyPressed();



        if (player != null){
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IKeyBound) {
                if (MiscUtil.InRange(this.keyPressed,0, Key.Keys.values().length)) {
                    ((IKeyBound) player.getCurrentEquippedItem().getItem()).doKeyAction(player, player.getCurrentEquippedItem(), Key.Keys.values()[this.keyPressed]);
                }
            }

            /*if (this.keyPressed  == Key.Keys.TimeAccelerate.ordinal()){
                SunHandler.changeSpeed(false, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            } else if (this.keyPressed  == Key.Keys.ReverseTime.ordinal()){
                SunHandler.changeSpeed(true, player.worldObj,  (int)  player.posX, (int)  player.posY, (int)  player.posZ);
            }*/
        }




        return null;
    }
}
