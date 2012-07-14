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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.Selection;
import de.minestar.therock.data.Value;
import de.minestar.therock.data.ValueList;
import de.minestar.therock.sqlthreads.GetSelectionBlockChangesThread;
import de.minestar.therock.sqlthreads.GetSingleBlockChangesThread;
import de.minestar.therock.sqlthreads.InsertThread;

public class DatabaseHandler extends AbstractDatabaseHandler {

    public DatabaseHandler(String pluginName, File dataFolder) {
        super(pluginName, dataFolder);
    }

    @Override
    protected DatabaseConnection createConnection(String pluginName, File dataFolder) throws Exception {
        File configFile = new File(dataFolder, "sqlconfig.yml");
        if (!configFile.exists()) {
            DatabaseUtils.createDatabaseConfig(DatabaseType.MySQL, configFile, pluginName);
            return null;
        } else {
            return new DatabaseConnection(pluginName, DatabaseType.MySQL, new MinestarConfig(configFile));
        }
    }

    @Override
    protected void createStructure(String pluginName, Connection con) throws Exception {
    }

    public boolean getBlockChanges(Player player, Block block) {
        new GetSingleBlockChangesThread(player.getName(), block).start();
        return true;
    }

    public boolean getSelectionBlockChanges(Player player, Selection selection) {
        new GetSelectionBlockChangesThread(player.getName(), selection.getMinCorner(), selection.getMaxCorner()).start();
        return true;
    }

    public boolean getSelectionTimeBlockChanges(Player player, Selection selection, long timestamp) {
        new GetSelectionBlockChangesThread(player.getName(), selection.getMinCorner(), selection.getMaxCorner(), timestamp).start();
        return true;
    }

    public boolean getSelectionPlayerBlockChanges(Player player, Selection selection, String targetPlayer) {
        new GetSelectionBlockChangesThread(player.getName(), targetPlayer, selection.getMinCorner(), selection.getMaxCorner()).start();
        return true;
    }

    public boolean getSelectionPlayerTimeBlockChanges(Player player, Selection selection, String targetPlayer, long timestamp) {
        new GetSelectionBlockChangesThread(player.getName(), targetPlayer, selection.getMinCorner(), selection.getMaxCorner(), timestamp).start();
        return true;
    }

    public boolean executeStatement(String query) {
        new InsertThread(query).start();
        return true;
    }

    public boolean executeStatementWithoutThread(String query) {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            return (statement.executeUpdate() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            ConsoleUtils.printException(e, Core.NAME, "Can't execute query: " + query);
            return false;
        }
    }

    public Connection getConnection() {
        return this.dbConnection.getConnection();
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
    }

    public boolean createTable(String worldName, String tableName, ValueList values) {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE IF NOT EXISTS `");
        builder.append(worldName);
        builder.append("_");
        builder.append(tableName);
        builder.append("` (`ID` INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,");
        int i = 0;
        for (Value value : values.getValues()) {
            builder.append("`");
            builder.append(value.getName());
            builder.append("` ");
            builder.append(value.getSqlDefinition());
            builder.append(" NOT NULL");
            ++i;
            if (i != values.getSize()) {
                builder.append(", ");
            }
        }
        builder.append(");");

        try {
            PreparedStatement statement = this.getConnection().prepareStatement(builder.toString());
            return (statement.executeUpdate() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            ConsoleUtils.printException(e, Core.NAME, "Can't execute query: " + builder.toString());
            return false;
        }
    }
}
