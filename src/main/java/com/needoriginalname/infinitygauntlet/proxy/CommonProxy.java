package com.needoriginalname.infinitygauntlet.proxy;


import com.needoriginalname.infinitygauntlet.proxy.tickhandlers.CommonTickhandler;
import com.needoriginalname.infinitygauntlet.util.AnimalNode;
import com.needoriginalname.infinitygauntlet.util.BlockNode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Al on 5/12/2015.
 */
public class CommonProxy implements IProxy {

    private HashMap scheduledDimTransfers = new HashMap<EntityLivingBase, Integer>();
    private Map<Integer, PriorityQueue<BlockNode>> scheduleBlockReplacementQueue = new HashMap<Integer, PriorityQueue<BlockNode>>();
    private CommonTickhandler tickhandler;
    private Map<Integer, PriorityQueue<AnimalNode>> scheduleAnimalSpawningQueue = new HashMap<Integer, PriorityQueue<AnimalNode>>();

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
    public void addDeferredBlockReplacement(World world, PriorityQueue<BlockNode> queue) {
        int dimId = world.provider.getDimensionId();
        if (!scheduleBlockReplacementQueue.containsKey(dimId)){
            scheduleBlockReplacementQueue.put(dimId, new PriorityQueue<BlockNode>());
        }
        PriorityQueue<BlockNode> q = scheduleBlockReplacementQueue.get(dimId);
        q.addAll(queue);
        scheduleBlockReplacementQueue.put(dimId, q);
    }

    @Override
    public PriorityQueue<BlockNode> getDeferredBlockReplacement(World world) {
        if (!scheduleBlockReplacementQueue.containsKey(world.provider.getDimensionId())){
            return null;
        } else {
            return scheduleBlockReplacementQueue.get(world.provider.getDimensionId());
        }
    }

    @Override
    public PriorityQueue<AnimalNode> getDeferredSpawning(World world) {
        if (!scheduleAnimalSpawningQueue.containsKey(world.provider.getDimensionId())){
            return null;
        } else {
            return scheduleAnimalSpawningQueue.get(world.provider.getDimensionId());
        }
    }

    @Override
    public void addDeferredSpawning(World worldIn, PriorityQueue<AnimalNode> animalQueue) {
        int dimId = worldIn.provider.getDimensionId();
        if (!scheduleAnimalSpawningQueue.containsKey(dimId)){
            scheduleAnimalSpawningQueue.put(dimId, new PriorityQueue<AnimalNode>());
        }
        PriorityQueue<AnimalNode> q = scheduleAnimalSpawningQueue.get(dimId);
        q.addAll(animalQueue);
        scheduleAnimalSpawningQueue.put(dimId, q);
    }
}
