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
import java.sql.SQLException;
import java.util.ArrayList;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.KeyHelper;
import de.minestar.therock.data.Value;
import de.minestar.therock.data.ValueList;
import de.minestar.therock.data.sqlElements.AbstractSQLElement;
import de.minestar.therock.data.sqlElements.MessageElement;

public class MessageQueue extends AbstractSQLUpdateQueue {

    private final String tableName;

    public MessageQueue(String tableName, int maxQueueSize) {
        super(maxQueueSize);
        this.tableName = tableName;
    }

    @Override
    protected void createTable() {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("CREATE TABLE IF NOT EXISTS `");
        queryBuilder.append(this.tableName);
        queryBuilder.append("` (`ID` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,");

        // APPEND VALUES
        ValueList values = new ValueList();
        values.addValue(new Value("timestamp", "BIGINT"));
        values.addValue(new Value("playerName", "VARCHAR(255)"));
        if (this.tableName.equalsIgnoreCase("general_chat")) {
            values.addValue(new Value("message", "TEXT"));
        } else {
            values.addValue(new Value("command", "TEXT"));
        }

        int i = 0;
        for (Value value : values.getValues()) {
            queryBuilder.append(value.getName());
            queryBuilder.append(value.getSqlTypeDefinition());
            queryBuilder.append(" NOT NULL");
            ++i;
            if (i != values.getSize()) {
                queryBuilder.append(", ");
            }
        }

        // APPEND KEYS
        ArrayList<ValueList> keyList = KeyHelper.getTimeAndPlayerKey();
        for (ValueList list : keyList) {
            if (list.getSize() > 0) {
                queryBuilder.append(", KEY ");

                // append keyname
                queryBuilder.append(list.getName());
                queryBuilder.append(" (");
                i = 0;
                for (Value key : list.getValues()) {
                    queryBuilder.append(key.getName());
                    ++i;
                    if (i != list.getSize()) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append(")");
            }
        }
        queryBuilder.append(") ENGINE=MYISAM DEFAULT CHARSET=utf8;");
        try {
            PreparedStatement statement = TheRockCore.databaseHandler.getConnection().prepareStatement(queryBuilder.toString());
            TheRockCore.databaseHandler.executeUpdateWithoutThread(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected PreparedStatement buildPreparedStatement() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO ");
        queryBuilder.append(this.tableName);
        if (this.tableName.equalsIgnoreCase("general_chat")) {
            queryBuilder.append(" ( timestamp, playerName, message) VALUES");
        } else {
            queryBuilder.append(" ( timestamp, playerName, command) VALUES");
        }

        for (int index = 0; index < this.list.size(); index++) {
            queryBuilder.append(" ( ?, ?, ? )");
            if (index < this.list.size() - 1) {
                queryBuilder.append(", ");
            }
        }

        PreparedStatement statement = null;
        try {
            statement = TheRockCore.databaseHandler.getConnection().prepareStatement(queryBuilder.toString());
            int currentIndex = 0;
            for (AbstractSQLElement abstractElement : this.list) {
                MessageElement element = (MessageElement) abstractElement;
                statement.setLong(1 + (currentIndex * 3), element.getTimestamp());
                statement.setString(2 + (currentIndex * 3), element.getPlayerName());
                statement.setString(3 + (currentIndex * 3), element.getMessage());
                currentIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statement = null;
        }
        return statement;
    }
}
