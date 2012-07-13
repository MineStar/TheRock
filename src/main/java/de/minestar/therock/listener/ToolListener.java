/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of TheRock.
 * 
 * TheRock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * TheRock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TheRock.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.therock.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.BlockEventTypes;
import de.minestar.therock.data.Selection;
import de.minestar.therock.events.GetBlockChangesEvent;
import de.minestar.therock.manager.MainManager;

public class ToolListener implements Listener {

    private MainManager mainManager;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss | ");
    private HashMap<String, Selection> selections = new HashMap<String, Selection>();

    public ToolListener(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    public Selection getSelection(Player player) {
        return this.getSelection(player.getName());
    }

    public Selection getSelection(String playerName) {
        Selection tmp = selections.get(playerName);
        if (tmp == null) {
            tmp = new Selection();
            selections.put(playerName, tmp);
        }
        return tmp;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // do we have the Lookup-Tool?
        if (event.getPlayer().getItemInHand().getTypeId() == this.mainManager.getToolLookupID()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (UtilPermissions.playerCanUseCommand(event.getPlayer(), "therock.tools.lookup")) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()));
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (UtilPermissions.playerCanUseCommand(event.getPlayer(), "therock.tools.lookup")) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock());
                }
            }
        }
        // do we have the Selection-Tool?
        else if (event.getPlayer().getItemInHand().getTypeId() == this.mainManager.getToolSelectionID()) {
            if (UtilPermissions.playerCanUseCommand(event.getPlayer(), "therock.tools.selection")) {
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Selection selection = this.getSelection(event.getPlayer());
                    selection.setCorner1(event.getClickedBlock().getLocation());
                    PlayerUtils.sendSuccess(event.getPlayer(), Core.NAME, "Point 1 set.");
                    if (!selection.isValid()) {
                        PlayerUtils.sendInfo(event.getPlayer(), Core.NAME, "Please select the second point in this world via rightclick.");
                    }
                    event.setCancelled(true);
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Selection selection = this.getSelection(event.getPlayer());
                    selection.setCorner2(event.getClickedBlock().getLocation());
                    PlayerUtils.sendSuccess(event.getPlayer(), Core.NAME, "Point 2 set.");
                    if (!selection.isValid()) {
                        PlayerUtils.sendInfo(event.getPlayer(), Core.NAME, "Please select the first point in this world via leftclick.");
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGetBlockChangeInfo(GetBlockChangesEvent event) {
        ResultSet results = event.getResults();
        Player player = Bukkit.getPlayerExact(event.getPlayerName());
        String message = "";

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "Changes for: " + event.getBlock().getWorld().getName() + " - [ " + event.getBlock().getX() + " / " + event.getBlock().getY() + " / " + event.getBlock().getZ() + " ]");
        try {
            while (results.next()) {
                message = dateFormat.format(results.getLong("timestamp"));
                switch (BlockEventTypes.byID(results.getInt("eventType"))) {
                    case PLAYER_PLACE : {
                        message += ChatColor.GRAY + results.getString("reason") + " placed " + Material.getMaterial(results.getInt("toID")) + ":" + results.getInt("toData");
                        break;
                    }
                    case PLAYER_BREAK : {
                        message += ChatColor.GRAY + results.getString("reason") + " destroyed " + Material.getMaterial(results.getInt("fromID")) + ":" + results.getInt("fromData");
                        break;
                    }
                    case PHYSICS_CREATE : {
                        message += ChatColor.GRAY + results.getString("reason") + " created " + Material.getMaterial(results.getInt("toID")) + ":" + results.getInt("toData");
                        break;
                    }
                    case PHYSICS_DESTROY : {
                        message += ChatColor.GRAY + results.getString("reason") + " destroyed " + Material.getMaterial(results.getInt("fromID")) + ":" + results.getInt("fromData");
                        break;
                    }
                    default : {
                        message += "UNKNOWN ACTION by " + results.getString("reason");
                        break;
                    }
                }
                PlayerUtils.sendMessage(player, ChatColor.GOLD, message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PlayerUtils.sendError(player, Core.NAME, "Oooops.. something went wrong!");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // do we have the Lookup-Tool?
        if (event.getItemDrop().getItemStack().getTypeId() == this.mainManager.getToolLookupID()) {
            if (UtilPermissions.playerCanUseCommand(event.getPlayer(), "therock.tools.lookup")) {
                event.setCancelled(true);
                PlayerUtils.sendError(event.getPlayer(), Core.NAME, "You cannot drop the lookup-tool!");
            }
        }
        // do we have the Selection-Tool?
        else if (event.getItemDrop().getItemStack().getTypeId() == this.mainManager.getToolSelectionID()) {
            if (UtilPermissions.playerCanUseCommand(event.getPlayer(), "therock.tools.selection")) {
                event.setCancelled(true);
                PlayerUtils.sendError(event.getPlayer(), Core.NAME, "You cannot drop the selection-tool!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (UtilPermissions.playerCanUseCommand(event.getEntity(), "therock.tools.lookup")) {
            for (int i = event.getDrops().size() - 1; i >= 0; i--) {
                // prevent dropping of the lookup-tool
                if (event.getDrops().get(i).getTypeId() == this.mainManager.getToolLookupID()) {
                    event.getDrops().remove(i);
                }
            }
        }
        if (UtilPermissions.playerCanUseCommand(event.getEntity(), "therock.tools.selection")) {
            for (int i = event.getDrops().size() - 1; i >= 0; i--) {
                // prevent dropping of the selection-tool
                if (event.getDrops().get(i).getTypeId() == this.mainManager.getToolSelectionID()) {
                    event.getDrops().remove(i);
                }
            }
        }
    }
}
