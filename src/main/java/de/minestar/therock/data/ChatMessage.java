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

public class ChatMessage implements Cloneable {

    private String playerName = null;
    private String message = null;

    private boolean wasCanceled;

    private long timeStamp = 0L;

    public ChatMessage() {

    }

    // For Clone
    private ChatMessage(String playerName, String message, long timeStamp, boolean wasCanceled) {
        this.playerName = playerName;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    // set the values instead of creating a new object
    public void update(String message, boolean wasCanceled, String playerName) {
        this.message = message;
        this.playerName = playerName;
        this.wasCanceled = wasCanceled;

        this.timeStamp = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean wasCanceled() {
        return wasCanceled;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ChatMessage(playerName, message, timeStamp, wasCanceled);
    }
}
