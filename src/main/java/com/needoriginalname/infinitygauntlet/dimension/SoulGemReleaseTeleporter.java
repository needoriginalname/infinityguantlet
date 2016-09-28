package com.needoriginalname.infinitygauntlet.dimension;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created by Al on 5/27/2015.
 */
public class SoulGemReleaseTeleporter extends Teleporter {
    private final WorldServer worldServerInstance;
    private final Random random;
    private int z;
    private int y;
    private int x;

    public SoulGemReleaseTeleporter(WorldServer worldServer, int x, int y, int z) {
        super(worldServer);
        this.worldServerInstance = worldServer;
        this.random = worldServer.rand;
        this.x = x;
        this.y = y;
        this.z = z;
    }







    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {


        while (!worldServerInstance.isAirBlock(new BlockPos(x,y,z))){
                y++;
        }


        entityIn.setPositionAndUpdate(x, y, z);
    }
}
