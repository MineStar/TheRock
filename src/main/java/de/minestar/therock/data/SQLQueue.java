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

import de.minestar.therock.database.DatabaseHandler;

public class SQLQueue {

    private int BUFFER_SIZE = 5;

    private DatabaseHandler databaseHandler;

    private int currentPointer = 0;
    private String tableName = "";
    private String worldName = "";
    private ValueList values = null;
    private StringBuilder stringBuilder;

    public SQLQueue(DatabaseHandler databaseHandler, String worldName, String tableName, ValueList values) {
        this(databaseHandler, worldName, tableName, values, 5);
    }

    public SQLQueue(DatabaseHandler databaseHandler, String worldName, String tableName, ValueList values, int buffer_size) {
        this.databaseHandler = databaseHandler;
        this.tableName = tableName;
        this.worldName = worldName;
        this.values = values;
        this.BUFFER_SIZE = buffer_size;
        this.stringBuilder = new StringBuilder();
        this.resetStringBuilder();
        this.databaseHandler.createTable(worldName, tableName, values);
    }

    private void resetStringBuilder() {
        this.stringBuilder.setLength(0);
        this.stringBuilder.append("INSERT INTO ");
        this.stringBuilder.append(worldName);
        this.stringBuilder.append("_");
        this.stringBuilder.append(tableName);
        this.stringBuilder.append(" (");
        int i = 0;
        for (Value value : this.values.getValues()) {
            this.stringBuilder.append(value.getName());
            ++i;
            if (i != this.values.getSize()) {
                this.stringBuilder.append(", ");
            }
        }
        this.stringBuilder.append(") VALUES ");
        this.currentPointer = 0;
    }

    public void addToQueue(StringBuilder otherBuilder) {
        // append text
        this.stringBuilder.append(otherBuilder);
        this.stringBuilder.append(", ");

        // increment pointer
        ++currentPointer;

        // flush queue, if needed
        if (this.currentPointer >= this.BUFFER_SIZE) {
            this.flush();
        }
    }

    public void flush() {
        // At least one object must be queued
        if (currentPointer < 1)
            return;

        // update sql
        this.databaseHandler.executeStatement(stringBuilder.substring(0, stringBuilder.length() - 2));

        // reset data
        this.resetStringBuilder();
    }

    public void flushWithoutThread() {
        // At least one object must be queued
        if (currentPointer < 1)
            return;

        // update sql
        this.databaseHandler.executeStatementWithoutThread(stringBuilder.substring(0, stringBuilder.length() - 2));

        // reset data
        this.resetStringBuilder();
    }
}
