package de.minestar.therock.commands;

import java.util.StringTokenizer;

import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.Selection;
import de.minestar.therock.manager.MainManager;
import de.minestar.therock.tools.SelectionTool;

public class SelectionCommand extends AbstractExtendedCommand {

    private MainManager mainManager;

    public SelectionCommand(String syntax, String arguments, String node) {
        super(Core.NAME, syntax, arguments, node);
        this.description = "Count the changes in the selected zone.";
        this.mainManager = Core.mainManager;
    }

    public void execute(String[] args, Player player) {
        // Validate selection
        Selection selection = ((SelectionTool) Core.toolListener.getTool(this.mainManager.getToolSelectionID())).getSelection(player);
        if (!selection.isValid()) {
            PlayerUtils.sendError(player, Core.NAME, "Your selection is not valid.");
            PlayerUtils.sendInfo(player, "NOTE: You must select two points in the same world.");
            return;
        }

        // wrong syntax : too many arguments
        if (args.length > 3) {
            PlayerUtils.sendError(player, Core.NAME, "Wrong syntax! Too many arguments.");
            PlayerUtils.sendInfo(player, "Example: /tr selection time 2d");
            PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 2d");
            return;
        }

        // Command: /tr selection
        if (args.length == 0) {
            PlayerUtils.sendInfo(player, Core.NAME, "Getting results...");
            Core.databaseHandler.getSelectionBlockChanges(player, selection);
        } else {
            // Command: /tr selection player <Player> [1d2h3m4s]
            if (args[0].equalsIgnoreCase("player")) {
                // wrong syntax : too less arguments
                if (args.length < 2) {
                    PlayerUtils.sendError(player, Core.NAME, "Wrong syntax! Too less arguments.");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 1d2h3m4s");
                    return;
                }
                String targetName = args[1];
                // Command: /tr selection player 'Player'
                if (args.length == 2) {
                    PlayerUtils.sendInfo(player, Core.NAME, "Getting results for player '" + targetName + "'...");
                    Core.databaseHandler.getSelectionPlayerBlockChanges(player, selection, targetName);
                } else {
                    int[] times = this.parseString(args[2], player);
                    if (times == null) {
                        PlayerUtils.sendError(player, Core.NAME, "Wrong syntax!");
                        PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen");
                        PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 1d2h3m4s");
                        return;
                    }

                    long seconds = times[3] + times[2] * 60 + times[1] * 60 * 60 + times[0] * 60 * 60 * 24;
                    long timestamp = System.currentTimeMillis() - seconds * 1000;

                    PlayerUtils.sendInfo(player, Core.NAME, "Getting results for player '" + targetName + "', time " + args[2] + " ...");
                    Core.databaseHandler.getSelectionPlayerTimeBlockChanges(player, selection, targetName, timestamp);
                }
            }
            // Command: /tr selection time <1d2h3m4s>
            else if (args[0].equalsIgnoreCase("time")) {
                int[] times = this.parseString(args[1], player);
                if (times == null) {
                    PlayerUtils.sendError(player, Core.NAME, "Wrong syntax!");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen");
                    PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 1d2h3m4s");
                    return;
                }

                long seconds = times[3] + times[2] * 60 + times[1] * 60 * 60 + times[0] * 60 * 60 * 24;
                long timestamp = System.currentTimeMillis() - seconds * 1000;

                PlayerUtils.sendInfo(player, Core.NAME, "Getting results for time " + args[1] + " ...");
                Core.databaseHandler.getSelectionTimeBlockChanges(player, selection, timestamp);
            } else {
                // wrong syntax
                PlayerUtils.sendError(player, Core.NAME, "Wrong syntax! Too many arguments.");
                PlayerUtils.sendInfo(player, "Example: /tr selection");
                PlayerUtils.sendInfo(player, "Example: /tr selection time 2d");
                PlayerUtils.sendInfo(player, "Example: /tr selection player GeMoschen 2d");
            }
        }
    }

    private int[] parseString(String date, Player player) {
        try {
            int[] result = new int[4];
            // split the string at 'h' OR 'm' OR 'd' and remaine the delimiter
            StringTokenizer st = new StringTokenizer(date, "[d,h,m,s]", true);
            // parsed integer
            int i = 0;
            // date identifier
            char c = 0;
            // parse string
            while (st.hasMoreTokens()) {
                i = Integer.parseInt(st.nextToken());
                c = st.nextToken().charAt(0);
                // assign date
                fillDates(result, c, i);
            }
            // when all numbers are zero or negative
            if (result[0] < 1 && result[1] < 1 && result[2] < 1 && result[3] < 1) {
                return null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private void fillDates(int[] result, char c, int i) {
        switch (c) {
            case 'd' :
                result[0] = i;
                break;
            case 'h' :
                result[1] = i;
                break;
            case 'm' :
                result[2] = i;
                break;
            case 's' :
                result[3] = i;
                break;
        }
    }
}