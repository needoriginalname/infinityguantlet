package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.util.AnimalNode;
import com.needoriginalname.infinitygauntlet.util.BlockSearchHandler;
import com.needoriginalname.infinitygauntlet.util.BlockNode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Created by Al on 5/16/2015.
 */
public class StateRealityGem extends AbstractGemState{




    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {


        IBlockState blockstate = null;
        for (int i = 0; i < 9; ++i){
            if (playerIn.inventory.getStackInSlot(i) == stack){
                ItemStack stackInSlot = playerIn.inventory.getStackInSlot(++i);
                if (stackInSlot != null && stackInSlot.getItem() instanceof ItemBlock){
                    //copy block state
                    int meta = stackInSlot.getMetadata();
                    Block b = ((ItemBlock) stackInSlot.getItem()).getBlock();
                    blockstate = b.getStateFromMeta(meta);


                    break;
                } else {
                    if (!shenanigans(stack, worldIn, playerIn, stackInSlot))
                        blockstate = Blocks.air.getDefaultState();
                    }

                }
        }
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

        if (blockstate != null && mop != null && mop.getBlockPos() != null){
            PriorityQueue<BlockNode> queue = new BlockSearchHandler().getBlocks(worldIn, mop.getBlockPos(), blockstate, 50, 10000);
            proxy.addDeferredBlockReplacement(worldIn, queue);
        }
/*
        EntityLivingBase entity = this.GetTargetEntityLiving(worldIn, playerIn, ConfigurationHandler.seekRangeForEntities);
        if (entity instanceof EntityPlayer){
            EntityPlayer otherPlayer = (EntityPlayer) entity;
            if (otherPlayer.capabilities.isCreativeMode){
                otherPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
            }
        }

        super.onPlayerStoppedUsing(stack, worldIn, playerIn, timeLeft);
        */
    }

    private boolean shenanigans(ItemStack stack, World worldIn, EntityPlayer playerIn, ItemStack stackInSlot) {
        if (stackInSlot != null && stackInSlot.getItem() == Items.name_tag) {
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
            String nameTagName = stackInSlot.getDisplayName();
            if (nameTagName.toLowerCase().equals("bluexephos")){
                BlueXephosShenanigan(worldIn, mop);
                return true;
            } else if ((nameTagName.toLowerCase().equals("saberial") || (nameTagName.toLowerCase().equals("fiona")))){
                FionaShenanigan(worldIn, mop);
                return true;
            } else if ((nameTagName.toLowerCase().equals("frostwave"))){
                FrostwaveShenanigan(worldIn, mop);
                return true;
            }


        }

        return false;
    }

    private void FrostwaveShenanigan(World worldIn, MovingObjectPosition mop){
        BlockPos pos = mop.getBlockPos().offset(mop.sideHit);

        PriorityQueue<AnimalNode> queue = new PriorityQueue<AnimalNode>();

        EntityDragon dragon = new EntityDragon(worldIn);
        EntityWither wither = new EntityWither(worldIn);
        EntityDragon dragon2 = new EntityDragon(worldIn);
        EntityWither wither2 = new EntityWither(worldIn);

        dragon2.setCustomNameTag("Grumm");
        wither2.setCustomNameTag("Grumm");

        Queue<Entity> q = new LinkedList<Entity>();
        q.add(dragon);
        q.add(wither);
        q.add(dragon2);
        q.add(wither2);


        long t = worldIn.getTotalWorldTime();

        while (!q.isEmpty()){
            Entity e = q.poll();
            e.setPosition(pos.getX() + 0.5d, pos.getY(), pos.getZ()+0.5d);
            t += 200;
            queue.add(new AnimalNode().setPos(pos).setEntity(e).setDistance(t));
        }

        proxy.addDeferredSpawning(worldIn, queue);

    }

    private void FionaShenanigan(World worldIn, MovingObjectPosition mop) {
        BlockPos pos = mop.getBlockPos();
        PriorityQueue<BlockNode> blockQueue = new PriorityQueue<BlockNode>();
        long timeToAdd = 0;
        //add rainbow blocks
        for (int x = 0; x < 16; ++x){
            int offsetX = x % 2 == 0 ? MathHelper.floor_double(x/2) + 1 : MathHelper.floor_double(x/2);
            int[] metadatas = {7, 8, 6, 9, 5, 10, 4, 11, 3, 12, 2, 13, 1, 14, 0, 15};
            EnumFacing facingX = x % 2 == 0 ? EnumFacing.WEST : EnumFacing.EAST;
            for (int z = 0; z < 16; z++) {
                int offsetZ = z % 2 == 0 ? MathHelper.floor_double(z/2) + 1 : MathHelper.floor_double(z/2);
                EnumFacing facingZ = z % 2 == 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
                IBlockState state = Blocks.wool.getStateFromMeta(metadatas[x]);
                BlockPos currentPos = pos.offset(facingX, offsetX).offset(facingZ, offsetZ);
                long priority = worldIn.getTotalWorldTime() + MathHelper.floor_double(currentPos.distanceSq(pos));

                //used to determine when to add continue
                if (timeToAdd < priority) timeToAdd = priority;

                BlockNode node = new BlockNode().setBlockState(state).setPos(currentPos).setDistance(priority);
                BlockNode node2 = new BlockNode().setBlockState(null).setPos(currentPos.up()).setDistance(priority);
                BlockNode node3 = new BlockNode().setBlockState(null).setPos(currentPos.up(2)).setDistance(priority);
                blockQueue.add(node);
                blockQueue.add(node2);
                blockQueue.add(node3);

            }
        }

        BlockPos topLeft = pos.up().west(8).north(7);
        BlockPos topRight = pos.up().east(7).south(8);
        timeToAdd += 5;
        IBlockState state = Blocks.oak_fence.getDefaultState();
        for (int i = 0; i < 16; ++i){
            BlockPos a = topLeft.east(i);
            BlockPos b = topLeft.south(i);
            BlockPos c = topRight.west(i);
            BlockPos d = topRight.north(i);
            blockQueue.add(new BlockNode().setDistance(timeToAdd).setPos(a).setBlockState(state));
            blockQueue.add(new BlockNode().setDistance(timeToAdd).setPos(b).setBlockState(state));
            blockQueue.add(new BlockNode().setDistance(timeToAdd).setPos(c).setBlockState(state));
            blockQueue.add(new BlockNode().setDistance(timeToAdd).setPos(d).setBlockState(state));



            ++timeToAdd;


        }

        PriorityQueue<AnimalNode> animalQueue = new PriorityQueue<AnimalNode>();
        BlockPos spawnSheepAreaBase = pos.up();
        Random r = new Random();
        for (EnumDyeColor color : EnumDyeColor.values()) {
            EntitySheep sheep = new EntitySheep(worldIn);
            BlockPos spawnSheepArea = spawnSheepAreaBase.offset(EnumFacing.SOUTH, MathHelper.getRandomIntegerInRange(r, -5, 5)).offset(EnumFacing.WEST, MathHelper.getRandomIntegerInRange(r, -5, 5));
            sheep.setPosition(spawnSheepArea.getX(), spawnSheepArea.getY(), spawnSheepArea.getZ());
            sheep.setFleeceColor(color);
            sheep.setCustomNameTag("jeb_");
            sheep.setAlwaysRenderNameTag(false);
            // 1 second after building, add sheep every 1/4 second
            timeToAdd += 5;
            animalQueue.add(new AnimalNode().setEntity(sheep).setPos(spawnSheepArea).setDistance(timeToAdd ));

        }

        proxy.addDeferredBlockReplacement(worldIn, blockQueue);
        proxy.addDeferredSpawning(worldIn, animalQueue);
    }

    private void BlueXephosShenanigan(World worldIn, MovingObjectPosition mop) {
        BlockPos pos = mop.getBlockPos().offset(mop.sideHit);
        BlockTorch torchBlock = (BlockTorch) Block.getBlockFromName("torch");
        if (torchBlock.canPlaceBlockAt(worldIn, pos)){
            PriorityQueue<BlockNode> queue = new PriorityQueue<BlockNode>();
            IBlockState state = torchBlock.getDefaultState().withProperty(BlockTorch.FACING, mop.sideHit);
            queue.add(new BlockNode().setDistance(worldIn.getTotalWorldTime()+2).setPos(pos).setBlockState(state));
            proxy.addDeferredBlockReplacement(worldIn, queue);
        }
    }

    public boolean isGauntletTypeEnabled() {
        return ConfigurationHandler.isRealityGauntletGemEnabled;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

        if (ConfigurationHandler.isRealityGemEnabled) {
            if (playerIn.isSneaking()) {

                worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "random.levelup", 1, 1);
                if (playerIn.capabilities.isCreativeMode) {
                    playerIn.setGameType(WorldSettings.GameType.SURVIVAL);
                } else {
                    playerIn.setGameType(WorldSettings.GameType.CREATIVE);
                }
            }
        }

        return super.onItemRightClick(itemStackIn, worldIn, playerIn);
    }
}
