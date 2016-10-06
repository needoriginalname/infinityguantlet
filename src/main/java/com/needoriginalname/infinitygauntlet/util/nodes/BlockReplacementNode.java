package com.needoriginalname.infinitygauntlet.util.nodes;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Owner on 10/5/2016.
 */
public class BlockReplacementNode extends Node {
    private final IBlockState oldState;
    private final IBlockState newState;
    private final short maxDepth;




    /**
     * @param oldState oldState to change from, null if from any state
     * @param newState New State to change to
     * @param pos Position of where it will occur
     * @param distance World tick to do this
     * @param w World that this action will occur on.
     * @param user the username of the block this was created by, if applicable
     */
    public BlockReplacementNode(IBlockState oldState, IBlockState newState, BlockPos pos, long distance, World w, @Nullable String user){
        this(oldState, newState, pos, distance, w, user, ConfigurationHandler.maxDepthOfBlockReplacement);
        EntityPlayerMP e;
    }

    /**
     * @param oldState oldState to change from, null if from any state
     * @param newState New State to change to
     * @param pos Position of where it will occur
     * @param distance World tick to do this
     * @param w World that this action will occur on.
     * @param user the username of the block this was created by, if applicable
     * @param maxDepthOfBlockReplacement the Max depth that this will recurisvly look from each last one.
     */
    public BlockReplacementNode(IBlockState oldState, IBlockState newState, BlockPos pos, long distance, World w, @Nullable String user, short maxDepthOfBlockReplacement) {
        super(w, pos, distance, user);
        this.oldState = oldState;
        this.newState = newState;
        this.maxDepth = maxDepthOfBlockReplacement;
    }


    @Override
    public void doAction() {
        //get to see if something else already replaced this
        if(oldState != newState && (oldState == null || getWorld().getBlockState(getBlockPos()) == oldState)){

            //replace the block with the new one, if applicable
            getWorld().destroyBlock(getBlockPos(), false);
            if (newState != Blocks.air.getDefaultState())
                getWorld().setBlockState(getBlockPos(), newState);


            //recursively adds a node in the queue in the proxy
            if (getGenerationsLeft() > 0) {
                List<BlockPos> poses = new LinkedList<BlockPos>();
                poses.add(getBlockPos().north());
                poses.add(getBlockPos().south());
                poses.add(getBlockPos().west());
                poses.add(getBlockPos().east());
                poses.add(getBlockPos().up());
                poses.add(getBlockPos().down());

                for (BlockPos pos : poses) {
                    BlockReplacementNode node = new BlockReplacementNode(oldState, newState, pos, getNextTime(), getWorld(), getPlayerUsername(), (short)(getGenerationsLeft() - 1) );
                    proxy.addDeferredAction(node);
                }


            }







        }



    }



    public short getGenerationsLeft() {
        return this.maxDepth;
    }
}
