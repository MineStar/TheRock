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

    FLOAT("FLOAT"), DOUBLE("REAL"),

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

    public void set(PreparedStatement statement, int index, Object var) {
        try {
            switch (this) {
                case INT_TINY :
                case INT_SMALL :
                case INT_MEDIUM :
                case INT :
                case INT_BIG : {
                    statement.setInt(index, (int) var);
                    return;
                }
                case BLOB_TINY :
                case BLOB_MEDIUM :
                case BLOB :
                case BLOB_LONG : {
                    statement.setBytes(index, (byte[]) var);
                    return;
                }
                case TEXT_TINY :
                case TEXT_MEDIUM :
                case TEXT :
                case TEXT_LONG :
                case VAR_CHAR_16 :
                case VAR_CHAR_32 :
                case VAR_CHAR_64 :
                case VAR_CHAR_128 :
                case VAR_CHAR_255 : {
                    statement.setString(index, var.toString());
                    return;
                }
                case FLOAT : {
                    statement.setFloat(index, (float) var);
                    return;
                }
                case DOUBLE : {
                    statement.setDouble(index, (double) var);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
