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
import de.minestar.therock.data.Value;
import de.minestar.therock.data.ValueList;

public class WorldConsumer {
    private SQLQueue blockQueue;

    public WorldConsumer(String worldName) {
        // BlockQueue
        ValueList values = new ValueList();
        values.addValue(new Value("timestamp", "BIGINT"));
        values.addValue(new Value("reason", "TEXT"));
        values.addValue(new Value("eventType", "INTEGER"));
        values.addValue(new Value("blockX", "INTEGER"));
        values.addValue(new Value("blockY", "INTEGER"));
        values.addValue(new Value("blockZ", "INTEGER"));
        values.addValue(new Value("fromID", "INTEGER"));
        values.addValue(new Value("fromData", "INTEGER"));
        values.addValue(new Value("toID", "INTEGER"));
        values.addValue(new Value("toData", "INTEGER"));
        this.blockQueue = new SQLQueue(worldName, "block", values, 3);
    }

    public void appendBlockEvent(StringBuilder stringBuilder) {
        this.blockQueue.addToQueue(stringBuilder);
    }

    public void flushWithoutThread() {
        this.blockQueue.flushWithoutThread();
    }
}
