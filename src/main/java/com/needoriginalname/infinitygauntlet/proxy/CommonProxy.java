package com.needoriginalname.infinitygauntlet.proxy;


import com.needoriginalname.infinitygauntlet.proxy.tickhandlers.CommonTickhandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;

/**
 * Created by Al on 5/12/2015.
 */
public class CommonProxy implements IProxy {

    private HashMap scheduledDimTransfers = new HashMap<EntityLivingBase, Integer>();
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
}
