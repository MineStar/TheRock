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

    private SQLTable table;
    private final String tableName;

    public MessageQueue(String tableName, int maxQueueSize) {
        super(maxQueueSize);
        this.tableName = tableName;
    }

    @Override
    protected void createTable() {
        this.table = new SQLTable(this.tableName, true);
        this.table.addVar(new SQLVar("timestamp", SQLVarType.INT_BIG).setNotNull(true).setIsKey(true));
        this.table.addVar(new SQLVar("playerName", SQLVarType.VAR_CHAR_255).setNotNull(true).setIsKey(true));
        if (this.tableName.equalsIgnoreCase("general_chat")) {
            this.table.addVar(new SQLVar("message", SQLVarType.TEXT).setNotNull(true));
        } else {
            this.table.addVar(new SQLVar("command", SQLVarType.TEXT).setNotNull(true));
        }

        try {
            PreparedStatement statement = TheRockCore.databaseHandler.getConnection().prepareStatement(this.table.getSQLQuery());
            TheRockCore.databaseHandler.executeUpdateWithoutThread(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected PreparedStatement buildPreparedStatement() {

        PreparedStatement statement = null;
        try {
            statement = TheRockCore.databaseHandler.getConnection().prepareStatement(this.table.getInsertQuery(this.list.size()));
            int offset = 0;
            for (AbstractSQLElement abstractElement : this.list) {
                MessageElement element = (MessageElement) abstractElement;
                this.table.getVar("timestamp").set(statement, 1 + (offset), element.getTimestamp());
                this.table.getVar("playerName").set(statement, 2 + (offset), element.getPlayerName());
                this.table.getVar(2).set(statement, 3 + (offset), element.getMessage());
                offset += this.table.getColumnAmount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statement = null;
        }
        return statement;
    }
}
