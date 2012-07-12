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

import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.BlockEventTypes;
import de.minestar.therock.events.GetBlockChangesEvent;
import de.minestar.therock.manager.MainManager;

public class ToolListener implements Listener {

    private MainManager mainManager;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss | ");;

    public ToolListener(MainManager mainManager) {
        this.mainManager = mainManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // event cancelled => return
        if (event.isCancelled())
            return;

        // do we have the Lookup-Tool?
        if (event.getPlayer().getItemInHand().getTypeId() == this.mainManager.getToolLookupID()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()));
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (event.getPlayer().isOp()) {
                    event.setCancelled(true);
                    Core.getInstance().getDatabaseHandler().getBlockChanges(event.getPlayer(), event.getClickedBlock());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGetBlockChangeInfo(GetBlockChangesEvent event) {
        ResultSet results = event.getResults();
        Player player = Bukkit.getPlayer(event.getPlayerName());
        String message = "";

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
            if (event.getPlayer().isOp()) {
                event.setCancelled(true);
                PlayerUtils.sendError(event.getPlayer(), Core.NAME, "You cannot drop the lookup-tool!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().isOp()) {
            for (int i = event.getDrops().size() - 1; i >= 0; i--) {
                // prevent dropping of the lookup-tool
                if (event.getDrops().get(i).getTypeId() == this.mainManager.getToolLookupID()) {
                    event.getDrops().remove(i);
                }
            }
        }
    }
}
