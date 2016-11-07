package com.needoriginalname.infinitygauntlet.command;

import com.needoriginalname.infinitygauntlet.hander.RewardListHandler;
import com.needoriginalname.infinitygauntlet.reference.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * Created by Owner on 11/6/2016.
 */
public class ForcePatreonReloadCommand extends CommandBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "ig_reloadlist";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "ig_reloadlist";
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        boolean b = RewardListHandler.loadList();
        if (b) {
            notifyOperators(sender, this, "command.reloadlist.succeed", new Object(){});
        } else {
            notifyOperators(sender, this, "command.reloadlist.failed", new Object(){});
        }
    }
}
