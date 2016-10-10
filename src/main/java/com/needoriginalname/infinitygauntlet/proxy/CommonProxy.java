package com.needoriginalname.infinitygauntlet.proxy;


import com.needoriginalname.infinitygauntlet.hander.ScheduleOverloadHandler;
import com.needoriginalname.infinitygauntlet.proxy.tickhandlers.CommonTickhandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import com.needoriginalname.infinitygauntlet.util.nodes.INode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

/**
 * Created by Al on 5/12/2015.
 */
public class CommonProxy {

    private HashMap scheduledDimTransfers = new HashMap<EntityLivingBase, Integer>();
    private CommonTickhandler tickhandler;
    private Queue<INode> Buffer = new LinkedList<INode>();

    public Map<Integer, Map<Integer, PriorityQueue<INode>>>getDeferredActionMap() {
        return deferredActionMap;
    }
    //worldId -> chainId -> actionNodes
    private Map<Integer, Map<Integer, PriorityQueue<INode>>> deferredActionMap = new HashMap<Integer, Map<Integer, PriorityQueue<INode>>>();

    //private Map<Integer, PriorityQueue<INode>> deferredActionMap = new HashMap<Integer, PriorityQueue<INode>>();

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

    

    


    


    
    public void addDeferredAction(INode node) {

        /*
        if (!deferredActionMap.containsKey(node.getChainedId())){
            deferredActionMap.put(node.getChainedId(), new PriorityQueue<INode>());
        }*/
        int dimId = node.getWorld().provider.getDimensionId();
        if (!deferredActionMap.containsKey(dimId)){
            deferredActionMap.put(dimId, new HashMap<Integer, PriorityQueue<INode>>());
        }

        long c1 = node.getDistance();
        node = ScheduleOverloadHandler.handle(node);
        long c2 = node.getDistance();

        if (c1 != c2){
            LogHelper.debug("node adjusted from " + c1 + " to " + c2);
        }

        Map<Integer, PriorityQueue<INode>> map2 = deferredActionMap.get(dimId);
        if (!map2.containsKey(node.getChainedId())){
            map2.put(node.getChainedId(), new PriorityQueue<INode>());
            LogHelper.debug(node.getChainedId() + " chainId has started");
        }
        PriorityQueue<INode> queue =  map2.get(node.getChainedId());
        queue.add(node);



        /*
        deferredActionMap.get(node.getChainedId()).add(node);
        */
    }


}
