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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.therock.Core;
import de.minestar.therock.data.WorldSettings;
import de.minestar.therock.listener.ToolListener;
import de.minestar.therock.tools.BlockChangeInfoTool;
import de.minestar.therock.tools.SelectionTool;

public class MainManager {
    private HashMap<String, WorldSettings> worlds;
    private MainConsumer mainConsumer;
    private ToolListener toolListener;

    // general settings
    private boolean logChat = true, logCommands = true;

    // queue settings
    private int buffer_blockChange = 100, buffer_chat = 50, buffer_commands = 50;

    // tool settings
    private int toolLookupID = Material.WATCH.getId();
    private int toolSelectionID = Material.STICK.getId();

    public MainManager() {
        this.mainConsumer = Core.mainConsumer;
        this.toolListener = Core.toolListener;
        this.loadConfig();
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

    private void loadConfig() {
        worlds = new HashMap<String, WorldSettings>();

        File file = new File(Core.INSTANCE.getDataFolder(), "settings.yml");
        if (!file.exists()) {
            this.writeDefaultConfig();
        }

        try {
            YamlConfiguration ymlFile = new YamlConfiguration();
            ymlFile.load(file);
            World world = null;
            List<String> worldList = ymlFile.getStringList("log.worlds");
            if (worldList != null) {
                for (String worldName : worldList) {
                    world = this.getBukkitWorld(worldName);
                    if (world != null) {
                        WorldSettings settings = new WorldSettings(worldName);
                        this.worlds.put(worldName.toLowerCase(), settings);
                        this.mainConsumer.addWorldConsumer(world.getName());
                    }
                }
            }
            // GENERAL
            logChat = ymlFile.getBoolean("log.general.chat", true);
            logCommands = ymlFile.getBoolean("log.general.commands", true);
            // BUFFER
            buffer_blockChange = ymlFile.getInt("config.buffer.blockchange", buffer_blockChange);
            buffer_chat = ymlFile.getInt("config.buffer.chat", buffer_chat);
            buffer_commands = ymlFile.getInt("config.buffer.commands", buffer_commands);
            // TOOLS
            toolLookupID = ymlFile.getInt("config.tool.lookup", toolLookupID);
            toolSelectionID = ymlFile.getInt("config.tool.selection", toolSelectionID);

            // TOOL-IDs must be valid
            if (Material.getMaterial(toolLookupID) == null) {
                toolLookupID = Material.WATCH.getId();
            }
            if (Material.getMaterial(toolSelectionID) == null) {
                toolSelectionID = Material.STICK.getId();
            }

            // register Tools
            this.toolListener.addTool(new BlockChangeInfoTool("Lookup", toolLookupID, "therock.tools.lookup"));
            this.toolListener.addTool(new SelectionTool("Selection", toolSelectionID, "therock.tools.selection"));

            ConsoleUtils.printInfo(Core.NAME, "Amount of logged worlds: " + this.worlds.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultConfig() {
        File file = new File(Core.INSTANCE.getDataFolder(), "settings.yml");
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
            // GENERAL
            ymlFile.set("log.general.chat", logChat);
            ymlFile.set("log.general.commands", logCommands);
            // BUFFER
            ymlFile.set("config.buffer.blockchange", buffer_blockChange);
            ymlFile.set("config.buffer.chat", buffer_chat);
            ymlFile.set("config.buffer.commands", buffer_commands);
            // TOOLS
            ymlFile.set("config.tool.lookup", toolLookupID);
            ymlFile.set("config.tool.selection", toolSelectionID);

            ymlFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isWorldWatched(String worldName) {
        return this.worlds.containsKey(worldName);
    }

    private World getBukkitWorld(String worldName) {
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equalsIgnoreCase(worldName))
                return world;
        }
        return null;
    }

    public boolean logChat() {
        return logChat;
    }

    public boolean logCommands() {
        return logCommands;
    }

    public int getBuffer_blockChange() {
        return buffer_blockChange;
    }

    public int getBuffer_chat() {
        return buffer_chat;
    }

    public int getBuffer_commands() {
        return buffer_commands;
    }

    public int getToolLookupID() {
        return toolLookupID;
    }

    public int getToolSelectionID() {
        return toolSelectionID;
    }
}
