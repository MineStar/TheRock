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

import de.minestar.minestarlibrary.config.MinestarConfig;
import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.SQLThread;

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
            new SQLThread(this, query).start();
            return true;
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
}
