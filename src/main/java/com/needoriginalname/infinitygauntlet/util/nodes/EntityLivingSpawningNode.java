package com.needoriginalname.infinitygauntlet.util.nodes;

import com.needoriginalname.infinitygauntlet.util.LogHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Owner on 10/8/2016.
 */
public class EntityLivingSpawningNode extends Node {
    private final EntityLiving e;

    public EntityLivingSpawningNode(EntityLiving e, World w, BlockPos pos, long distance, Integer chainId) {
        super(w, pos, distance, chainId);
        this.e = e;
        e.setPosition(pos.getX()+0.5d, pos.getY()+0.5d, pos.getZ()+ 0.5d);
    }

    @Override
    public void doAction() {
        getWorld().spawnEntityInWorld(e);
        LogHelper.debug("Spawning " + e.getName() + " at " + getBlockPos().toString() + " dim: " + getWorld().provider.getDimensionId());
    }
}
