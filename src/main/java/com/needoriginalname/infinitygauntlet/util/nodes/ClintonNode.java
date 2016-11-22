package com.needoriginalname.infinitygauntlet.util.nodes;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Owner on 11/21/2016.
 */
public class ClintonNode extends Node {
    private final EntityPlayer player;
    private final EnumFacing side;

    public ClintonNode(EntityPlayer player, EnumFacing side, World w, BlockPos pos, long distance, @NotNull Integer id) {
        super(w, pos, distance, id);
        this.player = player;
        this.side = side;
    }

    @Override
    public void doAction() {
        BlockPos chestPos = getBlockPos().offset(side);
        if (getWorld().canBlockBePlaced(Blocks.chest,chestPos, true, side, player, null)){
            getWorld().setBlockState(chestPos, Blocks.chest.getDefaultState().withProperty(BlockChest.FACING, player.getHorizontalFacing().getOpposite()));
            TileEntityChest tile = (TileEntityChest) getWorld().getTileEntity(chestPos);
            tile.setCustomName("Hillary's Email Server");
            if (tile != null){
                for (int i = 0; i < 27; i++) {
                    ItemStack stack = new ItemStack(Items.paper, 64);
                    NBTTagCompound nbt = stack.getTagCompound();
                    if (nbt == null){
                        stack.setTagCompound(new NBTTagCompound());
                        nbt = stack.getTagCompound();
                    }
                    if (!nbt.hasKey("display", Constants.NBT.TAG_COMPOUND)) {
                        nbt.setTag("display", new NBTTagCompound());
                    }
                    nbt = nbt.getCompoundTag("display");
                    nbt.setString("Name", "Hillary's Missing Email");
                    tile.setInventorySlotContents(i, stack);
                }
            }


        }
    }
}
