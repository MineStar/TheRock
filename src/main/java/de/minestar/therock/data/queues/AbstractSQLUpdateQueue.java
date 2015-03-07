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

package de.minestar.therock.data.queues;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.sqlElements.AbstractSQLElement;

public abstract class AbstractSQLUpdateQueue {

    private final int maxQueueSize;
    protected final List<AbstractSQLElement> list;

    public AbstractSQLUpdateQueue(int maxQueueSize) {
        this.list = new ArrayList<AbstractSQLElement>();
        this.maxQueueSize = maxQueueSize;
    }

    public final void init() {
        this.createTable();
    }

    public final void add(AbstractSQLElement element) {
        this.list.add(element);
        if (this.isQueueFull()) {
            this.flush(true);
        }
    }

    private boolean isQueueFull() {
        return this.list.size() >= this.maxQueueSize;
    }

    public final void flush(boolean useThread) {
        PreparedStatement statement = this.buildPreparedStatement();
        if (statement != null) {
            if (useThread) {
                TheRockCore.databaseHandler.executeUpdateWithThread(statement);
            } else {
                TheRockCore.databaseHandler.executeUpdateWithoutThread(statement);
            }
        }
        this.list.clear();
    }

    protected abstract PreparedStatement buildPreparedStatement();

    protected abstract void createTable();
}
