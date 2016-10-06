package com.needoriginalname.infinitygauntlet.util.nodes;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

/**
 * Created by Owner on 9/30/2016.
 */
public class AnimalNode implements Comparable<AnimalNode> {
    private Entity e;
    private long distence = -1;
    private BlockPos pos = null;
    private String playerUUID = null;
    public Entity getEntity() {
        return e;
    }

    public AnimalNode setEntity(Entity e) {
        this.e = e;
        return this;
    }


    public long getDistance(){
        return distence;
    }

    public BlockPos getPos(){
        return pos;
    }

    public AnimalNode setPos(BlockPos p) {
        pos = p;
        return this;
    }

    public AnimalNode setDistance(long n) {
        distence = n;
        return this;
    }
    @Override
    public int compareTo(AnimalNode o) {
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

    public AnimalNode setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
        return this;
    }
}
