package com.needoriginalname.infinitygauntlet.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Al on 5/28/2015.
 */
public class MessageCustomSoundPacket implements IMessage, IMessageHandler<MessageCustomSoundPacket, IMessage> {
    String soundfile;

    public String getSoundfile(){
        return soundfile;
    }

    public int getX() {
        return x;
    }

    int x;

    public int getY() {
        return y;
    }

    int y;

    public int getZ() {
        return z;
    }

    int z;

    public MessageCustomSoundPacket(){

    }

    public MessageCustomSoundPacket(String soundfile, int x, int y, int z){
        this.soundfile = soundfile;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        soundfile = new String();
        char temp = '\0';
        for (int i = 0; i < 50 && temp != '\n' && buf.isReadable(); i++){
            temp = buf.readChar();
            if (temp != '\n') {
                soundfile = soundfile + temp;
            }
        }
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        for (int i = 0; i < 50 && i < soundfile.length() && buf.isWritable(); i++) {
            buf.writeChar(soundfile.toCharArray()[i]);
        }
        buf.writeChar('\n');
    }

    /**
     * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
     * is needed.
     *
     * @param message The message
     * @param ctx
     * @return an optional return message
     */
    @Override
    public IMessage onMessage(MessageCustomSoundPacket message, MessageContext ctx) {

        x = message.getX();
        y = message.getY();
        z = message.getZ();
        soundfile = message.getSoundfile();

        Minecraft.getMinecraft().theWorld.playSound(x,y,z, soundfile, 1.0f, 1.0f, false);

        return null;
    }
}
