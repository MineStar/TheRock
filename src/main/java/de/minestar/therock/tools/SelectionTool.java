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

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.utils.PlayerUtils;
import de.minestar.therock.TheRockCore;
import de.minestar.therock.data.Selection;

public class SelectionTool extends Tool {

    private HashMap<String, Selection> selections = new HashMap<String, Selection>();

    public SelectionTool(String toolName, int toolID, String permission) {
        super(toolName, toolID, permission);
    }

    public Selection getSelection(Player player) {
        return this.getSelection(player.getName());
    }

    public Selection getSelection(String playerName) {
        Selection tmp = selections.get(playerName);
        if (tmp == null) {
            tmp = new Selection();
            selections.put(playerName, tmp);
        }
        return tmp;
    }

    @Override
    public void onBlockInteract(Player player, Block block, BlockFace blockFace, boolean isLeftClick) {
        if (this.hasPermission(player)) {
            if (isLeftClick) {
                Selection selection = this.getSelection(player);
                selection.setCorner1(block.getLocation());
                PlayerUtils.sendSuccess(player, TheRockCore.NAME, "Point 1 set.");
                if (!selection.isValid()) {
                    PlayerUtils.sendInfo(player, TheRockCore.NAME, "Please select the second point in this world via rightclick.");
                }
            } else {
                Selection selection = this.getSelection(player);
                selection.setCorner2(block.getLocation());
                PlayerUtils.sendSuccess(player, TheRockCore.NAME, "Point 2 set.");
                if (!selection.isValid()) {
                    PlayerUtils.sendInfo(player, TheRockCore.NAME, "Please select the first point in this world via leftclick.");
                }
            }
        }
    }
}
