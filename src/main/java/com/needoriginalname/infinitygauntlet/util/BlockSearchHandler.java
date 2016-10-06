package com.needoriginalname.infinitygauntlet.util;

import com.needoriginalname.infinitygauntlet.util.nodes.BlockNode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Owner on 9/29/2016.
 */


public class BlockSearchHandler {

    public PriorityQueue<BlockNode> getBlocks(World w, BlockPos pos, IBlockState newBlock, int maxDepth, int maxBlocks){



        HashMap<BlockPos, Integer> searchedBlocks;

        searchedBlocks =  searchBlocks(maxDepth, maxBlocks, w, pos);
        PriorityQueue<BlockNode> result = processBlocks(w, searchedBlocks, newBlock);

        return result;
    }

    private PriorityQueue<BlockNode> processBlocks(World w, HashMap<BlockPos, Integer> searchedBlocks, IBlockState newBlock) {
        PriorityQueue<BlockNode> queue = new PriorityQueue<BlockNode>();
        PriorityQueue<BlockNode> result = new PriorityQueue<BlockNode>();
        //takes the searched blocks out and them in a PriorityQueue
        for (BlockPos b : searchedBlocks.keySet()) {
            queue.add(new BlockNode().setPos(b).setDistance(searchedBlocks.get(b)).setBlockState(newBlock));
        }

        //sets what time to remove block
        long n = w.getTotalWorldTime();
        while (!queue.isEmpty()){
            BlockNode node = queue.poll();
            result.add(node.setDistance(n+node.getDistance()+1));//++n));
        }
        return result;
    }

    private HashMap<BlockPos, Integer> searchBlocks(int maxDepth, int maxBlocks, World w, BlockPos pos) {
        IBlockState startingState = w.getBlockState(pos);
        Queue<BlockPos> currentLevel = new LinkedList<BlockPos>();
        Queue<BlockPos> nextLevel = new LinkedList<BlockPos>();
        HashMap<BlockPos, Integer> searchedBlocks = new HashMap<BlockPos, Integer>();

        currentLevel.add(pos);
        searchedBlocks.put(pos, 0);
        int currentDepth = 0;
        int currentNBlocks = 0;
        while (currentDepth <= maxDepth){

            if (currentLevel.isEmpty()){
                // goes to next level
                ++currentDepth;
                if (!nextLevel.isEmpty()) {
                    currentLevel = nextLevel;
                    nextLevel = new LinkedList<BlockPos>();
                } else {
                    //stops if there is no next level
                    break;
                }
            } else {

                //Processes and finds new blocks poses
                BlockPos currentPos = currentLevel.poll();
                LinkedList<BlockPos> list = new LinkedList<BlockPos>();
                list.add(currentPos.up());
                list.add(currentPos.down());
                list.add(currentPos.north());
                list.add(currentPos.south());
                list.add(currentPos.west());
                list.add(currentPos.east());
                list.add(currentPos.up().north());
                list.add(currentPos.up().south());
                list.add(currentPos.up().west());
                list.add(currentPos.up().east());
                list.add(currentPos.down().north());
                list.add(currentPos.down().south());
                list.add(currentPos.down().west());
                list.add(currentPos.down().east());


                for (BlockPos nextPos: list) {
                    //checks to make sure it s block it can replace
                    if (w.getBlockState(nextPos).equals(startingState) && currentNBlocks < maxBlocks && !searchedBlocks.containsKey(nextPos)){
                        ++currentNBlocks;
                        searchedBlocks.put(nextPos, currentDepth);
                        nextLevel.add(nextPos);
                    }
                }
            }
        }
        return searchedBlocks;
    }


}
