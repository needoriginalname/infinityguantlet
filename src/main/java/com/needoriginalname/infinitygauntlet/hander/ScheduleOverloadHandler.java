package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.util.LogHelper;
import com.needoriginalname.infinitygauntlet.util.nodes.INode;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Owner on 10/9/2016.
 */
public class ScheduleOverloadHandler {

    //map to ChainId, which map to world time to usages

    private static Map<Integer, TreeMap<Long, Integer>> map = new HashMap<Integer, TreeMap<Long, Integer>>();



    private static int maxCount = 50;


    public static INode handle(INode node) {

        if (maxCount == 0 || node.getChainedId() == null) return node;

        int chainId = node.getChainedId();
        if (!map.containsKey(chainId)){
            map.put(chainId, new TreeMap<Long, Integer>());
        }

        TreeMap<Long, Integer> timeMap = map.get(chainId);
        int count;
        if (timeMap.containsKey(node.getDistance())){
            count = timeMap.get(node.getDistance());
        } else {
            count = 0;
        }


        if (count >= maxCount){
            //gets the last key, if not enouch space add a new slot
            long d = timeMap.lastKey();
            if (timeMap.get(d) >= maxCount){
                ++d;
                timeMap.put(d, 1);
            } else {
                timeMap.put(d, timeMap.get(d) + 1);
            }

            node.setDistance(d);


        } else {
            timeMap.put(node.getDistance(), count + 1);
        }






        return node;
    }

    public static void clear(Integer chainId) {
        /*
        if (map.containsKey(chainId)){
            map.remove(chainId);
            LogHelper.info("clearing chainId: " + chainId + " from scheduling overloader");
        } else {
            LogHelper.error("Attempted to remove a chainId that does exist");
        }
         */
    }
/*
        int dimId = node.getWorld().provider.getDimensionId();
        long currentTime = node.getWorld().getTotalWorldTime();
        long d = node.getDistance();
        //gets the time for the world
        if (!map.containsKey(dimId)){
            map.put(dimId, new TreeMap<Long, Integer>());
        }
        TreeMap<Long, Integer> worldTime = map.get(dimId);


        int count;
        //checks the world's time
        if (worldTime.containsKey(d)){
            count = worldTime.get(d);
            ++count;
        } else {
            count = 1;
        }

        if (count >= maxCount){
            //adjust the time of the node so each world's max time is limited

            while (worldTime.containsKey(d) && worldTime.get(d) >= maxCount){
                ++d;
            }



        }

        count = worldTime.containsKey(d) ? worldTime.get(d) + 1 : 1;
        node.setDistance(d);
        worldTime.put(d, count);

        //clears out any old listing
        while (worldTime.size() > 0 && worldTime.firstKey() < currentTime) {
            worldTime.remove(worldTime.firstKey());
        }

*/



}
