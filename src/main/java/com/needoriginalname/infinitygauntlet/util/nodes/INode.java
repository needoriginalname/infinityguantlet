package com.needoriginalname.infinitygauntlet.util.nodes;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Owner on 10/5/2016.
 */
public interface INode extends Comparable<INode> {

    void doAction();
    World getWorld();
    BlockPos getBlockPos();
    long getDistance();
    String getPlayerUsername();
}
