package com.needoriginalname.infinitygauntlet.proxy;


import com.needoriginalname.infinitygauntlet.proxy.tickhandlers.CommonTickhandler;
import com.needoriginalname.infinitygauntlet.util.GraphNode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.Int;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Al on 5/12/2015.
 */
public class CommonProxy implements IProxy {

    private HashMap scheduledDimTransfers = new HashMap<EntityLivingBase, Integer>();
    private Map<Integer, PriorityQueue<GraphNode>> scheduleBlockReplacementQueue = new HashMap<Integer, PriorityQueue<GraphNode>>();
    private CommonTickhandler tickhandler;

    @Override
    public void CreateAndRegisterHandlers(){
        tickhandler = new CommonTickhandler();
        MinecraftForge.EVENT_BUS.register(tickhandler);
    }

    @Override
    public void registerKeyBinding() {

    }

    @Override
    public void registerRenders() {

    }

    @Override
    public void addDeferredDimTransfer(EntityLivingBase e, int dim){
        scheduledDimTransfers.put(e,dim);
    }

    @Override
    public HashMap<EntityLivingBase, Integer> getDeferredDimTransfer(){
        return scheduledDimTransfers;
    }

    @Override
    public void clearDeferredDimensionTransfers() {
        scheduledDimTransfers.clear();
    }

    @Override
    public void addDeferredBlockReplacement(World world, PriorityQueue<GraphNode> queue) {
        int dimId = world.provider.getDimensionId();
        if (!scheduleBlockReplacementQueue.containsKey(dimId)){
            scheduleBlockReplacementQueue.put(dimId, new PriorityQueue<GraphNode>());
        }
        PriorityQueue<GraphNode> q = scheduleBlockReplacementQueue.get(dimId);
        q.addAll(queue);
        scheduleBlockReplacementQueue.put(dimId, q);
    }

    @Override
    public PriorityQueue<GraphNode> getDeferredBlockReplacement(World world) {
        if (!scheduleBlockReplacementQueue.containsKey(world.provider.getDimensionId())){
            return null;
        } else {
            return scheduleBlockReplacementQueue.get(world.provider.getDimensionId());
        }
    }
}
