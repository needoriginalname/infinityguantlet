package com.needoriginalname.infinitygauntlet.network;

import com.needoriginalname.infinitygauntlet.util.LogHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * Created by Owner on 11/9/2016.
 */
public class MessageUpdateForgeData implements IMessage, IMessageHandler<MessageUpdateForgeData, IMessage> {
    public  int entityID;
    public  NBTTagCompound nbt;

    public MessageUpdateForgeData(){
        
    }
    
    public MessageUpdateForgeData(Entity e, NBTTagCompound nbt){
        this.entityID = e.getEntityId();
        this.nbt = nbt;
    }
    
    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
        try {
            this.nbt = NetworkUtil.readNBTFromByteBuff(buf);
        } catch (IOException e) {
            LogHelper.warn("Unable get NBT data from byte buff");
            e.printStackTrace();
        }

    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        NetworkUtil.writeNBTToByteBuf(buf, nbt);
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
    public IMessage onMessage(MessageUpdateForgeData message, MessageContext ctx) {
        Entity e = Minecraft.getMinecraft().theWorld.getEntityByID(message.entityID);
        if (e != null){
            e.getEntityData().merge(message.nbt);
        }
        return null;
    }
}
