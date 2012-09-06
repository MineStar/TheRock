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

import java.util.ArrayList;

public class KeyHelper {

    public static ArrayList<ValueList> getBlockAndTimeKey() {
        // KEYS
        ArrayList<ValueList> keyList = new ArrayList<ValueList>();

        // 'blocks'
        ValueList blockKey = new ValueList("blocks");
        blockKey.addValue(new KeyValue("blockX"));
        blockKey.addValue(new KeyValue("blockY"));
        blockKey.addValue(new KeyValue("blockZ"));
        keyList.add(blockKey);

        // 'timestamp'
        ValueList timeKey = new ValueList("timestamp");
        timeKey.addValue(new KeyValue("timestamp"));
        keyList.add(timeKey);

        // return
        return keyList;
    }

    public static ArrayList<ValueList> getTimeAndPlayerKey() {
        // KEYS
        ArrayList<ValueList> keyList = new ArrayList<ValueList>();

        // 'timestamp'
        ValueList timeKey = new ValueList("timestamp");
        timeKey.addValue(new KeyValue("timestamp"));
        keyList.add(timeKey);

        // 'playerName'
        ValueList playerKey = new ValueList("playerName");
        playerKey.addValue(new KeyValue("playerName"));
        keyList.add(playerKey);

        // return
        return keyList;
    }

}
