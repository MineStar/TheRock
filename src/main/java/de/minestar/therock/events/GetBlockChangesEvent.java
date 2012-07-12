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

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Thrown when the SQL-Query is completed
 * 
 * @author GeMoschen
 * 
 */
public class GetBlockChangesEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String playerName;
    private final Block block;
    private final ResultSet results;

    public GetBlockChangesEvent(String playerName, Block block, ResultSet results) {
        this.playerName = playerName;
        this.block = block;
        this.results = results;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Block getBlock() {
        return block;
    }

    public ResultSet getResults() {
        return results;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
