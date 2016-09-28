package com.needoriginalname.infinitygauntlet.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

/**
 * Created by Al on 5/21/2015.
 */
public class MessageVectorParticle implements IMessage, IMessageHandler<MessageVectorParticle, IMessage> {


    private double offset;

    public double getDistanceBetween() {
        return distanceBetween;
    }


    public EnumParticleTypes getParticleType() {
        return particleType;
    }


    double distanceBetween;
    EnumParticleTypes particleType;
    double x;

    public double getOffset() {return offset;}

    public double getZ2() {
        return z2;
    }

    public double getY2() {
        return y2;
    }


    public double getX2() {
        return x2;
    }


    public double getZ() {
        return z;
    }
    public double getY() {
        return y;
    }


    public double getX() {
        return x;
    }

    double y;
    double z;
    double x2;
    double y2;
    double z2;

    public MessageVectorParticle(){

    }

    public MessageVectorParticle(EnumParticleTypes particleType, double x, double y, double z, double x2, double y2, double z2, double distanceBetween) {
        this(particleType, x, y, z, x2, y2, z2, distanceBetween, 0.0);
    }

    public MessageVectorParticle(EnumParticleTypes particleType, double x, double y, double z, double x2, double y2, double z2, double distanceBetween, double offset){
        this.particleType = particleType;

        this.x = x;
        this.y = y;
        this.z = z;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.distanceBetween = distanceBetween;
        this.offset = offset;


    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(particleType.ordinal());
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(x2);
        buf.writeDouble(y2);
        buf.writeDouble(z2);
        buf.writeDouble(distanceBetween);
        buf.writeDouble(offset);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.particleType = EnumParticleTypes.values()[buf.readShort()];
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.x2 = buf.readDouble();
        this.y2 = buf.readDouble();
        this.z2 = buf.readDouble();
        this.distanceBetween = buf.readDouble();
        this.offset = buf.readDouble();
    }

    @Override
    public IMessage onMessage(MessageVectorParticle message, MessageContext ctx) {

        Random rnd = new Random();
        EnumParticleTypes particleTypes = message.getParticleType();
        Vec3 sourceVector = new Vec3(message.getX(), message.getY(), message.getZ());
        Vec3 targetVector = new Vec3(message.getX2(), message.getY2(), message.getZ2());

        this.distanceBetween = message.getDistanceBetween();
        double distance = sourceVector.distanceTo(targetVector);

        int n = MathHelper.floor_double((distance/distanceBetween));

        Vec3 normalizedVector = (targetVector.subtract(sourceVector)).normalize();


        for ( ; n > 0; n--){
           Vec3 point = sourceVector.add(scaleVector(normalizedVector, n));
            double x = point.xCoord;
            double y = point.yCoord;
            double z = point.zCoord;
            double offsetX = rnd.nextDouble() * message.getOffset() * (rnd.nextBoolean() ? 1 : -1);
            double offsetY = rnd.nextDouble() * message.getOffset() * (rnd.nextBoolean() ? 1 : -1);
            double offsetZ = rnd.nextDouble() * message.getOffset() * (rnd.nextBoolean() ? 1 : -1);


            WorldClient world = Minecraft.getMinecraft().theWorld;
            if (world.isBlockLoaded(new BlockPos(x,y,z), true)){
                world.spawnParticle(particleTypes, x,y,z, offsetX,offsetY,offsetZ, new int[0]);
            }
        }
        return null;
    }

    private Vec3 scaleVector(Vec3 normalizedVector, int n) {
        double x = normalizedVector.xCoord * n;
        double y = normalizedVector.yCoord * n;
        double z = normalizedVector.zCoord * n;
        return new Vec3(x,y,z);


    }
}
