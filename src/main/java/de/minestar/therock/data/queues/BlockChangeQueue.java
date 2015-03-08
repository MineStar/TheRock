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
import de.minestar.therock.data.sqlElements.BlockChangeElement;
import de.minestar.therock.sql.vars.SQLTable;
import de.minestar.therock.sql.vars.SQLVar;
import de.minestar.therock.sql.vars.SQLVarType;

public class BlockChangeQueue extends AbstractSQLUpdateQueue {
    private final String worldName;

    public BlockChangeQueue(String worldName, int maxQueueSize) {
        super(maxQueueSize);
        this.worldName = worldName;
    }

    @Override
    protected void createTable() {
        SQLTable table = new SQLTable(this.worldName + "_block", true);
        table.addVar(new SQLVar("timestamp", SQLVarType.INT_BIG).setNotNull(true).setIsKey(true));
        table.addVar(new SQLVar("reason", SQLVarType.VAR_CHAR_255).setNotNull(false));
        table.addVar(new SQLVar("eventType", SQLVarType.INT).setNotNull(true));
        table.addVar(new SQLVar("blockX", SQLVarType.INT).setNotNull(true).setIsKey(true));
        table.addVar(new SQLVar("blockY", SQLVarType.INT).setNotNull(true).setIsKey(true));
        table.addVar(new SQLVar("blockZ", SQLVarType.INT).setNotNull(true).setIsKey(true));
        table.addVar(new SQLVar("fromID", SQLVarType.INT).setNotNull(true));
        table.addVar(new SQLVar("fromData", SQLVarType.INT).setNotNull(true));
        table.addVar(new SQLVar("toID", SQLVarType.INT).setNotNull(true));
        table.addVar(new SQLVar("toData", SQLVarType.INT).setNotNull(true));
        table.addVar(new SQLVar("extraData", SQLVarType.TEXT).setNotNull(true));

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
        queryBuilder.append(this.worldName + "_block");
        queryBuilder.append(" ( timestamp, reason, eventType, blockX, blockY, blockZ, fromID, fromData, toID, toData, extraData ) VALUES");

        for (int index = 0; index < this.list.size(); index++) {
            queryBuilder.append(" ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
            if (index < this.list.size() - 1) {
                queryBuilder.append(", ");
            }
        }

        PreparedStatement statement = null;
        try {
            statement = TheRockCore.databaseHandler.getConnection().prepareStatement(queryBuilder.toString());
            int currentIndex = 0;
            for (AbstractSQLElement abstractElement : this.list) {
                BlockChangeElement element = (BlockChangeElement) abstractElement;
                statement.setLong(1 + (currentIndex * 11), element.getTimestamp());
                statement.setString(2 + (currentIndex * 11), element.getReason());
                statement.setInt(3 + (currentIndex * 11), element.getEventType());
                statement.setInt(4 + (currentIndex * 11), element.getBlockX());
                statement.setInt(5 + (currentIndex * 11), element.getBlockY());
                statement.setInt(6 + (currentIndex * 11), element.getBlockZ());
                statement.setInt(7 + (currentIndex * 11), element.getFromID());
                statement.setInt(8 + (currentIndex * 11), element.getFromData());
                statement.setInt(9 + (currentIndex * 11), element.getToID());
                statement.setInt(10 + (currentIndex * 11), element.getToData());
                statement.setString(11 + (currentIndex * 11), element.getExtraData());
                currentIndex++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statement = null;
        }
        return statement;
    }
}
