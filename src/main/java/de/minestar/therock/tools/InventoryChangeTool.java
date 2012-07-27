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

package de.minestar.therock.tools;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import de.minestar.therock.Core;

public class InventoryChangeTool extends Tool {

    public InventoryChangeTool(String toolName, int toolID, String permission) {
        super(toolName, toolID, permission);
    }

    @Override
    public void onBlockInteract(Player player, Block block, BlockFace blockFace, boolean isLeftClick) {
        if (this.hasPermission(player)) {
            Core.databaseHandler.getInventoryChanges(player, block);
        }
    }
}
