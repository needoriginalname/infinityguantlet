package com.needoriginalname.infinitygauntlet.util;

/**
 * Created by Al on 5/13/2015.
 */
public class MiscUtil {
    public static boolean InRange(int value, int min, int max){
        return (value >= min && value <= max);
    }
    public static boolean InRange(float value, float min, float max){
        return (value >= min && value <= max);
    }
    public static boolean InRange(double value, double min, double max){
        return (value >= min && value <= max);
    }
}
