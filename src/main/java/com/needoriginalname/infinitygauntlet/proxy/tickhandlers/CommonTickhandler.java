package com.needoriginalname.infinitygauntlet.proxy.tickhandlers;

import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import com.needoriginalname.infinitygauntlet.util.nodes.AnimalNode;
import com.needoriginalname.infinitygauntlet.util.nodes.BlockNode;
import com.needoriginalname.infinitygauntlet.util.nodes.INode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

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
            applyDeferredAnimalSpawning(event.world);
        }
    }

    private void applyDeferredAction(World world){

        for (String username:
                proxy.getDeferredActionMap().keySet()) {
            PriorityQueue<INode> queue =  proxy.getDeferredActionMap().get(username);

            //since any action occur will have to occur during the next tick or backlog, get the size of the current size
            int len = queue.size();
            boolean isNextTask = queue.size() > 0 && queue.peek().getDistance() <= world.getTotalWorldTime() && len > 0;
            Queue itemsToPutBack = new LinkedList();
            while(isNextTask) {
                INode node = queue.poll();

                if (world.provider.getDimensionId() == node.getWorld().provider.getDimensionId()) {
                    node.doAction();
                } else {
                    itemsToPutBack.add(node);
                }
                isNextTask = queue.size() > 0 &&  queue.peek().getDistance() <= world.getTotalWorldTime() && len > 0;
            }

            if (!itemsToPutBack.isEmpty())
                queue.addAll(itemsToPutBack);

            if (queue.isEmpty()){
                proxy.getDeferredActionMap().remove(username);
            }
        }


    }

    private void applyDeferredAnimalSpawning(World world) {
        PriorityQueue<AnimalNode> queue = proxy.getDeferredSpawning(world);
        while (queue != null && !queue.isEmpty()){
            if (queue.peek().getDistance() <= world.getTotalWorldTime()){
                AnimalNode g = queue.poll();
                if (g.getEntity() != null) {
                    Entity e = g.getEntity();
                    e.setPosition(g.getPos().getX()+0.5d, g.getPos().getY(), g.getPos().getZ() + 0.5d);
                    world.spawnEntityInWorld(g.getEntity());
                }
            } else {
                break;
            }
        }


    }

    private void applyDeferredBlockReplacements(World world) {
        PriorityQueue<BlockNode> queue = proxy.getDeferredBlockReplacement(world);
        while (queue != null && !queue.isEmpty()){
            if (queue.peek().getDistance() <= world.getTotalWorldTime()){
                BlockNode g = queue.poll();
                if (g.getBlockState() != null) {
                    world.setBlockState(g.getPos(), g.getBlockState());
                } else {
                    world.setBlockToAir(g.getPos());
                }
            } else {
                break;
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
