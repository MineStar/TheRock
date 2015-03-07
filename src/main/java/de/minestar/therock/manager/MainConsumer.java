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

import java.util.HashMap;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.queues.AbstractSQLUpdateQueue;
import de.minestar.therock.data.queues.MessageQueue;
import de.minestar.therock.data.sqlElements.AbstractSQLElement;

public class MainConsumer {
    private HashMap<String, WorldConsumer> worldList;
    private AbstractSQLUpdateQueue chatQueue, commandQueue;

    public MainConsumer() {
        this.worldList = new HashMap<String, WorldConsumer>();
    }

    public void init() {
        this.chatQueue = new MessageQueue("general_chat", TheRockCore.mainManager.getBuffer_chat());
        this.commandQueue = new MessageQueue("general_commands", TheRockCore.mainManager.getBuffer_commands());
        this.chatQueue.init();
        this.commandQueue.init();
    }

    public WorldConsumer addWorldConsumer(String worldName) {
        WorldConsumer consumer = new WorldConsumer(worldName);
        this.worldList.put(worldName, consumer);
        return consumer;
    }

    public WorldConsumer getWorldConsumer(String worldName) {
        WorldConsumer tmp = this.worldList.get(worldName);
        if (tmp == null) {
            tmp = this.addWorldConsumer(worldName);
        }
        return tmp;
    }

    public void appendBlockEvent(String worldName, AbstractSQLElement element) {
        this.getWorldConsumer(worldName).appendBlockEvent(element);
    }

    public void appendInventoryEvent(String worldName, AbstractSQLElement element) {
        this.getWorldConsumer(worldName).appendInventoryEvent(element);
    }

    public void appendChatEvent(AbstractSQLElement element) {
        this.chatQueue.add(element);
    }

    public void appendCommandEvent(AbstractSQLElement element) {
        this.commandQueue.add(element);
    }

    public void flushWithoutThread() {
        this.chatQueue.flush(false);
        this.commandQueue.flush(false);

        for (WorldConsumer consumer : this.worldList.values()) {
            consumer.flushWithoutThread();
        }
    }

}
