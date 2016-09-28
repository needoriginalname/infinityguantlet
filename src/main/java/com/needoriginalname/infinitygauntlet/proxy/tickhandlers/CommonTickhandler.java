package com.needoriginalname.infinitygauntlet.proxy.tickhandlers;

import com.needoriginalname.infinitygauntlet.InfinityQuantletMod;
import com.needoriginalname.infinitygauntlet.dimension.NonPortalTeleporter;
import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Owner on 9/28/2016.
 */
public class CommonTickhandler {

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.END){
            applyDeferredDimensionTransfers();
        }
    }


    private void applyDeferredDimensionTransfers(){
        for (EntityLivingBase ent : InfinityQuantletMod.proxy.getDeferredDimTransfer().keySet()){
            int newdim = InfinityQuantletMod.proxy.getDeferredDimTransfer().get(ent);

            if (ent instanceof EntityPlayerMP){
                EntityPlayerMP player = (EntityPlayerMP) ent;
                new SpaceGemTeleporter(player.mcServer.worldServerForDimension(newdim)).teleport(player);
            }
        }

        InfinityQuantletMod.proxy.clearDeferredDimensionTransfers();
    }
}
