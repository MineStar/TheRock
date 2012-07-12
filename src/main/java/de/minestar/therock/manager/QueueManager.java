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

import de.minestar.therock.data.SQLQueue;
import de.minestar.therock.database.DatabaseHandler;

public class QueueManager {
    private SQLQueue chatQueue, blockQueue;

    public QueueManager(DatabaseHandler databaseHandler) {
        this.chatQueue = new SQLQueue(databaseHandler, "tbl_chat (timestamp, playername, message)", 2);
        this.blockQueue = new SQLQueue(databaseHandler, "tbl_block (timestamp, reason, eventType, worldName, blockX, blockY, blockZ, fromID, fromData, toID, toData)", 10);
    }

    public void appendChatEvent(StringBuilder stringBuilder) {
        this.chatQueue.addToQueue(stringBuilder);
    }

    public void appendBlockEvent(StringBuilder stringBuilder) {
        this.blockQueue.addToQueue(stringBuilder);
    }

    public void flushAll() {
        this.chatQueue.flushWithoutThread();
        this.blockQueue.flushWithoutThread();
    }

}
