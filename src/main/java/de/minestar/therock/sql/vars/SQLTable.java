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

import java.util.HashMap;

public class SQLTable {

    private final String tableName;
    private final HashMap<String, SQLVar> vars;
    private boolean useAutoID = false;
    private SQLVar primaryKey = null;

    public SQLTable(String tableName, boolean useAutoID) {
        this.tableName = tableName.replaceAll(" ", "_");
        this.vars = new HashMap<String, SQLVar>();
        this.useAutoID = useAutoID;
        if (this.useAutoID) {
            this.addVar(new SQLVar("ID", SQLVarType.INT).setAutoIncrement(true).setPrimaryKey(true).setNotNull(true));
        }
    }

    public SQLVar getVar(String name) {
        return this.vars.get(name.replaceAll(" ", "_"));
    }

    public SQLVar getVar(int index) {
        if (this.useAutoID) {
            index++;
        }
        int i = 0;
        for (SQLVar var : this.vars.values()) {
            if (i == index) {
                return var;
            }
            i++;
        }
        return null;
    }

    public boolean hasVar(SQLVar var) {
        return this.vars.containsKey(var.getName());
    }

    public boolean removeVar(SQLVar var) {
        return (this.vars.remove(var.getName()) != null);
    }

    public boolean addVar(SQLVar... vars) {
        for (SQLVar var : vars) {
            if (!this.addVar(var)) {
                return false;
            }
        }
        return true;
    }

    public boolean addVar(SQLVar var) {
        if (this.hasVar(var)) {
            return false;
        }
        if (var.isPrimaryKey() && this.primaryKey != null) {
            System.out.println("PRIMARY KEY is already set (" + this.primaryKey.getName() + ")!");
            return false;
        }
        this.vars.put(var.getName(), var);
        if (var.isPrimaryKey()) {
            this.primaryKey = var;
        }
        return true;
    }

    public String getInsertQuery(int amount) {
        if (this.vars.size() < 1) {
            return null;
        }

        try {
            StringBuilder queryBuilder = new StringBuilder();

            queryBuilder.append("INSERT INTO ");
            queryBuilder.append("`");
            queryBuilder.append(this.tableName);
            queryBuilder.append("` ");

            queryBuilder.append("(");

            // create insert
            String singleInsert = "(";
            for (int index = 0; index < amount; index++) {
                SQLVar var = this.getVar(index);

                if (var.isAutoIncrement()) {
                    continue;
                }

                // append columnnames
                queryBuilder.append("`");
                queryBuilder.append(var.getName());
                queryBuilder.append("`");

                singleInsert += "?";
                if (index < this.vars.size() - 1) {
                    singleInsert += ", ";
                    queryBuilder.append(", ");
                }
            }
            singleInsert += ")";

            queryBuilder.append(") VALUES ");

            // append inserts
            for (int index = 0; index < amount; index++) {
                queryBuilder.append(singleInsert);
                if (index < amount - 1) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append("; ");

            return queryBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getColumnAmount() {
        if (this.useAutoID) {
            return this.vars.size() - 1;
        } else {
            return this.vars.size();
        }
    }

    public String getSQLQuery() {
        if (this.vars.size() < 1) {
            return null;
        }

        try {
            StringBuilder queryBuilder = new StringBuilder();

            queryBuilder.append("CREATE TABLE IF NOT EXISTS ");
            queryBuilder.append("`");
            queryBuilder.append(this.tableName);
            queryBuilder.append("`");

            // open brackets
            queryBuilder.append(" (");

            // append vars
            for (int index = 0; index < this.vars.size(); index++) {
                SQLVar var = this.getVar(index);
                // append name
                queryBuilder.append("`");
                queryBuilder.append(var.getName());
                queryBuilder.append("`");

                // append type
                queryBuilder.append(" " + var.getVarType().getSqlDefinition());

                // append not null
                if (var.isNotNull()) {
                    queryBuilder.append(" NOT NULL");
                }

                // append primary key
                if (var.isPrimaryKey()) {
                    queryBuilder.append(" PRIMARY KEY");
                }

                // append auto_increment
                if (var.isAutoIncrement()) {
                    queryBuilder.append(" AUTO_INCREMENT");
                }

                // append ","
                if (index < this.vars.size() - 1) {
                    queryBuilder.append(", ");
                }
            }

            // append keys
            for (SQLVar var : this.vars.values()) {
                // only keys
                if (!var.isKey()) {
                    continue;
                }

                // append name
                queryBuilder.append(", KEY ");
                queryBuilder.append("`");
                queryBuilder.append(var.getName());
                queryBuilder.append("`");

                queryBuilder.append("(`");
                queryBuilder.append(var.getName());
                queryBuilder.append("`)");
            }

            // close brackets
            queryBuilder.append(")");

            // append engine & charset
            queryBuilder.append(" ENGINE=MYISAM DEFAULT CHARSET=utf8; ");

            return queryBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
