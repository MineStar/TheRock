/*
 * Copyright (C) 2011 MineStar.de 
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

package de.minestar.therock;

import org.bukkit.plugin.PluginManager;

import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.listener.BlockListener;
import de.minestar.therock.listener.PlayerListener;
import de.minestar.therock.manager.QueueManager;
import de.minestar.therock.manager.WorldManager;

public class Core extends AbstractCore {

    private static Core INSTANCE;

    public static final String NAME = "TheRock";

    /** LISTENER */
    private BlockListener blockListener;
    private PlayerListener playerListener;

    /** MANAGER */
    private DatabaseHandler databaseHandler;
    private WorldManager worldManager;

    /** QUEUES */
    private QueueManager queueManager;

    @Override
    protected boolean createManager() {
        INSTANCE = this;

        databaseHandler = new DatabaseHandler(NAME, getDataFolder());
        if (!databaseHandler.hasConnection())
            return false;

        // WorldManager
        worldManager = new WorldManager();

        // Queues
        queueManager = new QueueManager(databaseHandler);

        return true;
    }

    @Override
    protected boolean createListener() {
        blockListener = new BlockListener(queueManager, worldManager);
        playerListener = new PlayerListener(queueManager, worldManager);
        return true;
    }

    @Override
    protected boolean commonDisable() {
        if (this.databaseHandler.hasConnection()) {
            this.queueManager.flushAll();
            this.databaseHandler.closeConnection();
        }
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);

        return true;
    }

    public static Core getInstance() {
        return INSTANCE;
    }

    public DatabaseHandler getDatabaseHandler() {
        return this.databaseHandler;
    }
}
