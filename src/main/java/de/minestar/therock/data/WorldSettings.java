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

package de.minestar.therock.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.therock.Core;

public class WorldSettings {
    private boolean logBucketEmpty = true, logBucketFill = true;
    private boolean logBlockPlace = true, logBlockBreak = true;
    private boolean logWaterFlow = true, logLavaFlow = true;

    public WorldSettings() {
        logBucketEmpty = false;
        logBucketFill = false;
        logBlockPlace = false;
        logBlockBreak = false;
        logWaterFlow = false;
        logLavaFlow = false;
    }

    public WorldSettings(String worldName) {
        this.loadSettings(worldName);
    }

    private void loadSettings(String worldName) {
        File file = new File(Core.getInstance().getDataFolder(), "config_" + worldName + ".yml");
        if (!file.exists()) {
            this.saveSettings(worldName);
            return;
        }

        try {
            YamlConfiguration ymlFile = new YamlConfiguration();
            ymlFile.load(file);
            logBucketEmpty = ymlFile.getBoolean("log.bucket.empty", logBucketEmpty);
            logBucketFill = ymlFile.getBoolean("log.bucket.fill", logBucketFill);
            logBlockPlace = ymlFile.getBoolean("log.block.place", logBlockPlace);
            logBlockBreak = ymlFile.getBoolean("log.block.break", logBlockBreak);
            logWaterFlow = ymlFile.getBoolean("log.flow.water", logWaterFlow);
            logLavaFlow = ymlFile.getBoolean("log.flow.lava", logLavaFlow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSettings(String worldName) {
        File file = new File(Core.getInstance().getDataFolder(), "config_" + worldName + ".yml");
        if (file.exists()) {
            file.delete();
        }

        YamlConfiguration ymlFile = new YamlConfiguration();
        ymlFile.set("log.bucket.empty", logBucketEmpty);
        ymlFile.set("log.bucket.fill", logBucketFill);
        ymlFile.set("log.block.place", logBlockPlace);
        ymlFile.set("log.block.break", logBlockBreak);
        ymlFile.set("log.flow.water", logWaterFlow);
        ymlFile.set("log.flow.lava", logLavaFlow);
        try {
            ymlFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean logBucketEmpty() {
        return logBucketEmpty;
    }

    public boolean logBucketFill() {
        return logBucketFill;
    }

    public boolean logBlockPlace() {
        return logBlockPlace;
    }

    public boolean logBlockBreak() {
        return logBlockBreak;
    }

    public boolean logWaterFlow() {
        return logWaterFlow;
    }

    public boolean logLavaFlow() {
        return logLavaFlow;
    }
}
