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

import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.sqlElements.AbstractSQLElement;
import de.minestar.therock.data.sqlElements.MessageElement;
import de.minestar.therock.sql.vars.SQLTable;
import de.minestar.therock.sql.vars.SQLVar;
import de.minestar.therock.sql.vars.SQLVarType;

public class MessageQueue extends AbstractSQLUpdateQueue {

    private final String tableName;

    public MessageQueue(String tableName, int maxQueueSize) {
        super(maxQueueSize);
        this.tableName = tableName;
    }

    @Override
    protected void createTable() {
        SQLTable table = new SQLTable(this.tableName, true);
        table.addVar(new SQLVar("timestamp", SQLVarType.INT_BIG).setNotNull(true).setIsKey(true));
        table.addVar(new SQLVar("playerName", SQLVarType.VAR_CHAR_255).setNotNull(true).setIsKey(true));
        if (this.tableName.equalsIgnoreCase("general_chat")) {
            table.addVar(new SQLVar("message", SQLVarType.TEXT).setNotNull(true));
        } else {
            table.addVar(new SQLVar("command", SQLVarType.TEXT).setNotNull(true));
        }

        try {
            PreparedStatement statement = TheRockCore.databaseHandler.getConnection().prepareStatement(table.getSQLQuery());
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
