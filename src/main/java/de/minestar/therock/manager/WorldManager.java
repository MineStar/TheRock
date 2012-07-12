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

package de.minestar.therock.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.WorldSettings;

public class WorldManager {
    private HashMap<String, WorldSettings> worlds;

    private boolean logChat = true, logCommands = true;

    public WorldManager() {
        this.loadWorlds();
    }

    public WorldSettings getWorld(Player player) {
        return this.getWorld(player.getWorld().getName());
    }

    public WorldSettings getWorld(Block block) {
        return this.getWorld(block.getWorld().getName());
    }

    public WorldSettings getWorld(World world) {
        return this.getWorld(world.getName());
    }

    public WorldSettings getWorld(String worldName) {
        WorldSettings tmp = this.worlds.get(worldName.toLowerCase());
        if (tmp == null) {
            tmp = new WorldSettings();
            this.worlds.put(worldName.toLowerCase(), tmp);
        }
        return tmp;
    }

    private void loadWorlds() {
        worlds = new HashMap<String, WorldSettings>();

        File file = new File(Core.getInstance().getDataFolder(), "settings.yml");
        if (!file.exists()) {
            this.writeDefaultConfig();
        }

        try {
            YamlConfiguration ymlFile = new YamlConfiguration();
            ymlFile.load(file);
            List<String> worldList = ymlFile.getStringList("log.worlds");
            if (worldList != null) {
                for (String worldName : worldList) {
                    WorldSettings settings = new WorldSettings(worldName);
                    this.worlds.put(worldName.toLowerCase(), settings);
                }
            }

            logChat = ymlFile.getBoolean("log.general.chat");
            logCommands = ymlFile.getBoolean("log.general.commands");

            ConsoleUtils.printInfo(Core.NAME, "Amount of logged worlds: " + this.worlds.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultConfig() {
        File file = new File(Core.getInstance().getDataFolder(), "settings.yml");
        if (file.exists()) {
            file.delete();
        }

        try {
            YamlConfiguration ymlFile = new YamlConfiguration();
            List<String> worldList = new ArrayList<String>();
            worldList.add("world");
            worldList.add("world_nether");
            worldList.add("world_the_end");
            ymlFile.set("log.worlds", worldList);
            ymlFile.set("log.general.chat", logChat);
            ymlFile.set("log.general.commands", logCommands);
            ymlFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean logChat() {
        return logChat;
    }

    public boolean logCommands() {
        return logCommands;
    }
}
