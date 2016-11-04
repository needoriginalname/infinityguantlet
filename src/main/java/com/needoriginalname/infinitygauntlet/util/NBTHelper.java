package com.needoriginalname.infinitygauntlet.util;

import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Al on 5/3/2015.
 */
public class NBTHelper
{


    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack
     *         The ItemStack for which its NBT Tag Compound is being checked for initialization
     */
    private static NBTTagCompound getModLevelTags(ItemStack itemStack)
    {
        if (itemStack.getTagCompound() == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        if (!itemStack.getTagCompound().hasKey(Reference.MODID)){
            itemStack.getTagCompound().setTag(Reference.MODID, new NBTTagCompound());
        }

        return itemStack.getTagCompound().getCompoundTag(Reference.MODID);
    }

    public static void setLong(ItemStack itemStack, String keyName, long keyValue)
    {
        getModLevelTags(itemStack).setLong(keyName, keyValue);
    }

    // String
    public static String getString(ItemStack itemStack, String keyName)
    {
        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setString(itemStack, keyName, "");
        }

        return getModLevelTags(itemStack).getString(keyName);
    }

    public static void setString(ItemStack itemStack, String keyName, String keyValue)
    {
        getModLevelTags(itemStack).setString(keyName, keyValue);
    }

    // boolean
    public static boolean getBoolean(ItemStack itemStack, String keyName)
    {


        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setBoolean(itemStack, keyName, false);
        }

        return getModLevelTags(itemStack).getBoolean(keyName);
    }

    public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue)
    {
        getModLevelTags(itemStack).setBoolean(keyName, keyValue);
    }

    // byte
    public static byte getByte(ItemStack itemStack, String keyName)
    {
        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setByte(itemStack, keyName, (byte) 0);
        }

        return getModLevelTags(itemStack).getByte(keyName);
    }

    public static void setByte(ItemStack itemStack, String keyName, byte keyValue)
    {
        getModLevelTags(itemStack).setByte(keyName, keyValue);
    }

    // short
    public static short getShort(ItemStack itemStack, String keyName)
    {
        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setShort(itemStack, keyName, (short) 0);
        }

        return getModLevelTags(itemStack).getShort(keyName);
    }

    public static void setShort(ItemStack itemStack, String keyName, short keyValue)
    {
        getModLevelTags(itemStack).setShort(keyName, keyValue);
    }

    // int
    public static int getInt(ItemStack itemStack, String keyName)
    {

        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setInteger(itemStack, keyName, 0);
        }

        return getModLevelTags(itemStack).getInteger(keyName);
    }

    public static void setInteger(ItemStack itemStack, String keyName, int keyValue)
    {
        getModLevelTags(itemStack).setInteger(keyName, keyValue);
    }

    // long
    public static long getLong(ItemStack itemStack, String keyName)
    {
        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setLong(itemStack, keyName, 0);
        }

        return getModLevelTags(itemStack).getLong(keyName);
    }

    // float
    public static float getFloat(ItemStack itemStack, String keyName)
    {
        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setFloat(itemStack, keyName, 0);
        }

        return getModLevelTags(itemStack).getFloat(keyName);
    }

    public static void setFloat(ItemStack itemStack, String keyName, float keyValue)
    {
        getModLevelTags(itemStack).setFloat(keyName, keyValue);
    }

    // double
    public static double getDouble(ItemStack itemStack, String keyName)
    {

        if (!getModLevelTags(itemStack).hasKey(keyName))
        {
            setDouble(itemStack, keyName, 0);
        }

        return getModLevelTags(itemStack).getDouble(keyName);
    }

    public static void setDouble(ItemStack itemStack, String keyName, double keyValue)
    {
        getModLevelTags(itemStack).setDouble(keyName, keyValue);
    }
}