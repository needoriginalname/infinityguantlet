package com.needoriginalname.infinitygauntlet.network;

import com.needoriginalname.infinitygauntlet.reference.Reference;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

/**
 * Created by Al on 5/19/2015.
 */
public class PacketHandler {
    private static int packetID = 0;

    public static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    public static final void registerPackets(){
        registerMessage(MessageKeyPressed.class, MessageKeyPressed.class, Side.SERVER);
        registerMessage(MessageVectorParticle.class, MessageVectorParticle.class, Side.CLIENT);
        registerMessage(MessageCustomSoundPacket.class, MessageCustomSoundPacket.class, Side.CLIENT);
        registerMessage(MessageUpdateForgeData.class, MessageUpdateForgeData.class, Side.CLIENT);
     }


    private static final void registerMessage(Class handlerClass, Class messageClass, Side side){
       dispatcher.registerMessage(handlerClass, messageClass, packetID++, side);


    }

}
