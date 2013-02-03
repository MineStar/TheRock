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

package de.minestar.therock.sqlthreads;

import java.sql.PreparedStatement;

import org.bukkit.Bukkit;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.database.DatabaseHandler;

public class DeleteThread implements Runnable {

    private static int LIMIT = 1000;
    private long beforeDate = -1;
    public boolean lock = false;

    private final String worldName, tableType;

    public int TASKID = -1;

    public DeleteThread(String worldName, String tableType) {
        this.worldName = worldName;
        this.tableType = tableType;
        long aSecond = 1 * 1000;
        long aMinute = aSecond * 60;
        long aHour = aMinute * 60;
        long aDay = aHour * 24;
        long aMonth = aDay * 31;
        long fullTime = aMonth;
        this.beforeDate = System.currentTimeMillis() - fullTime;
    }

    @Override
    public void run() {
        if (lock) {
            return;
        }
        lock = true;
        System.out.println("Deleting " + tableType + " in " + worldName + "...");
        DatabaseHandler handler = TheRockCore.databaseHandler;
        int changes = this.runBlockDelete(handler);
        if (changes > 0) {
            lock = false;
        } else if (changes == 0) {
            System.out.println("NO CHANGES IN " + worldName + "_" + tableType + "! CANCELLING TASK!");
            Bukkit.getScheduler().cancelTask(TASKID);
            lock = false;
        } else {
            System.out.println("ERROR!");
            Bukkit.getScheduler().cancelTask(TASKID);
            lock = false;
        }
    }

    private int runBlockDelete(DatabaseHandler handler) {
        try {
            PreparedStatement statement = handler.getConnection().prepareStatement("DELETE FROM " + this.worldName + "_" + this.tableType + " WHERE timestamp<=" + this.beforeDate + " LIMIT " + LIMIT);
            int changes = statement.executeUpdate();
            lock = false;
            return changes;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
