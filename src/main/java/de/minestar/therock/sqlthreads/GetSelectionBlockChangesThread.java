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

package de.minestar.therock.sqlthreads;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.events.GetSelectionBlockChangesEvent;
import de.minestar.therock.events.GetSelectionPlayerBlockChangesEvent;
import de.minestar.therock.events.GetSelectionPlayerTimeBlockChangesEvent;
import de.minestar.therock.events.GetSelectionTimeBlockChangesEvent;

public class GetSelectionBlockChangesThread extends Thread {

    private final String playerName;
    private final Location min, max;
    private final DatabaseHandler databaseHandler;

    private final String targetPlayer;
    private final long timestamp;

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, Location min, Location max) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = null;
        this.timestamp = -1;
    }

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, String targetPlayer, Location min, Location max) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = targetPlayer;
        this.timestamp = -1;
    }

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, Location min, Location max, long timestamp) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = null;
        this.timestamp = timestamp;
    }

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, String targetPlayer, Location min, Location max, long timestamp) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = targetPlayer;
        this.timestamp = timestamp;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            if (targetPlayer == null) {
                if (timestamp < 0) {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetSelectionBlockChangesEvent event = new GetSelectionBlockChangesEvent(playerName, this.min.getWorld(), results);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } else {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE timestamp>=" + timestamp + " AND blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetSelectionTimeBlockChangesEvent event = new GetSelectionTimeBlockChangesEvent(playerName, this.min.getWorld(), results, timestamp);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                }
            } else {
                if (timestamp < 0) {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE reason='" + targetPlayer + "' AND blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetSelectionPlayerBlockChangesEvent event = new GetSelectionPlayerBlockChangesEvent(playerName, this.min.getWorld(), results, targetPlayer);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } else {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE reason='" + targetPlayer + "' AND timestamp>=" + timestamp + " AND blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetSelectionPlayerTimeBlockChangesEvent event = new GetSelectionPlayerTimeBlockChangesEvent(playerName, this.min.getWorld(), results, targetPlayer, timestamp);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stop();
    }
}
