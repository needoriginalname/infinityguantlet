package com.needoriginalname.infinitygauntlet.proxy;

import com.needoriginalname.infinitygauntlet.util.GraphNode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by Al on 5/12/2015.
 */
public interface IProxy {
    void registerRenders();

    void CreateAndRegisterHandlers();

    void registerKeyBinding();

    void addDeferredDimTransfer(EntityLivingBase e, int dim);

    HashMap<EntityLivingBase, Integer> getDeferredDimTransfer();

    void clearDeferredDimensionTransfers();

    void addDeferredBlockReplacement(World world, PriorityQueue<GraphNode> queue);

    PriorityQueue<GraphNode> getDeferredBlockReplacement(World world);
}
