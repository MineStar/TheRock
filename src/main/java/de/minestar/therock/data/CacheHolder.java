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

import java.util.HashMap;

public class CacheHolder {
    private HashMap<String, CacheElement> cacheList = new HashMap<String, CacheElement>();

    public void addCacheElement(CacheElement element) {
        this.cacheList.put(element.getPlayerName(), element);
    }

    public CacheElement getCacheElement(String playerName) {
        return this.cacheList.get(playerName);
    }

    public boolean hasCacheElement(String playerName) {
        return (this.getCacheElement(playerName) != null);
    }
}
