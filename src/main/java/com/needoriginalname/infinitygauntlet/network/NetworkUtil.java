package com.needoriginalname.infinitygauntlet.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

/**
 * Created by Owner on 11/9/2016.
 */
public class NetworkUtil {
    public static ByteBuf writeNBTToByteBuf(ByteBuf buf, NBTTagCompound nbt){
        if (nbt == null)
        {
            buf.writeByte(0);
        }
        else
        {
            try
            {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(buf));
            }
            catch (IOException ioexception)
            {
                throw new EncoderException(ioexception);
            }
        }

        return buf;
    }

    public static NBTTagCompound readNBTFromByteBuff(ByteBuf buf) throws IOException {
        int i = buf.readerIndex();
        byte b0 = buf.readByte();

        if (b0 == 0)
        {
            return null;
        }
        else
        {
            buf.readerIndex(i);
            return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
        }
    }
}
