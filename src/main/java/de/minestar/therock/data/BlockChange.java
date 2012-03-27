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

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockChange implements Cloneable {

    // position
    private int x, y, z;
    private String worldName;

    // block information
    private int oldID, newID;
    private byte oldSubID, newSubID;

    private long timeStamp;

    public BlockChange() {

    }

    // Destroy block
    public BlockChange(Block destroyedBlock) {
        update(destroyedBlock);
    }

    // Set block on existing block
    public BlockChange(Block replacedBlock, Block newBlock) {
        update(replacedBlock, newBlock);
    }

    // For clone
    private BlockChange(int x, int y, int z, String worldName, int oldID, int newID, byte oldSubID, byte newSubID, long timeStamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.worldName = worldName;
        this.oldID = oldID;
        this.newID = newID;
        this.oldSubID = oldSubID;
        this.newSubID = newSubID;
        this.timeStamp = timeStamp;
    }

    // Destroy block
    public void update(Block destroyedBlock) {
        this.x = destroyedBlock.getX();
        this.y = destroyedBlock.getY();
        this.z = destroyedBlock.getZ();
        this.worldName = destroyedBlock.getWorld().getName();

        this.oldID = destroyedBlock.getTypeId();
        this.oldSubID = destroyedBlock.getData();

        this.timeStamp = System.currentTimeMillis();

        this.newID = Material.AIR.getId();
        this.newSubID = 0;
    }

    // Set block on existing block
    public void update(Block replacedBlock, Block newBlock) {
        update(replacedBlock);
        this.newID = newBlock.getTypeId();
        this.newSubID = newBlock.getData();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getOldID() {
        return oldID;
    }

    public int getNewID() {
        return newID;
    }

    public byte getOldSubID() {
        return oldSubID;
    }

    public byte getNewSubID() {
        return newSubID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new BlockChange(x, y, z, worldName, oldID, newID, oldSubID, newSubID, timeStamp);
    }
}
