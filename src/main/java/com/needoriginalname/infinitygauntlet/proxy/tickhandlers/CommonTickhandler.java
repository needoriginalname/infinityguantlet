package com.needoriginalname.infinitygauntlet.proxy.tickhandlers;

import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import com.needoriginalname.infinitygauntlet.hander.ScheduleOverloadHandler;
import com.needoriginalname.infinitygauntlet.util.LogHelper;
import com.needoriginalname.infinitygauntlet.util.nodes.INode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import sun.rmi.runtime.Log;

import java.util.*;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Owner on 9/28/2016.
 */
public class CommonTickhandler {

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.END){
            applyDeferredDimensionTransfers();
            applyDeferredAction(event.world);
            //applyDeferredBlockReplacements(event.world);
        }
    }

    private void applyDeferredAction(World world) {

        int dimId = world.provider.getDimensionId();

        if (!proxy.getDeferredActionMap().containsKey(dimId)) {
            proxy.getDeferredActionMap().put(dimId, new HashMap<Integer, PriorityQueue<INode>>());
        }

        Map<Integer, PriorityQueue<INode>> chainMap = proxy.getDeferredActionMap().get(dimId);
        for (Integer chainId :
                chainMap.keySet()) {
            PriorityQueue<INode> queue = chainMap.get(chainId);

            if (queue.size() > 0) {


                while (queue.size() > 0 && queue.peek().getDistance() <= world.getTotalWorldTime()) {
                    INode node = queue.poll();
                    node.doAction();

                }


            }

        }
    }





    private void applyDeferredDimensionTransfers(){
        for (EntityLivingBase ent : proxy.getDeferredDimTransfer().keySet()){
            int newdim = proxy.getDeferredDimTransfer().get(ent);

            if (ent instanceof EntityPlayerMP){
                EntityPlayerMP player = (EntityPlayerMP) ent;
                new SpaceGemTeleporter(player.mcServer.worldServerForDimension(newdim)).teleport(player);
            }
        }

        proxy.clearDeferredDimensionTransfers();
    }
}
