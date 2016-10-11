package com.needoriginalname.infinitygauntlet.items.GemStates;

import com.needoriginalname.infinitygauntlet.hander.ConfigurationHandler;
import com.needoriginalname.infinitygauntlet.util.nodes.BlockReplacementNode;
import com.needoriginalname.infinitygauntlet.util.nodes.EntityLivingSpawningNode;
import com.needoriginalname.infinitygauntlet.util.nodes.TrumpNode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static com.needoriginalname.infinitygauntlet.InfinityQuantletMod.proxy;

/**
 * Controls the Reality Gem's functionality
 *
 *
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

        Integer id = new Random().nextInt();

        if (blockstate != null && mop != null && mop.getBlockPos() != null){
            proxy.addDeferredAction(new BlockReplacementNode(worldIn.getBlockState(mop.getBlockPos()),
                    blockstate,
                    mop.getBlockPos(),
                    worldIn.getTotalWorldTime() + 1,
                    worldIn,
                    id));



        }

    }

    private boolean shenanigans(ItemStack stack, World worldIn, EntityPlayer playerIn, ItemStack stackInSlot) {
        if (stackInSlot != null && stackInSlot.getItem() == Items.name_tag) {
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
            String nameTagName = stackInSlot.getDisplayName();
            if (nameTagName.toLowerCase().equals("bluexephos")){
                BlueXephosShenanigan(worldIn, mop, playerIn);
                return true;
            } else if ((nameTagName.toLowerCase().equals("saberial") || (nameTagName.toLowerCase().equals("fiona")))){
                FionaShenanigan(worldIn, mop, playerIn);
                return true;
            } else if ((nameTagName.toLowerCase().equals("frostwave"))){
                FrostwaveShenanigan(worldIn, mop, playerIn);
                FrostwaveShenanigan(worldIn, mop, playerIn);
                return true;
            } else if ((nameTagName.toLowerCase().equals("trump"))){
                TrumpShenanigan(worldIn, mop);
                return true;
            }


        }

        return false;
    }

    private void FrostwaveShenanigan(World worldIn, MovingObjectPosition mop, EntityPlayer player){
        BlockPos pos = mop.getBlockPos().offset(mop.sideHit);

        Integer chainId = new Random().nextInt();

        EntityDragon dragon = new EntityDragon(worldIn);
        EntityWither wither = new EntityWither(worldIn);
        EntityDragon dragon2 = new EntityDragon(worldIn);
        EntityWither wither2 = new EntityWither(worldIn);
        wither.setCustomNameTag("Grumm");
        wither2.setCustomNameTag("Grumm");

        Queue<EntityLiving> q = new LinkedList<EntityLiving>();
        q.add(dragon);
        q.add(wither);
        q.add(dragon2);
        q.add(wither2);


        long t = worldIn.getTotalWorldTime();

        while (!q.isEmpty()){
            EntityLiving e = q.poll();
            e.setPosition(pos.getX() + 0.5d, pos.getY(), pos.getZ()+0.5d);
            t += 200;
            proxy.addDeferredAction(new EntityLivingSpawningNode(e, worldIn, pos, t, chainId));
        }

        for (int i = 0; i < 25; i++){
            EntityLiving e;
            int n = new Random().nextInt(5);
            if (n == 0){
                e = new EntityCreeper(worldIn);
            } else if (n == 1){
                e = new EntityBlaze(worldIn);
            } else if (n == 2){
                e = new EntityEnderman(worldIn);
            } else if (n == 3){
                e = new EntityGhast(worldIn);
            } else{
                e = new EntityRabbit(worldIn);
                ((EntityRabbit)e).setRabbitType(99);
            }

            if (new Random().nextInt(2) == 0)
                e.setCustomNameTag("Grumm");
            proxy.addDeferredAction(new EntityLivingSpawningNode(e, worldIn, pos, t, chainId));


        }



    }


    private void TrumpShenanigan(World w, MovingObjectPosition mop){
        Integer chainId = new Random().nextInt();
        proxy.addDeferredAction(new TrumpNode(w, mop.getBlockPos(), w.getTotalWorldTime() + 4, chainId));


    }

    private void FionaShenanigan(World worldIn, MovingObjectPosition mop, EntityPlayer player) {
        BlockPos pos = mop.getBlockPos();
        Integer chainId = new Random().nextInt();
        long timeToAdd = 0;
        //add rainbow blocks
        for (int x = 0; x < 16; ++x){
            int offsetX = x % 2 == 0 ? MathHelper.floor_double(x/2) + 1 : MathHelper.floor_double(x/2);
            // related to the color values to appear, where 0 is first, seocnd is 1 left, 3rd 1 right, 4th 2 left, etc
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

                BlockReplacementNode node = new BlockReplacementNode(null, state,  currentPos, priority, worldIn, chainId, (short)0);
                BlockReplacementNode node2 = new BlockReplacementNode(null, Blocks.air.getDefaultState(),  currentPos.up(), priority, worldIn, chainId, (short) 0);
                BlockReplacementNode node3 = new BlockReplacementNode(null, Blocks.air.getDefaultState(),  currentPos.up(2), priority, worldIn, chainId, (short)0);

                proxy.addDeferredAction(node);
                proxy.addDeferredAction(node2);
                proxy.addDeferredAction(node3);

            }
        }

        BlockPos topLeft = pos.up().west(8).north(7);
        BlockPos topRight = pos.up().east(7).south(8);
        timeToAdd += 5;
        IBlockState state = Blocks.oak_fence.getDefaultState();
        for (int i = 0; i < 16; ++i){
            BlockReplacementNode node1 = new BlockReplacementNode(null, state,  topLeft.east(i), timeToAdd, worldIn, chainId, (short)0);
            BlockReplacementNode node2 = new BlockReplacementNode(null, state,  topLeft.south(i), timeToAdd, worldIn, chainId, (short) 0);
            BlockReplacementNode node3 = new BlockReplacementNode(null, state,  topRight.west(i), timeToAdd, worldIn, chainId, (short)0);
            BlockReplacementNode node4 = new BlockReplacementNode(null, state,  topRight.north(i), timeToAdd, worldIn, chainId, (short)0);
            proxy.addDeferredAction(node1);
            proxy.addDeferredAction(node2);
            proxy.addDeferredAction(node3);
            proxy.addDeferredAction(node4);
            ++timeToAdd;


        }


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
            EntityLivingSpawningNode node = new EntityLivingSpawningNode(sheep, worldIn, spawnSheepArea, timeToAdd, chainId);
            proxy.addDeferredAction(node);

        }

        //proxy.addDeferredAction(worldIn, blockQueue);
    }

    private void BlueXephosShenanigan(World worldIn, MovingObjectPosition mop, EntityPlayer player) {
        BlockPos pos = mop.getBlockPos().offset(mop.sideHit);
        BlockTorch torchBlock = (BlockTorch) Block.getBlockFromName("torch");
        if (torchBlock.canPlaceBlockAt(worldIn, pos)){
            IBlockState state = torchBlock.getDefaultState().withProperty(BlockTorch.FACING, mop.sideHit);
            BlockReplacementNode node = new BlockReplacementNode(null, state, pos, worldIn.getTotalWorldTime() + 2, worldIn, new Random().nextInt());
            proxy.addDeferredAction(node);
            //proxy.addDeferredAction(worldIn, queue);
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
