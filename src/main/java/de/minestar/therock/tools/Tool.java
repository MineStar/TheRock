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

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.therock.TheRockCore;

public abstract class Tool {

    private final String toolName;
    private final int toolID;
    private final String permission;

    public Tool(String toolName, int toolID, String permission) {
        this.toolName = toolName;
        this.toolID = toolID;
        this.permission = permission;
    }

    public String getToolName() {
        return toolName;
    }

    public int getToolID() {
        return toolID;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player player) {
        return UtilPermissions.playerCanUseCommand(player, this.getPermission()) && TheRockCore.mainManager.isWorldWatched(player.getWorld());
    }

    public abstract void onBlockInteract(final Player player, final Block block, final BlockFace blockFace, final boolean isLeftClick);
}
