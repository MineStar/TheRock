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

import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.BlockEventTypes;
import de.minestar.therock.data.CacheElement;
import de.minestar.therock.events.GetSelectionBlockChangesEvent;
import de.minestar.therock.events.GetSelectionPlayerBlockChangesEvent;
import de.minestar.therock.events.GetSelectionPlayerTimeBlockChangesEvent;
import de.minestar.therock.events.GetSelectionTimeBlockChangesEvent;
import de.minestar.therock.events.GetSingleBlockChangesEvent;

public class SQLListener implements Listener {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss | ");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSelectionBlockChangeInfo(GetSelectionBlockChangesEvent event) {
        Player player = Bukkit.getPlayerExact(event.getPlayerName());

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.RED, "RESULTS");
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Total changes: " + event.getTotalChanges());
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Blocks changed: " + event.getBlockChanges());

        // add cache-element for possible later use
        Core.cacheHolder.addCacheElement(new CacheElement(event.getPlayerName(), event.getWorld(), event.getResults()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSelectionTimeBlockChangeInfo(GetSelectionTimeBlockChangesEvent event) {
        Player player = Bukkit.getPlayerExact(event.getPlayerName());

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.RED, "RESULTS");
        PlayerUtils.sendMessage(player, ChatColor.RED, "After: '" + dateFormat.format(event.getTimestamp()) + "'");
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Total changes: " + event.getTotalChanges());
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Blocks changed: " + event.getBlockChanges());

        // add cache-element for possible later use
        Core.cacheHolder.addCacheElement(new CacheElement(event.getPlayerName(), event.getWorld(), event.getResults()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSelectionPlayerBlockChangeInfo(GetSelectionPlayerBlockChangesEvent event) {
        Player player = Bukkit.getPlayerExact(event.getPlayerName());

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.RED, "RESULTS");
        PlayerUtils.sendMessage(player, ChatColor.RED, "Player: '" + event.getTargetPlayer() + "'");
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Total changes: " + event.getTotalChanges());
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Blocks changed: " + event.getBlockChanges());

        // add cache-element for possible later use
        Core.cacheHolder.addCacheElement(new CacheElement(event.getPlayerName(), event.getWorld(), event.getResults()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSelectionPlayerTimeBlockChangeInfo(GetSelectionPlayerTimeBlockChangesEvent event) {
        Player player = Bukkit.getPlayerExact(event.getPlayerName());

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.RED, "RESULTS");
        PlayerUtils.sendMessage(player, ChatColor.RED, "Player: '" + event.getTargetPlayer() + "'");
        PlayerUtils.sendMessage(player, ChatColor.RED, "After: '" + dateFormat.format(event.getTimestamp()) + "'");
        PlayerUtils.sendMessage(player, ChatColor.RED, "----------------");
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Total changes: " + event.getTotalChanges());
        PlayerUtils.sendMessage(player, ChatColor.GRAY, "Blocks changed: " + event.getBlockChanges());

        // add cache-element for possible later use
        Core.cacheHolder.addCacheElement(new CacheElement(event.getPlayerName(), event.getWorld(), event.getResults()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGetSingleBlockChangeInfo(GetSingleBlockChangesEvent event) {
        ResultSet results = event.getResults();
        Player player = Bukkit.getPlayerExact(event.getPlayerName());
        String message = "";

        // we need to find the player
        if (player == null)
            return;

        // send info
        PlayerUtils.sendMessage(player, ChatColor.RED, "Changes for: " + event.getWorld().getName() + " - [ " + event.getBlock().getX() + " / " + event.getBlock().getY() + " / " + event.getBlock().getZ() + " ]");
        try {
            // iterate over blockchanges
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
            // add cache-element for possible later use
            Core.cacheHolder.addCacheElement(new CacheElement(event.getPlayerName(), event.getWorld(), event.getResults()));
        } catch (SQLException e) {
            e.printStackTrace();
            PlayerUtils.sendError(player, Core.NAME, "Oooops.. something went wrong!");
        }
    }

}