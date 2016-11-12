package com.needoriginalname.infinitygauntlet.util.nodes;

import com.needoriginalname.infinitygauntlet.dimension.SpaceGemTeleporter;
import com.sun.istack.internal.NotNull;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Owner on 11/10/2016.
 */
public class TransferPlayerNode extends Node{

    private final Entity e;
    private final Integer newWorldId;
    private final BlockPos newPos;
    private final boolean clearPath;
    private final World oldWorld;

    public TransferPlayerNode(EntityPlayerMP e, World oldWorld, Integer newWorldId, BlockPos oldPos, boolean clearPath) {
        super(oldWorld, oldPos, oldWorld.getTotalWorldTime() + 1, new Random().nextInt());
        this.e = e;
        this.newWorldId = newWorldId;
        this.newPos = oldPos;
        this.clearPath = clearPath;
        this.oldWorld = oldWorld;
    }


    public TransferPlayerNode(EntityPlayerMP e, World oldWorld, Integer newWorldId, BlockPos oldPos, BlockPos newPos, boolean clearPath) {
        super(oldWorld, oldPos, oldWorld.getTotalWorldTime() + 1, new Random().nextInt());
        this.e = e;
        this.newWorldId = newWorldId;
        this.newPos = newPos;
        this.clearPath = clearPath;
        this.oldWorld = oldWorld;
    }

    @Override
    public void doAction() {

        EntityPlayerMP player = (EntityPlayerMP) e;
        Teleporter teleporter;
        if (newPos != null) {
            teleporter = new SpaceGemTeleporter(player.mcServer.worldServerForDimension(newWorldId), newPos, clearPath);
        } else {
            teleporter = new SpaceGemTeleporter(player.mcServer.worldServerForDimension(newWorldId));
        }
        player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newWorldId, teleporter);
        if (oldWorld.provider.getDimensionId() == 1){
            if (player.isEntityAlive()){
                player.worldObj.spawnEntityInWorld(player);
                player.worldObj.updateEntityWithOptionalForce(player, false);
            }
        }

    }
}
