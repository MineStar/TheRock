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
import org.bukkit.block.Block;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.events.GetInventoryChangesEvent;

public class GetInventoryChangesThread implements Runnable {

    private final String playerName;
    private final Block block;
    private final DatabaseHandler databaseHandler;
    private final boolean showBlockInfo;

    public GetInventoryChangesThread(String playerName, Block block, boolean showBlockInfo) {
        this.databaseHandler = TheRockCore.databaseHandler;
        this.playerName = playerName;
        this.block = block;
        this.showBlockInfo = showBlockInfo;
    }

    @Override
    public void run() {
        try {
            PreparedStatement statement = this.databaseHandler.getConnection().prepareStatement("SELECT * FROM " + block.getWorld().getName() + "_inventory WHERE blockX=" + block.getX() + " AND blockY=" + block.getY() + " AND blockZ=" + block.getZ() + " ORDER BY timestamp DESC LIMIT 9");
            ResultSet results = statement.executeQuery();
            if (results != null) {
                GetInventoryChangesEvent event = new GetInventoryChangesEvent(playerName, block.getWorld(), results, block, showBlockInfo);
                Bukkit.getPluginManager().callEvent(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
