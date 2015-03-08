/*
 * Copyright (C) 2015 MineStar.de 
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

package de.minestar.therock.sql.vars;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum SQLVarType {

    INT_TINY("TINYINT"), INT_SMALL("SMALLINT"), INT_MEDIUM("MEDIUMINT"), INT("INTEGER"), INT_BIG("BIGINT"),

    FLOAT("FLOAT"), DOUBLE("REAL"), LONG("INTEGER"),

    TEXT_TINY("TINYTEXT"), TEXT_MEDIUM("MEDIUMTEXT"), TEXT("TEXT"), TEXT_LONG("LONGTEXT"),

    BLOB_TINY("TINYBLOB"), BLOB_MEDIUM("MEDIUMBLOB"), BLOB("BLOB"), BLOB_LONG("LONGBLOB"),

    VAR_CHAR_16("VARCHAR(16)"), VAR_CHAR_32("VARCHAR(32)"), VAR_CHAR_64("VARCHAR(64)"), VAR_CHAR_128("VARCHAR(128)"), VAR_CHAR_255("VARCHAR(255)");

    private final String sqlDefinition;

    private SQLVarType(String sqlDefinition) {
        this.sqlDefinition = sqlDefinition;
    }

    public String getSqlDefinition() {
        return sqlDefinition;
    }

    public void set(PreparedStatement statement, int index, int var) {
        try {
            statement.setInt(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, long var) {
        try {
            statement.setLong(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, String var) {
        try {
            statement.setString(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, byte var) {
        try {
            statement.setByte(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, float var) {
        try {
            statement.setFloat(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, double var) {
        try {
            statement.setDouble(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(PreparedStatement statement, int index, byte[] var) {
        try {
            statement.setBytes(index, var);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
