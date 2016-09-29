package com.needoriginalname.infinitygauntlet.util;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

/**
 * Created by Owner on 9/29/2016.
 */
public class GraphNode implements Comparable<GraphNode> {
    public long distence = -1;
    public BlockPos pos = null;
    private IBlockState block;

    public GraphNode setDistance(long n) {
        distence = n;
        return this;
    }

    public long getDistance(){
        return distence;
    }

    public BlockPos getPos(){
        return pos;
    }

    public IBlockState getBlockState(){
        return block;
    }

    public GraphNode setPos(BlockPos p) {
        pos = p;
        return this;
    }

    public GraphNode setBlockState(IBlockState state){
        block = state;
        return this;
    }

    @Override
    public int compareTo(GraphNode o) {
        if (this.getDistance() > o.getDistance()){
            return 1;
        } else if (this.getDistance() == o.getDistance()){
            return 0;
        } else {
            return -1;
        }
    }
}
