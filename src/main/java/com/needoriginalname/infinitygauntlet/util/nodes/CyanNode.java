package com.needoriginalname.infinitygauntlet.util.nodes;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import static com.needoriginalname.infinitygauntlet.InfinityGuantletMod.proxy;

/**
 * Created by Owner on 12/8/2016.
 */
public class CyanNode extends Node {
    private final BlockPos base;
    private final EnumFacing xDir;
    private final EnumFacing yDir;


    /**
     *  xLRU
     *  x: Unused
     *  L: create nodes to the left of origin
     *  R: creates nodes to the right of origin
     *  U: creates nodes above the origin
     */
    private final byte flag;
    private final byte UP_FLAG = 1;
    private final byte LEFT_FLAG = 4;
    private final byte RIGHT_FLAG = 2;
    private final byte ALL_FLAG = UP_FLAG + LEFT_FLAG + RIGHT_FLAG;

    private EnumFacing getNextFacingAngle(EnumFacing f){

        EnumFacing r = EnumFacing.NORTH;
        if (f == EnumFacing.NORTH){
            r = EnumFacing.EAST;
        } else if (f == EnumFacing.EAST){
            r = EnumFacing.SOUTH;
        } else if (f == EnumFacing.SOUTH){
            r = EnumFacing.WEST;
        } else if (f == EnumFacing.WEST){
            r = EnumFacing.NORTH;
        }
        return r;
    }

    public CyanNode(World w, BlockPos pos, long distance, @NotNull Integer id, EnumFacing side, EntityPlayer player) {
        super(w, pos, distance, id);
        this.base = pos;

        if (side == EnumFacing.DOWN){
            yDir = player.getHorizontalFacing().getOpposite();
            xDir = this.getNextFacingAngle(yDir);
        } else if (side == EnumFacing.UP){
            yDir = player.getHorizontalFacing();
            xDir = this.getNextFacingAngle(yDir).getOpposite();
        } else {
            yDir = EnumFacing.UP;
            xDir = getNextFacingAngle(player.getHorizontalFacing());
        }
        this.flag = (byte) 7;

    }


    public CyanNode(World w, BlockPos pos, long distance, Integer id, EnumFacing xDir, EnumFacing yDir, BlockPos base, byte flag) {
        super(w, pos, distance, id);
        this.xDir = xDir;
        this.yDir = yDir;
        this.base = base;
        this.flag = flag;
    }


    private enum BlocksToUse {
        GREEN_WOOL(Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN)),
        WOOD(Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.NONE)),
        CYAN_WOOL(Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.CYAN)),
        BLUE_WOOL(Blocks.wool.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE)),
        NONE(Blocks.air.getDefaultState());

        private final IBlockState state;
        public IBlockState getState(){
            return state;
        }
        BlocksToUse(IBlockState state){
            this.state = state;
        }

        
    };

    BlocksToUse[][]blocksToMake = new BlocksToUse[][]{
            {BlocksToUse.NONE,       BlocksToUse.NONE,        BlocksToUse.CYAN_WOOL,   BlocksToUse.CYAN_WOOL,   BlocksToUse.NONE,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.CYAN_WOOL,   BlocksToUse.BLUE_WOOL,   BlocksToUse.CYAN_WOOL,   BlocksToUse.CYAN_WOOL,   BlocksToUse.CYAN_WOOL},
            {BlocksToUse.CYAN_WOOL,  BlocksToUse.CYAN_WOOL,   BlocksToUse.BLUE_WOOL,   BlocksToUse.CYAN_WOOL,   BlocksToUse.BLUE_WOOL,   BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.CYAN_WOOL,   BlocksToUse.CYAN_WOOL,   BlocksToUse.BLUE_WOOL,   BlocksToUse.BLUE_WOOL,   BlocksToUse.CYAN_WOOL},
            {BlocksToUse.NONE,       BlocksToUse.CYAN_WOOL,   BlocksToUse.GREEN_WOOL,  BlocksToUse.GREEN_WOOL,  BlocksToUse.NONE,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.NONE,        BlocksToUse.WOOD,        BlocksToUse.NONE,        BlocksToUse.NONE,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.WOOD,        BlocksToUse.GREEN_WOOL,  BlocksToUse.NONE,        BlocksToUse.NONE,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.NONE,        BlocksToUse.GREEN_WOOL,  BlocksToUse.GREEN_WOOL,  BlocksToUse.NONE,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.NONE,        BlocksToUse.NONE,        BlocksToUse.GREEN_WOOL,  BlocksToUse.WOOD,        BlocksToUse.NONE},
            {BlocksToUse.NONE,       BlocksToUse.NONE,        BlocksToUse.NONE,        BlocksToUse.GREEN_WOOL,  BlocksToUse.NONE,        BlocksToUse.NONE},
    };
    @Override
    public void doAction() {
        int MULT = 8;

        //gets the axis for array
        EnumFacing.Axis yAxis = yDir.getAxis();
        EnumFacing.Axis xAxiz = xDir.getAxis();

        //gets offset
        int yOffset = 9 - getAxisDifference(yAxis, base, getBlockPos()) / MULT;
        int xOffSet = 6 - (b(xDir, base, getBlockPos()) ? (3 + (getAxisDifference(xAxiz, base, getBlockPos()) / MULT)) :
                (3 - (getAxisDifference(xAxiz, base, getBlockPos()) / MULT) - 1));

        //adjust xOffset for the -1

        if (yOffset <= 9 && xOffSet <= 5 && yOffset >= 0 && xOffSet >= 0){
            BlocksToUse s = blocksToMake[yOffset][xOffSet];
            IBlockState state = s.getState();

            if (state.getBlock() != Blocks.air) {
                getWorld().setBlockState(getBlockPos(), state);
            }

            if ((flag & UP_FLAG) == UP_FLAG) proxy.addDeferredAction(new CyanNode(getWorld(), getBlockPos().offset(yDir), getNextTime(), getChainedId(), xDir, yDir, base, ALL_FLAG));
            if ((flag & LEFT_FLAG) == LEFT_FLAG) proxy.addDeferredAction(new CyanNode(getWorld(), getBlockPos().offset(xDir, -1), getNextTime(), getChainedId(), xDir, yDir, base, LEFT_FLAG));
            if ((flag & RIGHT_FLAG) == RIGHT_FLAG)proxy.addDeferredAction(new CyanNode(getWorld(), getBlockPos().offset(xDir), getNextTime(), getChainedId(), xDir, yDir, base, RIGHT_FLAG));
        }
    }

    private int  getAxisDifference(EnumFacing.Axis axis, BlockPos base, BlockPos blockPos) {
        if (axis == EnumFacing.Axis.X){
            return Math.abs(base.getX() - blockPos.getX());
        } else if (axis == EnumFacing.Axis.Y){
            return Math.abs(base.getY() - blockPos.getY());
        } else if (axis == EnumFacing.Axis.Z){
            return Math.abs(base.getZ() - blockPos.getZ());
        } else {
            return 0;
        }
    }

    private boolean b(EnumFacing facing, BlockPos base, BlockPos blockPos){
        if (facing == EnumFacing.WEST){
            return blockPos.getX() <= base.getX();
        } else if (facing == EnumFacing.EAST){
            return blockPos.getX() >= base.getX();
        } else if (facing == EnumFacing.NORTH){
            return blockPos.getZ() <= base.getZ();
        } else{
            return blockPos.getZ() >= base.getZ();
        }
    }
}
