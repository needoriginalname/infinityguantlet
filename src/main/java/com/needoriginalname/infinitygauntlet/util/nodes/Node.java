package com.needoriginalname.infinitygauntlet.util.nodes;

import com.sun.istack.internal.NotNull;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Owner on 10/5/2016.
 */
abstract class Node implements INode {
    private final World w;
    private final BlockPos pos;
    private final Integer id;
    private long distance;

    Node(World w, BlockPos pos, long distance,@NotNull Integer id) {
        this.w = w;
        this.pos = pos;
        this.distance = distance;
        this.id = id;
    }

    @Override
    public int compareTo(@NotNull INode o) {

        return (this.getDistance() < o.getDistance()) ? -1 : ((this.getDistance() == o.getDistance()) ? 0 : 1);
    }

    @Override
    public World getWorld() {
        return this.w;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public long getDistance() {
        return distance;
    }

    @Override
    public void setDistance(long d){
        this.distance = d;
    }

    @Override
    public Integer getChainedId() {
        return id;
    }


    long getNextTime(){
        return getDistance() + 1 > getWorld().getTotalWorldTime() ? getDistance() + 1 : getWorld().getTotalWorldTime() + 1;
    }
}
