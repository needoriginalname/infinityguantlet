package com.needoriginalname.infinitygauntlet.util.nodes;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.hander.EventListener;
import com.sun.istack.internal.NotNull;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

import static com.needoriginalname.infinitygauntlet.InfinityGuantletMod.proxy;

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
     * @param chainId the username of the block this was created by, if applicable
     */
    public BlockReplacementNode(IBlockState oldState, IBlockState newState, BlockPos pos, long distance, World w, @NotNull Integer chainId){
        this(oldState, newState, pos, distance, w, chainId, ConfigurationHandler.maxDepthOfBlockReplacement);

    }

    /**
     * @param oldState oldState to change from, null if from any state
     * @param newState New State to change to
     * @param pos Position of where it will occur
     * @param distance World tick to do this
     * @param w World that this action will occur on.
     * @param chainId the username of the block this was created by, if applicable
     * @param maxDepthOfBlockReplacement the Max depth that this will recurisvly look from each last one.
     */
    public BlockReplacementNode(IBlockState oldState, IBlockState newState, BlockPos pos, long distance, World w, @NotNull Integer chainId, short maxDepthOfBlockReplacement) {
        super(w, pos, distance, chainId);
        this.oldState = oldState;
        this.newState = newState;
        this.maxDepth = maxDepthOfBlockReplacement;
    }


    @Override
    public void doAction() {
        //get to see if something else already replaced this, if not replace it
        if((oldState != newState)
                && (oldState == null ||
                    getWorld().getBlockState(getBlockPos()) == oldState) ||

                    //if replacing a liquid, do it regardless of state
                    (oldState.getBlock().getMaterial() instanceof MaterialLiquid
                            && oldState.getBlock().getMaterial() == getWorld().getBlockState(getBlockPos()).getBlock().getMaterial()) ){

            if (oldState != null && oldState.getBlock() != null && oldState.getBlock().getMaterial() instanceof MaterialLiquid) {
                if (shouldStopBlockUpdate(oldState, newState)) {
                    EventListener.addStopBlockUpdateList(getWorld(), getBlockPos());
                }
            }

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
                    BlockReplacementNode node = new BlockReplacementNode(oldState, newState, pos, getNextTime(), getWorld(), getChainedId(), (short)(getGenerationsLeft() - 1) );
                    proxy.addDeferredAction(node);
                }

                poses.clear();
                poses.add(getBlockPos().up().north());
                poses.add(getBlockPos().up().south());
                poses.add(getBlockPos().up().east());
                poses.add(getBlockPos().up().west());
                poses.add(getBlockPos().down().north());
                poses.add(getBlockPos().down().south());
                poses.add(getBlockPos().down().east());
                poses.add(getBlockPos().down().west());

                for (BlockPos pos : poses) {
                    BlockReplacementNode node = new BlockReplacementNode(oldState, newState, pos, getNextTime() + 1, getWorld(), getChainedId(), (short)(getGenerationsLeft() - 1) );
                    proxy.addDeferredAction(node);
                }

            }
        } else {
            getWorld().notifyBlockOfStateChange(getBlockPos(), Blocks.air);
        }
    }

    private boolean shouldStopBlockUpdate(IBlockState oldState, IBlockState newState) {
        byte flag = ConfigurationHandler.stopBlockUpdateForLiquids;
        boolean b = false;
        if ((getGenerationsLeft() > 0) && ((flag & 1) == 1)){
            b = true;
        }

        if (getGenerationsLeft() == 0 && ((flag & 2) == 2)){
            b = true;
        }


        if (newState != Blocks.air || newState != null
                && oldState.getBlock().getMaterial() instanceof MaterialLiquid){
            return b;
        } else {
            return false;
        }
    }


    public short getGenerationsLeft() {
        return this.maxDepth;
    }
}
