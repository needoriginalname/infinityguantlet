package com.needoriginalname.infinitygauntlet.dimension;

import com.needoriginalname.infinitygauntlet.blocks.BlockSoulGemTrap;
import com.needoriginalname.infinitygauntlet.blocks.ModBlocks;
import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Al on 5/26/2015.
 */
public class ChuckProviderSoulGem extends ChunkProviderGenerate implements IChunkProvider {

    //private final String settings = "{"coordinateScale":3000.0,"heightScale":6000.0,"lowerLimitScale":512.0,"upperLimitScale":250.0,"depthNoiseScaleX":200.0,"depthNoiseScaleZ":200.0,"depthNoiseScaleExponent":0.5,"mainNoiseScaleX":80.0,"mainNoiseScaleY":160.0,"mainNoiseScaleZ":80.0,"baseSize":8.5,"stretchY":10.0,"biomeDepthWeight":1.0,"biomeDepthOffset":0.0,"biomeScaleWeight":1.0,"biomeScaleOffset":0.0,"seaLevel":63,"useCaves":false,"useDungeons":false,"dungeonChance":1,"useStrongholds":false,"useVillages":false,"useMineShafts":false,"useTemples":false,"useMonuments":false,"useRavines":false,"useWaterLakes":false,"waterLakeChance":1,"useLavaLakes":false,"lavaLakeChance":10,"useLavaOceans":false,"fixedBiome":1,"biomeSize":1,"riverSize":3,"dirtSize":33,"dirtCount":10,"dirtMinHeight":0,"dirtMaxHeight":256,"gravelSize":33,"gravelCount":8,"gravelMinHeight":0,"gravelMaxHeight":256,"graniteSize":33,"graniteCount":10,"graniteMinHeight":0,"graniteMaxHeight":80,"dioriteSize":33,"dioriteCount":10,"dioriteMinHeight":0,"dioriteMaxHeight":80,"andesiteSize":33,"andesiteCount":10,"andesiteMinHeight":0,"andesiteMaxHeight":80,"coalSize":1,"coalCount":0,"coalMinHeight":0,"coalMaxHeight":128,"ironSize":1,"ironCount":0,"ironMinHeight":0,"ironMaxHeight":64,"goldSize":1,"goldCount":0,"goldMinHeight":0,"goldMaxHeight":32,"redstoneSize":1,"redstoneCount":0,"redstoneMinHeight":0,"redstoneMaxHeight":16,"diamondSize":1,"diamondCount":0,"diamondMinHeight":0,"diamondMaxHeight":16,"lapisSize":1,"lapisCount":0,"lapisCenterHeight":16,"lapisSpread":16}"
    private final String settings = "{\"coordinateScale\":684.412,\"heightScale\":684.412,\"lowerLimitScale\":512.0,\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0,\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0,\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0,\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":1,\"useCaves\":false,\"useDungeons\":false,\"dungeonChance\":1,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useMonuments\":false,\"useRavines\":false,\"useWaterLakes\":false,\"waterLakeChance\":1,\"useLavaLakes\":false,\"lavaLakeChance\":10,\"useLavaOceans\":false,\"fixedBiome\":0,\"biomeSize\":1,\"riverSize\":1,\"dirtSize\":1,\"dirtCount\":0,\"dirtMinHeight\":0,\"dirtMaxHeight\":0,\"gravelSize\":1,\"gravelCount\":0,\"gravelMinHeight\":0,\"gravelMaxHeight\":0,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":1,\"coalCount\":0,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":1,\"ironCount\":0,\"ironMinHeight\":0,\"ironMaxHeight\":0,\"goldSize\":1,\"goldCount\":0,\"goldMinHeight\":0,\"goldMaxHeight\":0,\"redstoneSize\":1,\"redstoneCount\":0,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":0,\"diamondSize\":1,\"diamondCount\":0,\"diamondMinHeight\":0,\"diamondMaxHeight\":0,\"lapisSize\":1,\"lapisCount\":0,\"lapisCenterHeight\":0,\"lapisSpread\":0}";
    ChuckProviderSoulGem(World world, long seed) {
        super(world, seed,false, "{\"coordinateScale\":684.412,\"heightScale\":684.412,\"lowerLimitScale\":512.0,\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0,\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0,\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0,\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":1,\"useCaves\":false,\"useDungeons\":false,\"dungeonChance\":1,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useMonuments\":false,\"useRavines\":false,\"useWaterLakes\":false,\"waterLakeChance\":1,\"useLavaLakes\":false,\"lavaLakeChance\":10,\"useLavaOceans\":false,\"fixedBiome\":0,\"biomeSize\":1,\"riverSize\":1,\"dirtSize\":1,\"dirtCount\":0,\"dirtMinHeight\":0,\"dirtMaxHeight\":0,\"gravelSize\":1,\"gravelCount\":0,\"gravelMinHeight\":0,\"gravelMaxHeight\":0,\"graniteSize\":1,\"graniteCount\":0,\"graniteMinHeight\":0,\"graniteMaxHeight\":0,\"dioriteSize\":1,\"dioriteCount\":0,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":1,\"andesiteCount\":0,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":1,\"coalCount\":0,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":1,\"ironCount\":0,\"ironMinHeight\":0,\"ironMaxHeight\":0,\"goldSize\":1,\"goldCount\":0,\"goldMinHeight\":0,\"goldMaxHeight\":0,\"redstoneSize\":1,\"redstoneCount\":0,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":0,\"diamondSize\":1,\"diamondCount\":0,\"diamondMinHeight\":0,\"diamondMaxHeight\":0,\"lapisSize\":1,\"lapisCount\":0,\"lapisCenterHeight\":0,\"lapisSpread\":0}");
        /*super(world, seed, true, "3;minecraft:bedrock,2*" +
                Reference.MODID + ":" + Names.SoulGemTrap + ";" + l.soulGemBiomeID + ";"); */
    }







}
