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
    PLAYER_PLACE(0), PLAYER_BREAK(1), PLAYER_INTERACT(2),

    PHYSICS_CREATE(3), PHYSICS_DESTROY(4);

    private final int eventID;

    private BlockEventTypes(int ID) {
        this.eventID = ID;
    }

    public int getID() {
        return this.eventID;
    }
}
