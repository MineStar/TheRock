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
import java.sql.SQLException;

public class UpdateSQLThread implements Runnable {

    private final PreparedStatement statement;

    public UpdateSQLThread(PreparedStatement statement) {
        this.statement = statement;
    }

    @Override
    public void run() {
        try {
            this.statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(this.statement);
        }
    }
}
