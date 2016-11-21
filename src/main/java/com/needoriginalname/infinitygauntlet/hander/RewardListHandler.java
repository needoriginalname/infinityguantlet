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
    static List<String> usernamesLC = new ArrayList<String>();

    public static boolean loadList(){
        usernames = new ArrayList<String>();
        usernamesLC = new ArrayList<String>();
        usernames.add("needoriginalname"); //mod ev
        usernames.add("Geenium"); //created the textures and models
        usernames.add("nanosounds"); //inspiration for mod and doing first video
        usernames.add("lividcoffee"); //for being duncan.
        try {
            URL url = new URL("http://pastebin.com/raw/P1tXsC3P");
            Scanner s = new Scanner(url.openStream());
            while (s.hasNextLine()){
                String nextUsername = s.nextLine();
                if (!usernames.contains(nextUsername)) {
                    usernames.add(nextUsername);
                }



            }
            for (String name: usernames) {
                usernamesLC.add(name.toLowerCase());
            }

            System.out.print(usernamesLC);
            return true;
        } catch (IOException e) {
            LogHelper.warn("Unable to load Donor List, defaulting to built in Contribution list.");
        }

        for (String name: usernames) {
            usernamesLC.add(name.toLowerCase());
        }
        return false;
    }

    public static List<String> getRewardUsernames(){
        if (usernamesLC.size() == 0) loadList();

        return usernamesLC;
    }

    public static String getRandomRewardUsername(){
        if (usernames.size() == 0) loadList();
        List<String> list = usernames;
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }
}
