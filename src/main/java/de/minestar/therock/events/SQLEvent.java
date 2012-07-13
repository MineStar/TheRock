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

package de.minestar.therock.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minestar.therock.data.BlockVector;

public class SQLEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String playerName;
    private final World world;
    private final ResultSet results;
    private int totalChanges, blockChanges;

    public SQLEvent(String playerName, World world, ResultSet results) {
        this.playerName = playerName;
        this.world = world;
        this.results = results;

        try {
            HashSet<BlockVector> blocks = new HashSet<BlockVector>();
            BlockVector tmpVector;
            while (results.next()) {
                tmpVector = new BlockVector(playerName, results.getInt("blockX"), results.getInt("blockY"), results.getInt("blockZ"));
                if (!blocks.contains(tmpVector)) {
                    blocks.add(tmpVector);
                    blockChanges++;
                }
                totalChanges++;
            }
            this.results.last();
            blocks.clear();
            blocks = null;
        } catch (SQLException e) {
            this.totalChanges = -1;
            this.blockChanges = -1;
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public World getWorld() {
        return world;
    }

    public ResultSet getResults() {
        return results;
    }

    public int getTotalChanges() {
        return totalChanges;
    }

    public int getBlockChanges() {
        return blockChanges;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
