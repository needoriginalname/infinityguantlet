package com.needoriginalname.infinitygauntlet.proxy.tickhandlers;

import com.needoriginalname.infinitygauntlet.InfinityQuantletMod;
import com.needoriginalname.infinitygauntlet.dimension.NonPortalTeleporter;
import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import com.needoriginalname.infinitygauntlet.util.GraphNode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.PriorityQueue;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Owner on 9/28/2016.
 */
public class CommonTickhandler {

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.END){
            applyDeferredDimensionTransfers();
            applyDeferredBlockReplacements(event.world);
        }
    }

    private void applyDeferredBlockReplacements(World world) {
        PriorityQueue<GraphNode> queue = proxy.getDeferredBlockReplacement(world);
        while (queue != null && !queue.isEmpty()){
            if (queue.peek().getDistance() <= world.getTotalWorldTime()){
                GraphNode g = queue.poll();
                if (g.getBlockState() != null) {
                    world.setBlockState(g.pos, g.getBlockState());
                } else {
                    world.setBlockToAir(g.pos);
                }
                System.out.print(world);
                System.out.print(g.pos);
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
