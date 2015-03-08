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

public class SQLVar {
    private final String name;
    private final SQLVarType varType;
    private boolean autoIncrement = false, primaryKey = false, isKey = false, notNull = false;

    public SQLVar(String name, SQLVarType varType) {
        this.name = name;
        this.varType = varType;
    }

    public String getName() {
        return name;
    }

    public SQLVarType getVarType() {
        return varType;
    }

    public SQLVar setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public SQLVar setNotNull(boolean notNull) {
        this.notNull = notNull;
        return this;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public SQLVar setIsKey(boolean isKey) {
        this.isKey = isKey;
        return this;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isKey() {
        return isKey;
    }

    public SQLVar setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SQLVar)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        SQLVar sqlObject = (SQLVar) obj;
        return (sqlObject.name.equalsIgnoreCase(this.name) && sqlObject.autoIncrement == this.autoIncrement && sqlObject.isKey == this.isKey && sqlObject.notNull == this.notNull && sqlObject.primaryKey == this.primaryKey);
    }
}
