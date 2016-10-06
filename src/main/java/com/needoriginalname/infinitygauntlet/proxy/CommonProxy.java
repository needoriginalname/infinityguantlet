package com.needoriginalname.infinitygauntlet.proxy;


import com.needoriginalname.infinitygauntlet.proxy.tickhandlers.CommonTickhandler;
import com.needoriginalname.infinitygauntlet.util.nodes.AnimalNode;
import com.needoriginalname.infinitygauntlet.util.nodes.BlockNode;
import com.needoriginalname.infinitygauntlet.util.nodes.INode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Al on 5/12/2015.
 */
public class CommonProxy {

    private HashMap scheduledDimTransfers = new HashMap<EntityLivingBase, Integer>();
    private Map<Integer, PriorityQueue<BlockNode>> scheduleBlockReplacementQueue = new HashMap<Integer, PriorityQueue<BlockNode>>();
    private CommonTickhandler tickhandler;
    private Map<Integer, PriorityQueue<AnimalNode>> scheduleAnimalSpawningQueue = new HashMap<Integer, PriorityQueue<AnimalNode>>();

    public Map<String, PriorityQueue<INode>> getDeferredActionMap() {
        return deferredActionMap;
    }

    private Map<String, PriorityQueue<INode>> deferredActionMap = new HashMap<String, PriorityQueue<INode>>();

    public void CreateAndRegisterHandlers(){
        tickhandler = new CommonTickhandler();
        MinecraftForge.EVENT_BUS.register(tickhandler);
    }

    
    public void registerKeyBinding() {

    }

    
    public void registerRenders() {

    }

    
    public void addDeferredDimTransfer(EntityLivingBase e, int dim){
        if (e != null)
            scheduledDimTransfers.put(e,dim);
    }

    
    public HashMap<EntityLivingBase, Integer> getDeferredDimTransfer(){
        return scheduledDimTransfers;
    }

    
    public void clearDeferredDimensionTransfers() {
        scheduledDimTransfers.clear();
    }

    

    
    public PriorityQueue<BlockNode> getDeferredBlockReplacement(World world) {
        if (!scheduleBlockReplacementQueue.containsKey(world.provider.getDimensionId())){
            return null;
        } else {
            return scheduleBlockReplacementQueue.get(world.provider.getDimensionId());
        }
    }

    
    public PriorityQueue<AnimalNode> getDeferredSpawning(World world) {
        if (!scheduleAnimalSpawningQueue.containsKey(world.provider.getDimensionId())){
            return null;
        } else {
            return scheduleAnimalSpawningQueue.get(world.provider.getDimensionId());
        }
    }

    
    public void addDeferredAction(INode node) {
        String username = node.getPlayerUsername();
        if (username == null) username = "herobrine";
        if (!deferredActionMap.containsKey(node.getPlayerUsername())){
            deferredActionMap.put(username, new PriorityQueue<INode>());
        }

        deferredActionMap.get(username).add(node);

    }



    
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
