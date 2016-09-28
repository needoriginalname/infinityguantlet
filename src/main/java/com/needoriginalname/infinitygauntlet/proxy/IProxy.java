package com.needoriginalname.infinitygauntlet.proxy;

import net.minecraft.entity.EntityLivingBase;

import java.util.HashMap;

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
}
