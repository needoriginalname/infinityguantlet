package com.needoriginalname.infinitygauntlet.dimension;

import com.needoriginalname.infinitygauntlet.blocks.ModBlocks;
import net.minecraft.world.biome.BiomeGenBase;

import java.awt.*;

/**
 * Created by Al on 5/26/2015.
 */
public class BiomeGemSoulGem extends BiomeGenBase {
    public BiomeGemSoulGem(int p_i1971_1_) {
        super(p_i1971_1_);
        this.minHeight = 0.1F;
        this.maxHeight = 0.6F;
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.topBlock = ModBlocks.blockSoulGemTrap.getDefaultState();
        this.fillerBlock = ModBlocks.blockSoulGemTrap.getDefaultState();
        this.setBiomeName("SoulGem");

        this.waterColorMultiplier = 0xFFFFFF;
        this.setDisableRain();

    }

    /**
     * takes temperature, returns color
     *
     * @param p_76731_1_
     */
    @Override
    public int getSkyColorByTemp(float p_76731_1_) {
        return Color.black.getRGB();
    }


}
