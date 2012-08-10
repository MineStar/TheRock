package de.minestar.therock.commands;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractCommand;
import de.minestar.minestarlibrary.commands.AbstractSuperCommand;
import de.minestar.therock.TheRockCore;

public class TheRockCommand extends AbstractSuperCommand {

    public TheRockCommand(String syntax, String arguments, String node, AbstractCommand... subCommands) {
        super(TheRockCore.NAME, syntax, arguments, node, false, subCommands);
    }

    public void execute(String[] args, Player player) {
        // Do nothing
    }

    @Override
    public void execute(String[] args, ConsoleCommandSender console) {
        // Do nothing
    }
}