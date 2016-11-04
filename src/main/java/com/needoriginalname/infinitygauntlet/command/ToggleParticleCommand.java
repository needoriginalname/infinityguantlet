package com.needoriginalname.infinitygauntlet.command;

import com.needoriginalname.infinitygauntlet.reference.Names;
import com.needoriginalname.infinitygauntlet.util.NBTHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * Created by Owner on 11/4/2016.
 */
public class ToggleParticleCommand extends CommandBase{

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "toggle_ig_particule";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "toggle_ig_particle";
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        boolean b = NBTHelper.getBoolean(sender.getCommandSenderEntity(), Names.HIDE_PARTICLE_SETTING);
        NBTHelper.setBoolean(sender.getCommandSenderEntity(), Names.HIDE_PARTICLE_SETTING, !b);
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     *
     * @param sender
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
