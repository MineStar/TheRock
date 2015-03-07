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

package de.minestar.therock.manager;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.queues.AbstractSQLUpdateQueue;
import de.minestar.therock.data.queues.BlockChangeQueue;
import de.minestar.therock.data.queues.InventoryChangeQueue;
import de.minestar.therock.data.sqlElements.AbstractSQLElement;

public class WorldConsumer {
    private AbstractSQLUpdateQueue inventoryChangeQueue, blockChangeQueue;

    public WorldConsumer(String worldName) {
        this.blockChangeQueue = new BlockChangeQueue(worldName, TheRockCore.mainManager.getBuffer_blockChange());
        this.inventoryChangeQueue = new InventoryChangeQueue(worldName, TheRockCore.mainManager.getBuffer_inventory());
    }

    public void appendBlockEvent(AbstractSQLElement element) {
        this.blockChangeQueue.add(element);
    }

    public void appendInventoryEvent(AbstractSQLElement element) {
        this.inventoryChangeQueue.add(element);
    }

    public void flushWithoutThread() {
        this.blockChangeQueue.flush(false);
        this.inventoryChangeQueue.flush(false);
    }
}
