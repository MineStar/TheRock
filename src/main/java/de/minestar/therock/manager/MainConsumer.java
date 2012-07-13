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

import de.minestar.therock.data.SQLQueue;
import de.minestar.therock.data.Value;
import de.minestar.therock.data.ValueList;
import de.minestar.therock.database.DatabaseHandler;

public class MainConsumer {
    private DatabaseHandler databaseHandler;
    private HashMap<String, WorldConsumer> worldList;

    private SQLQueue chatQueue, commandQueue, playerDeathQueue;

    public MainConsumer(DatabaseHandler databaseHandler) {
        this.worldList = new HashMap<String, WorldConsumer>();

        this.databaseHandler = databaseHandler;
        // ChatQueue
        ValueList values = new ValueList();
        values.addValue(new Value("timestamp", "BIGINT"));
        values.addValue(new Value("playerName", "TEXT"));
        values.addValue(new Value("message", "TEXT"));
        this.chatQueue = new SQLQueue(databaseHandler, "general", "chat", values, 2);

        // CommandQueue
        values = new ValueList();
        values.addValue(new Value("timestamp", "BIGINT"));
        values.addValue(new Value("playerName", "TEXT"));
        values.addValue(new Value("command", "TEXT"));
        this.commandQueue = new SQLQueue(databaseHandler, "general", "commands", values, 2);

        // PlayerDeathQueue
        values = new ValueList();
        values.addValue(new Value("timestamp", "BIGINT"));
        values.addValue(new Value("playerName", "TEXT"));
        values.addValue(new Value("reason", "TEXT"));
        this.playerDeathQueue = new SQLQueue(databaseHandler, "general", "playerDeath", values, 2);
    }

    public WorldConsumer addWorldConsumer(String worldName) {
        WorldConsumer consumer = new WorldConsumer(worldName, this.databaseHandler);
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

    public void appendBlockEvent(String worldName, StringBuilder stringBuilder) {
        this.getWorldConsumer(worldName).appendBlockEvent(stringBuilder);
    }

    public void appendChatEvent(StringBuilder stringBuilder) {
        this.chatQueue.addToQueue(stringBuilder);
    }

    public void appendCommandEvent(StringBuilder stringBuilder) {
        this.commandQueue.addToQueue(stringBuilder);
    }

    public void appendPlayerDeathEvent(StringBuilder stringBuilder) {
        this.playerDeathQueue.addToQueue(stringBuilder);
    }

    public void flushWithoutThread() {
        this.chatQueue.flushWithoutThread();
        this.commandQueue.flushWithoutThread();
        this.playerDeathQueue.flushWithoutThread();

        for (WorldConsumer consumer : this.worldList.values()) {
            consumer.flushWithoutThread();
        }
    }

}
