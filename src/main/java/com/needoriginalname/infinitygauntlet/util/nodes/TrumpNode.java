package com.needoriginalname.infinitygauntlet.util.nodes;

import com.sun.istack.internal.NotNull;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Owner on 10/10/2016.
 */
public class TrumpNode extends Node {
    private final int maxCount;

    public TrumpNode(World w, BlockPos pos, long distance, @NotNull Integer id) {
        this(w, pos, distance, id, 5000);
    }

    TrumpNode(World w, BlockPos pos, long distance, @NotNull Integer id, int maxCount) {
        super(w, pos, distance, id);
        this.maxCount = maxCount;

    }

    @Override
    public void doAction() {
        if (getWorld().getBlockState(getBlockPos()).getBlock() != Blocks.bedrock){
            getWorld().destroyBlock(getBlockPos(), false);
            getWorld().setBlockState(getBlockPos(), Blocks.bedrock.getDefaultState());


            if (maxCount > 0) {
                INode node = new TrumpNode(getWorld(), getBlockPos().west(), getNextTime(), getChainedId(), maxCount - 1);
                INode node2 = new TrumpNode(getWorld(), getBlockPos().east(), getNextTime(), getChainedId(), maxCount - 1);
                INode node3 = new TrumpNode(getWorld(), getBlockPos().down(), getNextTime(), getChainedId(), maxCount);
                INode node4 = new TrumpNode(getWorld(), getBlockPos().up(), getNextTime(), getChainedId(), maxCount);

                proxy.addDeferredAction(node);
                proxy.addDeferredAction(node2);
                if (node3.getBlockPos().getY() > 0) {
                    proxy.addDeferredAction(node3);
                }
                if (node4.getBlockPos().getY() < getWorld().getActualHeight()) {
                    proxy.addDeferredAction(node4);
                }
            }




        }


    }
}
