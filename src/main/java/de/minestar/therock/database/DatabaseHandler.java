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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.Core;
import de.minestar.therock.events.GetBlockChangesEvent;
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
        DatabaseUtils.createStructure(getClass().getResourceAsStream("/structure.sql"), con, pluginName);
    }

    public boolean executeStatement(String query) {
        try {
            new InsertThread(this, query).start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ConsoleUtils.printException(e, Core.NAME, "Can't execute query: " + query);
            return false;
        }
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

    public boolean getBlockChanges(Player player, Block block) {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement("SELECT * FROM tbl_block WHERE worldName='" + block.getWorld().getName() + "' AND blockX=" + block.getX() + " AND blockY=" + block.getY() + " AND blockZ=" + block.getZ() + " ORDER BY ID DESC");
            ResultSet results = statement.executeQuery();
            if (results != null) {
                GetBlockChangesEvent event = new GetBlockChangesEvent(player.getName(), block, results);
                Bukkit.getPluginManager().callEvent(event);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Connection getConnection() {
        return this.dbConnection.getConnection();
    }

    @Override
    protected void createStatements(String pluginName, Connection con) throws Exception {
    }
}
