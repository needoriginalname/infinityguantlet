package com.needoriginalname.infinitygauntlet.dimension;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.awt.*;

/**
 * Created by Al on 5/26/2015.
 */
public class WorldProviderSoulGem extends WorldProvider {




    /**
     * Gets the Sun Brightness for rendering sky.
     *
     * @param par1
     */
    @Override
    public float getSunBrightness(float par1) {
        return 0;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public boolean doesWaterVaporize() {
        return true;
    }

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    @Override
    public String getDimensionName() {
        return "Soul Gem Trap";
    }

    @Override
    public String getInternalNameSuffix() {
        return "_soulworld";
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
    @Override
    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(new BiomeGemSoulGem(ConfigurationHandler.soulGemBiomeID), 0.0F);
        this.dimensionId = ConfigurationHandler.soulGemDimensionID;
    }

    /**
     * Return Vec3D with biome specific fog color
     *
     * @param p_76562_1_
     * @param p_76562_2_
     */
    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        Color white = Color.BLACK;
        return new Vec3(white.getRed(), white.getGreen(), white.getBlue());
    }

    /**
     * Returns a new chunk provider which generates chunks for this world
     */
    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChuckProviderSoulGem(worldObj, worldObj.getSeed());
    }
}
