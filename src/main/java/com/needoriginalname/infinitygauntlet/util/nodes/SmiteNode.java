package com.needoriginalname.infinitygauntlet.util.nodes;

import com.needoriginalname.infinitygauntlet.util.MiscUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.needoriginalname.infinitygauntlet.InfinityGuantletMod.proxy;

/**
 * Created by Owner on 10/19/2016.
 */
public class SmiteNode extends Node{
    private final List<EntityLivingBase> toSmite;
    private final EntityPlayer player;


    public SmiteNode(World w, BlockPos pos, long distence, int chainId, EntityPlayer player, List<EntityLivingBase> toSmite){
        super(w, pos, distence, chainId);
        this.toSmite = toSmite;
        this.player = player;

    }

    @Override
    public void doAction() {
        if(toSmite.isEmpty()) return;

        boolean findingATargetToSmite = true;
        EntityLivingBase targetToSmite = null;

        while (findingATargetToSmite){
            EntityLivingBase possibleTarget = toSmite.remove(0);
            if (!possibleTarget.isDead && possibleTarget.getHealth() > 0){
                targetToSmite = possibleTarget;
                findingATargetToSmite = false;
            }
            if (toSmite.isEmpty()) findingATargetToSmite = false;
        }

        if (targetToSmite != null){
            MiscUtil.DamageEntity(targetToSmite, player);
            getWorld().addWeatherEffect(new EntityLightningBolt(getWorld(), targetToSmite.posX, targetToSmite.posY, targetToSmite.posZ));
            proxy.addDeferredAction(new SmiteNode(getWorld(), getBlockPos(), getNextTime(), getChainedId(), player, toSmite));
        }


    }
}
