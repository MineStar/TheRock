package de.minestar.therock.commands;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.Selection;
import de.minestar.therock.manager.MainManager;
import de.minestar.therock.tools.SelectionTool;

public class SelectionCommand extends AbstractExtendedCommand {

    private MainManager mainManager;

    public SelectionCommand(String syntax, String arguments, String node, MainManager mainManager) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Count the changes in the selected zone.";
        this.mainManager = mainManager;
    }

    public void execute(String[] args, Player player) {
        // Validate selection
        Selection selection = ((SelectionTool) Core.getInstance().getToolListener().getTool(this.mainManager.getToolSelectionID())).getSelection(player);
        if (!selection.isValid()) {
            PlayerUtils.sendError(player, Core.NAME, "Your selection is not valid.");
            PlayerUtils.sendInfo(player, "NOTE: You must select two points in the same world.");
            return;
        }

        // wrong syntax : too many arguments
        if (args.length > 3) {
            PlayerUtils.sendError(player, Core.NAME, "Wrong syntax! Too many arguments.");
            PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen");
            PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 2d");
            return;
        }

        // Command: /tr selection
        if (args.length == 0) {
            PlayerUtils.sendInfo(player, Core.NAME, "Getting results...");
            Core.getInstance().getDatabaseHandler().getSelectionBlockChanges(player, selection);
        } else {
            // Command: /tr selection player <Player> [1month2day3min4sec]
            if (args[0].equalsIgnoreCase("player")) {
                // wrong syntax : too less arguments
                if (args.length < 2) {
                    PlayerUtils.sendError(player, Core.NAME, "Wrong syntax! Too less arguments.");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 2d");
                    return;
                }
                String targetName = args[1];
                // Command: /tr selection player 'Player'
                if (args.length == 2) {
                    PlayerUtils.sendInfo(player, Core.NAME, "Getting results for player '" + targetName + "'...");
                    Core.getInstance().getDatabaseHandler().getSelectionPlayerBlockChanges(player, selection, targetName);
                } else {
                    PlayerUtils.sendInfo(player, Core.NAME, "Not implemented yet.");
                }
            }
        }
    }
}