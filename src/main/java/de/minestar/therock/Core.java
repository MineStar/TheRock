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

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.therock.data.BlockQueue;
import de.minestar.therock.data.ChatQueue;
import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.listener.BlockListener;
import de.minestar.therock.listener.PlayerListener;

public class Core extends AbstractCore {

    public static final String NAME = "TheRock";

    /** LISTENER */
    private Listener blockListener, playerListener;

    /** MANAGER */
    private DatabaseHandler dbHandler;

    /** QUEUES */
    private BlockQueue blockQueue;
    private ChatQueue chatQueue;

    @Override
    protected boolean createManager() {
        dbHandler = new DatabaseHandler(NAME, getDataFolder());
        if (!dbHandler.hasConnection())
            return false;

        // Queues
        blockQueue = new BlockQueue(dbHandler);
        chatQueue = new ChatQueue(dbHandler);

        return true;
    }

    @Override
    protected boolean createListener() {
        blockListener = new BlockListener(blockQueue);
        playerListener = new PlayerListener(chatQueue);

        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);

        return true;
    }
}
