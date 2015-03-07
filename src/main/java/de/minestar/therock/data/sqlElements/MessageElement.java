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

public class MessageElement implements AbstractSQLElement {
    private final long timestamp;
    private final String playerName;
    private final String message;

    public MessageElement(String playerName, String message) {
        this.timestamp = System.currentTimeMillis();
        this.playerName = playerName;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMessage() {
        return message;
    }
}
