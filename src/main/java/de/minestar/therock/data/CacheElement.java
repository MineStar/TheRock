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

package de.minestar.therock.data;

import java.sql.ResultSet;

import org.bukkit.World;

public class CacheElement {
    private final String playerName;
    private final World world;
    private final ResultSet results;

    public CacheElement(String playerName, World world, ResultSet results) {
        this.playerName = playerName;
        this.world = world;
        this.results = results;
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
}
