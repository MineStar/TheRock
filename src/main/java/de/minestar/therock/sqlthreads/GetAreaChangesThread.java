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

import de.minestar.therock.Core;
import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.events.GetAreaPlayerChangesEvent;
import de.minestar.therock.events.GetAreaPlayerTimeChangesEvent;
import de.minestar.therock.events.GetAreaTimeChangesEvent;

public class GetAreaChangesThread extends Thread {

    private final String playerName;
    private final Location location;
    private final DatabaseHandler databaseHandler;

    private final int radius;
    private final long timestamp;
    private final String targetPlayer;

    public GetAreaChangesThread(String playerName, Location location, int radius, long timestamp) {
        this(playerName, location, radius, timestamp, null);
    }

    public GetAreaChangesThread(String playerName, Location location, int radius, String targetPlayer) {
        this(playerName, location, radius, -1, targetPlayer);
    }

    public GetAreaChangesThread(String playerName, Location location, int radius, long timestamp, String targetPlayer) {
        this.databaseHandler = Core.databaseHandler;
        this.playerName = playerName;
        this.location = location;
        this.radius = radius;
        this.timestamp = timestamp;
        this.targetPlayer = targetPlayer;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            int minX = location.getBlockX() - radius;
            int minY = location.getBlockY() - radius;
            int minZ = location.getBlockZ() - radius;
            int maxX = location.getBlockX() + radius;
            int maxY = location.getBlockY() + radius;
            int maxZ = location.getBlockZ() + radius;
            if (targetPlayer == null) {
                PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + location.getWorld().getName() + "_block WHERE timestamp>=" + timestamp + " AND blockX>=" + minX + " AND blockX<=" + maxX + " AND blockY>=" + minY + " AND blockY<=" + maxY + " AND blockZ>=" + minZ + " AND blockZ<=" + maxZ + " GROUP BY blockX, blockY, blockZ ORDER BY timestamp DESC");
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    GetAreaTimeChangesEvent event = new GetAreaTimeChangesEvent(playerName, location.getWorld(), results, radius, timestamp);
                    Bukkit.getPluginManager().callEvent(event);
                }
            } else {
                if (timestamp < 0) {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + location.getWorld().getName() + "_block WHERE reason='" + targetPlayer + "' AND blockX>=" + minX + " AND blockX<=" + maxX + " AND blockY>=" + minY + " AND blockY<=" + maxY + " AND blockZ>=" + minZ + " AND blockZ<=" + maxZ + " GROUP BY blockX, blockY, blockZ ORDER BY timestamp DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetAreaPlayerChangesEvent event = new GetAreaPlayerChangesEvent(playerName, location.getWorld(), results, radius, targetPlayer);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                } else {
                    PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + location.getWorld().getName() + "_block WHERE reason='" + targetPlayer + "' AND timestamp>=" + timestamp + " AND blockX>=" + minX + " AND blockX<=" + maxX + " AND blockY>=" + minY + " AND blockY<=" + maxY + " AND blockZ>=" + minZ + " AND blockZ<=" + maxZ + " GROUP BY blockX, blockY, blockZ ORDER BY timestamp DESC");
                    ResultSet results = statement.executeQuery();
                    if (results != null) {
                        GetAreaPlayerTimeChangesEvent event = new GetAreaPlayerTimeChangesEvent(playerName, location.getWorld(), results, radius, targetPlayer, timestamp);
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
