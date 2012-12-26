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

import java.io.File;

import org.bukkit.plugin.PluginManager;

import de.minestar.minestarlibrary.AbstractCore;
import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.therock.commands.AreaCommand;
import de.minestar.therock.commands.RollbackCommand;
import de.minestar.therock.commands.SelectionCommand;
import de.minestar.therock.commands.TheRockCommand;
import de.minestar.therock.data.CacheHolder;
import de.minestar.therock.database.DatabaseHandler;
import de.minestar.therock.listener.BlockChangeListener;
import de.minestar.therock.listener.ChatAndCommandListener;
import de.minestar.therock.listener.InventoryListener;
import de.minestar.therock.listener.SQLListener;
import de.minestar.therock.listener.ToolListener;
import de.minestar.therock.manager.MainConsumer;
import de.minestar.therock.manager.MainManager;

public class TheRockCore extends AbstractCore {

    public static TheRockCore INSTANCE;

    public static final String NAME = "TheRock";

    /** LISTENER */
    public static BlockChangeListener blockListener;
    public static InventoryListener inventoryListener;
    public static ChatAndCommandListener playerListener;
    public static ToolListener toolListener;
    public static SQLListener sqlListener;

    /** MANAGER */
    public static DatabaseHandler databaseHandler;
    public static MainManager mainManager;

    /** CONSUMER */
    public static MainConsumer mainConsumer;

    /** CACHE */
    public static CacheHolder cacheHolder;

    /** CONSTRUCTOR */
    public TheRockCore() {
        super(NAME);
    }

    @Override
    protected boolean createManager() {
        INSTANCE = this;

        databaseHandler = new DatabaseHandler(NAME, new File(getDataFolder(), "sqlconfig.yml"));
        if (!databaseHandler.hasConnection()) {
            return false;
        }

        // startCleaningThreads();

        // ToolManager
        toolListener = new ToolListener();

        // WorldManager
        mainManager = new MainManager();

        // Queues
        mainConsumer = new MainConsumer();

        // init manager
        mainManager.loadConfig();
        mainConsumer.init();

        // CacheHolder
        cacheHolder = new CacheHolder();

        return true;
    }

    private void startCleaningThreads() {
        // DeleteThread thread = new DeleteThread("world", "block");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("world", "inventory");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("probe", "block");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("probe", "inventory");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("world_nether", "block");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("world_nether", "inventory");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("world_the_end", "block");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
        //
        // thread = new DeleteThread("world_the_end", "inventory");
        // thread.TASKID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, thread, 20, 20 * 5);
    }

    @Override
    protected boolean createListener() {
        blockListener = new BlockChangeListener();
        playerListener = new ChatAndCommandListener();
        inventoryListener = new InventoryListener();
        sqlListener = new SQLListener();

        return true;
    }

    @Override
    protected boolean commonDisable() {
        if (databaseHandler.hasConnection()) {
            mainConsumer.flushWithoutThread();
            databaseHandler.closeConnection();
        }
        return true;
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(inventoryListener, this);
        pm.registerEvents(toolListener, this);
        pm.registerEvents(sqlListener, this);
        return true;
    }

    @Override
    protected boolean createCommands() {
        //@formatter:off;
        this.cmdList = new CommandList(
                new TheRockCommand    ("/tr", "", "",
                            new AreaCommand         ("area",         "<Radius> < player NAME [SINCE] | time SINCE>",    "therock.tools.area"),
                            new SelectionCommand    ("selection",    "[ Player ] [ since ]",                    "therock.tools.selection"),
                            new RollbackCommand     ("rollback",     "",                                        "therock.tools.rollback")
                          )
         );
        // @formatter: on;
        return true;
    }
}
