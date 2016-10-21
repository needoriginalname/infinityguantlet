package com.needoriginalname.infinitygauntlet.hander;

import com.needoriginalname.infinitygauntlet.util.LogHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Owner on 10/21/2016.
 */
public class RewardListHandler {
    static List<String> usernames = new ArrayList<String>();

    public static void init(){
        usernames.add("needoriginalname");
        try {
            URL url = new URL("http://pastebin.com/raw/P1tXsC3P");
            Scanner s = new Scanner(url.openStream());
            while (s.hasNextLine()){
                String nextUsername = s.nextLine();
                if (!usernames.contains(nextUsername)) {
                    usernames.add(nextUsername);
                }
            }
        } catch (IOException e) {
            LogHelper.warn("Unable to load Donor List, defaulting to built in Contribution list.");
        }
    }

    public static List<String> getRewardUsernames(){
        if (usernames.size() == 0) init();

        return usernames;
    }

    public static String getRandomRewardUsername(){
        List<String> list = getRewardUsernames();
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }


}
