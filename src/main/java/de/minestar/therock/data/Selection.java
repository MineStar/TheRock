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

import org.bukkit.Location;

public class Selection {
    private Location corner1, corner2;

    public Selection() {
        this.corner1 = null;
        this.corner2 = null;
    }

    public Location getMinCorner() {
        return new Location(corner1.getWorld(), Math.min(corner1.getBlockX(), corner2.getBlockX()), Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
    }

    public Location getMaxCorner() {
        return new Location(corner1.getWorld(), Math.max(corner1.getBlockX(), corner2.getBlockX()), Math.max(corner1.getBlockY(), corner2.getBlockY()), Math.max(corner1.getBlockZ(), corner2.getBlockZ()));
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public boolean isValid() {
        return corner1 != null && corner2 != null && corner1.getWorld().getName().equalsIgnoreCase(corner2.getWorld().getName());
    }
}
