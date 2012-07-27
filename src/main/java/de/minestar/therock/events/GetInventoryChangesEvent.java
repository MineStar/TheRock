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

import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Thrown when the SQL-Query is completed
 * 
 * @author GeMoschen
 * 
 */
public class GetInventoryChangesEvent extends SQLEvent {

    private final Block block;

    public GetInventoryChangesEvent(String playerName, World world, ResultSet results, Block block) {
        super(playerName, world, results);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
