/*
 * Copyright (C) 2015 MineStar.de 
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

package de.minestar.therock.data.sqlElements;

public class InventoryChangeElement implements AbstractSQLElement {
    private final long timestamp;
    private final String reason;
    private final int eventType;
    private final String worldName;
    private final int blockX, blockY, blockZ;
    private final int ID, data, amount;

    public InventoryChangeElement(String reason, int eventType, String worldName, int blockX, int blockY, int blockZ, int ID, short data, int amount) {
        this.timestamp = System.currentTimeMillis();
        this.reason = reason;
        this.eventType = eventType;
        this.worldName = worldName;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.ID = ID;
        this.data = data;
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }

    public int getEventType() {
        return eventType;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public int getID() {
        return ID;
    }

    public int getData() {
        return data;
    }

    public int getAmount() {
        return amount;
    }

}
