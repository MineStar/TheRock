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

import java.util.ArrayList;

public class ValueList {
    private String name;

    private ArrayList<Value> values = new ArrayList<Value>();

    public ValueList() {
        this.name = "";
    }

    public ValueList(String name) {
        this.name = "`" + name + "`";
    }

    public String getName() {
        return name;
    }

    public void addValue(Value value) {
        this.values.add(value);
    }

    public ArrayList<Value> getValues() {
        return this.values;
    }

    public int getSize() {
        return this.values.size();
    }

    public void clear() {
        this.values.clear();
    }
}
