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

package de.minestar.therock.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.database.AbstractMySQLHandler;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.Selection;
import de.minestar.therock.data.Value;
import de.minestar.therock.data.ValueList;
import de.minestar.therock.sqlthreads.GetAreaChangesThread;
import de.minestar.therock.sqlthreads.GetInventoryChangesThread;
import de.minestar.therock.sqlthreads.GetSelectionBlockChangesThread;
import de.minestar.therock.sqlthreads.GetSingleBlockChangesThread;
import de.minestar.therock.sqlthreads.UndoLastBlockChangesThread;
import de.minestar.therock.sqlthreads.UpdateSQLThread;

public class DatabaseHandler extends AbstractMySQLHandler {

    public DatabaseHandler(String pluginName, File SQLConfigFile) {
        super(pluginName, SQLConfigFile);
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
    }

    public boolean getInventoryChanges(Player player, Block block, boolean showBlockInfo) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetInventoryChangesThread(player.getName(), block, showBlockInfo));
        return true;
    }

    public boolean getBlockChanges(Player player, Block block) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetSingleBlockChangesThread(player.getName(), block));
        return true;
    }

    public boolean getUndoLastBlockChanges(Player player, Block block) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new UndoLastBlockChangesThread(player.getName(), block));
        return true;
    }

    public boolean getAreaTimeChanges(Player player, int radius, long timestamp) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetAreaChangesThread(player.getName(), player.getLocation().clone(), radius, timestamp));
        return true;
    }

    public boolean getAreaPlayerChanges(Player player, int radius, String targetPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetAreaChangesThread(player.getName(), player.getLocation().clone(), radius, targetPlayer));
        return true;
    }

    public boolean getAreaPlayerTimeChanges(Player player, int radius, String targetPlayer, long timestamp) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetAreaChangesThread(player.getName(), player.getLocation().clone(), radius, timestamp, targetPlayer));
        return true;
    }

    public boolean getSelectionBlockChanges(Player player, Selection selection) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetSelectionBlockChangesThread(player.getName(), selection.getMinCorner(), selection.getMaxCorner()));
        return true;
    }

    public boolean getSelectionTimeBlockChanges(Player player, Selection selection, long timestamp) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetSelectionBlockChangesThread(player.getName(), selection.getMinCorner(), selection.getMaxCorner(), timestamp));
        return true;
    }

    public boolean getSelectionPlayerBlockChanges(Player player, Selection selection, String targetPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetSelectionBlockChangesThread(player.getName(), targetPlayer, selection.getMinCorner(), selection.getMaxCorner()));
        return true;
    }

    public boolean getSelectionPlayerTimeBlockChanges(Player player, Selection selection, String targetPlayer, long timestamp) {
        Bukkit.getScheduler().runTaskAsynchronously(TheRockCore.INSTANCE, new GetSelectionBlockChangesThread(player.getName(), targetPlayer, selection.getMinCorner(), selection.getMaxCorner(), timestamp));
        return true;
    }

    public boolean executeUpdateWithThread(PreparedStatement statement) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(TheRockCore.INSTANCE, new UpdateSQLThread(statement), 1);
        return true;
    }

    public boolean executeUpdateWithoutThread(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ConsoleUtils.printException(e, TheRockCore.NAME, "Can't execute query: " + statement);
            return false;
        }
    }

    public Connection getConnection() {
        return this.dbConnection.getConnection();
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
    }

    public boolean createTable(String worldName, String tableName, ValueList values, ArrayList<ValueList> keyList) {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE IF NOT EXISTS `");
        builder.append(worldName);
        builder.append("_");
        builder.append(tableName);
        builder.append("` (`ID` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,");
        int i = 0;
        for (Value value : values.getValues()) {
            builder.append(value.getName());
            builder.append(value.getSqlTypeDefinition());
            builder.append(" NOT NULL");
            ++i;
            if (i != values.getSize()) {
                builder.append(", ");
            }
        }

        if (keyList != null && keyList.size() > 0) {
            for (int currentListIndex = 0; currentListIndex < keyList.size(); currentListIndex++) {
                ValueList list = keyList.get(currentListIndex);
                if (list.getSize() > 0) {
                    builder.append(", KEY ");

                    // append keyname
                    builder.append(list.getName());
                    builder.append(" (");
                    i = 0;
                    for (Value key : list.getValues()) {
                        builder.append(key.getName());
                        ++i;
                        if (i != list.getSize()) {
                            builder.append(", ");
                        }
                    }
                    builder.append(")");
                }
            }
        }
        builder.append(") ENGINE=MYISAM DEFAULT CHARSET=utf8;");

        try {
            PreparedStatement statement = this.getConnection().prepareStatement(builder.toString());
            return (statement.executeUpdate() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            ConsoleUtils.printException(e, TheRockCore.NAME, "Can't execute query: " + builder.toString());
            return false;
        }
    }
}
