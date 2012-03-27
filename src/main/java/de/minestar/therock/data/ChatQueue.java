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

import java.util.Arrays;

import org.bukkit.entity.Player;

import de.minestar.therock.database.DatabaseHandler;

public class ChatQueue {

    private DatabaseHandler dbHandler;

    private ChatMessage[] queue;
    private int pointer = 0;

    private final static int BUFFER = 64;

    public ChatQueue(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        init();
    }

    private void init() {
        queue = new ChatMessage[BUFFER];
        for (int i = 0; i < BUFFER; ++i)
            queue[i] = new ChatMessage();
    }

    public void queueMessage(String message, boolean isCanceled, Player player) {
        ChatMessage chatMessage = queue[pointer++];
        chatMessage.update(message, isCanceled, player.getName());
        if (pointer == BUFFER)
            flush();
    }

    public void flush() {
        ChatMessage[] copy = Arrays.copyOfRange(queue, 0, pointer);
        dbHandler.flushChatQueue(copy, pointer == BUFFER);
        pointer = 0;
    }

}
