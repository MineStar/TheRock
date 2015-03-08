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
import de.minestar.therock.data.sqlElements.InventoryChangeElement;
import de.minestar.therock.sql.vars.SQLTable;
import de.minestar.therock.sql.vars.SQLVar;
import de.minestar.therock.sql.vars.SQLVarType;

public class InventoryChangeQueue extends AbstractSQLUpdateQueue {
    private SQLTable table;
    private final String worldName;

    public InventoryChangeQueue(String worldName, int maxQueueSize) {
        super(maxQueueSize);
        this.worldName = worldName;
    }

    @Override
    protected void createTable() {
        // create table
        this.table = new SQLTable(this.worldName + "_inventory", true);
        this.table.addVar(new SQLVar("timestamp", SQLVarType.INT_BIG).setNotNull(true).setIsKey(true));
        this.table.addVar(new SQLVar("reason", SQLVarType.VAR_CHAR_255).setNotNull(false));
        this.table.addVar(new SQLVar("eventType", SQLVarType.INT).setNotNull(true));
        this.table.addVar(new SQLVar("blockX", SQLVarType.INT).setNotNull(true).setIsKey(true));
        this.table.addVar(new SQLVar("blockY", SQLVarType.INT).setNotNull(true).setIsKey(true));
        this.table.addVar(new SQLVar("blockZ", SQLVarType.INT).setNotNull(true).setIsKey(true));
        this.table.addVar(new SQLVar("TypeID", SQLVarType.INT).setNotNull(true));
        this.table.addVar(new SQLVar("Data", SQLVarType.INT).setNotNull(true));
        this.table.addVar(new SQLVar("Amount", SQLVarType.INT).setNotNull(true));

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
                InventoryChangeElement element = (InventoryChangeElement) abstractElement;
                statement.setLong(1 + (offset), element.getTimestamp());
                statement.setString(2 + (offset), element.getReason());
                statement.setInt(3 + (offset), element.getEventType());
                statement.setInt(4 + (offset), element.getBlockX());
                statement.setInt(5 + (offset), element.getBlockY());
                statement.setInt(6 + (offset), element.getBlockZ());
                statement.setInt(7 + (offset), element.getID());
                statement.setInt(8 + (offset), element.getData());
                statement.setInt(9 + (offset), element.getAmount());
                offset += this.table.getColumnAmount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statement = null;
        }
        return statement;
    }
}
