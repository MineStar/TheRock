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

public enum BlockEventTypes {
    PLAYER_PLACE(0, " placed "), PLAYER_BREAK(1, " destroyed "), PLAYER_INTERACT(2, " interacted "),

    PHYSICS_CREATE(3, " created "), PHYSICS_DESTROY(4, " destroyed "),

    UNKNOWN(-1, " UNKNOWN ");

    private final int eventID;
    private final String text;

    private BlockEventTypes(int ID, String text) {
        this.eventID = ID;
        this.text = text;
    }

    public int getID() {
        return this.eventID;
    }

    public String getText() {
        return this.text;
    }

    public static BlockEventTypes byID(int ID) {
        for (BlockEventTypes type : BlockEventTypes.values()) {
            if (type.getID() == ID)
                return type;
        }
        return UNKNOWN;
    }
}
