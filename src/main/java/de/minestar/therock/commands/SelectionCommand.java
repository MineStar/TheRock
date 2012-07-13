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

        Selection selection = ((SelectionTool) Core.getInstance().getToolListener().getTool(this.mainManager.getToolSelectionID())).getSelection(player);
        if (!selection.isValid()) {
            PlayerUtils.sendError(player, Core.NAME, "Your selection is not valid.");
            PlayerUtils.sendInfo(player, "NOTE: You must select two points in the same world.");
            return;
        }

        PlayerUtils.sendInfo(player, Core.NAME, "Getting results...");

        if (args.length == 0) {
            Core.getInstance().getDatabaseHandler().getSelectionBlockChanges(player, selection);
        }
    }
}