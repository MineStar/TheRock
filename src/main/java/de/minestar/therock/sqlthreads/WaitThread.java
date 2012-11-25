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
import java.util.TimerTask;

import de.minestar.therock.TheRockCore;
import de.minestar.therock.database.DatabaseHandler;

public class WaitThread extends TimerTask {

    private static int LIMIT = 5000;
    private long beforeDate = -1;
    public boolean end = false;

    private final String worldName, tableType;

    public WaitThread(String worldName, String tableType) {
        this.worldName = worldName;
        this.tableType = tableType;
        long aSecond = 1 * 1000;
        long aMinute = aSecond * 60;
        long aHour = aMinute * 60;
        long aDay = aHour * 24;
        long aMonth = aDay * 31;
        long fullTime = aMonth + (aDay * 14);
        this.beforeDate = System.currentTimeMillis() - fullTime;
    }

    @Override
    public void run() {
        DatabaseHandler handler = TheRockCore.databaseHandler;
        int changes = this.runBlockDelete(handler);
        if (changes > 0) {
            System.out.println("DELETED " + changes + " ROWS!");
        } else if (changes == 0) {
            System.out.println("NO CHANGES! CANCELLING TASK!");
            end = true;
        } else {
            System.out.println("ERROR!");
        }
    }

    private int runBlockDelete(DatabaseHandler handler) {
        try {
            PreparedStatement statement = handler.getConnection().prepareStatement("DELETE FROM " + this.worldName + "_" + this.tableType + " WHERE timestamp<=" + this.beforeDate + " LIMIT " + LIMIT);
            return statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
