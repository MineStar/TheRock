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

public class GetSelectionBlockChangesThread extends Thread {

    private final String playerName;
    private final Location min, max;
    private final DatabaseHandler databaseHandler;

    private final String targetPlayer;

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, Location min, Location max) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = null;
    }

    public GetSelectionBlockChangesThread(DatabaseHandler databaseHandler, String playerName, String targetPlayer, Location min, Location max) {
        this.databaseHandler = databaseHandler;
        this.playerName = playerName;
        this.min = min;
        this.max = max;
        this.targetPlayer = targetPlayer;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            if (targetPlayer == null) {
                PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    GetSelectionBlockChangesEvent event = new GetSelectionBlockChangesEvent(playerName, this.min.getWorld(), results);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + min.getWorld().getName() + "_block WHERE reason='" + targetPlayer + "' AND blockX>=" + min.getBlockX() + " AND blockX<=" + max.getBlockX() + " AND blockY>=" + min.getBlockY() + " AND blockY<=" + max.getBlockY() + " AND blockZ>=" + min.getBlockZ() + " AND blockZ<=" + max.getBlockZ() + " ORDER BY TIMESTAMP DESC");
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    GetSelectionPlayerBlockChangesEvent event = new GetSelectionPlayerBlockChangesEvent(playerName, this.min.getWorld(), results, targetPlayer);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.stop();
    }
}
