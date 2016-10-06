package com.needoriginalname.infinitygauntlet.util.nodes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

/**
 * Created by Owner on 9/29/2016.
 */
public class BlockNode implements Comparable<BlockNode> {


    private IBlockState block;
    private String playerUUID = null;
    private long distence = -1;
    private BlockPos pos = null;

    public BlockNode setBlockState(IBlockState state){
        block = state;
        return this;
    }

    public IBlockState getBlockState(){
        return block;
    }



    public long getDistance(){
        return distence;
    }

    public BlockPos getPos(){
        return pos;
    }

    public BlockNode setPos(BlockPos p) {
        pos = p;
        return this;
    }

    public BlockNode setDistance(long n) {
        distence = n;
        return this;
    }

    @Override
    public int compareTo(BlockNode o) {
        if (this.getDistance() > o.getDistance()){
            return 1;
        } else if (this.getDistance() == o.getDistance()){
            return 0;
        } else {
            return -1;
        }
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public BlockNode setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }
}